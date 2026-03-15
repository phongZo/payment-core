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

    public Payment(int id, long amount, LocalDate paymentDate, PaymentState state, int billId) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.state = state;
        this.billId = billId;
    }
}
