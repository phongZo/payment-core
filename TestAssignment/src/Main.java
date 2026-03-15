import main.models.Bill;
import main.repository.BillRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BillRepository billRepository = new BillRepository();

        initBillsData(billRepository);
    }


    private static void initBillsData(BillRepository repo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        repo.add(new Bill(1, "ELECTRIC", 200000, LocalDate.parse("25/10/2020", formatter), "EVN"));
        repo.add(new Bill(2, "WATER", 175000, LocalDate.parse("30/10/2020", formatter), "SAVACO"));
        repo.add(new Bill(3, "INTERNET", 800000, LocalDate.parse("30/11/2020", formatter), "VNPT"));
    }
}