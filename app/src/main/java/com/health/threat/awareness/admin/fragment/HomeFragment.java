package com.health.threat.awareness.admin.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.health.threat.awareness.admin.LocationActivity;
import com.health.threat.awareness.admin.R;
public class HomeFragment extends Fragment {
    CardView AllPatientsCard,VirusCasesCountCard,MarkedRegionOnMapCard;
    TextView textViewVirusCount;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        AllPatientsCard = view.findViewById(R.id.AllPatientsCard);
        VirusCasesCountCard = view.findViewById(R.id.VirusCasesCountCard);
        MarkedRegionOnMapCard = view.findViewById(R.id.MarkedRegionOnMapCard);
        textViewVirusCount= view.findViewById(R.id.textViewVirusCount);

        AllPatientsCard.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, new AllPatientsFragment());
            fragmentTransaction.addToBackStack("HomeFragment");
            fragmentTransaction.commit();
        });

        VirusCasesCountCard.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, new AllVirusFragment());
            fragmentTransaction.addToBackStack("HomeFragment");
            fragmentTransaction.commit();
        });

        MarkedRegionOnMapCard.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LocationActivity.class));
        });

        FirebaseDatabase.getInstance().getReference().child("Virus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    long count  = snapshot.getChildrenCount();

                    textViewVirusCount.setText(String.valueOf(count));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}