package tech.blindbool.mooglegaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import io.radar.sdk.Radar;

public class MessageActivity extends MainActivity {

    private Button messageButton;
    private TextView messageText;
    private TextView locationText;
    private Button currentLocation;
    private TextView codeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        currentLocation = findViewById(R.id.CurrentLocation);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionId = getIntent().getStringExtra("key");
                locationText.setText(String.format("Long = %s, Lat = %s", sessionId.substring(0,
                        sessionId.indexOf(" ")), sessionId.substring(sessionId.indexOf(" ") + 1)));
            }
        });

        messageButton = findViewById(R.id.messageButton);
        messageText = findViewById(R.id.MessageText);
        locationText = findViewById(R.id.locationCoordinateText);
        codeText = findViewById(R.id.CodeText);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.map.containsValue(Integer.parseInt(codeText.getText().toString()))) {
                    messageText.setText(MainActivity.messageMap.
                            get(Integer.parseInt(codeText.getText().toString())));
                }
                Radar.getLocation(new Radar.RadarLocationCallback() {
                    @Override
                    public void onComplete(Radar.@NotNull RadarStatus radarStatus,
                                           @Nullable Location location, boolean b) {
                        try {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                            Log.i("Unique tag", Double.toString(latitude));
                            String latlong = String.format("%.2f,%.2f", latitude,longitude);
                            db.collection("messages").document(latlong).get().addOnCompleteListener(
                                    new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            String TAG = "Unique";
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                    messageText.setText(String.valueOf(document.getData().get(String.valueOf(codeText.getText()))));

                                                } else {
                                                    Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    }
                            );
                            Log.i("Unique", "FIrebase sent");

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
    }
}