package main.services;

import main.models.Bill;
import main.models.Payment;
import main.repository.BillRepository;
import main.repository.PaymentRepository;

import java.time.LocalDate;
import java.util.List;

public class PaymentService {
    private UserService userService;
    private BillRepository billRepository;
    private PaymentRepository paymentRepository;

    public PaymentService(UserService userService,
                          BillRepository billRepository,
                          PaymentRepository paymentRepository) {

        this.userService = userService;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }

    public String payBill(int billId) {
        Bill bill = billRepository.findById(billId);

        if (bill == null) {
            return "Sorry! Not found a bill with such id";
        }
        if (bill.getState() == Bill.BillState.PAID) {
            return "Bill already paid";
        }
        if (!userService.withdraw(bill.getAmount())) {
            return "Sorry! Not enough fund to proceed with payment.";
        }

        bill.setState(Bill.BillState.PAID);
        Payment payment = new Payment(
                paymentRepository.findAll().size() + 1,
                bill.getAmount(),
                LocalDate.now(),
                Payment.PaymentState.PROCESSED,
                billId
        );
        paymentRepository.addNewPayment(payment);

        return "Payment has been completed for Bill with id " + billId +
                ". Your current balance is: " + userService.getBalance();
    }
}
