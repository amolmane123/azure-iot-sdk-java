/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

package tests.unit.com.microsoft.azure.sdk.iot.service.transport.amqps;

import com.microsoft.azure.sdk.iot.service.IotHubServiceClientProtocol;
import com.microsoft.azure.sdk.iot.service.Message;
import com.microsoft.azure.sdk.iot.service.transport.amqps.AmqpSend;
import com.microsoft.azure.sdk.iot.service.transport.amqps.AmqpSendHandler;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.apache.qpid.proton.Proton;
import org.apache.qpid.proton.engine.Connection;
import org.apache.qpid.proton.engine.Event;
import org.apache.qpid.proton.engine.Session;
import org.apache.qpid.proton.reactor.Reactor;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/** Unit tests for AmqpSend */
@RunWith(JMockit.class)
public class AmqpSendTest
{
    @Mocked Proton proton;
    @Mocked Reactor reactor;
    @Mocked Event event;
    @Mocked Connection connection;
    @Mocked Session session;

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_002: [The constructor shall copy all input parameters to private member variables for event processing]
    @Test
    public void constructor_copies_params_to_members_amqps()
    {
        // Arrange
        String hostName = "aaa";
        String userName = "bbb";
        String sasToken = "ccc";
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        // Act
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
        String _hostName = Deencapsulation.getField(amqpSend, "hostName");
        String _userName = Deencapsulation.getField(amqpSend, "userName");
        String _sasToken = Deencapsulation.getField(amqpSend, "sasToken");
        IotHubServiceClientProtocol _ioIotHubServiceClientProtocol = Deencapsulation.getField(amqpSend, "iotHubServiceClientProtocol");
        // Assert
        assertEquals(hostName, _hostName);
        assertEquals(userName, _userName);
        assertEquals(sasToken, _sasToken);
        assertEquals(iotHubServiceClientProtocol, _ioIotHubServiceClientProtocol);
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_002: [The constructor shall copy all input parameters to private member variables for event processing]
    @Test
    public void constructor_copies_params_to_members_amqps_ws()
    {
        // Arrange
        String hostName = "aaa";
        String userName = "bbb";
        String sasToken = "ccc";
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS_WS;
        // Act
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
        String _hostName = Deencapsulation.getField(amqpSend, "hostName");
        String _userName = Deencapsulation.getField(amqpSend, "userName");
        String _sasToken = Deencapsulation.getField(amqpSend, "sasToken");
        IotHubServiceClientProtocol _ioIotHubServiceClientProtocol = Deencapsulation.getField(amqpSend, "iotHubServiceClientProtocol");
        // Assert
        assertEquals(hostName, _hostName);
        assertEquals(userName, _userName);
        assertEquals(sasToken, _sasToken);
        assertEquals(iotHubServiceClientProtocol, _ioIotHubServiceClientProtocol);
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_001: [The constructor shall throw IllegalArgumentException if any of the input parameter is null or empty]
    // Assert
    @Test (expected = IllegalArgumentException.class)
    public void constructor_checks_if_hostName_null()
    {
        // Arrange
        String hostName = null;
        String userName = "bbb";
        String sasToken = "ccc";
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        // Act
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_001: [The constructor shall throw IllegalArgumentException if any of the input parameter is null or empty]
    // Assert
    @Test (expected = IllegalArgumentException.class)
    public void constructor_checks_if_hostName_empty()
    {
        // Arrange
        String hostName = "";
        String userName = "bbb";
        String sasToken = "ccc";
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        // Act
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_001: [The constructor shall throw IllegalArgumentException if any of the input parameter is null or empty]
    // Assert
    @Test (expected = IllegalArgumentException.class)
    public void constructor_checks_if_userName_null()
    {
        // Arrange
        String hostName = "aaa";
        String userName = null;
        String sasToken = "ccc";
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        // Act
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_001: [The constructor shall throw IllegalArgumentException if any of the input parameter is null or empty]
    // Assert
    @Test (expected = IllegalArgumentException.class)
    public void constructor_checks_if_userName_empty()
    {
        // Arrange
        String hostName = "aaa";
        String userName = "";
        String sasToken = "ccc";
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        // Act
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_001: [The constructor shall throw IllegalArgumentException if any of the input parameter is null or empty]
    // Assert
    @Test (expected = IllegalArgumentException.class)
    public void constructor_checks_if_sasToken_null()
    {
        // Arrange
        String hostName = "aaa";
        String userName = "bbb";
        String sasToken = null;
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        // Act
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_001: [The constructor shall throw IllegalArgumentException if any of the input parameter is null or empty]
    // Assert
    @Test (expected = IllegalArgumentException.class)
    public void constructor_checks_if_sasToken_empty()
    {
        // Arrange
        String hostName = "aaa";
        String userName = "bbb";
        String sasToken = "";
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        // Act
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
    }
    
    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_001: [The constructor shall throw IllegalArgumentException if any of the input parameter is null or empty]
    // Assert
    @Test (expected = IllegalArgumentException.class)
    public void constructor_checks_if_protocol_null()
    {
        // Arrange
        String hostName = "aaa";
        String userName = "bbb";
        String sasToken = "ccc";
        
        IotHubServiceClientProtocol iotHubServiceClientProtocol = null;
        // Act
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
    }


    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_003: [The event handler shall set the member AmqpsSendHandler object to handle the given connection events]
    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_004: [The event handler shall create an AmqpsSendHandler object to handle reactor events]
    @Test
    public void onReactorInit_creates_SendHandler() throws IOException
    {
        // Arrange
        String hostName = "aaa";
        String userName = "bbb";
        String sasToken = "ccc";
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
        amqpSend.open();
        // Assert
        new Expectations()
        {
            {
                reactor = event.getReactor();
                connection = reactor.connection(Deencapsulation.getField(amqpSend, "amqpSendHandler"));
            }
        };
        // Act
        amqpSend.onReactorInit(event);
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_12_005: [The event handler shall invalidate the member AmqpsSendHandler object]
    @Test
    public void onReactorInit_invalidates_SendHandler() throws IOException
    {
        // Arrange
        String hostName = "aaa";
        String userName = "bbb";
        String sasToken = "ccc";
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
        amqpSend.open();
        amqpSend.onReactorInit(event);
        // Act
        amqpSend.close();
        // Assert
        assertNull(Deencapsulation.getField(amqpSend, "amqpSendHandler"));
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_28_006: [The function shall create a binary message with the given content with deviceId only if moduleId is null]
    @Test
    public void send_creates_ProtonMessage() throws Exception
    {
        // Arrange
        String hostName = "aaa";
        String userName = "bbb";
        String sasToken = "ccc";
        String deviceId = "deviceId";
        String content = "abcdefghijklmnopqrst";
        Message message = new Message(content);
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
        amqpSend.open();

        AmqpSendHandler handler = Deencapsulation.getField(amqpSend, "amqpSendHandler");
        // Assert
        new Expectations()
        {
            {
                Deencapsulation.invoke(handler, "createProtonMessage"
                        , deviceId, message);
            }
        };
        // Act
        amqpSend.send(deviceId, null, message);
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_28_001: [The event handler shall create a Proton message with the given content]
    @Test
    public void sendToModule_creates_ProtonMessage() throws Exception
    {
        // Arrange
        String hostName = "aaa";
        String userName = "bbb";
        String sasToken = "ccc";
        String deviceId = "deviceId";
        String moduleId = "moduleId";
        String content = "abcdefghijklmnopqrst";
        Message message = new Message(content);
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
        amqpSend.open();

        AmqpSendHandler handler = Deencapsulation.getField(amqpSend, "amqpSendHandler");
        // Assert
        new Expectations()
        {
            {
                Deencapsulation.invoke(handler, "createProtonMessage"
                        , deviceId, moduleId, message);
            }
        };
        // Act
        amqpSend.send(deviceId, moduleId, message);
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_28_002: [The event handler shall initialize the Proton reactor object]
    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_28_003: [The event handler shall start the Proton reactor object]
    @Test
    public void sendToModule_initializes_Reactor() throws Exception
    {
        // Arrange
        String hostName = "aaa";
        String userName = "bbb";
        String sasToken = "ccc";
        String deviceId = "deviceId";
        String moduleId = "moduleId";
        String content = "abcdefghijklmnopqrst";
        Message message = new Message(content);
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
        amqpSend.open();
        // Assert
        new Expectations()
        {
            {
                reactor = proton.reactor(amqpSend);
                reactor.run();
            }
        };
        // Act
        amqpSend.send(deviceId, moduleId, message);
    }

    // Tests_SRS_SERVICE_SDK_JAVA_AMQPSEND_28_005: [The event handler shall throw IOException if the send handler object is not initialized]
    // Assert
    @Test (expected = IOException.class)
    public void sendToModule_throwsIOException_when_open_has_not_been_called() throws Exception
    {
        // Arrange
        String hostName = "aaa";
        String userName = "bbb";
        String sasToken = "ccc";
        String deviceId = "deviceId";
        String moduleId = "moduleId";
        String content = "abcdefghijklmnopqrst";
        Message message = new Message(content);
        IotHubServiceClientProtocol iotHubServiceClientProtocol = IotHubServiceClientProtocol.AMQPS;
        AmqpSend amqpSend = new AmqpSend(hostName, userName, sasToken, iotHubServiceClientProtocol);
        // Act
        amqpSend.send(deviceId, moduleId, message);
    }
}
