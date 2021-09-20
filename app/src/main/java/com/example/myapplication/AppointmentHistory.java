package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppointmentHistory extends AppCompatActivity {
    RecyclerView recview;
    ArrayList<ModelPatientData> datalist1;
    PatientAdapter adapter1;
    FirebaseFirestore firestore;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);

        String docName = getIntent().getStringExtra("doc_name");

        toolbar = findViewById(R.id.toolbar_appointment_history);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();



            recview = findViewById(R.id.recView);
            recview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            datalist1 = new ArrayList<>();
            adapter1 = new PatientAdapter(datalist1);
            recview.setAdapter(adapter1);


            firestore.collection("Patient Info").whereEqualTo("Appointed_Doctor", docName)
                    .whereEqualTo("isChecked", "1")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> list1 = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d:list1){
                                ModelPatientData obj = d.toObject(ModelPatientData.class);
                                datalist1.add(obj);
                            }
                            adapter1.notifyDataSetChanged();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Not added", Toast.LENGTH_SHORT).show();
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