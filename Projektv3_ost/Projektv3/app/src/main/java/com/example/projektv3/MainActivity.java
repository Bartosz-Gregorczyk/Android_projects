package com.example.projektv3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ExampleItem> mExampleList;

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createExampleList();
        buildRecyclerView();

        buttonAdd = findViewById(R.id.button_add);

        buttonAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //zamiana layoutu na dodawanie kontaktow
                Intent intent = new Intent(v.getContext(),ContactAdd.class);
                startActivity(intent);
            }
        });
    }

    public void removeItem(int position){
        mExampleList.remove(position);
        mAdapter.notifyDataSetChanged();
    }

    public void createExampleList(){
        mExampleList = new ArrayList<>(); //przykladowa lista
        mExampleList.add(new ExampleItem(R.drawable.avatar1_foreground, "Karol Wajs")); //dodanie przykladowego elementu do listy
        mExampleList.add(new ExampleItem(R.drawable.avatar2_foreground,"Piotr Szatkowski"));
        mExampleList.add(new ExampleItem(R.drawable.avatar3_foreground,"Roksi Waszka"));
    }


    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {

                removeItem(position);
            }
        });

    }
}
