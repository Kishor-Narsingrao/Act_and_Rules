package com.example.samvid.myapplication;

public class BookIndexModel {
    int iBookId;
    int iBookIndexId;
    String strBookIndexName;

    public BookIndexModel(int strBookId,int iBookIndexId, String strBookIndexName)
    {
        this.iBookId=strBookId;
        this.iBookIndexId=iBookIndexId;
        this.strBookIndexName=strBookIndexName;
    }

    public int getiBookId() {
        return iBookId;
    }

    public void setiBookId(int iBookId) {
        this.iBookId = iBookId;
    }

    public int getiBookIndexId() {
        return iBookIndexId;
    }

    public void setiBookIndexId(int iBookIndexId) {
        this.iBookIndexId = iBookIndexId;
    }

    public String getStrBookIndexName() {
        return strBookIndexName;
    }

    public void setStrBookIndexName(String strBookIndexName) {
        this.strBookIndexName = strBookIndexName;
    }
}
