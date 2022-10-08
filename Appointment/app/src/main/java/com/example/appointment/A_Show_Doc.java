package com.example.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

public class A_Show_Doc extends Fragment {

    ListView lv;
    ArrayList<String> arrayList1,arrayList2,arrayList3;
    String nm,dept,mb,sch;
    FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_a__show__doc,container,false);
        lv=view.findViewById(R.id.docList);
        arrayList1=new ArrayList<>();
        arrayList2=new ArrayList<>();
        arrayList3=new ArrayList<>();

        db=FirebaseFirestore.getInstance();
        db.collection("Doctors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nm=document.get("name").toString();
                                dept=document.get("department").toString();
                                mb=document.get("mobile").toString();
                                arrayList1.add(nm);
                                arrayList2.add("Department : "+dept);
                                arrayList3.add("Mobile : "+mb);
                            }
                            if(arrayList1.isEmpty()){
                                Toast.makeText(getContext(), "Doctors Not Available", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                DocListAdmin arrayAdapter=new DocListAdmin(getContext(),arrayList1,arrayList2,arrayList3);
                                lv.setAdapter(arrayAdapter);
                            }
                        } else {
                            Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return view;
    }
}
