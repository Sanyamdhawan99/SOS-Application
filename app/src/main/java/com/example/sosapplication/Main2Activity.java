package com.example.sosapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Main2Activity extends AppCompatActivity {
    private Button twoWheeler;
    private  Button fourWheeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        twoWheeler = (Button) findViewById(R.id.button2);
        fourWheeler = (Button) findViewById(R.id.button3);

        configButtons();
    }
    void configButtons() {
        twoWheeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send_mode = new Intent(Main2Activity.this, MainActivity.class);
                send_mode.putExtra("Mode", "two");
                startActivity(send_mode);
            }
        });

        fourWheeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send_mode = new Intent(Main2Activity.this, MainActivity.class);
                send_mode.putExtra("Mode", "four");
                startActivity(send_mode);
            }
        });
    }
}
