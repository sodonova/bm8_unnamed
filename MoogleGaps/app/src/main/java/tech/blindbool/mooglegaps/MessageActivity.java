package tech.blindbool.mooglegaps;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
                if(MainActivity.map.containsValue(Integer.parseInt(codeText.toString()))) {
                    messageText.setText(MainActivity.messageMap.
                            get(Integer.parseInt(codeText.toString())));
                }
            }
        });
    }
}