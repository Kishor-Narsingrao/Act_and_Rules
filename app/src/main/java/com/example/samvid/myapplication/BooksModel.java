package com.example.samvid.myapplication;

public class BooksModel {
    int iBookId;
    String strBookName;

    public BooksModel(int strBookId,String strBookName)
    {
        this.iBookId=strBookId;
        this.strBookName=strBookName;
    }

    public int getStrBookId() {
        return iBookId;
    }

    public void setStrBookId(int strBookId) {
        this.iBookId = strBookId;
    }

    public String getStrBookName() {
        return strBookName;
    }

    public void setStrBookName(String strBookName) {
        this.strBookName = strBookName;
    }
}
