package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterDocActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    EditText degreeTxt , hospitalTxt , specializationTxt , pincodeTxt , addressTxt;
    Button docRegisterTxt;
    Boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_doc);

        degreeTxt = findViewById(R.id.degreeTxt);
        hospitalTxt = findViewById(R.id.hospitalTxt);
        specializationTxt = findViewById(R.id.specializationTxt);
        pincodeTxt = findViewById(R.id.pincodeTxt);
        docRegisterTxt = findViewById(R.id.docRegisterTxt);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        docRegisterTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean degreeCheck = checkField(degreeTxt);
                Boolean hospitalCheck = checkField(hospitalTxt);
                Boolean specializationCheck = checkField(specializationTxt);
                Boolean pincodeCheck = checkField(pincodeTxt);

                if(degreeCheck && hospitalCheck && specializationCheck && pincodeCheck) {

                    FirebaseUser user = auth.getCurrentUser();
                    DocumentReference df = firestore.collection("Doctors")
                            .document(user.getUid());
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("Degree", degreeTxt.getText().toString());
                    userInfo.put("Hospital", hospitalTxt.getText().toString());
                    userInfo.put("Specialization", specializationTxt.getText().toString());
                    userInfo.put("PIN_Code", "P"+pincodeTxt.getText().toString());
                    df.update(userInfo)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterDocActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterDocActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    Intent intent = new Intent(RegisterDocActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
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