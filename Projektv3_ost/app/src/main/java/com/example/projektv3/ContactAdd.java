package com.example.projektv3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ContactAdd extends AppCompatActivity {
    public static final String EXTRA_TEXT="com.example.projektv3.EXTRA_TEXT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_add);

        Button button = findViewById(R.id.contact_button_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
    }

    public void openMainActivity() {
        EditText EditName = findViewById(R.id.Name);
        String Name=EditName.getText().toString();
        Intent intent2 = new Intent(this, MainActivity.class);
        intent2.putExtra("EXTRA_TEXT",Name);
        startActivity(intent2);
    }
}
