package com.example.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class P_show_doctor extends Fragment {
    ListView lv;
    com.example.appointment.DocListAdmin arrayAdapter;
    ArrayList<String> arrayList1,arrayList2,arrayList3;
    String nm,dept,mb,name,mobile,pname,pmobile;
    FirebaseFirestore db;
    Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_p_show_doctor,container,false);
        lv=view.findViewById(R.id.docList);
        arrayList1=new ArrayList<>();
        arrayList2=new ArrayList<>();
        arrayList3=new ArrayList<>();
        bundle=new Bundle();

        bundle=getArguments();
        pname=bundle.getString("name");
        pmobile=bundle.getString("mb");
        db=FirebaseFirestore.getInstance();
        showDoctor();
        return view;
    }
    public void showDoctor(){
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
                                Toast.makeText(getContext(),"Doctors Not Available",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                arrayAdapter = new com.example.appointment.DocListAdmin(getContext(), arrayList1, arrayList2, arrayList3);
                                lv.setAdapter(arrayAdapter);
                            }
                        } else {
                            Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String selectedItem = (String) arg0.getItemAtPosition(position);

                Intent i=new Intent(getContext(),P_doc_schedule.class);
                i.putExtra("name",selectedItem);
                i.putExtra("pname",pname);
                i.putExtra("pmobile",pmobile);
                startActivity(i);
            }
        });
    }
}
