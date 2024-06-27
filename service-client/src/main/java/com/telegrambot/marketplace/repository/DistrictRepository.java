package com.telegrambot.marketplace.repository;

import com.telegrambot.marketplace.entity.location.District;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Long> {
}
