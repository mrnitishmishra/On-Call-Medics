package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class DocAppointment extends AppCompatActivity {
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    TextView name , email , bar;
    ImageView image;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    FirebaseStorage storage;
    Uri selectedImage;
    String ImageUrl;
    String docName;

    RecyclerView recview;
    ArrayList<ModelPatientData> datalist1;
    PatientAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_appointment);

        String doc_name = getIntent().getStringExtra("doc_name");


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav = findViewById(R.id.navmenu);
        View headerView = nav.getHeaderView(0);
        drawerLayout = findViewById(R.id.drawer);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.menu_home:
                        Intent intent = new Intent(getApplicationContext(), DocAppointment.class);
                        intent.putExtra("doc_name", doc_name);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_time_table:
                        Intent intent1 = new Intent(getApplicationContext(), TimeTable.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_app_history:
                        Intent intent2 = new Intent(getApplicationContext(), AppointmentHistory.class);
                        intent2.putExtra("doc_name", doc_name);
                        startActivity(intent2);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_logout:
                        Toast.makeText(DocAppointment.this, "Logged out", Toast.LENGTH_SHORT).show();
                        Intent intent3 = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent3);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

       

        name = headerView.findViewById(R.id.nameTxt);
        email = headerView.findViewById(R.id.emailTxt);
        image = headerView.findViewById(R.id.doc_image);
        bar = findViewById(R.id.welcome_message);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        DocumentReference df = firestore.collection("Doctors").document(user.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    name.setText(documentSnapshot.getString("Name"));
                    email.setText(documentSnapshot.getString("Email"));
                    bar.setText("Hello "+documentSnapshot.getString("Name"));
                    ImageUrl = documentSnapshot.getString("Image_Url");
                    Picasso.get().load(ImageUrl).placeholder(R.mipmap.ic_launcher_round).into(image);
                    docName = documentSnapshot.getString("Name");
                }
                else{
                    Toast.makeText(getApplicationContext(), "Not Found........", Toast.LENGTH_SHORT).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to Fetch data", Toast.LENGTH_SHORT).show();
                    }
                });

        storage = FirebaseStorage.getInstance();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, 45);



            }
        });



            recview = findViewById(R.id.recView);
            recview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            datalist1 = new ArrayList<>();
            adapter1 = new PatientAdapter(datalist1);
            recview.setAdapter(adapter1);

        String s = docName;


        firestore.collection("Patient Info").whereEqualTo("Appointed_Doctor", doc_name)
                .whereEqualTo("isChecked", "0")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
//                                bar.setText("Hello "+s);
                                bar.setText("Hello "+docName);
//                                Toast.makeText(DocAppointment.this, "Not Found", Toast.LENGTH_SHORT).show();

                            } else {

                                List<DocumentSnapshot> list1 = queryDocumentSnapshots.getDocuments();
                                for(DocumentSnapshot d:list1){
                                    ModelPatientData obj = d.toObject(ModelPatientData.class);
                                    datalist1.add(obj);
                                }
                                adapter1.notifyDataSetChanged();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Not added", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.getMessage().toString());
                            bar.setText(e.getMessage());
                        }
                    });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                image.setImageURI(data.getData());
                selectedImage = data.getData();
                if (selectedImage != null) {
                    StorageReference reference = storage.getReference().child("Doctor_Profiles").child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        FirebaseUser user = auth.getCurrentUser();
                                        DocumentReference df = firestore.collection("Doctors")
                                                .document(user.getUid());
                                        Map<String, Object> userInfo = new HashMap<>();
                                        userInfo.put("Image_Url", imageUrl);
                                        df.update(userInfo);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onBackPressed() { }
}