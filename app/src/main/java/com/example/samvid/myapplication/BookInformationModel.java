package com.example.samvid.myapplication;

public class BookInformationModel {
    int iBookInfoId;
    int iBookIndexId;
    String strBookInfoName;
    int iParentBookInfoId;
    int iTypeId;

    public BookInformationModel(int iBookInfoId,int iBookIndexId,String strBookInfoName,int iParentBookInfoId,int iTypeId)
    {
        this.iBookInfoId=iBookInfoId;
        this.iBookIndexId=iBookIndexId;
        this.strBookInfoName=strBookInfoName;
        this.iParentBookInfoId=iParentBookInfoId;
        this.iTypeId=iTypeId;
    }

    public int getiBookInfoId() {
        return iBookInfoId;
    }

    public void setiBookInfoId(int iBookInfoId) {
        this.iBookInfoId = iBookInfoId;
    }

    public int getiBookIndexId() {
        return iBookIndexId;
    }

    public void setiBookIndexId(int iBookIndexId) {
        this.iBookIndexId = iBookIndexId;
    }

    public String getStrBookInfoName() {
        return strBookInfoName;
    }

    public void setStrBookInfoName(String strBookInfoName) {
        this.strBookInfoName = strBookInfoName;
    }

    public int getiParentBookInfoId() {
        return iParentBookInfoId;
    }

    public void setiParentBookInfoId(int iParentBookInfoId) {
        this.iParentBookInfoId = iParentBookInfoId;
    }

    public int getiTypeId() {
        return iTypeId;
    }

    public void setiTypeId(int iTypeId) {
        this.iTypeId = iTypeId;
    }
}
