package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.location.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
}