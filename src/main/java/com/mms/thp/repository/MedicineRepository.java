/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.repository;

import com.mms.thp.model.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long>, MedicineExtendedRepository {

    //TODO extra logic to do db operation
    List<Medicine> findMedicineByName(String name);
    List<Medicine> findMedicineByCompanyStringName(String company);
    List<Medicine> findMedicineByNameStartingWith(String name);
    //List<Medicine> findMedicineByBoxNumber(int boxNumber);
    Medicine findMedicineByNameAndCompanyStringNameAndVolume(String name, String company, int volume);

    Page<Medicine> findAllByOrderByNameAsc(Pageable pageable);

    List<Medicine> findAllByOrderByNameAsc();
    @Modifying
    @Query(value = "delete from medicine where medicineId = :id", nativeQuery = true)
    void deleteMedicineById(long id);

    @Query("select distinct(m.volume) from Medicine m order by m.volume asc")
    List<String> findAvailableMedicinesVolumes();
    @Query("select count(m) from Medicine m where m.volume = :volume")
    int findMedicineCountOfVolume(int volume);

    @Query("select m from Medicine m where m.volume = :volume order by m.name asc")
    Page<Medicine> findAllMedicineOfVolume(int volume, Pageable pageable);
    List<Medicine> findMedicineByNameEndsWithAndNameStartsWithOrderByNameAsc(String end, String start);
    List<Medicine> findMedicineByNameEndsWithOrderByNameAsc(String str);
}
