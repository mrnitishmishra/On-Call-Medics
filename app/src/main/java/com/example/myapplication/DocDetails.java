package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class DocDetails extends AppCompatActivity {
    TextView name , degree , fee , hospital , address , status;
    ImageView image , days;
    Button book;
    String day , username , useremail;
    Toolbar toolbar;

    Boolean isMonday = true , isTuesday = true , isWednesday = true , isThursday = true , isFriday = true , isSaturday = true ;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_details);

        toolbar = findViewById(R.id.toolbar_time_table);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name= findViewById(R.id.textView12);
        username = getIntent().getStringExtra("DocName");
        useremail = getIntent().getStringExtra("DocEmail");
        name.setText(username);
        degree = findViewById(R.id.textView15);
        fee = findViewById(R.id.fee);
        hospital = findViewById(R.id.textView17);
        address = findViewById(R.id.textView21);
        status = findViewById(R.id.status);
        image = findViewById(R.id.docImage);
        days = findViewById(R.id.appCompatImageView);
        book = findViewById(R.id.buttonBook);

        firestore = FirebaseFirestore.getInstance();

        try {
            DocumentReference df = firestore.collection("Doctor Info").document(useremail.toString());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        degree.setText(documentSnapshot.getString("Degree"));
                        hospital.setText(documentSnapshot.getString("Hospital"));
                        fee.setText(documentSnapshot.getString("Fee"));

                        String url = documentSnapshot.getString("Image_Url");
                        Glide.with(getApplicationContext()).load(url).placeholder(R.drawable.doctor).into(image);

                        if(documentSnapshot.getString("Day1") == null && documentSnapshot.getString("Day2") == null &&
                                documentSnapshot.getString("Day3") == null && documentSnapshot.getString("Day4") == null &&
                                documentSnapshot.getString("Day5") == null && documentSnapshot.getString("Day6") == null ) {

                            status.setText("Currently Unavailable");

                        }
                        else if (documentSnapshot.getString("Shift") == null) {
                            status.setText("Currently Unavailable");
                        }
                        else {
                            status.setText("Available");
                        }
                        if (documentSnapshot.get("Day1") == null) {
                            isMonday = false;
                        }
                        if (documentSnapshot.get("Day2") == null) {
                            isTuesday = false;
                        }
                        if (documentSnapshot.get("Day3") == null) {
                            isWednesday = false;
                        }
                        if (documentSnapshot.get("Day4") == null) {
                            isThursday = false;
                        }
                        if (documentSnapshot.get("Day5") == null) {
                            isFriday = false;
                        }
                        if (documentSnapshot.get("Day6") == null) {
                            isSaturday = false;
                        }
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DocDetails.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DocDetails.this, "ok", Toast.LENGTH_SHORT).show();
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), days);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_days, popupMenu.getMenu());

                if(!isMonday) {
                    popupMenu.getMenu().removeItem(R.id.monday);
                }
                if(!isTuesday) {
                    popupMenu.getMenu().removeItem(R.id.tuesday);
                }
                if(!isWednesday) {
                    popupMenu.getMenu().removeItem(R.id.wednesday);
                }
                if(!isThursday) {
                    popupMenu.getMenu().removeItem(R.id.thursday);
                }
                if(!isFriday) {
                    popupMenu.getMenu().removeItem(R.id.friday);
                }
                if(!isSaturday) {
                    popupMenu.getMenu().removeItem(R.id.saturday);
                }

                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.monday:
                                day = "Monday";
                                break;

                            case R.id.tuesday:
                                day = "Tuesday";
                                break;

                            case R.id.wednesday:
                                day = "Wednesday";
                                break;

                            case R.id.thursday:
                                day = "Thursday";
                                break;

                            case R.id.friday:
                                day = "Friday";
                                break;

                            case R.id.saturday:
                                day = "Saturday";
                                break;
                        }

                        return false;
                    }

                });
            }



        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookAppointment.class);
                intent.putExtra("DocName", username);
                intent.putExtra("DocEmail", useremail);
                intent.putExtra("Day", day);
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