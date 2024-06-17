package com.telegrambot.marketplace.service.entity;

import com.telegrambot.marketplace.entity.location.City;

import java.util.List;

public interface CityService {

    List<City> findByCountryIdAndAllowed(Long countryId);

    City findById(Long countryId);
}
