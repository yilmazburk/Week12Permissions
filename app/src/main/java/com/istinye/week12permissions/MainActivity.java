package com.istinye.week12permissions;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private Button permissionButton;
    private Button locationButton;
    private TextView myLocationTextView;

    private static final int UNIQUE_WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 9900;
    private static final int UNIQUE_LOCATION_REQUEST_CODE = 9901;

    private FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        permissionButton = (Button) findViewById(R.id.permissionButton);
        permissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, UNIQUE_WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    Toast.makeText(MainActivity.this, "You have already permitted that. Permission granted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        locationButton = (Button) findViewById(R.id.locationPermissionButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, UNIQUE_LOCATION_REQUEST_CODE);
                } else {
                    getLastKnowLocation();
                    Toast.makeText(MainActivity.this, "You have already permitted that. Location Permission granted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myLocationTextView = (TextView) findViewById(R.id.locationText);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == UNIQUE_WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Do something about write external storage
                Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder myDialog = new AlertDialog.Builder(this);

                    myDialog.setMessage("Permission is important to save data on your phone. Please give us permission").setTitle("Important Permission Warning");

                    myDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, UNIQUE_WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                        }
                    });

                    myDialog.setNegativeButton("No, I'm not sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "You can not use this functionality.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    myDialog.show();
                } else {
                    Toast.makeText(MainActivity.this, "You can not use this functionality. If you want to use it then you should go to settings and permit me", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == UNIQUE_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission Granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "You can not use this functionality.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLastKnowLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    myLocationTextView.setText("Latitude: " + location.getLatitude() +  "\nLongitude: " + location.getLongitude());
                }
            }
        });
    }
}