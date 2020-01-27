package com.upp.naucnacentrala.repository;

import com.upp.naucnacentrala.model.OrderObject;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderObjectRepository extends JpaRepository<OrderObject, Long> {
}
