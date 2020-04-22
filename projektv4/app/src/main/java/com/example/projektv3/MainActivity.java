package com.example.projektv3;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static ArrayList<ExampleItem> mExampleList=new ArrayList<ExampleItem>();        //tablica elementow
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton buttonAdd;
    private Button Add;
    public static final int BUTTON_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildRecyclerView();
        buttonAdd = findViewById(R.id.button_add);

//-------------------------------przykladowe kontakty-------------------------------------
        mExampleList.add(new ExampleItem(R.drawable.avatar3_foreground,"Jan Kowalski","12/12/2000","666888222"));
        mExampleList.add(new ExampleItem(R.drawable.avatar3_foreground,"Jan Nowak","01/02/1998","123123444"));
        mExampleList.add(new ExampleItem(R.drawable.avatar3_foreground,"John Smith","02/01/1990","123999222"));
//-----------------------------------------------------------------------------------------

//--------------------------Obsluga przejscia do dodawania kontaktow-----------------------
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //zamiana layoutu na dodawanie kontaktow
                Intent intent = new Intent(getApplicationContext(),ContactAdd.class);
                startActivityForResult(intent, BUTTON_REQUEST);
            }
        });
//------------------------------------------------------------------------------------------
    }

//---------------------------------usuwanie elementu----------------------------------------
    public void removeItem(int position){
        mExampleList.remove(position);
        mAdapter.notifyDataSetChanged();
    }
    //----------------------------------------------------------------------------------------



    //-----------------------------informacje o elemencie--------------------------------------
    public void detailsItem(int position){
        Intent intent = new Intent(MainActivity.this,ContactDetails.class);
        intent.putExtra("Example Item",mExampleList.get(position));
        startActivity(intent);
    }
    //----------------------------------------------------------------------------------------

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

//---------------------------Porcedura w przypadku dlugiego klikniecia------------------------------
        mAdapter.setOnItemLongClickListener(new ExampleAdapter.OnItemLongClickListener(){
            @Override
            //Wykonywanie polaczenia
            public void onCallClick(int position) {
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Zadzwonic do "+ mExampleList.get(position).getmText()+" ?")
                        .setPositiveButton("Tak",null)
                        .setNegativeButton("Nie",null)
                        .show();
            }
        });
//--------------------------------------------------------------------------------------------------

//-----------------------procedury w przypadku pojedynczego klikniecia------------------------------
        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            //Usuwanie kontaktow
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
            }

            @Override
            //Wyswietlanie info o kontakcie
            public void onItemClick(int position) {
                    detailsItem(position);


            }
      });
//--------------------------------------------------------------------------------------------------
    }

}
















