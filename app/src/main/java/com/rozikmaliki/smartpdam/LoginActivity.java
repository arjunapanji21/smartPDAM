package com.rozikmaliki.smartpdam;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    // variabel
    private EditText email;
    private EditText password;
    private Button login;
    private TextView daftar;
    private FirebaseAuth auth;
    private ImageButton pwVisibilitiy;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference root =  database.getReference();

    boolean passwordVisibility = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ambil komponen berdasarkan id
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        daftar = findViewById(R.id.daftar);
        pwVisibilitiy = findViewById(R.id.pwVisibility);

        // inisialisasi firebase auth
        auth = FirebaseAuth.getInstance();

        // set on click listener untuk melihat password;
        pwVisibilitiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwordVisibility == false){
                    pwVisibilitiy.setImageResource(R.drawable.ic_visibility);
                    passwordVisibility = true;
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    pwVisibilitiy.setImageResource(R.drawable.ic_visibility_off);
                    passwordVisibility = false;
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        // set on click listener pada button login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = email.getText().toString();
                String txtPass = password.getText().toString();
                // menampilkan peringatan jika kolom email dan password kosong
                if(TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPass)){
                    Toast.makeText(LoginActivity.this, "Email dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                }
                // menampilkan peringatan jika password terlalu pendek
                else if (txtPass.length() < 6){
                    Toast.makeText(LoginActivity.this, "Password tidak boleh kurang dari 6 karakter!", Toast.LENGTH_SHORT).show();
                }
                else {
                    loginUser(txtEmail, txtPass);
                }
            }
        });

        // set on click listener pada text daftar
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrasiActivity.class));
                finish();
            }
        });
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    setProgressBarVisibility(true);
                    Toast.makeText(LoginActivity.this, "Login berhasil!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Login gagal! periksa format email dan password atau user belum ada, silahkan registrasi.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}