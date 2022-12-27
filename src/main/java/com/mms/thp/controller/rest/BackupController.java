/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.controller.rest;

import com.mms.thp.service.impl.BoxDatabaseBackupService;
import com.mms.thp.service.impl.CompanyDatabaseBackupService;
import com.mms.thp.service.impl.MedicineBoxDatabaseBackupService;
import com.mms.thp.service.impl.MedicineDatabseBackupService;
import com.mms.thp.service.impl.RetailDatabaseBackupService;
import com.mms.thp.service.impl.RetailMedicineDatabaseBackupService;
import com.mms.thp.service.impl.SimpleDatabaseBackupService;
import com.mms.thp.utility.ExcelPrepareUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Shubhasish
 */
@RestController
@RequestMapping("/backup")
public class BackupController {

    @Autowired
    BoxDatabaseBackupService boxDatabaseBackupService;
    @Autowired
    CompanyDatabaseBackupService companyDatabaseBackupService;
    @Autowired
    MedicineBoxDatabaseBackupService medicineBoxDatabaseBackupService;
    @Autowired
    MedicineDatabseBackupService medicineDatabseBackupService;
    @Autowired
    RetailDatabaseBackupService retailDatabaseBackupService;
    @Autowired
    RetailMedicineDatabaseBackupService retailMedicineDatabaseBackupService;

    @Autowired
    SimpleDatabaseBackupService simpleDatabaseBackupService;

    @GetMapping
    public void backupTables(HttpServletResponse response) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();

        boxDatabaseBackupService.prepareExcelData(workbook);
        companyDatabaseBackupService.prepareExcelData(workbook);
        medicineBoxDatabaseBackupService.prepareExcelData(workbook);
        medicineDatabseBackupService.prepareExcelData(workbook);
        retailDatabaseBackupService.prepareExcelData(workbook);
        retailMedicineDatabaseBackupService.prepareExcelData(workbook);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=file.xlsx");
        outputStream.writeTo(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/basic")
    public void simpleBackupTables(HttpServletResponse response) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        ExcelPrepareUtil.prepareExcel(simpleDatabaseBackupService.getData(), workbook);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=file.xlsx");
        try {
            workbook.write(outputStream);
            outputStream.writeTo(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
