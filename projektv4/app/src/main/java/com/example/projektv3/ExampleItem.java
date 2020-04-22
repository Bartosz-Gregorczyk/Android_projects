package com.example.projektv3;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class ExampleItem implements Parcelable {
    private int mImageResource;
    private String mText;
    private String mPhone;
    private String mBirthday;

    public ExampleItem(int imageResource,String text,  String birthday,String phone) //konstruktor
    {
        mImageResource=imageResource; //avatar
        mText=text; //imie i nazwisko
        mBirthday = birthday;
        mPhone = phone;
    }

    protected ExampleItem(Parcel in) {
        mImageResource = in.readInt();
        mText = in.readString();
        mBirthday = in.readString();
       mPhone=in.readString();
    }

    public static final Creator<ExampleItem> CREATOR = new Creator<ExampleItem>() {
        @Override
        public ExampleItem createFromParcel(Parcel in) {
            return new ExampleItem(in);
        }

        @Override
        public ExampleItem[] newArray(int size) {
            return new ExampleItem[size];
        }
    };

    public int getmImageResource() {
        return mImageResource;
    }
    public String getmText() {
        return mText;
    }
    public String getmBirthday() {
        return mBirthday;
    }
    public String getmPhone() {
        return mPhone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mImageResource);
        dest.writeString(mText);
        dest.writeString(mBirthday);
        dest.writeString(mPhone);
    }
}
