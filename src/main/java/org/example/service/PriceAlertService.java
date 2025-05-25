package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.PriceAlert;
import org.example.model.Product;
import org.example.repository.PriceAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PriceAlertService {

    private final PriceAlertRepository alertRepository;
    private final EmailService emailService;

    public void checkAlerts(Product product, double newPrice) {
        List<PriceAlert> alerts = alertRepository.findByProductIdAndActiveTrue(product.getId());

        for (PriceAlert alert : alerts) {
            if (newPrice <= alert.getTargetPrice()) {
                emailService.sendPriceDropAlert(alert.getUserEmail(), product.getName(), newPrice);

                alert.setActive(false);
                alertRepository.save(alert);
            }
        }
    }
}
