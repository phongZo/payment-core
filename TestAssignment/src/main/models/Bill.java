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

    public String showBill() {
        return(getId() + " " + getType() + " " + getAmount() + " " + getDueDate() + " " + getState() + " " + getProvider());
    }

    public Bill(int id, String type, long amount, LocalDate dueDate, String provider) {
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

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
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

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
