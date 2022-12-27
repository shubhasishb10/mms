/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shubhasish
 */
public interface DatabaseBackupService<T> {

    List<String> invalidAnnotations = List.of("OneToMany", "ManyToOne", "OneToOne", "ManyToMany", "Transient");


    default List<T> getTableData() {
        return getRepository().findAll();
    }

    default List<String> getTableHeaders() {
        Field[] fields = getTableName().getDeclaredFields();
        return Arrays.stream(fields).filter(this::validateFiled).map(Field::getName).collect(Collectors.toList());
    }

    default boolean validateFiled(Field field) {
        List<Annotation> annotations = Arrays.stream(field.getAnnotations()).collect(Collectors.toList());
        return annotations.stream().map(Annotation::annotationType).map(Class::getSimpleName).noneMatch(invalidAnnotations::contains);
    }


    Class<T> getTableName();
    JpaRepository<T, ?> getRepository();
    void prepareExcelData(XSSFWorkbook workbook);
}
