package com.example.myapplication;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DocAdapter extends RecyclerView.Adapter<DocAdapter.myviewholder> implements Filterable{


    ArrayList<ModelDocData> datalist;
    List<ModelDocData> datalistAll;


    public DocAdapter(ArrayList<ModelDocData> datalist) {
        this.datalist = datalist;
        datalistAll = new ArrayList<>(datalist);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_doc, parent, false);
        myviewholder holder = new myviewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        Log.d("TAG", "onBindViewHolder: called.");
        holder.docName.setText(datalist.get(position).getName());
        holder.pin.setText(datalist.get(position).getPIN_Code());
        holder.degree.setText(datalist.get(position).getDegree());
        holder.specialization.setText(datalist.get(position).getSpecialization());
        String DocEmail = datalist.get(position).getEmail();
        Glide.with(holder.image.getContext()).load(datalist.get(position).getImage_Url()).placeholder(R.drawable.doctor).into(holder.image);
        holder.parrentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "yes", Toast.LENGTH_SHORT).show();
                String DocName = holder.docName.getText().toString();
                Intent intent = new Intent(v.getContext(), DocDetails.class);
                intent.putExtra("DocName", DocName);
                intent.putExtra("DocEmail", DocEmail);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (datalistAll.size() == 0) {
                datalistAll  = new ArrayList<>(datalist);
            }
            List<ModelDocData> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(datalistAll);
            }
            else {
                String filterPatttern = constraint.toString().toLowerCase().trim();

                for (ModelDocData item : datalistAll) {
                    if (item.getPIN_Code().contains(filterPatttern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            datalist.clear();
            datalist.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    class myviewholder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView docName , pin , degree , specialization;
        androidx.constraintlayout.widget.ConstraintLayout parrentLayout;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile);
            docName = itemView.findViewById(R.id.docName);
            pin = itemView.findViewById(R.id.pincode1);
            parrentLayout = itemView.findViewById(R.id.parrentLayout);
            degree = itemView.findViewById(R.id.degree1);
            specialization = itemView.findViewById(R.id.specialization1);
        }
    }
}
