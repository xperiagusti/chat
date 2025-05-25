package com.example.mychat.sign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.mychat.MainActivity;
import com.example.mychat.R;
import com.example.mychat.databinding.ActivityInfoAkunBinding;
import com.example.mychat.model.Tab;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class infoAkun extends AppCompatActivity {


    private ActivityInfoAkunBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_info_akun);
        progressDialog = new ProgressDialog(this);

        klikTombol();
    }

    private void klikTombol() {
        binding.btnLanjut.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(binding.edNama.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Silahkan mengisi nama akun anda",Toast.LENGTH_SHORT).show();
                } else {
                    simpan();
                }
            }
        });
    }

    private void simpan() {
        progressDialog.setMessage("Sedang Memproses . . .");
        progressDialog.show();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {

            String ID = firebaseUser.getUid();
            Tab akun = new Tab(ID,
                    firebaseUser.getPhoneNumber(),
                    binding.edNama.getText().toString(),
                    "",
                    "");

            firebaseFirestore.collection("Akun").document(firebaseUser.getUid())
                    .set(akun).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Berhasil Menyimpan Akun", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

            });
        }else{
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
        }
    }

}