/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Shubhasish
 */

@Service
public class SimpleDatabaseBackupService {

    private final Map<String, List<List<String>>> excelData = new HashMap<>();
    private final Set<String> tablesNames = Set.of("medicine", "box", "medicine_box", "company", "retail", "retail_medicine");

    @Autowired
    protected EntityManager entityManager;

    public Map<String, List<List<String>>> getData() {

        tablesNames.forEach(
                tableName -> {
                    ColumnExtractor extractor = new ColumnExtractor(tableName);
                    try {
                        List<List<String>> data = extractor.getTableDataWithHeader();
                        excelData.put(tableName, data);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return excelData;
    }


    class ColumnExtractor {

        private final String QUERY;
        private final String tableName;
        private final String schema = "thp";

        ColumnExtractor(String tableName) {
            QUERY = "select * from " + tableName;
            this.tableName = tableName;
        }

        public List<List<String>> getTableDataWithHeader() throws SQLException {

            Query query = entityManager.createNativeQuery("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='" + this.schema + "' AND TABLE_NAME='" + this.tableName + "'");

            List<String> headers = query.getResultList();

            Query query1 = entityManager.createNativeQuery(QUERY);
            List<Object[]> result = query1.getResultList();
            List<List<String>> valueList = result.stream()
                    .map(Arrays::asList)
                    .map(x -> x.stream().map(String::valueOf).collect(Collectors.toList()))
                    .collect(Collectors.toList());

            valueList.add(0, headers.stream().map(String::toUpperCase).collect(Collectors.toList()));
            return valueList;
        }
    }
}
