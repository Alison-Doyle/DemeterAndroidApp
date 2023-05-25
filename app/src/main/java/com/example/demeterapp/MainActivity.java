package com.example.demeterapp;

/* Imports */
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements ValueEventListener {
    TextView textView, textView2;
    EditText editMoistureView, editLightView;
    Button submissionButton;
    String currentLightSettings;
private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
private DatabaseReference databaseReference = firebaseDatabase.getReference();
private DatabaseReference firstDatabase = databaseReference.child("Moisture");
private DatabaseReference secondDatabase = databaseReference.child("Light");
private DatabaseReference plantLightSettingsPath = databaseReference.child("plantSettings/minLightLevel");
private DatabaseReference plantMoistureSettingsPath = databaseReference.child("plantSettings/minMoistureLevel");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.moistureLevelDisplay);
        textView2 = (TextView)findViewById(R.id.lightLevelDisplay);
        editLightView = findViewById(R.id.editLightLevel);
        editMoistureView = findViewById(R.id.editMoistureLevel);
        submissionButton = (Button)findViewById(R.id.updateValuesButton);

        //Making update button
        submissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting values from edittext
                final String newMinLight = editLightView.getText().toString();
                final String newMinMoisture = editMoistureView.getText().toString();

                // updating minimum Light Settings
                if (newMinLight.isEmpty()){
                    Toast.makeText(MainActivity.this, "No value entered for light level. Light level not updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    plantLightSettingsPath.setValue(Long.parseLong(newMinLight));
                }

                //updating minimum moisture settings
                if (newMinMoisture.isEmpty()){
                    Toast.makeText(MainActivity.this, "No value entered for soil moisture. Soil Moisture not updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    plantMoistureSettingsPath.setValue(Long.parseLong(newMinMoisture));
                }

            }
        });

    }

    /* Updating Values based on Firebase */
    @Override
    public void onDataChange(DataSnapshot snapshot) {

        if (snapshot.getValue(Long.class) != null) {

            String key = snapshot.getKey();
            if (key.equals("Moisture")) {

                Long moisture = snapshot.getValue(Long.class);
                textView.setText(moisture.toString());

            }
            if (key.equals("Light")) {

                Long light = snapshot.getValue(Long.class);
                textView2.setText(light.toString());
                /*Long minLight = snapshot.getValue(Long.class);
                editLightView.setHint(minLight.toString());*/

            }
            if (key.equals("minLightLevel")) {
                Long minLight = snapshot.getValue(Long.class);
                editLightView.setText(minLight.toString());
            }
            if (key.equals("minMoistureLevel")) {
                Long minMoisture = snapshot.getValue(Long.class);
                editMoistureView.setText(minMoisture.toString());
            }

        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        firstDatabase.addValueEventListener(this);
        secondDatabase.addValueEventListener(this);
        plantLightSettingsPath.addValueEventListener(this);
        plantMoistureSettingsPath.addValueEventListener(this);
    }

}