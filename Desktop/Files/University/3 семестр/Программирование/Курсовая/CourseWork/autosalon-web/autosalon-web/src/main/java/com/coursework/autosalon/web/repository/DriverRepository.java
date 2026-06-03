package com.coursework.autosalon.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coursework.autosalon.web.model.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
