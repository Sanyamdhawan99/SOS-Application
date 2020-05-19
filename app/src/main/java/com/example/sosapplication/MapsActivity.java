package com.example.sosapplication;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.SmsManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.VIBRATE}, PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng equator = new LatLng(0, 0);
        mMap.addMarker(new MarkerOptions().position(equator).title("Marker at 0, 0"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(equator));

        Intent get_data = getIntent();
        Double myLatitude = get_data.getDoubleExtra("myLatitude", 0.0);
        Double myLongitude = get_data.getDoubleExtra("myLongitude", 0.0);
        Float speed = get_data.getFloatExtra("speed", 0);
//        System.out.println("in map this is what we get: ");
//        System.out.println(myLatitude + ", " + myLongitude + ", " + speed);
        latLng = new LatLng(myLatitude, myLongitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Current Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        try {
            ArrayList<String> phoneNumber = new ArrayList<String>();
                                phoneNumber.add("9530610167");
            //                    phoneNumber.add("8427014751");
            //                    phoneNumber.add("9478302690");

            String msg = "Hey! I am in trouble, please reach out to me at: " + "\n" + "https://maps.google.com/?q=" + myLatitude
                    + "," + myLongitude + "\n" + "Current Speed: " + speed;
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS}
                            , 10);
                }
                return;
            }
            SmsManager smsManager = SmsManager.getDefault();
            //                    System.out.println("size: " + phoneNumber.size());
            for (int idx = 0; idx < phoneNumber.size(); idx += 1) {
                //                        System.out.println("index: " + idx);
                smsManager.sendTextMessage(phoneNumber.get(idx), null, msg, null, null);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber.get(idx)));
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}
                                , 10);
                    }
                    return;
                }
                startActivity(callIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
