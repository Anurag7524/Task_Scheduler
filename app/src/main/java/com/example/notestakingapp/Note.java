package com.example.notestakingapp;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Date;


@Entity(tableName ="my_notes")
public class Note {
    private String title;
    private String desc;

    private String date;

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Ignore
    public Note(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }
    public Note(String title,String desc,String date){
        this.title=title;
        this.desc=desc;
        this.date=date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
