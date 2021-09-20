package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;


public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.myviewholder1> {

    ArrayList<ModelPatientData> datalist1;

    public PatientAdapter(ArrayList<ModelPatientData> datalist1) {
        this.datalist1 = datalist1;
    }

    @Override
    public PatientAdapter.myviewholder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_patient, parent, false);
        PatientAdapter.myviewholder1 holder = new PatientAdapter.myviewholder1(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapter.myviewholder1 holder, int position) {

        holder.patientName.setText(datalist1.get(position).getPatient_Name());
//        holder.patientEmail.setText(datalist1.get(position).getPatient_Email());

        holder.day.setText(datalist1.get(position).getAppointed_Day());
        holder.doc.setText(datalist1.get(position).getAppointed_Doctor());

        if (datalist1.get(position).getIsChecked().equals("0")) {
            holder.status.setText("Active");
        }
        else {
            holder.status.setText("Checked");
        }

        String PatientName = datalist1.get(position).getPatient_Name();
        String PatientEmail = datalist1.get(position).getPatient_Email();

        holder.parrentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), PatientDetails.class);
                intent.putExtra("PatientName", PatientName);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist1.size();
    }

    class myviewholder1 extends RecyclerView.ViewHolder{

        TextView patientName , patientEmail , day , doc , status;
        androidx.constraintlayout.widget.ConstraintLayout parrentLayout;

        public myviewholder1(@NonNull View itemView) {
            super(itemView);

            patientName = itemView.findViewById(R.id.PatientName);
//            patientEmail = itemView.findViewById(R.id.hidden_email);
            parrentLayout = itemView.findViewById(R.id.parrentLayout1);
            day = itemView.findViewById(R.id.showDay);
            doc = itemView.findViewById(R.id.showDoc);
            status = itemView.findViewById(R.id.showStatus);
        }
    }
}
