package main.services;

import main.models.Bill;
import main.repository.BillRepository;
import main.utils.DateUtils;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BillService {
    private BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill getBill(int id) {
        return billRepository.findById(id);
    }

    public String searchBillByProvider(String provider) {
        List<Bill> bills = billRepository.findAll()
                .stream()
                .filter(b -> b.getProvider().equalsIgnoreCase(provider))
                .collect(Collectors.toList());

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        StringBuilder output = new StringBuilder();
        output.append("Bill No. Type Amount Due Date State PROVIDER\n");

        int index = 1;
        for (Bill bill : bills) {
            output.append(index).append(". ")
                    .append(bill.getType()).append(" ")
                    .append(bill.getAmount()).append(" ")
                    .append(bill.getDueDate().format(formatter)).append(" ")
                    .append(bill.getState()).append(" ")
                    .append(bill.getProvider())
                    .append("\n");

            index++;
        }
        return output.toString().trim();
    }

    public String getDueBills() {
        List<Bill> bills = billRepository.findAll()
                .stream()
                .filter(b -> b.getState() == Bill.BillState.NOT_PAID)
                .sorted(Comparator.comparing(Bill::getDueDate))
                .collect(Collectors.toList());

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        StringBuilder output = new StringBuilder();
        output.append("Bill No. Type Amount Due Date State PROVIDER\n");

        int index = 1;
        for (Bill bill : bills) {
            output.append(index).append(". ")
                    .append(bill.getType()).append(" ")
                    .append(bill.getAmount()).append(" ")
                    .append(bill.getDueDate().format(formatter)).append(" ")
                    .append(bill.getState()).append(" ")
                    .append(bill.getProvider())
                    .append("\n");

            index++;
        }
        return output.toString().trim();
    }


    public String listBills() {
        List<Bill> bills = billRepository.findAll();

        if (bills.isEmpty()) {
            return "No bills found";
        }
        StringBuilder output = new StringBuilder();
        output.append("Bill No. Type Amount Due Date State PROVIDER\n");

        int index = 1;
        for (Bill bill : bills) {
            output.append(index).append(". ")
                    .append(bill.getType()).append(" ")
                    .append(bill.getAmount()).append(" ")
                    .append(bill.getDueDate().format(DateUtils.FORMATTER)).append(" ")
                    .append(bill.getState()).append(" ")
                    .append(bill.getProvider())
                    .append("\n");

            index++;
        }
        return output.toString().trim();
    }
}
