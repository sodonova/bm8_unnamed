package tech.blindbool.mooglegaps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button generateCodeButton;
    private TextView generateCodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateCodeButton = findViewById(R.id.generateCodeButton);
        generateCodeText = findViewById(R.id.generatedCodeText);
        generateCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateCodeText.setText(generateCode());
            }
        });
    }

    public String generateCode() {
        Random r = new Random();
        return Integer.toString(10000 + r.nextInt(90000));
    }
}