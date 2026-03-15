package main.services;

import main.models.Bill;
import main.repository.BillRepository;

import java.util.List;

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

    public List<Bill> searchByProvider(String provider) {
        return billRepository.findByProvider(provider);
    }
}
