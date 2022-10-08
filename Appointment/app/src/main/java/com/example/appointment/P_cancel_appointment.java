package com.example.appointment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class P_cancel_appointment extends Fragment {
    ListView lv;
    ArrayList<String> arrayList1,arrayList2,arrayList3,arrayList4;
    String dname,date,from_time,pname,pmobile;
    AlertDialog.Builder builder;
    FirebaseFirestore db;
    Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_p_cancel_appointment,container,false);
        lv=view.findViewById(R.id.appointmentlist);
        arrayList1=new ArrayList<>();
        arrayList2=new ArrayList<>();
        arrayList3=new ArrayList<>();
        arrayList4=new ArrayList<>();
        bundle=new Bundle();
        builder = new AlertDialog.Builder(getContext());
        bundle=getArguments();
        pmobile=bundle.getString("mb");
        db=FirebaseFirestore.getInstance();
        db.collection("Records").document(pmobile).collection("info")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                pname=document.get("pname").toString();
                                date=document.get("date").toString();
                                from_time=document.get("from_time").toString();
                                dname=document.get("dname").toString();
                                arrayList1.add(pname);
                                arrayList2.add(date);
                                arrayList3.add(from_time);
                                arrayList4.add(dname);
                            }
                            if(arrayList1.isEmpty()){
                                Toast.makeText(getContext(), "Appointments Not Available", Toast.LENGTH_SHORT).show();
                            }
                            com.example.appointment.Patient_Appointment arrayAdapter=new com.example.appointment.Patient_Appointment(getContext(),arrayList1,arrayList2,arrayList3,arrayList4);
                            lv.setAdapter(arrayAdapter);
                        } else {
                            Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String selectedItem = (String) arg0.getItemAtPosition(position);
                selectAppointment(selectedItem);
            }
        });
        return view;
    }
    private void selectAppointment(String nm){
        db.collection("Records").document(pmobile).collection("info")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot:task.getResult()){
                                if(nm.equals(documentSnapshot.getString("pname"))){
                                    String dname=documentSnapshot.getString("dname");
                                    String date=documentSnapshot.getString("date");
                                    String from_time=documentSnapshot.getString("from_time");
                                    setConfirmation(nm,dname,date,from_time);
                                }
                            }
                        }
                    }
                });
    }
    private void setConfirmation(String nm, String dname, String date, String from_time){
        builder.setMessage("Are you sure you want to cancel appointment")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cancelAppointment(nm,dname,date,from_time);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Cancel Appointment Confirmation");
        alert.show();
    }
    private void cancelAppointment(String nm, String dname, String date, String from_time){
        db.collection("Appointments").document(dname).collection(date).document(from_time).collection("info").document(nm)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Appointment Cancelled", Toast.LENGTH_LONG).show();
                        arrayList1.remove(nm);
                        arrayList2.remove(date);
                        arrayList3.remove(from_time);
                        arrayList4.remove(dname);
                        com.example.appointment.Patient_Appointment arrayAdapter=new com.example.appointment.Patient_Appointment(getContext(),arrayList1,arrayList2,arrayList3,arrayList4);
                        lv.setAdapter(arrayAdapter);
                    }
                });
        db.collection("Records").document(pmobile).collection("info").document(nm)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
}
