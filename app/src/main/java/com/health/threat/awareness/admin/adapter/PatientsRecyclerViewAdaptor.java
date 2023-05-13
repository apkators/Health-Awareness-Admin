package com.health.threat.awareness.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.health.threat.awareness.admin.R;
import com.health.threat.awareness.admin.dialog_fragments.PatientDetailDialogFragment;
import com.health.threat.awareness.admin.model.Patient;

import java.util.ArrayList;

public class PatientsRecyclerViewAdaptor extends RecyclerView.Adapter<PatientsRecyclerViewAdaptor.MyHolder> {
    FragmentActivity ct;
    ArrayList<Patient> al;

    public PatientsRecyclerViewAdaptor(FragmentActivity cont, ArrayList<Patient> al) {
        this.ct = cont;
        this.al = al;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(ct);
        View v = li.inflate(R.layout.patient_recycler_view_item, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        final Patient p1 = al.get(position);

        if (p1.getName()!=null && !p1.getName().equals(""))
            holder.PatientName.setText("" + p1.getName());
        else
            holder.PatientName.setVisibility(View.GONE);

        if (p1.getContactInformation()!=null && !p1.getContactInformation().equals(""))
            holder.ContactInfo.setText("" + p1.getContactInformation());
        else
            holder.ContactInfo.setVisibility(View.GONE);

        if (p1.getMedicalHistory()!=null && !p1.getMedicalHistory().equals(""))
            holder.MedicalHistory.setText("" + p1.getMedicalHistory());
        else
            holder.MedicalHistory.setVisibility(View.GONE);

        if (p1.getSymptoms()!=null && !p1.getSymptoms().equals(""))
            holder.SymptomsDescription.setText("" + p1.getSymptoms());
        else
            holder.SymptomsDescription.setVisibility(View.GONE);

        holder.cld.setOnClickListener(view -> {
            PatientDetailDialogFragment dialogFragment = new PatientDetailDialogFragment(p1);
            dialogFragment.show(ct.getSupportFragmentManager(), "Show");
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView PatientName;
        TextView ContactInfo,MedicalHistory,SymptomsDescription;
        CardView cld;

        public MyHolder(View itemView) {
            super(itemView);
            cld = itemView.findViewById(R.id.PatientCard);
            PatientName = itemView.findViewById(R.id.PatientName);
            ContactInfo = itemView.findViewById(R.id.ContactInfo);
            MedicalHistory = itemView.findViewById(R.id.MedicalHistory);
            SymptomsDescription = itemView.findViewById(R.id.SymptomsDescription);
        }
    }
}