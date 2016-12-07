package models;

import models.implementations.UsersDBImpl;
import models.pojos.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Observer;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UsersTest {
    private final Log log = mock(Log.class);
    private final UsersDB usersDB = new UsersDBImpl(log);
    private final Observer observerMock = mock(Observer.class);

    @Before
    public void prepare() {
        usersDB.addObserver(observerMock);
    }

    @Test
    public void shouldContainAfterAdd() {
        usersDB.addUser("Alice", "127.0.0.2:9999");
        User actual = usersDB.getUser("Alice");

        assertEquals(alice, actual);
        verify(observerMock, times(1)).update(eq(usersDB), any());

        usersDB.addUser("Bob", "127.0.0.5:9999");

        actual = usersDB.getUser("Alice");
        assertEquals(alice, actual);
        actual = usersDB.getUser("Bob");
        assertEquals(bob, actual);

        verify(observerMock, times(2)).update(eq(usersDB), any());
    }

    @Test
    public void shouldntNotifyOnSecondAddition() {
        usersDB.addUser("Alice", "127.0.0.2:9999");
        usersDB.addUser("Alice", "127.0.0.2:9999");
        verify(observerMock, times(1)).update(eq(usersDB), any());
    }

    @Test
    public void shouldntNotifyOnAliases() {
        usersDB.addUser("Alice", "127.0.0.2:9999");

        // Add aliases on existing address
        usersDB.addUser("Charlie", "127.0.0.2:9999");
        usersDB.addUser("Danny", "127.0.0.2:9999");

        // Check that DB understands that all these names maps to one and the same user "Alice"
        User alice = usersDB.getUser("Alice");
        User bob = usersDB.getUser("Charlie");
        User danny = usersDB.getUser("Danny");

        assertEquals(alice, bob);
        assertEquals(bob, danny);
        assertEquals(danny, alice);

        // Check that UI was notified only once
        verify(observerMock, times(1)).update(eq(usersDB), any());
    }

    @Test
    public void shouldReturnNullForAbsent() {
        usersDB.addUser("Alice", "127.0.0.2:9999");
        usersDB.addUser("Charlie", "127.0.0.3:9999");
        usersDB.addUser("Danny", "127.0.0.4:9999");

        User bob = usersDB.getUser("Bob");
        assertEquals(null, bob);
    }

    /** Testing data **/
    private final User alice = new User("Alice", "127.0.0.2:9999");
    private final User bob = new User("Bob", "127.0.0.5:9999");
}
