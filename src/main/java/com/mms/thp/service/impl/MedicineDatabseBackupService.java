/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.service.impl;

import com.mms.thp.model.Medicine;
import com.mms.thp.repository.MedicineRepository;
import com.mms.thp.service.DatabaseBackupService;
import com.mms.thp.utility.ExcelPrepareUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

/**
 * @author Shubhasish
 */
@Service
public class MedicineDatabseBackupService implements DatabaseBackupService<Medicine> {

    @Autowired
    MedicineRepository medicineRepository;

    @Override
    public Class<Medicine> getTableName() {
        return Medicine.class;
    }

    @Override
    public JpaRepository<Medicine, ?> getRepository() {
        return medicineRepository;
    }

    @Override
    public void prepareExcelData(XSSFWorkbook workbook) {
        ExcelPrepareUtil.prepareWorkbook(workbook, getTableData(), getTableHeaders(), Medicine.class.getSimpleName());
    }
}
