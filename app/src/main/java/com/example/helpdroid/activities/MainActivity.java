package com.example.helpdroid.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpdroid.FetchAddressIntentService;
import com.example.helpdroid.R;
import com.example.helpdroid.constants.Constants;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.imageview.ShapeableImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int dateTime;
    CardView police, hospital, fireService, help;
    ShapeableImageView profile;
    TextView time;
    TextView currentLocation;
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultReceiver = new AddressResultReceiver(new Handler());
        police = findViewById(R.id.police);
        hospital = findViewById(R.id.hospital);
        fireService = findViewById(R.id.fireService);
        help = findViewById(R.id.help);
        currentLocation = findViewById(R.id.currentLocation);
        profile = findViewById(R.id.profile);

//        time = findViewById(R.id.time);

        police.setOnClickListener(this);
        hospital.setOnClickListener(this);
        fireService.setOnClickListener(this);
        help.setOnClickListener(this);
        profile.setOnClickListener(this);

//        int currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//
//
//        if (currentTime >= 0 && currentTime <= 5) {
//            time.setText("Good Night");
//        } else if (currentTime >= 6 && currentTime <= 10) {
//            time.setText("Good Morning");
//        } else if (currentTime >= 11 && currentTime <= 15) {
//            time.setText("Good Noon");
//        } else if (currentTime >= 16 && currentTime <= 18) {
//            time.setText("Good Afternoon");
//        } else if (currentTime >= 19 && currentTime <= 20) {
//            time.setText("Good Evening");
//        } else if (currentTime >= 21 && currentTime <= 24) {
//            time.setText("Good Night");
//        }

        getCurrentLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 100 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            LocationServices.getFusedLocationProviderClient(MainActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(MainActivity.this).removeLocationUpdates(this);
                    if (locationRequest != null && locationResult.getLocations().size() > 0) {
                        int latestLocationIndex = locationResult.getLocations().size() - 1;
                        double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                        double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();

                        //currentLocation.setText(String.format("Latitude: %s\nLongitude: %s", latitude, longitude));
                        Location location = new Location("providerNA");
                        location.setLatitude(latitude);
                        location.setLongitude(longitude);
                        fetchAddressFromLatLong(location);
                    }
                }
            }, Looper.getMainLooper());


        }

    }

    private void fetchAddressFromLatLong(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                currentLocation.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            } else {
                Toast.makeText(MainActivity.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.police:
                Intent police = new Intent(MainActivity.this, DetailsListPoliceActivity.class);
                startActivity(police);
                break;
            case R.id.profile:
                Intent profileButton = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileButton);
                break;
            case R.id.help:
                //check conditions
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    sendMessage();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }
        }
    }

    private void sendMessage() {
        String message = "Help me please! \nI am stack in " + currentLocation + ".";
        if (!message.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("01914120202", null, message, null, null);
            Toast.makeText(MainActivity.this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
        } else {
            //handle error or something else
        }
    }

}