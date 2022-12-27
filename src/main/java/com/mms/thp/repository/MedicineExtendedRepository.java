/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.repository;

import com.mms.thp.dto.SearchCriteria;
import com.mms.thp.model.Medicine;

import java.util.List;

public interface MedicineExtendedRepository {
    List<Medicine> searchMedicineByCriteria(SearchCriteria criteria);
    List<Medicine> findMedicinesByFirstLetter(String firstLetter);
}
