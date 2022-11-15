package com.mms.thp.service.impl;

import com.mms.thp.model.Company;
import com.mms.thp.repository.CompanyRepository;
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
public class CompanyDatabaseBackupService implements DatabaseBackupService<Company> {

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public Class<Company> getTableName() {
        return Company.class;
    }

    @Override
    public JpaRepository<Company, ?> getRepository() {
        return companyRepository;
    }

    @Override
    public void prepareExcelData(XSSFWorkbook workbook) {
        ExcelPrepareUtil.prepareWorkbook(workbook, getTableData(), getTableHeaders(), Company.class.getSimpleName());
    }
}
