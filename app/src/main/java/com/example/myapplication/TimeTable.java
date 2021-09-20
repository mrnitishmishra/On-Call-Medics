package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TimeTable extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String name , email , specialization , pincode , degree , phone , hospital , gender , image_url;
    EditText fee;
    CheckBox monday , tuesday , wednesday , thursday , friday , saturday;
    RadioButton morning , evening;
    Button update;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        toolbar = findViewById(R.id.toolbar_time_table);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        DocumentReference df = firestore.collection("Doctors").document(user.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    name = documentSnapshot.getString("Name");
                    email = documentSnapshot.getString("Email");
                    specialization = documentSnapshot.getString("Specialization");
                    pincode = documentSnapshot.getString("PIN_Code");
                    degree = documentSnapshot.getString("Degree");
                    phone = documentSnapshot.getString("Phone");
                    hospital = documentSnapshot.getString("Hospital");
                    gender = documentSnapshot.getString("Gender");
                    image_url = documentSnapshot.getString("Image_Url");
                }
                else{
                    Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to Fetch data", Toast.LENGTH_SHORT).show();
                    }
                });

        fee = findViewById(R.id.fee);
        monday = findViewById(R.id.isMonday);
        tuesday = findViewById(R.id.isTuesday);
        wednesday = findViewById(R.id.isWednesday);
        thursday = findViewById(R.id.isThursday);
        friday = findViewById(R.id.isFriday);
        saturday = findViewById(R.id.isSaturday);
        morning = findViewById(R.id.isMorning);
        evening = findViewById(R.id.isEvening);
        update = findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference ref = firestore.collection("Doctor Info").document(email.toString());
                Map<String, Object> userinfo = new HashMap<>();
                userinfo.put("Name", name.toString());
                userinfo.put("Email", email.toString());
                userinfo.put("Specialization", specialization.toString());
                userinfo.put("PIN_Code", pincode.toString());
                userinfo.put("Degree", degree.toString());
                userinfo.put("Phone", phone.toString());
                userinfo.put("Hospital", hospital.toString());
                userinfo.put("Gender", gender.toString());
                userinfo.put("Fee", fee.getText().toString());
                userinfo.put("Image_Url", image_url.toString());
                if(morning.isChecked()){
                    userinfo.put("Shift", morning.getText().toString());
                }
                if (evening.isChecked()){
                    userinfo.put("Shift", evening.getText().toString());
                }
                if (monday.isChecked()){
                    userinfo.put("Day1", monday.getText().toString());
                }
                if (tuesday.isChecked()){
                    userinfo.put("Day2", tuesday.getText().toString());
                }
                if (wednesday.isChecked()){
                    userinfo.put("Day3", wednesday.getText().toString());
                }
                if (thursday.isChecked()){
                    userinfo.put("Day4", thursday.getText().toString());
                }
                if (friday.isChecked()){
                    userinfo.put("Day5", friday.getText().toString());
                }
                if (saturday.isChecked()){
                    userinfo.put("Day6", saturday.getText().toString());
                }
                ref.set(userinfo);
                Intent intent = new Intent(getApplicationContext(), DocAppointment.class);
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