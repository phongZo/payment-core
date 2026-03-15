package main.repository;

import main.models.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {
    private List<Payment> payments = new ArrayList<>();

    public void addNewPayment(Payment payment) {
        payments.add(payment);
    }

    public List<Payment> findAll() {
        return payments;
    }
}
