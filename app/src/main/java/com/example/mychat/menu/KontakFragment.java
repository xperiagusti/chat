package com.example.mychat.menu;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mychat.R;
import com.example.mychat.adapter.AdapterTab;
import com.example.mychat.databinding.FragmentTabBinding;
import com.example.mychat.model.Tab;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class KontakFragment extends Fragment {

    private FragmentTabBinding binding;
    private List<Tab> list = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;


    public KontakFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tab, container, false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(firebaseUser != null){
            bacaKontak();
        }
        return binding.getRoot();
    }

    private void bacaKontak() {
        firebaseFirestore.collection("Akun").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                    String ID = snapshot.getString("id");

                    Tab akun = new Tab(ID,
                            snapshot.getString("noTelp"),
                            snapshot.getString("nama"),
                            snapshot.getString("keterangan"),
                            snapshot.getString("tanggal"));
                    if(ID != null && !ID.equals(firebaseUser.getUid())){
                        list.add(akun);
                    }
                }
                binding.recyclerView.setAdapter(new AdapterTab(list,getContext()));
            }
        });

    }
}