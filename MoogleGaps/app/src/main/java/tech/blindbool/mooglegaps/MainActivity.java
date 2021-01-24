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

import java.util.HashMap;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;


public class MainActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    private Button generateCodeButton;
    private TextView generateCodeText;
    private TextView locationText;
    private Button locationButton;
    private Button findActivityButton;
    public static MainActivity mainActivity;
    public static String longitudeCoordinate;
    public static String latitudeCoordinate;
    private TextView message;
    public static HashMap<Integer, Integer> map = new HashMap<>();
    public static HashMap<Integer, String> messageMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Unique", "Running");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.i("Unique", "Firebase");
                // backend workflow:
        // send geofence to radar with POST request:
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        Request request = ;
        // send the message to firebase under the collection "messages" and the document name as a string
        // i.e.
        String latlong = "double.tostring(latitude)"+","+"double.tostring(longitude)";
        HashMap<String, String> data = new HashMap<>();
        data.put("text", "Hello, I'm a message");
        db.collection("messages").document(latlong).set(data);
        Log.i("Unique", "Firebase");

        // Firebase stuff
        // Access a Cloud Firestore instance from your Activity
        // get perms (every time)
        //TODO: check if we have perms first
        Log.i("Unique", "requesting perms");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED}, 100);

        message = (TextView) findViewById(R.id.MessageText);
        //generate code button and display text
        generateCodeButton = findViewById(R.id.generateCodeButton);
        generateCodeText = findViewById(R.id.generatedCodeText);
        generateCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Radar.getLocation(new Radar.RadarLocationCallback() {
                    @Override
                    public void onComplete(Radar.@NotNull RadarStatus radarStatus,
                                           @Nullable Location location, boolean b) {
                        try {
                            String a = Double.toString(location.getLongitude());
                            String c = Double.toString(location.getLatitude());
                            locationText.setText(String.format("%s %s",
                                    Double.toString(location.getLongitude()),
                                    Double.toString(location.getLatitude())));
                            longitudeCoordinate = Double.toString(location.getLongitude());
                            latitudeCoordinate = Double.toString(location.getLatitude());
                            String value= String.format("%s %s",
                                    Double.toString(location.getLongitude()),
                                    String.format(Double.toString(location.getLatitude())));
                            Intent i = new Intent(MainActivity.this,
                                    MessageActivity.class);
                            i.putExtra("key",value);
                            startActivity(i);
                        } catch (NullPointerException ne) {
                            CharSequence toastText = "There was an error while collecting the location." +
                                    "Please ensure your location is on and has high accuracy";
                            Toast toast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                    }
                });


                    generateCodeText.setText(generateCode());
                    CharSequence toastText = "Code Generated.";
                    Toast t = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
                    t.show();
                    //TODO: FIX MAPS BELOW,
                    //get longitude -- the location.getLongitude are displaying longitude
                    map.put(Integer.parseInt("5"),
                            Integer.parseInt(generateCodeText.getText().toString()));
                    //messageMap.put(Integer.parseInt(generateCodeText.getText().toString()),
                    //       message.getText().toString());
//                generateCodeText.setText(String.format("%s-%s",
//                        generateCodeText.getText().toString(), "5"));
//                if(message.getText().toString().isEmpty() || message == null) {
//                    message.setText("No message was provided");
//                }
                    messageMap.put(Integer.parseInt(generateCodeText.getText().toString()),
                            "This is a sample message");
                    return;
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
                Radar.getLocation(new Radar.RadarLocationCallback() {
                    @Override
                    public void onComplete(Radar.@NotNull RadarStatus radarStatus,
                                           @Nullable Location location, boolean b) {
                        try {
                            String a = Double.toString(location.getLongitude());
                            String c = Double.toString(location.getLatitude());
                            locationText.setText(String.format("%s %s",
                                    Double.toString(location.getLongitude()),
                                    Double.toString(location.getLatitude())));
                            longitudeCoordinate = Double.toString(location.getLongitude());
                            latitudeCoordinate = Double.toString(location.getLatitude());
                            String value= String.format("%s %s",
                                    Double.toString(location.getLongitude()),
                                    String.format(Double.toString(location.getLatitude())));
                            Intent i = new Intent(MainActivity.this,
                                    MessageActivity.class);
                            i.putExtra("key",value);
                            startActivity(i);
                        } catch (NullPointerException ne) {
                            CharSequence toastText = "There was an error while collecting the location." +
                                    "Please ensure your location is on and has high accuracy";
                            Toast toast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                    }
                });
            }
        });

        //open new activity
        findActivityButton = findViewById(R.id.finder);
        findActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Radar.getLocation(new Radar.RadarLocationCallback() {
                    @Override
                    public void onComplete(Radar.@NotNull RadarStatus radarStatus,
                                           @Nullable Location location, boolean b) {
                        try {
                            String a = Double.toString(location.getLongitude());
                            String c = Double.toString(location.getLatitude());
                            locationText.setText(String.format("%s %s",
                                    Double.toString(location.getLongitude()),
                                    Double.toString(location.getLatitude())));
                            longitudeCoordinate = Double.toString(location.getLongitude());
                            latitudeCoordinate = Double.toString(location.getLatitude());
                            String value= String.format("%s %s",
                                    Double.toString(location.getLongitude()),
                                    String.format(Double.toString(location.getLatitude())));
                            Intent i = new Intent(MainActivity.this,
                                    MessageActivity.class);
                            i.putExtra("key",value);
                            startActivity(i);
                        } catch (NullPointerException ne) {
                            CharSequence toastText = "There was an error while collecting the location." +
                                    "Please ensure your location is on and has high accuracy";
                            Toast toast = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                    }
                });
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);

                //startActivity(intent);
            } // onClick
        }); //findActivityButton

    } // on create

    public String getLatitude() {
        final String[] tempLong = new String[1];
        Radar.getLocation(new Radar.RadarLocationCallback() {
            @Override
            public void onComplete(Radar.@NotNull RadarStatus radarStatus, @Nullable Location location, boolean b) {
                tempLong[0] = Double.toString(location.getLongitude());
            }
        });
        return tempLong[0];
    }

    public String getLongitude() {
        final String[] tempLat = new String[1];
        Radar.getLocation(new Radar.RadarLocationCallback() {
            @Override
            public void onComplete(Radar.@NotNull RadarStatus radarStatus, @Nullable Location location, boolean b) {
                tempLat[0] = Double.toString(location.getLongitude());
            }
        });
        return tempLat[0];
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

