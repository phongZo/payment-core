package main.models;

import java.time.LocalDate;


public class Bill {
    private long id;
    private String type;
    private long amount;
    private LocalDate dueDate;
    private BillState state;
    private String provider;

    public enum BillState {
        NOT_PAID,
        PAID
    }

    public Bill(long id, String type, long amount, LocalDate dueDate, String provider) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.provider = provider;
        this.state = BillState.NOT_PAID;
    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public long getAmount() {
        return amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public BillState getState() {
        return state;
    }

    public void setState(BillState state) {
        this.state = state;
    }

    public String getProvider() {
        return provider;
    }
}
