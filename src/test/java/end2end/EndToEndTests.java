package end2end;

import models.*;
import models.implementations.ChatImpl;
import models.implementations.UsersDBImpl;
import models.pojos.Message;
import models.pojos.User;
import net.Agent;
import net.NetAgent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class EndToEndTests {
    private static final int TEST_SERVER_PORT = 9999;
    private final Log serverLog = mock(Log.class);
    private final Chat serverChat = new ChatImpl(serverLog);
    private final Observer serverUIMock = mock(Observer.class);
    private final UsersDB serverUsersDB = new UsersDBImpl(serverLog);
    private Agent serverAgent = null;

    private static final int TEST_CLIENT_PORT = 8888;
    private final Log clientLog = mock(Log.class);
    private final Chat clientChat = new ChatImpl(clientLog);
    private final Observer clientUIMock = mock(Observer.class);
    private final UsersDB clientUsersDB = new UsersDBImpl(clientLog);
    private Agent clientAgent = null;

    @Before
    public void prepare() throws Exception {
        serverAgent = new NetAgent(TEST_SERVER_PORT, serverLog, serverChat, serverUsersDB);
        clientAgent = new NetAgent(TEST_CLIENT_PORT, clientLog, clientChat, clientUsersDB);

        serverChat.addObserver(serverUIMock);
        serverUsersDB.addObserver(serverUIMock);

        clientChat.addObserver(clientUIMock);
        clientUsersDB.addObserver(clientUIMock);
    }

    @Test
    public void shouldReceiveMessage() {
        clientAgent.sendMessage(alice, bob, text1);
        clientAgent.sendMessage(alice, bob, text2);
        clientAgent.sendMessage(alice, bob, text3);

        // Give some time for IO
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) { }

        List<Message> receivedMessages = serverChat.getMessages();
        assertEquals(3, receivedMessages.size());
        assertTrue(receivedMessages.stream().anyMatch(it -> it.getText().equals(text1)));
        assertTrue(receivedMessages.stream().anyMatch(it -> it.getText().equals(text2)));
        assertTrue(receivedMessages.stream().anyMatch(it -> it.getText().equals(text3)));
    }

    @Test
    public void shouldNotifyServerUIOnReceive() {
        clientAgent.sendMessage(alice, bob, text1);

        // Give some time for IO
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) { }

        // Check that message addition was called
        verify(serverUIMock).update(eq(serverChat), anyString());
        // Check that user addition was called
        verify(serverUIMock).update(eq(serverUsersDB), eq("Alice"));
    }

    @Test
    public void shouldNotAddAliases() {
        clientAgent.sendMessage(alice, bob, text1);
        clientAgent.sendMessage(charlie, bob, text3);

        // Give some time for IO
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) { }

        // Check that message addition was called twice
        verify(serverUIMock, times(2)).update(eq(serverChat), anyString());
        // Check that user addition was called once with first name
        verify(serverUIMock).update(eq(serverUsersDB), eq("Alice"));
    }




    @After
    public void teardown() throws Exception {
        serverAgent.shutdown();
        clientAgent.shutdown();
    }

    /** Testing data **/
    private final User alice = new User("Alice", "localhost:8888");
    private final User bob = new User("Bob", "localhost:9999");
    private final User charlie = new User("Charlie", "localhost:8888");

    private final Date firstDate = new Date(1337);
    private final Date secondDate = new Date(228);
    private final Date thirdDate = new Date(322);

    private final String text1 = "First message";
    private final String text2 = "Строка, требующая интернационализации";
    private final String text3 = "A lot of strange symbols: !@#$%^&*()_+}{:\"\\";

    private final Message msg1 = new Message(alice, firstDate, text1);
    private final Message msg2 = new Message(bob, secondDate, text2);
    private final Message msg3 = new Message(alice, thirdDate, text3);
}
