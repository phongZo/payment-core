package main.scheduler;

import main.services.PaymentService;

import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PaymentScheduler {
    private ScheduledExecutorService scheduler;

    public PaymentScheduler(PaymentService paymentService) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            paymentService.processScheduledPayments(LocalDate.now());
        }, 0, 1, TimeUnit.MINUTES);
    }
}
