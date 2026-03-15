package test;

import main.models.Bill;
import main.repository.BillRepository;
import main.services.BillService;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class BillServiceTest {
    private BillService billService;
    private BillRepository billRepository;

    @Before
    public void setup() {
        billRepository = new BillRepository();
        billRepository.add(new Bill(
                1,
                "ELECTRIC",
                200000,
                LocalDate.of(2026,3,25),
                "EVN"
        ));

        billRepository.add(new Bill(
                2,
                "WATER",
                150000,
                LocalDate.of(2026,3,20),
                "SAVACO"
        ));

        billRepository.add(new Bill(
                3,
                "INTERNET",
                300000,
                LocalDate.of(2026,4,1),
                "VNPT"
        ));

        billService = new BillService(billRepository);
    }

    @Test
    public void getAllBills_shouldReturnAllBills() {
        List<Bill> bills = billService.getAllBills();
        assertEquals(3, bills.size());
    }

    @Test
    public void searchByProvider_shouldReturnMatchingBills() {
        String result = billService.searchBillByProvider("VNPT");
        System.out.println(result);
        String expected =
                "Bill No. Type Amount Due Date State PROVIDER\n" +
                        "1. INTERNET 300000 01/04/2026 NOT_PAID VNPT";

        assertEquals(expected.trim(), result.trim());
    }

    @Test
    public void getBill_shouldReturnBill_whenIdExists() {
        Bill bill = billService.getBill(1);

        assertNotNull(bill);
        assertEquals(1, bill.getId());
    }

    @Test
    public void getBill_shouldReturnNull_whenBillNotFound() {
        Bill bill = billService.getBill(99);
        assertNull(bill);
    }

    @Test
    public void dueDate_shouldReturnBillsSortedByDueDate() {
        String result = billService.getDueBills();
        String expected =
                "Bill No. Type Amount Due Date State PROVIDER\n" +
                        "1. WATER 150000 20/03/2026 NOT_PAID SAVACO\n" +
                        "2. ELECTRIC 200000 25/03/2026 NOT_PAID EVN\n" +
                        "3. INTERNET 300000 01/04/2026 NOT_PAID VNPT";

        assertEquals(expected.trim(), result.trim());
    }
}
