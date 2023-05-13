package com.health.threat.awareness.admin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.health.threat.awareness.admin.R;
import com.health.threat.awareness.admin.api.ClientApi;
import com.health.threat.awareness.admin.model.Appointment;
import com.health.threat.awareness.admin.model.Data;
import com.health.threat.awareness.admin.model.MyResponse;
import com.health.threat.awareness.admin.model.NotificationSender;
import com.health.threat.awareness.admin.model.Patient;
import com.health.threat.awareness.admin.services.ApiServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class AddDetailsOnMapFragment extends Fragment {
    Appointment appointment;
    Patient patient;

    Button UploadToMapBtn, date_time_set;
    EditText edLatitude, edLongitude, edAltitude;
    EditText Case_title, Case_description;
    TextView DateAndTimeTV;
    Spinner VirusCategorySpinner;
    ArrayList<String> VirusIDs = new ArrayList<>();
    List<String> VirusNames = new ArrayList<>();
    String date, month, year, hour, minutes;
    EditText Case_Category_title;
    ArrayAdapter<String> spinnerArrayAdapter;

    public AddDetailsOnMapFragment() {
        // Required empty public constructor
    }

    public AddDetailsOnMapFragment(Appointment a) {
        appointment = a;
    }

    public AddDetailsOnMapFragment(Patient p) {
        patient = p;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_details_on_map, container, false);

        date = month = year = hour = minutes = null;

        UploadToMapBtn = view.findViewById(R.id.UploadToMapBtn);
        date_time_set = view.findViewById(R.id.date_time_set);
        edLatitude = view.findViewById(R.id.edLatitude);
        edLongitude = view.findViewById(R.id.edLongitude);
        edAltitude = view.findViewById(R.id.edAltitude);
        Case_title = view.findViewById(R.id.Case_title);
        Case_description = view.findViewById(R.id.Case_description);
        DateAndTimeTV = view.findViewById(R.id.DateAndTimeTV);
        VirusCategorySpinner = view.findViewById(R.id.VirusCategorySpinner);
        Case_Category_title = view.findViewById(R.id.Case_Category_title);
        Case_Category_title.setVisibility(View.GONE);

        if (appointment != null) {
            edLatitude.setText(appointment.getLatitude());
            edLongitude.setText(appointment.getLongitude());
            edAltitude.setText(appointment.getAltitude());
        } else if (patient != null) {
            edLatitude.setText(patient.getLatitude() + "");
            edLongitude.setText(patient.getLongitude() + "");
            edAltitude.setText(patient.getAltitude() + "");
        }

        getVirusCategory();

        UploadToMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edLatitude.getText() == null || edLatitude.getText().toString().equals("")) {
                    Toast.makeText(requireActivity(), "Latitude is Required", Toast.LENGTH_SHORT).show();
                    edLatitude.requestFocus();
                    edLatitude.setError("Required");
                    return;
                }
                if (edLongitude.getText() == null || edLongitude.getText().toString().equals("")) {
                    Toast.makeText(requireActivity(), "Longitude is Required", Toast.LENGTH_SHORT).show();
                    edLongitude.requestFocus();
                    edLongitude.setError("Required");
                    return;
                }
                if (edAltitude.getText() == null || edAltitude.getText().toString().equals("")) {
                    Toast.makeText(requireActivity(), "edAltitude is Required", Toast.LENGTH_SHORT).show();
                    edAltitude.requestFocus();
                    edAltitude.setError("Required");
                    return;
                }

                if (VirusCategorySpinner.getSelectedItem() == null) {
                    Toast.makeText(requireActivity(), "Please Select Case Category from List", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (VirusCategorySpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(requireActivity(), "Please Select Case a Category from List", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (VirusCategorySpinner.getSelectedItemPosition() == spinnerArrayAdapter.getCount() - 1) {
                    if (edAltitude.getText() == null || edAltitude.getText().toString().equals("")) {
                        Toast.makeText(requireActivity(), "Case Category is Required", Toast.LENGTH_SHORT).show();
                        Case_Category_title.requestFocus();
                        Case_Category_title.setError("Required");
                        Case_Category_title.setVisibility(View.VISIBLE);
                        return;
                    }
                }

                if (Case_title.getText() == null || Case_title.getText().toString().equals("")) {
                    Toast.makeText(requireActivity(), "Category Title is Required", Toast.LENGTH_SHORT).show();
                    Case_title.requestFocus();
                    Case_title.setError("Required");
                    return;
                }

                if (Case_description.getText() == null || Case_description.getText().toString().equals("")) {
                    Toast.makeText(requireActivity(), "Case Description is Required", Toast.LENGTH_SHORT).show();
                    Case_description.requestFocus();
                    Case_description.setError("Required");
                    return;
                }

                if (date == null || month == null || year == null || hour == null || minutes == null) {
                    Toast.makeText(requireActivity(), "Please Select Date and Time", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(requireActivity(), "Please wait...", Toast.LENGTH_SHORT).show();

                SaveVirusToDatabase();
            }
        });

        view.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = View.inflate(getActivity(), R.layout.date_time_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(requireActivity()).create();

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = dialogView.findViewById(R.id.time_picker);

                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getHour(),
                                timePicker.getMinute());

                        date = String.valueOf(datePicker.getDayOfMonth());
                        month = String.valueOf(datePicker.getMonth());
                        year = String.valueOf(datePicker.getYear());
                        hour = String.valueOf(timePicker.getHour());
                        minutes = String.valueOf(timePicker.getMinute());

                        DateAndTimeTV.setText(date + "-" + month + "-" + year + " " + hour + ":" + minutes);
                        //time = calendar.getTimeInMillis();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
        return view;
    }

    private void getVirusCategory() {
        FirebaseDatabase.getInstance().getReference().child("Virus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                VirusIDs.clear();
                VirusNames.clear();

                spinnerArrayAdapter = new ArrayAdapter<String>
                        (requireActivity(), android.R.layout.simple_spinner_item); //selected item will look like a spinner set from XML

                if (dataSnapshot.exists()) {
                    spinnerArrayAdapter.add("Select");

                    for (DataSnapshot eachAdRecord : dataSnapshot.getChildren()) {
                        {
                            if (!VirusNames.contains(eachAdRecord.child("CaseCategory").getValue(String.class))) {
                                VirusIDs.add(eachAdRecord.getKey());
                                VirusNames.add(eachAdRecord.child("CaseCategory").getValue(String.class));

                                spinnerArrayAdapter.add(eachAdRecord.child("CaseCategory").getValue(String.class));
                            }
                        }
                    }

                    spinnerArrayAdapter.add("Add New Category");

                    if (!VirusIDs.isEmpty()) {
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        VirusCategorySpinner.setAdapter(spinnerArrayAdapter);

                        VirusCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == spinnerArrayAdapter.getCount() - 1) {
                                    Case_Category_title.setVisibility(View.VISIBLE);
                                } else
                                    Case_Category_title.setVisibility(View.GONE);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } else {
                        spinnerArrayAdapter.clear();

                        spinnerArrayAdapter.add("Select");
                        spinnerArrayAdapter.add("Add New Category");

                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        VirusCategorySpinner.setAdapter(spinnerArrayAdapter);

                        VirusCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position == spinnerArrayAdapter.getCount() - 1) {
                                    Case_Category_title.setVisibility(View.VISIBLE);
                                } else
                                    Case_Category_title.setVisibility(View.GONE);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                } else {
                    spinnerArrayAdapter.add("Select");
                    spinnerArrayAdapter.add("Add New Category");

                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                            .simple_spinner_dropdown_item);
                    VirusCategorySpinner.setAdapter(spinnerArrayAdapter);

                    VirusCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == spinnerArrayAdapter.getCount() - 1) {
                                Case_Category_title.setVisibility(View.VISIBLE);
                            } else
                                Case_Category_title.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SaveVirusToDatabase() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("Case_title", Case_title.getText().toString());
        data.put("Name", Case_title.getText().toString());
        data.put("Case_description", Case_description.getText().toString());
        data.put("Latitude", edLatitude.getText().toString());
        data.put("Longitude", edLongitude.getText().toString());
        data.put("Altitude", edAltitude.getText().toString());
        if (appointment!=null) {
            data.put("HospitalID", appointment.getHospitalID());
            data.put("HospitalName", appointment.getHospitalName());
            data.put("Remarks", appointment.getRemarks());
            data.put("CaseStatus", appointment.getCaseStatus());
            data.put("AffectedUserID", appointment.getBy());
        }else if (patient!=null){
            data.put("Name",patient.getName());
            data.put("ContactInformation",patient.getContactInformation());
            data.put("Gender",patient.getGender());

            /*data.put("HospitalID", patient.getHospitalID());
            data.put("HospitalName", patient.getHospitalName());
            data.put("Remarks", patient.getRemarks());
            data.put("CaseStatus", patient.getCaseStatus());
            data.put("AffectedUserID", patient.getBy());*/
        }
        data.put("date", date);
        data.put("month", month);
        data.put("year", year);
        data.put("hour", hour);
        data.put("minutes", minutes);
        data.put("By", FirebaseAuth.getInstance().getInstance().getUid());
        data.put("SicknessIdentified", true);
        data.put("Sickness", "");
        if (VirusCategorySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Please Add Category", Toast.LENGTH_SHORT).show();
        } else if (VirusCategorySpinner.getSelectedItemPosition() == spinnerArrayAdapter.getCount() - 1) {
            data.put("CaseCategory", Case_Category_title.getText().toString());
        } else {
            data.put("CaseCategory", VirusCategorySpinner.getSelectedItem().toString());
        }

        DatabaseReference ownerRef;
        ownerRef = FirebaseDatabase.getInstance().getReference().child("Virus");

        Map<String, Object> update = new HashMap<>();
        update.put("SeenByAdmin", true);

        if (appointment!=null) {
            FirebaseDatabase.getInstance().getReference().child("Appointments").child(appointment.getID())
                    .updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireActivity(), "Case added successfully", Toast.LENGTH_SHORT).show();

                                ownerRef.child(ownerRef.push().getKey()).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                                sendNotificationToAllUsers(Case_title.getText().toString() + " Case", Case_description.getText().toString());

                                getActivity().onBackPressed();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(requireActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else if (patient!=null) {
            FirebaseDatabase.getInstance().getReference().child("Patients").child(patient.getID())
                    .updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireActivity(), "Case added successfully", Toast.LENGTH_SHORT).show();

                                ownerRef.child(ownerRef.push().getKey()).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                                sendNotificationToAllUsers(Case_title.getText().toString() +" Case",Case_description.getText().toString());

                                getActivity().onBackPressed();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(requireActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void sendNotificationToAllUsers(String title, String message) {

        ApiServices apiServices = ClientApi.getRetrofit("https://fcm.googleapis.com/").create(ApiServices.class);

        Data data = new Data(title, message);
        NotificationSender notificationSender = new NotificationSender(data, "/topics/AllUsers");
        Log.d("TAG", "sendNotification: ");

        apiServices.sendNotification(notificationSender).enqueue(new retrofit2.Callback<MyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        if (response.body().success != 1) {
                            Log.d("TAG", "onResponse: SUCCESS");
                            //Toast.makeText(GoogleLocationActivity.this,"Notification Sent",Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("TAG", "onFailure: FAILED");
                            //Toast.makeText(GoogleLocationActivity.this,"Notification failed",Toast.LENGTH_SHORT).show();
                        }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                Log.d("TAG", "onFailure: FAILED");
            }
        });
    }

    public void sendNotificationToOneUser(String deviceToken, String title, String message) {
        ApiServices apiServices = ClientApi.getRetrofit("https://fcm.googleapis.com/").create(ApiServices.class);

        Data data = new Data(title, message);
        NotificationSender notificationSender = new NotificationSender(data, deviceToken);
        Log.d("TAG", "sendNotification: ");

        apiServices.sendNotification(notificationSender).enqueue(new retrofit2.Callback<MyResponse>() {
            @Override
            public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        if (response.body().success != 1) {
                            Log.d("TAG", "onResponse: SUCCESS");
                            //Toast.makeText(GoogleLocationActivity.this,"Notification Sent",Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("TAG", "onFailure: FAILED");
                            //Toast.makeText(GoogleLocationActivity.this,"Notification failed",Toast.LENGTH_SHORT).show();
                        }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                Log.d("TAG", "onFailure: FAILED");
            }
        });
    }

}