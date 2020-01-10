package com.upp.naucnacentrala.repository;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    User findOneByUsername(String username);
}
