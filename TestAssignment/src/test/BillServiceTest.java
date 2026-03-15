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
                LocalDate.now(),
                "EVN"
        ));

        billRepository.add(new Bill(
                2,
                "INTERNET",
                800000,
                LocalDate.now(),
                "VNPT"
        ));

        billService = new BillService(billRepository);
    }

    @Test
    public void getAllBills_shouldReturnAllBills() {
        List<Bill> bills = billService.getAllBills();
        assertEquals(2, bills.size());
    }

    @Test
    public void searchByProvider_shouldReturnMatchingBills() {
        List<Bill> bills = billService.searchByProvider("VNPT");

        assertEquals(1, bills.size());
        assertEquals("VNPT", bills.get(0).getProvider());
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
}
