package com.upp.naucnacentrala.repository.jpa;

import com.upp.naucnacentrala.model.Magazine;
import com.upp.naucnacentrala.model.ScienceField;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    Magazine findOneById(Long id);

    @Query("select magazine from Magazine magazine where magazine.chiefEditor.username = :username")
    List<Magazine> findAllByChiefEditor(@Param("username") String username);

    Magazine findBySellerId(long sellerId);

    Magazine findByName(String name);

}
