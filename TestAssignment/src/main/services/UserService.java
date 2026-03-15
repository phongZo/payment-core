package main.services;

import main.models.User;

public class UserService {
    private User user;

    public UserService(User user) {
        this.user = user;
    }
    public void increaseBalance(long amount) {
        user.setBalance(user.getBalance() + amount);
    }
    public boolean withdraw(long amount) {
        if (user.getBalance() < amount) {
            return false;
        }

        user.setBalance(user.getBalance() - amount);
        return true;
    }
    public long getBalance() {
        return user.getBalance();
    }
}
