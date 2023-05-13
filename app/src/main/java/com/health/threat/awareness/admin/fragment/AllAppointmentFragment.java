package com.health.threat.awareness.admin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.threat.awareness.admin.R;
import com.health.threat.awareness.admin.adapter.AppointmentRecyclerViewAdaptor;
import com.health.threat.awareness.admin.model.Appointment;

import java.util.ArrayList;

public class AllAppointmentFragment extends Fragment {
    public FirebaseAuth mAuth;
    ArrayList<Appointment> al;
    AppointmentRecyclerViewAdaptor md;
    RecyclerView rv;
    DatabaseReference databaseReference;

    public AllAppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_appointment, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        rv = view.findViewById(R.id.rec);
        RecyclerView.LayoutManager rlm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rlm);

        mAuth = FirebaseAuth.getInstance();

        al = new ArrayList<>();

        view.findViewById(R.id.loading).setVisibility(View.VISIBLE);

        FirebaseDatabase.getInstance().getReference().child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                al.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot eachAdRecord : dataSnapshot.getChildren()) {
                        {
                            Appointment p = new Appointment();
                            p.setID(eachAdRecord.getKey());
                            p.setSickness_title(eachAdRecord.child("Sickness_title").getValue(String.class));
                            p.setSymptoms_description(eachAdRecord.child("Symptoms_description").getValue(String.class));
                            p.setHospitalID(eachAdRecord.child("HospitalID").getValue(String.class));
                            p.setHospitalName(eachAdRecord.child("HospitalName").getValue(String.class));
                            p.setDate(eachAdRecord.child("date").getValue(String.class));
                            p.setMonth(eachAdRecord.child("month").getValue(String.class));
                            p.setYear(eachAdRecord.child("year").getValue(String.class));
                            p.setHour(eachAdRecord.child("hour").getValue(String.class));
                            p.setMinutes(eachAdRecord.child("minutes").getValue(String.class));
                            p.setBy(eachAdRecord.child("By").getValue(String.class));
                            p.setLatitude(eachAdRecord.child("Latitude").getValue(String.class));
                            p.setLongitude(eachAdRecord.child("Longitude").getValue(String.class));
                            p.setAltitude(eachAdRecord.child("Altitude").getValue(String.class));
                            p.setAppointmentStatus(eachAdRecord.child("AppointmentStatus").getValue(String.class));
                            p.setSicknessIdentified(Boolean.TRUE.equals(eachAdRecord.child("SicknessIdentified").getValue(Boolean.class)));
                            p.setSickness(eachAdRecord.child("Sickness").getValue(String.class));
                            p.setCaseStatus(eachAdRecord.child("CaseStatus").getValue(String.class));
                            p.setRemarks(eachAdRecord.child("Remarks").getValue(String.class));
                            p.setSendToAdmin(Boolean.TRUE.equals(eachAdRecord.child("SendToAdmin").getValue(Boolean.class)));

                            boolean SeenByAdmin = false;
                            if (eachAdRecord.child("SeenByAdmin").exists())
                            {
                                SeenByAdmin = Boolean.TRUE.equals(eachAdRecord.child("SeenByAdmin").getValue(Boolean.class));
                            }

                            if (p.getCaseStatus() != null) {
                                if (!p.getCaseStatus().equalsIgnoreCase("Normal") && p.isSendToAdmin())
                                    if (!SeenByAdmin)
                                        al.add(p);
                            } else
                                al.add(p);
                        }
                    }
                    if (!al.isEmpty()) {
                        rv.setVisibility(View.VISIBLE);
                        md = new AppointmentRecyclerViewAdaptor(getActivity(), al);
                        rv.setAdapter(md);
                    } else {
                        Toast.makeText(getActivity(), "No Reports found", Toast.LENGTH_SHORT).show();
                        rv.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(getActivity(), "No Reports found", Toast.LENGTH_SHORT).show();
                    rv.setVisibility(View.GONE);
                }

                view.findViewById(R.id.loading).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}