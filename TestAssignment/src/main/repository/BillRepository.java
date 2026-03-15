package main.repository;

import main.models.Bill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillRepository {
    private Map<Long, Bill> bills = new HashMap<>();

    public void add(Bill bill) {
        bills.put(bill.getId(), bill);
    }

    public Bill findById(long id) {
        return bills.get(id);
    }

    public List<Bill> findAll() {
        return new ArrayList<>(bills.values());
    }
}
