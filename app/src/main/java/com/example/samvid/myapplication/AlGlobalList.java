package com.example.samvid.myapplication;

import android.app.Application;

import java.util.ArrayList;

public class AlGlobalList extends Application {
    private ArrayList<BooksModel> AlBooks=new ArrayList<BooksModel>();
    private ArrayList<BookIndexModel> AlBookIndex=new ArrayList<BookIndexModel>();
    private ArrayList<BookInformationModel> AlBookInformation=new ArrayList<BookInformationModel>();

    //Books model
    public BooksModel getBooks(int aPosition)
    {
        return AlBooks.get(aPosition);
    }

    public void setBooks(BooksModel Product)
    {
        AlBooks.add(Product);
    }

    public  int getBooksSize(){ return  AlBooks.size();}


//BookIndex Model
    public BookIndexModel getBookIndex(int aPosition)
    {
        return AlBookIndex.get(aPosition);
    }

    public void setBookIndex(BookIndexModel Product)
    {
        AlBookIndex.add(Product);
    }

    public  int getBookIndexSize(){ return  AlBookIndex.size();}


    //BookInformation Model
    public BookInformationModel getBookInformation(int aPosition)
    {
        return AlBookInformation.get(aPosition);
    }

    public void setBookInformation(BookInformationModel Product)
    {
        AlBookInformation.add(Product);
    }

    public  int getBookInformationSize(){ return  AlBookInformation.size();}
}
