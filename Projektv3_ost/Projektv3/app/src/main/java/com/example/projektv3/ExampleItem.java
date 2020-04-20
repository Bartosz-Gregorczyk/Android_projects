package com.example.projektv3;

public class ExampleItem {
    private int mImageResource;
    private String mText;


    public ExampleItem(int imageResource,String text) //konstruktor
    {
        mImageResource=imageResource; //avatar
        mText=text; //imie i nazwisko
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getmText() {
        return mText;
    }
}
