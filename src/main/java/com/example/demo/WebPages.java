package com.example.demo;

public enum WebPages {
    HOME("home"),
    MEDICINE("medicine"),
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
