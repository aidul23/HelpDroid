package com.example.helpdroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.helpdroid.R;
import com.example.helpdroid.adapter.Adapter;
import com.example.helpdroid.model.Pojo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailsListPoliceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView callButton;
    public static final int REQUEST_CALL = 1;
    List<Pojo> list = new ArrayList<>();
    String division;
    Pojo pojo;
    Adapter adapter;
    RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "result";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list_police);

        //Static data for test.
//        list.add(new Pojo("Memon City Corporation","+880-31-621799","55, Sadarghat Road Chittagong"));
//        list.add(new Pojo("Chattagram Maa-Shishu General Hospital","+880-31-2520063","55, Sadarghat Road Chittagong"));
//        list.add(new Pojo("Chittagong Metropolitan Hospital Pvt Ltd.","+88-031-654732","55, Sadarghat Road Chittagong"));

        //spinner
        Spinner spinner = findViewById(R.id.division_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.divisionName, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        //recyclerView
        recyclerView = findViewById(R.id.policeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        division = spinner.getSelectedItem().toString();
        Log.d(TAG, "onCreate: "+division);

        db.collection("Hospitals")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Pojo pojo = new Pojo(
                                    documentSnapshot.getData().get("division").toString(),
                                    documentSnapshot.getData().get("hospitalAddress").toString(),
                                    documentSnapshot.getData().get("hospitalName").toString(),
                                    documentSnapshot.getData().get("hospitalNumber").toString()
                            );
                            list.add(pojo);
                            Log.d(TAG, "onSuccess: " + documentSnapshot);
                        }
                        adapter = new Adapter(list);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        //for call intent
                        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
                            @Override
                            public void OnItemClick(Pojo pojo) {
                                String number = pojo.getNumber();
                                if (number.trim().length() > 0) {
                                    if (ContextCompat.checkSelfPermission(DetailsListPoliceActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(DetailsListPoliceActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                                    } else {
                                        String dial = "tel:" + number;
                                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                                    }
                                } else {
                                    Toast.makeText(DetailsListPoliceActivity.this, "Invalid Number!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String number = pojo.getNumber();
                if (number.trim().length() > 0) {
                    if (ContextCompat.checkSelfPermission(DetailsListPoliceActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DetailsListPoliceActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                    } else {
                        String dial = "tel:" + number;
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                    }
                } else {
                    Toast.makeText(DetailsListPoliceActivity.this, "Invalid Number!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

