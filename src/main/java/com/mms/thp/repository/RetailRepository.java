package com.mms.thp.repository;

import com.mms.thp.model.Retail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface RetailRepository extends JpaRepository<Retail, Long> {

    @Query(value = "select * from retail group by retailDate order by retailDate desc", nativeQuery = true)
    Page<Retail> findAllByOrderByRetailDateDesc(Pageable pageable);
    @Query(value = "select count(*) from (select r.* from retail r group by r.retailDate) a", nativeQuery = true)
    long findAllRecordGroupByDate();
    List<Retail> findRetailByRetailDate(Date date);
}
