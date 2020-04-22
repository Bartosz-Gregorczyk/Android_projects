package com.example.projektv3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
public class ContactDetails extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        Intent intent = getIntent();
        ExampleItem exampleItem = intent.getParcelableExtra("Example Item");

        int image = exampleItem.getmImageResource();
        String text = exampleItem.getmText();
        String birthday = exampleItem.getmBirthday();
        String phone_number = exampleItem.getmPhone();

        ImageView imageView = findViewById(R.id.imageView_details);
        imageView.setImageResource(image);

        TextView textView = findViewById(R.id.name_surname_details);
        textView.setText(text);

        TextView birthdayView = findViewById(R.id.birthday_details);
        birthdayView.setText("Birthday: " + birthday);

        TextView phoneView=findViewById(R.id.phone_details);
        phoneView.setText("Phone Number: " + phone_number);

    }
}
