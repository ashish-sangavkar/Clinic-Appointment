package com.example.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class A_Delete_Doc extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner s1;
    EditText e1,e2;
    Button b1;
    ArrayList<String> arrayList1,arrayList2;
    FirebaseFirestore db;
    String nm="",id1="";
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_a__delete__doc,container,false);
        e1=view.findViewById(R.id.e1);
        e2=view.findViewById(R.id.e2);
        s1=view.findViewById(R.id.s1);
        b1=view.findViewById(R.id.b1);

        db = FirebaseFirestore.getInstance();
        loadDocId();
        s1.setOnItemSelectedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDoctor();
            }
        });
        return view;
    }
    private void loadDocId(){
        arrayList1=new ArrayList<>();
        db.collection("Doctors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                arrayList1.add(document.get("name").toString());
                            }
                            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,arrayList1);
                            s1.setAdapter(arrayAdapter);
                        } else {
                            Toast.makeText(getContext(),"Document not found",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void deleteDoctor(){
        db.collection("Doctors").document(""+id1).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(),"Data Deleted",Toast.LENGTH_SHORT).show();
                        loadDocId();
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        nm=parent.getItemAtPosition(position).toString();
        db.collection("Doctors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(nm.equalsIgnoreCase(document.get("name").toString())){
                                    e1.setText(document.get("mobile").toString());
                                    e2.setText(document.get("department").toString());
                                    id1=document.getId().toString();
                                }
                            }
                        } else {
                            Toast.makeText(getContext(),"Document not found",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
