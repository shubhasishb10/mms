package com.mms.thp.utility;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Shubhasish
 */
public class ExcelPrepareUtil {

    private ExcelPrepareUtil(){}

    public static void prepareWorkbook(XSSFWorkbook workbook, List<?> actualData, List<String> headerRow, String tableName) {

        try {
            XSSFSheet sheet = workbook.createSheet(tableName);
            XSSFRow row = sheet.createRow(0);
            for(int i=0;i< headerRow.size();i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(headerRow.get(i));
            }
            for(var i = 1; i <= actualData.size(); i++) {
                row = sheet.createRow(i);
                Object currentObject = actualData.get(i-1);
                for(var j = 0; j<currentObject.getClass().getDeclaredFields().length; j++) {
                    XSSFCell cell = row.createCell(j);
                    cell.setCellType(CellType.STRING);
                    if(!headerRow.contains(currentObject.getClass().getDeclaredFields()[j].getName()))
                        continue;
                    cell.setCellValue(getValue(currentObject.getClass().getDeclaredFields()[j].getName(), currentObject));
                }
            }
            //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //workbook.write(outputStream);
            //return outputStream;
        }catch(Exception e) {
            e.printStackTrace();
        }
        //return null;
    }

    private static String getValue(String fieldName, Object object) {

        System.out.println("FieldName="+fieldName);
        Method method = Arrays.stream(object.getClass().getDeclaredMethods()).filter(m-> m.getName().toLowerCase().contains("get")).filter( m-> m.getName().toLowerCase().contains(fieldName.toLowerCase())).findAny().get();
        try {
            return String.valueOf(method.invoke(object));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void prepareExcel(Map<String, List<List<String>>> data, XSSFWorkbook workbook) {

        data.forEach(
                (tableName, tableDataList) -> {
                    XSSFSheet sheet = workbook.createSheet(tableName);
                    for(int i= 0 ;i< tableDataList.size(); i++) {
                        List<String> dataRow = tableDataList.get(i);
                        XSSFRow row = sheet.createRow(i);
                        for(int j=0;j< dataRow.size();j++) {
                            XSSFCell cell = row.createCell(j);
                            cell.setCellType(CellType.STRING);
                            if( i== 0) {
                                CellStyle cellStyle = workbook.createCellStyle();
                                //cellStyle.setFillBackgroundColor(IndexedColors.CORAL.getIndex());
                                cellStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());
                                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                cell.setCellStyle(cellStyle);
                            }
                            cell.setCellValue(dataRow.get(j));
                        }
                    }
                }
        );

        for(var i=0;i<workbook.getNumberOfSheets();i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            for(var j=0;j<numberOfRows;j++) {
                int noOfColumns = sheet.getRow(j).getPhysicalNumberOfCells();
                for(var k=0;k<noOfColumns;k++) {
                    sheet.autoSizeColumn(k);
                }
            }
        }
    }
}
