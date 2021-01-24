package tech.blindbool.mooglegaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import io.radar.sdk.Radar;
import io.radar.sdk.model.RadarAddress;
import io.radar.sdk.model.RadarEvent;
import io.radar.sdk.model.RadarUser;
import android.Manifest.permission.*;
public class MainActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private Button generateCodeButton;
    private TextView generateCodeText;
    private TextView locationText;
    private Button locationButton;
    private Button findActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get perms (every time)
        //TODO: check if we have perms first
        Log.i("Unique", "requesting perms");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED}, 100);


        //generate code button and display text
        generateCodeButton = findViewById(R.id.generateCodeButton);
        generateCodeText = findViewById(R.id.generatedCodeText);
        generateCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateCodeText.setText(generateCode());
            }
        });

        //update location
        locationButton = findViewById(R.id.locationButton);
        locationText = findViewById(R.id.locationText);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: set text to the location
                //locationText.setText("");
            }
        });

        //open new activity
        findActivityButton = findViewById(R.id.finder);
        findActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);

                startActivity(intent);
            }
        });

    }

    public String getGeoCode() {
        return "Hi there";
    }

    public String generateCode() {
        Random r = new Random();
        return Integer.toString(10000 + r.nextInt(90000));
    }
        // handle the check for perms
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                               int[] grantResults) {
            switch (requestCode) {
                case 100:
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0 &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // acknowledge perms exist
                        Toast.makeText(this, "Thanks!", Toast.LENGTH_SHORT).show();

                        // get radar publishable key
                        String key = null;
                        try {
                            key = new Scanner(new File("radarkeys")).nextLine();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        // radar needs google play services
                        Log.i("Unique Tag", "onCreate: " +
                                GoogleApiAvailability.getInstance()
                                        .isGooglePlayServicesAvailable(this));
                        // initialize radar
                        Radar.initialize(this, key);
                        //attempt to track location, log result
                        Radar.trackOnce(new Radar.RadarTrackCallback() {
                            @Override
                            public void onComplete(@NotNull Radar.RadarStatus radarStatus,
                                                   @Nullable Location location,
                                                   @Nullable RadarEvent[] radarEvents,
                                                   @Nullable RadarUser radarUser) {
                                Log.i("Unique Tag", "Success? " + radarStatus.toString());
                            }
                        });

                    }  else {
                        Toast.makeText(this, "Permissions are needed!",
                                Toast.LENGTH_LONG).show();
                        // Explain to the user that the feature is unavailable because
                        // the features requires a permission that the user has denied.
                        // At the same time, respect the user's decision. Don't link to
                        // system settings in an effort to convince the user to change
                        // their decision.
                    }
                    return;
            }
            // Other 'case' lines to check for other
            // permissions this app might request.

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

}

