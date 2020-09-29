package tests.integration.com.microsoft.azure.sdk.iot.digitaltwin;

import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.*;
import com.microsoft.azure.sdk.iot.service.Device;
import com.microsoft.azure.sdk.iot.service.RegistryManager;
import com.microsoft.azure.sdk.iot.service.auth.AuthenticationType;
import com.microsoft.azure.sdk.iot.service.digitaltwin.DigitalTwinAsyncClient;
import com.microsoft.azure.sdk.iot.service.digitaltwin.DigitalTwinClient;
import com.microsoft.azure.sdk.iot.service.digitaltwin.generated.models.DigitalTwinGetHeaders;
import com.microsoft.azure.sdk.iot.service.digitaltwin.helpers.UpdateOperationUtility;
import com.microsoft.azure.sdk.iot.service.digitaltwin.models.*;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;
import com.microsoft.rest.ServiceResponseWithHeaders;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tests.integration.com.microsoft.azure.sdk.iot.digitaltwin.helpers.E2ETestConstants;
import tests.integration.com.microsoft.azure.sdk.iot.helpers.IntegrationTest;
import tests.integration.com.microsoft.azure.sdk.iot.helpers.Tools;
import tests.integration.com.microsoft.azure.sdk.iot.helpers.annotations.DigitalTwinTest;
import tests.integration.com.microsoft.azure.sdk.iot.iothub.twin.TwinPnPTests;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.microsoft.azure.sdk.iot.device.IotHubClientProtocol.MQTT;
import static com.microsoft.azure.sdk.iot.device.IotHubClientProtocol.MQTT_WS;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@DigitalTwinTest
@Slf4j
@RunWith(Parameterized.class)
public class DigitalTwinClientComponentTests extends IntegrationTest
{

    private static final String IOTHUB_CONNECTION_STRING = Tools.retrieveEnvironmentVariableValue(E2ETestConstants.IOTHUB_CONNECTION_STRING_ENV_VAR_NAME);
    private static RegistryManager registryManager;
    private String deviceId;
    private DeviceClient deviceClient;
    private DigitalTwinClient digitalTwinClient = null;
    private static final String DEVICE_ID_PREFIX = "DigitalTwinServiceClientTests_";

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5 * 60); // 5 minutes max per method tested

    @Parameterized.Parameter(0)
    public IotHubClientProtocol protocol;

    @Parameterized.Parameters(name = "{index}: Digital Twin Test: protocol={0}")
    public static Collection<Object[]> data() {
        return asList(new Object[][] {
                {MQTT},
                {MQTT_WS},
        });
    }

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        registryManager = RegistryManager.createFromConnectionString(IOTHUB_CONNECTION_STRING);
    }

    @Before
    public void setUp() throws URISyntaxException, IOException, IotHubException {

        this.deviceClient = createDeviceClient(protocol);
        deviceClient.open();
        digitalTwinClient = DigitalTwinClient.createFromConnectionString(IOTHUB_CONNECTION_STRING);
    }

    @After
    public void cleanUp() {
        try {
            deviceClient.closeNow();
            registryManager.removeDevice(deviceId);
        } catch (Exception ex) {
            log.error("An exception occurred while closing/ deleting the device {}: {}", deviceId, ex);
        }
    }

    private DeviceClient createDeviceClient(IotHubClientProtocol protocol) throws IOException, IotHubException, URISyntaxException {
        ClientOptions options = new ClientOptions();
        options.setModelId(E2ETestConstants.TEMPERATURE_CONTROLLER_MODEL_ID);

        this.deviceId = DEVICE_ID_PREFIX.concat(UUID.randomUUID().toString());
        Device device = Device.createDevice(deviceId, AuthenticationType.SAS);
        Device registeredDevice = registryManager.addDevice(device);
        String deviceConnectionString = registryManager.getDeviceConnectionString(registeredDevice);
        return new DeviceClient(deviceConnectionString, protocol, options);
    }

    @AfterClass
    public static void cleanUpAfterClass()
    {
        registryManager.close();
    }

    @Test
    public void getDigitalTwin() {
        // act
        BasicDigitalTwin response = this.digitalTwinClient.getDigitalTwin(deviceId, BasicDigitalTwin.class);
        ServiceResponseWithHeaders<BasicDigitalTwin, DigitalTwinGetHeaders> responseWithHeaders = this.digitalTwinClient.getDigitalTwinWithResponse(deviceId, BasicDigitalTwin.class);

        // assert
        assertEquals(response.getMetadata().getModelId(), E2ETestConstants.TEMPERATURE_CONTROLLER_MODEL_ID);
        assertEquals(responseWithHeaders.body().getMetadata().getModelId(), E2ETestConstants.TEMPERATURE_CONTROLLER_MODEL_ID);
    }

    @Test
    public void updateDigitalTwin() throws IOException {
    }

    @Test
    public void invokeComponentLevelCommand() throws IOException {
        // arrange
        String componentName = "thermostat1";
        String commandName = "getMaxMinReport";
        String commandInput = ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(5).format(DateTimeFormatter.ISO_DATE_TIME);
        DigitalTwinInvokeCommandRequestOptions options = new DigitalTwinInvokeCommandRequestOptions();
        options.setConnectTimeoutInSeconds(15);
        options.setResponseTimeoutInSeconds(15);

        // setup device callback
        Integer deviceSuccessResponseStatus = 200;
        String deviceSuccessResponseMessage = "Success";
        Integer deviceFailureResponseStatus = 500;
        String deviceFailureResponseMessage = "Failed";

        // Device method callback
        String componentCommandName = componentName + "*" + commandName;
        DeviceMethodCallback deviceMethodCallback = (methodName, methodData, context) -> {
            if(methodName.equalsIgnoreCase(componentCommandName)) {
                return new DeviceMethodData(deviceSuccessResponseStatus, deviceSuccessResponseMessage);
            }
            else {
                return new DeviceMethodData(deviceFailureResponseStatus, deviceFailureResponseMessage);
            }
        };

        // IotHub event callback
        IotHubEventCallback iotHubEventCallback = (responseStatus, callbackContext) -> {};

        deviceClient.subscribeToDeviceMethod(deviceMethodCallback, commandName, iotHubEventCallback, commandName);

        // act
        DigitalTwinCommandResponse responseWithNoPayload = this.digitalTwinClient.invokeComponentCommand(deviceId, componentName, commandName, null);
        DigitalTwinCommandResponse response = this.digitalTwinClient.invokeComponentCommand(deviceId, componentName, commandName, commandInput);
        ServiceResponseWithHeaders<DigitalTwinCommandResponse, DigitalTwinInvokeCommandHeaders> responseWithHeaders = this.digitalTwinClient.invokeComponentCommandWithResponse(deviceId, componentName, commandName, commandInput, options);

        // assert
        String receivedDeviceResponseStatus = "\"" + deviceSuccessResponseMessage + "\"";
        assertEquals(deviceSuccessResponseStatus, responseWithNoPayload.getStatus());
        assertEquals(receivedDeviceResponseStatus, responseWithNoPayload.getPayload());
        assertEquals(deviceSuccessResponseStatus, responseWithNoPayload.getStatus());
        assertEquals(receivedDeviceResponseStatus, response.getPayload());
        assertEquals(deviceSuccessResponseStatus, responseWithHeaders.body().getStatus());
        assertEquals(receivedDeviceResponseStatus, responseWithHeaders.body().getPayload());
    }

    @Test
    public void invokeRootLevelCommand() throws IOException {
        // arrange
        String commandName = "reboot";
        String commandInput = "5";
        DigitalTwinInvokeCommandRequestOptions options = new DigitalTwinInvokeCommandRequestOptions();
        options.setConnectTimeoutInSeconds(15);
        options.setResponseTimeoutInSeconds(15);

        // setup device callback
        Integer deviceSuccessResponseStatus = 200;
        String deviceSuccessResponseMessage = "Success";
        Integer deviceFailureResponseStatus = 500;
        String deviceFailureResponseMessage = "Failed";

        // Device method callback
        DeviceMethodCallback deviceMethodCallback = (methodName, methodData, context) -> {
            if(methodName.equalsIgnoreCase(commandName)) {
                return new DeviceMethodData(deviceSuccessResponseStatus, deviceSuccessResponseMessage);
            }
            else {
                return new DeviceMethodData(deviceFailureResponseStatus, deviceFailureResponseMessage);
            }
        };

        // IotHub event callback
        IotHubEventCallback iotHubEventCallback = (responseStatus, callbackContext) -> {};

        deviceClient.subscribeToDeviceMethod(deviceMethodCallback, commandName, iotHubEventCallback, commandName);

        // act
        DigitalTwinCommandResponse responseWithNoPayload = this.digitalTwinClient.invokeCommand(deviceId, commandName, null);
        DigitalTwinCommandResponse response = this.digitalTwinClient.invokeCommand(deviceId, commandName, commandInput);
        ServiceResponseWithHeaders<DigitalTwinCommandResponse, DigitalTwinInvokeCommandHeaders> responseWithHeaders = this.digitalTwinClient.invokeCommandWithResponse(deviceId, commandName, commandInput, options);

        // assert
        String receivedDeviceResponseStatus = "\"" + deviceSuccessResponseMessage + "\"";
        assertEquals(deviceSuccessResponseStatus, responseWithNoPayload.getStatus());
        assertEquals(receivedDeviceResponseStatus, responseWithNoPayload.getPayload());
        assertEquals(deviceSuccessResponseStatus, response.getStatus());
        assertEquals(receivedDeviceResponseStatus, response.getPayload());
        assertEquals(deviceSuccessResponseStatus, responseWithHeaders.body().getStatus());
        assertEquals(receivedDeviceResponseStatus, responseWithHeaders.body().getPayload());
    }
}