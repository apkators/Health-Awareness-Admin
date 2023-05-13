package com.health.threat.awareness.admin.dialog_fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.health.threat.awareness.admin.R;
import com.health.threat.awareness.admin.fragment.AddDetailsOnMapFragment;
import com.health.threat.awareness.admin.model.Patient;

import java.util.Objects;

public class PatientDetailDialogFragment extends DialogFragment {
    Patient patient;

    ImageButton cancelBtn;
    private EditText fullNameInput;
    private EditText dateOfBirthInput;
    private EditText ageInput;
    private EditText weightInput;
    private EditText heightInput;
    private EditText bloodPressureInput;
    private EditText pulseRateInput;
    private EditText respiratoryRateInput;
    private EditText bodyTemperatureInput;
    private EditText latitudeInput;
    private EditText longitudeInput;
    private EditText altitudeInput;
    private EditText contactInput;
    private EditText insuranceInput;
    private EditText historyInput,hrInput;
    private EditText symptoms_input;
    private Button btnMarkAreaOnMap;
    RadioGroup grp;

    public PatientDetailDialogFragment(Patient p) {
        // Required empty public constructor
        patient = p;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroy() {
        if (getDialog() != null && getDialog().isShowing())
            getDialog().dismiss();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_detail_dialog, container, false);

        // Get references to all the EditText views
        fullNameInput = view.findViewById(R.id.name_input);
        dateOfBirthInput = view.findViewById(R.id.dob_input);
        ageInput = view.findViewById(R.id.age_input);
        weightInput = view.findViewById(R.id.weight_input);
        heightInput = view.findViewById(R.id.height_input);
        bloodPressureInput = view.findViewById(R.id.blood_pressure_input);
        pulseRateInput = view.findViewById(R.id.pulse_rate_input);
        respiratoryRateInput = view.findViewById(R.id.respiratory_rate_input);
        bodyTemperatureInput = view.findViewById(R.id.body_temperature_input);
        latitudeInput = view.findViewById(R.id.latitude_input);
        longitudeInput = view.findViewById(R.id.longitude_input);
        altitudeInput = view.findViewById(R.id.altitude_input);
        symptoms_input = view.findViewById(R.id.symptoms_input);
        contactInput = view.findViewById(R.id.contact_info_input);
        hrInput = view.findViewById(R.id.heart_rate_input);
        insuranceInput = view.findViewById(R.id.insurance_details_input);
        historyInput = view.findViewById(R.id.medical_history_input);
        grp = view.findViewById(R.id.gender_radio_group);

        btnMarkAreaOnMap = view.findViewById(R.id.btnMarkAreaOnMap);
        btnMarkAreaOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new AddDetailsOnMapFragment(patient));
                fragmentTransaction.addToBackStack("HomeFragment");
                fragmentTransaction.commit();
                getDialog().dismiss();
            }
        });

        cancelBtn = view.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        fullNameInput.setText(patient.getName());
        dateOfBirthInput.setText(patient.getDateOfBirth());
        ageInput.setText(patient.getAge() + "");
        weightInput.setText(patient.getWeight() + "");
        heightInput.setText(patient.getHeight() + "");
        bloodPressureInput.setText(patient.getBloodPressure() + "");
        pulseRateInput.setText(patient.getPulseRate()+"");
        respiratoryRateInput.setText(patient.getRespiratoryRate()+"");
        bodyTemperatureInput.setText(patient.getBodyTemperature() + "");
        latitudeInput.setText(patient.getLatitude() + "");
        longitudeInput.setText(patient.getLongitude() + "");
        altitudeInput.setText(patient.getAltitude() + "");
        symptoms_input.setText(patient.getSymptoms());
        contactInput.setText(patient.getContactInformation());
        hrInput.setText(patient.getHeartRate()+"");
        insuranceInput.setText(patient.getInsuranceDetails());
        historyInput.setText(patient.getMedicalHistory());
        if (patient.getGender().equalsIgnoreCase("Male"))
            grp.check(R.id.male_radio_button);
        else
            grp.check(R.id.female_radio_button);

        return view;
    }
}