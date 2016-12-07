package models;

import models.implementations.ChatImpl;
import models.pojos.Message;
import models.pojos.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ChatTest {
    private final Log logMock = mock(Log.class);
    private final Chat chat = new ChatImpl(logMock);
    private final Observer observerMock = mock(Observer.class);

    @Before
    public void prepare() {
        chat.addObserver(observerMock);
    }

    @Test
    public void shouldBeEmpty() {
        assertTrue(chat.getMessages().isEmpty());
    }

    @Test
    public void shouldContainAfterAdditions() {
        chat.addMessage(alice, firstDate, text1);
        chat.addMessage(bob, secondDate, text2);
        List<Message> messages = chat.getMessages();

        assertTrue(messages.contains(msg1));
        assertTrue(messages.contains(msg2));
        assertEquals(2, messages.size());
        verify(observerMock, times(2)).update(eq(chat), any());
    }

    @Test
    public void shouldBeEmptyAfterClear() {
        chat.addMessage(alice, firstDate, text1);
        chat.addMessage(bob, secondDate, text2);
        chat.clear();

        assertTrue(chat.getMessages().isEmpty());
        verify(observerMock, times(3)).update(eq(chat), any());
    }

    @Test
    public void shouldContainAfterClearAndAdditions() {
        chat.addMessage(alice, firstDate, text1);
        chat.addMessage(bob, secondDate, text2);
        chat.clear();
        chat.addMessage(alice, thirdDate, text3);

        List<Message> messages = chat.getMessages();
        assertTrue(messages.contains(msg3));
        assertEquals(1, messages.size());
        verify(observerMock, times(4)).update(eq(chat), any());
    }


    /** Testing data **/
    private final User alice = new User("Alice", "127.0.0.2:9999");
    private final User bob = new User("Bob", "127.0.0.5:9999");

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
