package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    TextView newUser;
    EditText etEmail , etPassword ;
    Button btnLogin;
    FirebaseAuth auth;
    Spinner etAccountType;
    FirebaseFirestore firestore;
    Boolean valid = true;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        newUser = findViewById(R.id.newUser);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        etAccountType = findViewById(R.id.etAccountType);
        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this, R.array.user, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etAccountType.setAdapter(adapter);

        etAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Boolean emailCheck = checkField(etEmail);
                            Boolean passCheck = checkField(etPassword);
                            if(emailCheck && passCheck) {

                                progressDialog.setMessage("Please Wait...");
                                progressDialog.show();

                                auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                checkIfPatient(authResult.getUser().getUid());
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });

                }
                else if (position == 1){
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Boolean emailCheck = checkField(etEmail);
                            Boolean passCheck = checkField(etPassword);

                            if(emailCheck && passCheck) {

                                progressDialog.setMessage("Please Wait...");
                                progressDialog.show();

                                auth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                checkIfDocotor(authResult.getUser().getUid());
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
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

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void  checkIfPatient(String uid){
        DocumentReference df = firestore.collection("Patients").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                if(documentSnapshot.getString("isPatient") != null){
                    Intent intent = new Intent(LoginActivity.this, PatientHomePage.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "No Patient Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void  checkIfDocotor(String uid){
        DocumentReference df1 = firestore.collection("Doctors").document(uid);
        df1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                if(documentSnapshot.getString("isDoctor") != null){
                    Intent intent = new Intent(LoginActivity.this, DocAppointment.class);
                    intent.putExtra("doc_name", documentSnapshot.getString("Name"));
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "No Doctor Found", Toast.LENGTH_SHORT).show();
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
}