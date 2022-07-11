package com.example.demo;

public enum WebPages {
    HOME("home"),
    MEDICINE_DETAIL("medicine_detail"),
    ADD_MEDICINE("add_medicine"),
    SEARCH_MEDICINE("search_medicine"),
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
