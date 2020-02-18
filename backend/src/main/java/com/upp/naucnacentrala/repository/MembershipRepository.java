package com.upp.naucnacentrala.repository;

import com.upp.naucnacentrala.model.Membership;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

    List<Membership> findAllByMagazineId(Long id);

    @Query("select membership from Membership membership where membership.magazine = :magazineId and membership.agreementId = :agrID")
    Membership findByAgrAndCas(@Param("magazineId") Long magazineId,@Param("agrID") Long agrID);

}
