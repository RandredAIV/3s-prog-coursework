package com.coursework.autosalon.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coursework.autosalon.web.model.User;
import com.coursework.autosalon.web.model.UserCartItem;

public interface UserCartItemRepository extends JpaRepository<UserCartItem, Long> {

    List<UserCartItem> findByUser(User user);

    void deleteByUser(User user);

    void deleteByUserAndCarId(User user, Long carId);

    boolean existsByUserAndCarId(User user, Long carId);
}
