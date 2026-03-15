package main.models;

import java.time.LocalDate;

public class Payment {
    private long id;
    private long amount;
    private LocalDate paymentDate;
    private PaymentState state;
    private long billId;
    private LocalDate scheduledDate;

    public enum PaymentState {
        PROCESSED,
        PENDING
    }

    public Payment(int id, long amount, LocalDate paymentDate, PaymentState state, long billId, LocalDate scheduledDate) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.state = state;
        this.billId = billId;
        this.scheduledDate = scheduledDate;
    }

    public long getAmount() {
        return amount;
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

    public long getBillId() {
        return billId;
    }

    public LocalDate getScheduledDate() { return scheduledDate; }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}
