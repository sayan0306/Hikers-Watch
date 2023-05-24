package com.sayan.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);            }
        }
    }

    public void updateLocationInfo(Location location){
        Log.i("Location", location.toString());

        TextView latTextView = (TextView) findViewById(R.id.latTextView);
        TextView lonTextView = (TextView) findViewById(R.id.lonTextView);
        TextView altTextView = (TextView) findViewById(R.id.altTextView);
        TextView accTextView = (TextView) findViewById(R.id.accTextView);
        TextView addressTextView = (TextView) findViewById(R.id.addTextView);


        latTextView.setText("Latitude: "+ location.getLatitude());

        lonTextView.setText("Longitude: "+ location.getLongitude());

        accTextView.setText("Accuracy: "+ location.getAccuracy());

        altTextView.setText("Altitude: "+location.getAltitude());

        Geocoder geocoder=new Geocoder(this, Locale.getDefault());

        try {
            String address="";
            List<Address>listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.i("Location",listAddress.get(0).toString());
            if(listAddress.get(0).getSubThoroughfare()!=null) {
                address += listAddress.get(0).getSubThoroughfare()+",\n ";
            }if(listAddress.get(0).getThoroughfare()!=null) {
                address += listAddress.get(0).getThoroughfare()+", \n";
            }if(listAddress.get(0).getLocality()!=null) {
                address += listAddress.get(0).getLocality() + ", \n";
            }if(listAddress.get(0).getAdminArea()!=null) {
                address += listAddress.get(0).getAdminArea()  +", \n";
            }if(listAddress.get(0).getPostalCode()!=null) {
                address += listAddress.get(0).getPostalCode() +", \n";
            }if(listAddress.get(0).getCountryName()!=null) {
                address += listAddress.get(0).getCountryName() +" .";
            }
            if(address!=null) {
                addressTextView.setText("Address: \n"+address);
            }else{
                addressTextView.setText("Could not find location address");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

            Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null) {
                updateLocationInfo(location);
            }
        }
    }
}

