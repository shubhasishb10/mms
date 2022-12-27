/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.repository;

import com.mms.thp.model.RetailMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RetailMedicineRepository extends JpaRepository<RetailMedicine, Long> {

    @Modifying
    @Query(value = "update retail_medicine set medicineId = :newMedicineId where medicineId=:oldMedicineId", nativeQuery = true)
    public void updateMedicineIdOfDeletedMedicine(long newMedicineId, long oldMedicineId);
}
