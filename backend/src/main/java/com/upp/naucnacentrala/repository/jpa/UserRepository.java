package com.upp.naucnacentrala.repository.jpa;

import com.upp.naucnacentrala.dto.FormSubmissionDto;
import com.upp.naucnacentrala.model.Reviewer;
import com.upp.naucnacentrala.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    User findOneByUsername(String username);

    @Query("select user from User user where user.firstName = :firstname and user.lastName = :lastname")
    User findOneByFirstNameAndLastName(@Param("firstname") String firstname, @Param("lastname") String lastname);

    @Query("select user from User user, Membership membership, Magazine magazine where membership.magazine.id = :id and membership.username = user.username")
    List<User> findAllMagazineId(@Param("id") Long id);

}
