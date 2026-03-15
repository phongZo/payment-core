package main.models;

import java.time.LocalDate;

public class Payment {
    private long id;
    private long amount;
    private LocalDate paymentDate;
    private PaymentState state;
    private int billId;

    public enum PaymentState {
        PROCESSED,
        PENDING
    }

    public String showPayment() {
        return getId() + " " + getAmount() + " " + getPaymentDate() + " " + getState() + " " + getBillId();
    }

    public Payment(int id, long amount, LocalDate paymentDate, PaymentState state, int billId) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.state = state;
        this.billId = billId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentState getState() {
        return state;
    }

    public void setState(PaymentState state) {
        this.state = state;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }
}
