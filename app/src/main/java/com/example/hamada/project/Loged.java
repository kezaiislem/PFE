package com.example.hamada.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;
import android.content.Context;

public class Loged extends AppCompatActivity {
    TextView txt,txt2;
    Button logout;
    double longitude;
    double latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged);
        txt = (TextView) findViewById(R.id.longitute);
        txt2 = (TextView) findViewById(R.id.latitude);
        logout = (Button)   findViewById(R.id.button);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    txt.setText(""+longitude);
                    txt2.setText(""+latitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
            }
        };
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilsClipCodes.saveSharedSetting(Loged.this, "ClipCodes", "true");
                UtilsClipCodes.SharedPrefesSAVE(getApplicationContext(), "");
                Intent LogOut = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(LogOut);
                finish();
            }
        });
        try{
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 3,locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 3, locationListener);
        }
        catch (SecurityException E) {
            Toast.makeText(getApplicationContext(), "Need Permissions", Toast.LENGTH_SHORT).show();
        }
        CheckUser();
    }
    public void CheckUser(){
        Boolean Check = Boolean.valueOf(UtilsClipCodes.readSharedSetting(Loged.this, "ClipCodes", "true"));

        Intent introIntent = new Intent(Loged.this, MainActivity.class);
        introIntent.putExtra("ClipCodes", Check);

        if (Check) {
            startActivity(introIntent);
            finish();
        }
    }
}
