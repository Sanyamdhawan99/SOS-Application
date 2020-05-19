package com.example.sosapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button b;
    private TextView t, t1;

    private LocationListener locationListener;
    private LocationManager locationManager;
    private Double myLatitude;
    private Double myLongitude;
    private float speed;
    private int count = 0;

    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button) findViewById(R.id.button);
        t = (TextView) findViewById(R.id.textView2);
        t1 = (TextView) findViewById(R.id.textView);
        t.append("\n -------------------------------");
        t1.append("\n -------------------------------");
        locationListener = new LocationListener() {
            @Override

            public void onLocationChanged(Location location) {
                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();
                speed = location.getSpeed();
                t.append("\n Latitude: " + location.getLatitude() + " , Longitude: " + location.getLongitude());
                t.append("\n Speed: " + location.getSpeed());
                t.append("\n -----------------------------------------------------------------------");
                count += 1;
                if(count % 8 == 0) {
                    t.setText("Data:" + "\n -------------------------------");
                }
                configure_button();
                alarmLogic();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };` `

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);
                }
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);
                }
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alarmLogic() {
        Intent send_mode = getIntent();
        String mode = send_mode.getStringExtra("Mode");
        System.out.println("current mode "+ mode);
        t1.append("\n Your current mode of transportation is : " + mode + " wheeler");
        if(mode.equals("two")) {
            t1.setText("Basic Information");
            t1.append("\n -------------------------------");
            t1.append("\n Your current mode of transportation is : " + mode + " wheeler");
            t1.append("\n Speed limit for you is: 60km/hr \n");
            generate_alarm("two");
        }
        else if(mode.equals("four"))  {
            t1.setText("Basic Information");
            t1.append("\n -------------------------------");
            t1.append("\n Your current mode of transportation is : " + mode + " wheeler");
            t1.append("\n Speed limit for you is: 100km/hr \n");
            generate_alarm("four");
        }
    }

    void generate_alarm(String transportMode) {
        if(transportMode.equals("two")) {
            if(speed > 0.5) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(VibrationEffect.createOneShot(1000, 255));
                System.out.println("two wheeler detected");
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            if(speed > 1) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(VibrationEffect.createOneShot(1000, 255));
                System.out.println("four wheeler detected");
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    void configure_button() {
        System.out.println(myLatitude + ", " + myLongitude + ", " + speed);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent get_data = new Intent(MainActivity.this, MapsActivity.class);
                get_data.putExtra("myLatitude", myLatitude);
                get_data.putExtra("myLongitude", myLongitude);
                get_data.putExtra("speed", speed);
                startActivity(get_data);
            }
        });
    }
}
