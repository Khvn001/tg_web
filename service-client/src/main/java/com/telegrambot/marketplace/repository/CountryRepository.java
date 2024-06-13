package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.location.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}