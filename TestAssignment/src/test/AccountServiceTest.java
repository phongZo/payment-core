package test;

import main.models.User;
import main.services.UserService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountServiceTest {
    private UserService userService;

    @Before
    public void setup() {
        userService = new UserService(new User());
    }

    @Test
    public void cashIn_shouldIncreaseBalance() {
        userService.increaseBalance(1000);
        assertEquals(1000, userService.getBalance());
    }

    @Test
    public void withdraw_shouldDecreaseBalance_whenEnoughMoney() {
        userService.increaseBalance(1000);
        boolean result = userService.withdraw(500);
        assertTrue(result);
        assertEquals(500, userService.getBalance());
    }

    @Test
    public void withdraw_shouldFail_whenNotEnoughMoney() {
        userService.increaseBalance(200);
        boolean result = userService.withdraw(500);
        assertFalse(result);
        assertEquals(200, userService.getBalance());
    }
}
