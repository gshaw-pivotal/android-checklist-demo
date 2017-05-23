package com.example.pivotal.checklist.models;


import java.io.Serializable;
import java.util.List;

public class Checklist implements Serializable {

    private int id;
    private String name;
    private List<ChecklistItem> checklistItemList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChecklistItem> getChecklistItemList() {
        return checklistItemList;
    }

    public void setChecklistItemList(List<ChecklistItem> checklistItemList) {
        this.checklistItemList = checklistItemList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
