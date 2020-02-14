package com.upp.naucnacentrala.repository;

import com.upp.naucnacentrala.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findAllByMagazineId(Long id);

}
