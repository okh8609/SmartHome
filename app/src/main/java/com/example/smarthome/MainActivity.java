package com.example.smarthome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 80;
    private static final int MY_SMS_PERMISSION_REQUEST_CODE = 88;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getSupportActionBar().hide(); //hide the title bar

        checkSmsPermission();

        //允許執行緒從網路下載
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment, MainFragment.newInstance());
        fragmentTransaction.commit();
    }

    private void checkSmsPermission() {

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) +
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) +
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS))
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS
                    }, MY_PERMISSIONS_REQUEST);

        }

/*
        // 10 OK
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS))
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted. Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "Manifest.permission.READ_SMS") ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, "Manifest.permission.READ_SMS")) {
                // Show an explanation to the user *asynchronously* -- don't block this thread waiting for the user's response!
                // After the user sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this, new String[]{"Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS"}, MY_SMS_PERMISSION_REQUEST_CODE);
                // REQUEST_CODE is an app-defined int constant.
                // The callback method gets the result of the request.
            }
        } else {
            // Permission has already been granted
        }
*/

    }
}