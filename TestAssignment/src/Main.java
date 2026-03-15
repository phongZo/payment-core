import main.models.Bill;
import main.models.User;
import main.repository.BillRepository;
import main.repository.PaymentRepository;
import main.scheduler.PaymentScheduler;
import main.services.BillService;
import main.services.PaymentService;
import main.services.UserService;
import main.utils.DateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BillRepository billRepository = new BillRepository();
        PaymentRepository paymentRepository = new PaymentRepository();

        UserService userService = new UserService(new User());
        BillService billService = new BillService(billRepository);
        PaymentService paymentService =
                new PaymentService(userService, billRepository, paymentRepository);

        initBillsData(billRepository);

        new PaymentScheduler(paymentService);

        while (true) {
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            String command = parts[0];

            switch (command) {
                case "CASH_IN":
                    long amount = Long.parseLong(parts[1]);
                    userService.increaseBalance(amount);
                    System.out.println("Your available balance: " +
                            userService.getBalance());
                    break;

                case "LIST_BILL":
                    System.out.println(billService.listBills());
                    break;

                case "PAY":
                    List<Integer> billIds = new ArrayList<>();
                    for (int i = 1; i < parts.length; i++) {
                        billIds.add(Integer.parseInt(parts[i]));
                    }
                    System.out.println(paymentService.payBills(billIds));
                    break;

                case "SCHEDULE":
                    int billId = Integer.parseInt(parts[1]);
                    LocalDate date = LocalDate.parse(
                            parts[2],
                            DateUtils.FORMATTER
                    );
                    System.out.println(paymentService.schedulePayment(billId, date));
                    break;

                case "DUE_DATE":
                    System.out.println(billService.getDueBills());
                    break;

                case "LIST_PAYMENT":
                    System.out.println(paymentService.listPayments());
                    break;

                case "SEARCH_BILL_BY_PROVIDER":
                    String provider = parts[1];
                    System.out.println(billService.searchBillByProvider(provider));
                    break;

                case "EXIT":
                    System.out.println("Good bye!");
                    return;

                default:
                    System.out.println("Unknown command");
            }
        }
    }

    private static void initBillsData(BillRepository repo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        repo.add(new Bill(1, "ELECTRIC", 200000, LocalDate.parse("25/10/2020", formatter), "EVN"));
        repo.add(new Bill(2, "WATER", 175000, LocalDate.parse("30/10/2020", formatter), "SAVACO"));
        repo.add(new Bill(3, "INTERNET", 800000, LocalDate.parse("30/11/2020", formatter), "VNPT"));
    }
}