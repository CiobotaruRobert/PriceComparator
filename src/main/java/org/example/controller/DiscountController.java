package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.DiscountInfoDto;
import org.example.service.DiscountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping("/best")
    public List<DiscountInfoDto> getBestDiscounts(@RequestParam(required = false) String date,
                                                  @RequestParam(defaultValue = "10") int limit) {
        LocalDate queryDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        return discountService.getBestDiscounts(queryDate, limit);
    }

    @GetMapping("/new")
    public List<DiscountInfoDto> getNewlyAddedDiscountDetails() {
        LocalDate today = LocalDate.now();
        return discountService.getNewDiscounts(today);
    }
}
