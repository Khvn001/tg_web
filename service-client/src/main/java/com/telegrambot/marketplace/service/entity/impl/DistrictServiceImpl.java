package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.repository.DistrictRepository;
import com.telegrambot.marketplace.service.entity.DistrictService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

@Service
@AllArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    @Override
    public District findById(Long productId) {
        return districtRepository.findById(productId).orElse(null);
    }
}
