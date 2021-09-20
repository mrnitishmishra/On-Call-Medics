package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PatientDetails extends AppCompatActivity {

    FirebaseFirestore firestore;
    TextView name , email , phone , age , gender , symptoms , pincode , shift;
    Button checked;
    String PatientName;
    String DocName;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        toolbar = findViewById(R.id.scrview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        PatientName = getIntent().getStringExtra("PatientName");

        firestore = FirebaseFirestore.getInstance();
        name = findViewById(R.id.TVpatientName);
        email = findViewById(R.id.TVemail);
        phone = findViewById(R.id.TVphone);
        age = findViewById(R.id.TVAge);
        gender = findViewById(R.id.TVGender);
        symptoms = findViewById(R.id.TVsymptoms);
        pincode = findViewById(R.id.TVPincode);
        shift = findViewById(R.id.TVshift);
        checked = findViewById(R.id.button_checked);


        DocumentReference ref = firestore.collection("Patient Info").document(PatientName);
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    if (documentSnapshot.getString("isChecked").equals("1")) {
                        checked.setText("View Current Appointments");
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(PatientDetails.this, "Not Done", Toast.LENGTH_SHORT).show();
            }
        });

        try {
            DocumentReference df = firestore.collection("Patient Info").document(PatientName);
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {

                        name.setText(documentSnapshot.getString("Patient_Name"));
                        email.setText(documentSnapshot.getString("Patient_Email"));
                        phone.setText(documentSnapshot.getString("Phone_No"));
                        pincode.setText(documentSnapshot.getString("PinCode"));
                        age.setText(documentSnapshot.getString("Age"));
                        symptoms.setText(documentSnapshot.getString("Symptoms"));
                        gender.setText(documentSnapshot.getString("Gender"));
                        shift.setText(documentSnapshot.getString("Shift"));
                        DocName = documentSnapshot.getString("Appointed_Doctor");

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(PatientDetails.this, "Not Done", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Not Done", Toast.LENGTH_SHORT).show();
        }

        checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference df = firestore.collection("Patient Info").document(PatientName);
                Map<String, Object> userinfo = new HashMap<>();
                userinfo.put("isChecked", "1");
                df.update(userinfo);
                Intent intent = new Intent(getApplicationContext(), DocAppointment.class);
                intent.putExtra("doc_name", DocName);
                startActivity(intent);
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}