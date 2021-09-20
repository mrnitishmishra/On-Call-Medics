package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseFirestore firestore;
    EditText nameTxt , emailTxt , phoneTxt , passwordTxt , ageTxt;
    Button registerTxt;
    Spinner userTypeTxt , genderTxt;
    String gender;

    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        nameTxt = findViewById(R.id.nameTxt);
        emailTxt = (EditText)findViewById(R.id.emailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        passwordTxt = (EditText)findViewById(R.id.passwordTxt);
        ageTxt = findViewById(R.id.ageTxt);
        userTypeTxt = findViewById(R.id.userTypeTxt);
        genderTxt = findViewById(R.id.gendertxt);
        registerTxt = findViewById(R.id.registerTxt);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeTxt.setAdapter(adapter);
        genderTxt.setAdapter(adapter1);
        gender = genderTxt.getSelectedItem().toString();

        userTypeTxt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    registerTxt.setText("Register");
                    registerTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Boolean nameCheck = checkField(nameTxt);
                            Boolean emailCheck = checkField(emailTxt);
                            Boolean phoneCheck = checkField(phoneTxt);
                            Boolean passCheck = checkField(passwordTxt);
                            Boolean ageCheck = checkField(ageTxt);

                            if(nameCheck && emailCheck && phoneCheck && passCheck && ageCheck) {

                                final Task<AuthResult> successfully_registered = auth.createUserWithEmailAndPassword
                                        (emailTxt.getText().toString(), passwordTxt.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();

                                                    FirebaseUser user = auth.getCurrentUser();
                                                    DocumentReference df = firestore.collection("Patients").document(user.getUid());
                                                    Map<String, Object> userInfo = new HashMap<>();
                                                    userInfo.put("Name", nameTxt.getText().toString());
                                                    userInfo.put("Email", emailTxt.getText().toString());
                                                    userInfo.put("Phone", phoneTxt.getText().toString());
                                                    userInfo.put("Age", ageTxt.getText().toString());
                                                    userInfo.put("Gender", gender);
                                                    userInfo.put("isPatient", "1");
                                                    df.set(userInfo);

                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });
                }
                else if(position == 1){
                    registerTxt.setText("Next");
                    registerTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Boolean nameCheck = checkField(nameTxt);
                            Boolean emailCheck = checkField(emailTxt);
                            Boolean phoneCheck = checkField(phoneTxt);
                            Boolean passCheck = checkField(passwordTxt);
                            Boolean ageCheck = checkField(ageTxt);

                            if(nameCheck && emailCheck && phoneCheck && passCheck && ageCheck) {

                                final Task<AuthResult> successfully_registered = auth.createUserWithEmailAndPassword
                                        (emailTxt.getText().toString(), passwordTxt.getText().toString())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(MainActivity2.this, "Successfully Registered", Toast.LENGTH_SHORT).show();

                                                    FirebaseUser user = auth.getCurrentUser();
                                                    DocumentReference df = firestore.collection("Doctors").document(user.getUid());
                                                    Map<String, Object> userInfo = new HashMap<>();
                                                    userInfo.put("Name", nameTxt.getText().toString());
                                                    userInfo.put("Email", emailTxt.getText().toString());
                                                    userInfo.put("Phone", phoneTxt.getText().toString());
                                                    userInfo.put("Age", ageTxt.getText().toString());
                                                    userInfo.put("Gender", gender);
                                                    userInfo.put("isDoctor", "1");
                                                    df.set(userInfo);

                                                    Intent intent = new Intent(RegisterActivity.this, RegisterDocActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
}