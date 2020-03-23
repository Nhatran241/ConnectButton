package com.nhatran241.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.nhatran241.connectbutton.ConnectButton;

public class MainActivity extends AppCompatActivity {
    ConnectButton connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectButton = findViewById(R.id.btnConnect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectButton.setStatus(ConnectButton.STATUS_CONNECTING);

            }
        });

    }
}
