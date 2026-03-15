package main.services;

import main.models.Bill;
import main.models.Payment;
import main.repository.BillRepository;
import main.repository.PaymentRepository;
import main.utils.DateUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public String listPayments() {
        List<Payment> payments = paymentRepository.findAll();
        StringBuilder output = new StringBuilder();
        output.append("No. Amount Payment Date State Bill Id\n");

        int index = 1;
        for (Payment payment : payments) {

            LocalDate date = payment.getPaymentDate() != null
                    ? payment.getPaymentDate()
                    : payment.getScheduledDate();

            output.append(index).append(". ")
                    .append(payment.getAmount()).append(" ")
                    .append(date.format(DateUtils.FORMATTER)).append(" ")
                    .append(payment.getState()).append(" ")
                    .append(payment.getBillId())
                    .append("\n");

            index++;
        }

        return output.toString().trim();
    }


    public String payBills(List<Integer> billIds) {
        List<Bill> bills = billIds.stream()
                .map(billRepository::findById)
                .filter(b -> b != null && b.getState() == Bill.BillState.NOT_PAID)
                .sorted(Comparator.comparing(Bill::getDueDate))
                .collect(Collectors.toList());

        if (bills.isEmpty()) {
            return "Sorry! Not found a bill with such id";
        }

        long total = bills.stream()
                .mapToLong(Bill::getAmount)
                .sum();

        if (userService.getBalance() < total) {
            return "Sorry! Not enough fund to proceed with payment.";
        }

        StringBuilder output = new StringBuilder();
        for (Bill bill : bills) {
            userService.withdraw(bill.getAmount());
            bill.setState(Bill.BillState.PAID);

            Payment payment = new Payment(
                    paymentRepository.findAll().size() + 1,
                    bill.getAmount(),
                    LocalDate.now(),
                    Payment.PaymentState.PROCESSED,
                    bill.getId(),
                    null
            );
            paymentRepository.addNewPayment(payment);
            output.append("Payment has been completed for Bill with id ")
                    .append(bill.getId())
                    .append(".\n");

            output.append("Your current balance is: ")
                    .append(userService.getBalance())
                    .append("\n");
        }
        return output.toString().trim();
    }

    public String schedulePayment(int billId, LocalDate scheduledDate) {
        Bill bill = billRepository.findById(billId);

        if (bill == null || bill.getState() == Bill.BillState.PAID) {
            return "Sorry! Not found a bill with such id";
        }

        // If payment of the bill existed, just updated scheduled date, not add new
        for (Payment payment : paymentRepository.findAll()) {
            if (payment.getBillId() == billId &&
                    payment.getState() == Payment.PaymentState.PENDING) {
                payment.setScheduledDate(scheduledDate);

                return "Payment schedule updated for bill id "
                        + billId + " to " + scheduledDate;
            }
        }

        Payment payment = new Payment(
                paymentRepository.findAll().size() + 1,
                bill.getAmount(),
                LocalDate.now(),
                Payment.PaymentState.PENDING,
                billId,
                scheduledDate
        );
        paymentRepository.addNewPayment(payment);

        return "Payment for bill id " + billId + " is scheduled on " + scheduledDate;
    }

    public void processScheduledPayments(LocalDate today) {
        for (Payment payment : paymentRepository.findAll()) {
            if (payment.getState() == Payment.PaymentState.PENDING &&
                    payment.getScheduledDate() != null &&
                    !payment.getScheduledDate().isAfter(today)) {

                Bill bill = billRepository.findById(payment.getBillId());

                if (bill != null &&
                        bill.getState() == Bill.BillState.NOT_PAID &&
                        userService.getBalance() >= bill.getAmount()) {
                    userService.withdraw(bill.getAmount());
                    bill.setState(Bill.BillState.PAID);

                    payment.setState(Payment.PaymentState.PROCESSED);
                    payment.setPaymentDate(today);
                }
            }
        }
    }
}
