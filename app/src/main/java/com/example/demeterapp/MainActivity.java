package com.example.demeterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements ValueEventListener {
    TextView textView, textView2;
private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
private DatabaseReference databaseReference = firebaseDatabase.getReference();
private DatabaseReference firstDatabase = databaseReference.child("Moisture");
private DatabaseReference secondDatabase = databaseReference.child("Light");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.moistureLevelDisplay);
        textView2 = (TextView)findViewById(R.id.lightLevelDisplay);
    }

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
    }
}