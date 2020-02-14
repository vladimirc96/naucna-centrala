package com.upp.naucnacentrala.repository;

import com.upp.naucnacentrala.dto.OrderDTO;
import com.upp.naucnacentrala.model.OrderObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderObjectRepository extends JpaRepository<OrderObject, Long> {

    List<OrderObject> findAllByUserId(String username);

}
