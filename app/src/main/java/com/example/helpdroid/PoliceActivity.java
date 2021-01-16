package com.example.helpdroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class PoliceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police);

        ArrayList<Pojo> list = new ArrayList<>();
        list.add(new Pojo("Aidul","01679733976"));
        list.add(new Pojo("Aidul","01679733976"));
        list.add(new Pojo("Aidul","01679733976"));

        RecyclerView recyclerView = findViewById(R.id.policeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        Adapter adapter = new Adapter(list);
        recyclerView.setAdapter(adapter);
    }
}