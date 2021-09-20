package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BookAppointment extends AppCompatActivity {

    EditText name , email , phone , age , symptoms , pincode;
    Spinner gender , shift;
    String Gender , DocName , Day , DocEmail , Shift;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button book;
    Boolean valid = true;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        toolbar = findViewById(R.id.scview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DocName = getIntent().getStringExtra("DocName");
        DocEmail = getIntent().getStringExtra("DocEmail");
        Day = getIntent().getStringExtra("Day");

        firestore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.patientName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        symptoms = findViewById(R.id.symptoms);
        age = findViewById(R.id.Age);
        pincode = findViewById(R.id.Pincode);
        gender = findViewById(R.id.Gender);
        shift = findViewById(R.id.shift);
        book = findViewById(R.id.book_appointment);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.shifts, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter1);
        shift.setAdapter(adapter);
        Gender = gender.getSelectedItem().toString();
        Shift = shift.getSelectedItem().toString();

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();




        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Boolean namecheck = checkField(name);
                    Boolean agecheck = checkField(age);
                    Boolean phonecheck = checkField(phone);
                    Boolean pincheck = checkField(pincode);
                    Boolean symptomcheck = checkField(symptoms);
                    Boolean emailcheck = checkField(email);
                    if (namecheck && agecheck && phonecheck && pincheck && symptomcheck && emailcheck) {

                    DocumentReference ref = firestore.collection("Doc Patient Info").document(DocEmail)
                            .collection(user.getUid()).document(name.getText().toString());
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("Patient_Name", name.getText().toString());
                    userInfo.put("Patient_Email", email.getText().toString());
                    userInfo.put("Phone_No", phone.getText().toString());
                    userInfo.put("Symptoms", symptoms.getText().toString());
                    userInfo.put("Age", age.getText().toString());
                    userInfo.put("PinCode", pincode.getText().toString());
                    userInfo.put("Gender", Gender.toString());
                    userInfo.put("Shift", Shift.toString());
                    userInfo.put("Appointed_Doctor", DocName.toString());
                    userInfo.put("Appointed_Day", Day.toString());
                    userInfo.put("isChecked", false);

                    ref.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(BookAppointment.this, "Done", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(getApplicationContext(), PatientHomePage.class);
                            startActivity(intent1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(BookAppointment.this, "Not Done", Toast.LENGTH_SHORT).show();
                        }
                    });

                        DocumentReference ref1 = firestore.collection("Patient Info").document(name.getText().toString());
                        Map<String, Object> userInfo1 = new HashMap<>();
                        userInfo1.put("Patient_Name", name.getText().toString());
                        userInfo1.put("Patient_Email", email.getText().toString());
                        userInfo1.put("Phone_No", phone.getText().toString());
                        userInfo1.put("Symptoms", symptoms.getText().toString());
                        userInfo1.put("Age", age.getText().toString());
                        userInfo1.put("PinCode", pincode.getText().toString());
                        userInfo1.put("Gender", Gender.toString());
                        userInfo1.put("Shift", Shift.toString());
                        userInfo1.put("Appointed_Doctor", DocName.toString());
                        userInfo1.put("Appointed_Day", Day.toString());
                        userInfo1.put("isChecked", "0");
                        userInfo1.put("user_id", user.getUid());

                        ref1.set(userInfo1);
                }
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(BookAppointment.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Required Field");
            valid = false;
        }
        else {
            valid = true;
        }
        return valid;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}