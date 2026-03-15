package test;

import main.models.Bill;
import main.models.Payment;
import main.models.User;
import main.repository.BillRepository;
import main.repository.PaymentRepository;
import main.services.PaymentService;
import main.services.UserService;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PaymentServiceTest {
    private PaymentService paymentService;
    private UserService userService;
    private BillRepository billRepository;
    private PaymentRepository paymentRepository;

    @Before
    public void setup() {
        userService = new UserService(new User());

        billRepository = new BillRepository();
        paymentRepository = new PaymentRepository();
        billRepository.add(new Bill(
                1,
                "ELECTRIC",
                200000,
                LocalDate.now(),
                "EVN"
        ));

        paymentService = new PaymentService(
                userService,
                billRepository,
                paymentRepository
        );
    }

    @Test
    public void pay_shouldSucceed_whenEnoughBalance() {
        userService.increaseBalance(500000);
        String result = paymentService.payBill(1);
        assertTrue(result.contains("Payment has been completed"));
        assertEquals(300000, userService.getBalance());

        Bill bill = billRepository.findById(1);
        assertEquals(Bill.BillState.PAID, bill.getState());
    }

    @Test
    public void pay_shouldFail_whenBillNotFound() {
        userService.increaseBalance(500000);
        String result = paymentService.payBill(99);
        assertEquals("Sorry! Not found a bill with such id", result);
    }

    @Test
    public void pay_shouldFail_whenNotEnoughBalance() {
        userService.increaseBalance(100000);
        String result = paymentService.payBill(1);
        assertEquals("Sorry! Not enough fund to proceed with payment.", result);
    }

    @Test
    public void pay_shouldFail_whenBillAlreadyPaid() {
        userService.increaseBalance(500000);
        paymentService.payBill(1);
        String result = paymentService.payBill(1);
        assertEquals("Bill already paid", result);
    }

    @Test
    public void paymentHistory_shouldStorePayment() {
        userService.increaseBalance(500000);
        paymentService.payBill(1);
        assertEquals(1, paymentRepository.findAll().size());

        Payment payment = paymentRepository.findAll().get(0);
        assertEquals(1, payment.getBillId());
        assertEquals(200000, payment.getAmount());
        assertEquals(Payment.PaymentState.PROCESSED, payment.getState());
    }
}
