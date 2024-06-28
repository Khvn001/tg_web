package com.telegrambot.marketplace.service.entity.impl;

import com.telegrambot.marketplace.entity.location.City;
import com.telegrambot.marketplace.entity.location.Country;
import com.telegrambot.marketplace.entity.location.District;
import com.telegrambot.marketplace.repository.DistrictRepository;
import com.telegrambot.marketplace.service.entity.DistrictService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    @Override
    public District findById(final Long productId) {
        return districtRepository.findById(productId).orElse(null);
    }

    @Override
    public District findByCountryAndCityAndName(final Country country, final City city, final String name) {
        return districtRepository.findByCountryAndCityAndNameAndAllowedIsTrue(country, city, name).orElse(null);
    }
}
