package com.errang.app.kotlinandroiddemo.democomparejava.java;

import java.util.List;

public class JBookStore {

    private String name;

    private List<JBookCategory> categoryList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JBookCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<JBookCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
