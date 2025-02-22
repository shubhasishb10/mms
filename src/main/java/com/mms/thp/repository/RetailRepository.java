/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.repository;

import com.mms.thp.model.Retail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface RetailRepository extends JpaRepository<Retail, Long> {

    //@Query(value = "select retailId,retailDate from retail group by retailDate order by retailDate desc", nativeQuery = true)
    Page<Retail> findAllByOrderByRetailDateDesc(Pageable pageable);
    @Query(value = "select count(*) from (select r.retailDate from retail r group by r.retailDate) a", nativeQuery = true)
    long findAllRecordGroupByDate();
    List<Retail> findRetailByRetailDate(Date date);
}
