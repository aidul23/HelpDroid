package com.example.helpdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailsListActivity extends AppCompatActivity {

    ImageView callButton;
    public static final int REQUEST_CALL = 1;
    ArrayList<Pojo> list = new ArrayList<>();
    Pojo pojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);

        //Static data for test.
        list.add(new Pojo("Arif","013456789"));
        list.add(new Pojo("Sadman","013456789"));
        list.add(new Pojo("Rakib","013456789"));

        RecyclerView recyclerView = findViewById(R.id.policeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        Adapter adapter = new Adapter(list);
        recyclerView.setAdapter(adapter);

        //for call intent
        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Pojo pojo) {
                String number = pojo.getNumber();
                if(number.trim().length() > 0){
                    if(ContextCompat.checkSelfPermission(DetailsListActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(DetailsListActivity.this,new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    }else{
                        String dial = "tel:" + number;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }
                }else{
                    Toast.makeText(DetailsListActivity.this, "Invalid Number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                String number = pojo.getNumber();
                if(number.trim().length() > 0){
                    if(ContextCompat.checkSelfPermission(DetailsListActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(DetailsListActivity.this,new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    }else{
                        String dial = "tel:" + number;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }
                }else{
                    Toast.makeText(DetailsListActivity.this, "Invalid Number!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

