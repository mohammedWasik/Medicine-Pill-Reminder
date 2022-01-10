package com.example.medicinepillreminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registerPage extends AppCompatActivity {
    EditText email,password,retypePassword;
    Button register;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        email = findViewById(R.id.registerEmail);
        password =  findViewById(R.id.registerPassword);
        retypePassword = findViewById(R.id.retypePassword);
        register = findViewById(R.id.registerFinal);
        progressBar = findViewById(R.id.progressBar);

        fAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerFormHandle();
            }
        });
    }
    public void registerFormHandle(){
        String emailStr = email.getText().toString().trim();
        String passStr = password.getText().toString().trim();
        String retype = retypePassword.getText().toString().trim();

        if (TextUtils.isEmpty(emailStr)) {
            email.setError("email is required");
            return;
        }
        if (TextUtils.isEmpty(passStr)) {
            password.setError("password is required");
            return;
        }
        if(passStr.length()<7){
            password.setError("must be atleast 7 charecters long");
            return;
        }
        if (TextUtils.isEmpty(retype)) {
            retypePassword.setError("retype password");
            return;
        }
        if(!passStr.equals(retype)){
            retypePassword.setError("not same");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        fAuth.createUserWithEmailAndPassword(emailStr,passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(),loginPage.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"User Created", Toast.LENGTH_SHORT);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error! "+task.getException().getMessage(),Toast.LENGTH_LONG);
                }
            }
        });



    }

}