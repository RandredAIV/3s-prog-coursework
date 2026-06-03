package com.coursework.autosalon.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coursework.autosalon.web.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
