/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

package com.mms.thp.utility;

public enum WebPages {
    HOME("home"),
    MEDICINE_DETAIL("medicine_detail"),
    ADD_MEDICINE("add_medicine"),
    SEARCH_MEDICINE("search_medicine"),
    UPLOAD_FILE("upload_file"),
    BOX_LIST("box_list"),
    ML_LIST("ml_list"),
    SELL_REPORT("sell_report"),
    MOTHER_LIST("mother-list"),
    MEDICINE_CENSUS("medicine_census"),
    REDIRECT_TO_LIST("redirect:/mvc/medicine/list"),
    INDEX("index");
    final String pageName;
    WebPages(String pageName) {
        this.pageName = pageName;
    }
    @Override
    public String toString(){
        return this.pageName;
    }
}
