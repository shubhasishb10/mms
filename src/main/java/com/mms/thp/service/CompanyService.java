/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.service;

import com.mms.thp.model.Company;

import java.util.List;

public interface CompanyService {
    Company findCompanyByName(String name);
    List<Company> findCompanies(int page, int size);
}
