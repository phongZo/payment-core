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
import java.util.Arrays;

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
                LocalDate.of(2026,3,20),
                "EVN"
        ));

        billRepository.add(new Bill(
                2,
                "WATER",
                175000,
                LocalDate.of(2026,3,18),
                "SAVACO"
        ));

        billRepository.add(new Bill(
                3,
                "INTERNET",
                300000,
                LocalDate.of(2026,3,25),
                "VNPT"
        ));

        paymentService = new PaymentService(
                userService,
                billRepository,
                paymentRepository
        );
    }

    /*
        PAY MULTIPLE SUCCESS
     */
    @Test
    public void payMultipleBills_shouldSucceed_whenEnoughBalance() {
        userService.increaseBalance(1000000);
        String result = paymentService.payBills(Arrays.asList(1,2));

        assertTrue(result.contains("Payment has been completed for Bill with id 1"));
        assertTrue(result.contains("Your current balance is"));
        assertEquals(1000000 - 375000, userService.getBalance());
        assertEquals(Bill.BillState.PAID, billRepository.findById(1).getState());
        assertEquals(Bill.BillState.PAID, billRepository.findById(2).getState());
        assertEquals(2, paymentRepository.findAll().size());
    }

    /*
        PAY MULTIPLE FAIL WHEN NOT ENOUGH MONEY
     */
    @Test
    public void payMultipleBills_shouldFail_whenNotEnoughBalance() {
        userService.increaseBalance(100000);
        String result = paymentService.payBills(Arrays.asList(1,2));
        assertEquals(
                "Sorry! Not enough fund to proceed with payment.",
                result
        );
        assertEquals(100000, userService.getBalance());
        assertEquals(Bill.BillState.NOT_PAID,
                billRepository.findById(1).getState());
        assertEquals(0, paymentRepository.findAll().size());
    }

    /*
        PRIORITY BY DUE DATE
     */
    @Test
    public void payMultipleBills_shouldPrioritizeEarlierDueDate() {
        userService.increaseBalance(1000000);
        paymentService.payBills(Arrays.asList(1,2));
        Payment firstPayment = paymentRepository.findAll().get(0);
        assertEquals(2, firstPayment.getBillId());
    }

    /*
        SCHEDULE PAYMENT
     */
    @Test
    public void schedulePayment_shouldCreatePendingPayment() {
        String result = paymentService.schedulePayment(
                1,
                LocalDate.of(2026,3,21)
        );
        assertTrue(result.contains("scheduled"));
        assertEquals(1, paymentRepository.findAll().size());
        Payment payment = paymentRepository.findAll().get(0);
        assertEquals(Payment.PaymentState.PENDING, payment.getState());
    }

    /*
        PROCESS SCHEDULED PAYMENT SUCCESS
     */
    @Test
    public void scheduledPayment_shouldExecute_whenBalanceEnough() {
        userService.increaseBalance(500000);
        paymentService.schedulePayment(
                1,
                LocalDate.now()
        );
        paymentService.processScheduledPayments(LocalDate.now());
        Payment payment = paymentRepository.findAll().get(0);
        assertEquals(Payment.PaymentState.PROCESSED, payment.getState());
        assertEquals(Bill.BillState.PAID,
                billRepository.findById(1).getState());
    }

    /*
        SCHEDULE SHOULD WAIT IF NOT ENOUGH MONEY
     */
    @Test
    public void scheduledPayment_shouldStayPending_whenNotEnoughBalance() {
        paymentService.schedulePayment(
                1,
                LocalDate.now()
        );
        paymentService.processScheduledPayments(LocalDate.now());
        Payment payment = paymentRepository.findAll().get(0);
        assertEquals(Payment.PaymentState.PENDING, payment.getState());
    }

    @Test
    public void schedulePayment_shouldUpdateExistingSchedule() {
        paymentService.schedulePayment(
                1,
                LocalDate.of(2026,3,21)
        );
        paymentService.schedulePayment(
                1,
                LocalDate.of(2026,3,25)
        );
        assertEquals(1, paymentRepository.findAll().size());
        Payment payment = paymentRepository.findAll().get(0);
        assertEquals(
                LocalDate.of(2026,3,25),
                payment.getScheduledDate()
        );
    }
}
