package com.mms.thp.service.impl;

import com.mms.thp.model.Box;
import com.mms.thp.repository.BoxRepository;
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
public class BoxDatabaseBackupService implements DatabaseBackupService<Box> {

    @Autowired
    private BoxRepository boxRepository;

    @Override
    public Class<Box> getTableName() {
        return Box.class;
    }

    @Override
    public JpaRepository<Box, ?> getRepository() {
        return boxRepository;
    }

    @Override
    public void prepareExcelData(XSSFWorkbook workbook) {
        ExcelPrepareUtil.prepareWorkbook(workbook, getTableData(), getTableHeaders(), Box.class.getSimpleName());
    }

}
