package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.WidgetContainer;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;


public class PatientAdapter2 extends RecyclerView.Adapter<PatientAdapter2.myviewholder2> {

    ArrayList<ModelPatientData> datalist2;

    public PatientAdapter2(ArrayList<ModelPatientData> datalist2) {
        this.datalist2 = datalist2;
    }

    @Override
    public PatientAdapter2.myviewholder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_patient, parent, false);
        PatientAdapter2.myviewholder2 holder = new PatientAdapter2.myviewholder2(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapter2.myviewholder2 holder, int position) {

        holder.patientName.setText(datalist2.get(position).getPatient_Name());
//        holder.patientEmail.setText(datalist2.get(position).getPatient_Email());
        holder.day.setText(datalist2.get(position).getAppointed_Day());
        holder.doc.setText(datalist2.get(position).getAppointed_Doctor());

        if (datalist2.get(position).getIsChecked().equals("0")) {
            holder.status.setText("Active");
        }
        else {
            holder.status.setText("Checked");
        }

        String PatientName = datalist2.get(position).getPatient_Name();
        String PatientEmail = datalist2.get(position).getPatient_Email();

        holder.parrentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), PatientDetails2.class);
                intent.putExtra("PatientName", PatientName);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist2.size();
    }

    class myviewholder2 extends RecyclerView.ViewHolder{

        TextView patientName , patientEmail , day , doc , status;
        androidx.constraintlayout.widget.ConstraintLayout parrentLayout;

        public myviewholder2(@NonNull View itemView) {
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
