package com.example.projektv3;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ExampleItem> mExampleList;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton buttonAdd;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createExampleList();
        buildRecyclerView();

        buttonAdd = findViewById(R.id.button_add);
        textView = findViewById(R.id.textView);

        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //zmiana layoutu - dodawanie kontaktow
                Intent intent = new Intent(v.getContext(),ContactAdd.class);
                startActivity(intent);
            }
        });
    }
        //usuwanie kontaktu
    public void removeItem(int position){
        mExampleList.remove(position);
        mAdapter.notifyDataSetChanged();
    }
        //informacje o kontakcie
    public void detailsItem(int position){
        Intent intent = new Intent(MainActivity.this,ContactDetails.class);
        intent.putExtra("Example Item",mExampleList.get(position));
        startActivity(intent);
    }

    public void createExampleList(){
        mExampleList = new ArrayList<>(); //przykladowa lista

        mExampleList.add(new ExampleItem(generateImage(), "Jan Kowalski")); //dodanie przykladowego elementu do listy
        mExampleList.add(new ExampleItem(generateImage(),"Jan Nowak"));
        mExampleList.add(new ExampleItem(generateImage(),"Marek Nowak"));
       // String Imie=getIntent().getExtras().getString("EXTRA_TEXT");
       // mExampleList.add(new ExampleItem(generateImage(),Imie));
    }
    public int generateImage()
    {
        int[] pictures = new int[3];
        int number;
        pictures[0]=R.drawable.avatar1_foreground;
        pictures[1]=R.drawable.avatar2_foreground;
        pictures[2]=R.drawable.avatar3_foreground;
        Random generator = new Random();
        number=generator.nextInt(3);
        return pictures[number];
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //usuwanie elementow
        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(final int position) {
                //okno dialogowe
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Usuwanie")
                        .setMessage("Czy chcesz usunać kontakt?")
                        .setPositiveButton("Tak",null)
                        .setNegativeButton("Nie",null)
                        .show();

                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(position);
                        dialog.hide();
                        Toast.makeText(MainActivity.this,"Pomyslnie usunales kontakt",Toast.LENGTH_SHORT).show();
                    }
                });
                //removeItem(position);
            }
            @Override
            public void onItemClick(int position) {
                detailsItem(position);
            }
        });


    }
}
