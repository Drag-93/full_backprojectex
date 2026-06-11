package org.spring.backendprojectex.open.weather.repository;

import org.spring.backendprojectex.open.weather.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity,Long> {
    Optional<WeatherEntity> findByName(String name);
}
