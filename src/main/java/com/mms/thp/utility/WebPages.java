package com.mms.thp.utility;

public enum WebPages {
    HOME("home"),
    MEDICINE_DETAIL("medicine_detail"),
    ADD_MEDICINE("add_medicine"),
    SEARCH_MEDICINE("search_medicine"),
    UPLOAD_FILE("upload_file"),
    BOX_LIST("box_list"),
    MEDICINE_CENSUS("medicine_census"),
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
