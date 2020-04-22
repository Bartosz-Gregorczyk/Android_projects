package com.example.projektv3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.Random;

import static com.example.projektv3.MainActivity.mExampleList;

public class ContactAdd extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_add);
        EditText EditBirthday = findViewById(R.id.Birthday);
        new DateInputMask(EditBirthday);

        Button button = findViewById(R.id.contact_button_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText EditName = findViewById(R.id.Name);
                EditText EditSurname = findViewById(R.id.Surname);
                EditText EditBirthday = findViewById(R.id.Birthday);
                EditText EditPhone = findViewById(R.id.PhoneNumber);

                String Name = EditName.getText().toString();
                String Surname = EditSurname.getText().toString();
                String Birthday = EditBirthday.getText().toString();
                String Phone=EditPhone.getText().toString();
                Intent data=new Intent();

                if(Name.isEmpty())
                    Toast.makeText(getApplicationContext(), "Wpisz nazwe kontaktu!", Toast.LENGTH_SHORT).show();
                else if(Phone.length()!=9)
                    Toast.makeText(getApplicationContext(), "Niepoprawny numer telefonu!", Toast.LENGTH_SHORT).show();
                else {
                    mExampleList.add(new ExampleItem(generateImage(), Name + " " + Surname,Birthday,Phone));
                    finish();
                }
            }
        });
    }

    public int generateImage()   //Generuje losowy avatar
    {
        int[] pictures = new int[10];
        int number;
        pictures[0]=R.drawable.avatar1_foreground;
        pictures[1]=R.drawable.avatar2_foreground;
        pictures[2]=R.drawable.avatar3_foreground;
        pictures[3]=R.drawable.avatar4_foreground;
        pictures[4]=R.drawable.avatar5_foreground;
        pictures[5]=R.drawable.avatar6_foreground;
        pictures[6]=R.drawable.avatar7_foreground;
        pictures[7]=R.drawable.avatar8_foreground;
        pictures[8]=R.drawable.avatar9_foreground;
        pictures[9]=R.drawable.avatar10_foreground;
        Random generator = new Random();
        number=generator.nextInt(10);
        return pictures[number];
    }
    }

