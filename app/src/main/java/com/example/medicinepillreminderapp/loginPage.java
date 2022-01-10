package com.example.medicinepillreminderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginPage extends AppCompatActivity {
    EditText email,password;
    Button login,register;
    FirebaseAuth fAuth;
    CheckBox rememberMe;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        login = findViewById(R.id.finalLogin);
        fAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.signup);
        rememberMe = findViewById(R.id.rememberCheck);

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String check = preferences.getString("remember","");
        if(check.equals("true")){
            startActivity(new Intent(loginPage.this,dashboard.class));
        }else if(check.equals("false")){
            Toast.makeText(loginPage.this,"please sign in",Toast.LENGTH_SHORT);
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginHandler();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(loginPage.this,"checked Remember me",Toast.LENGTH_SHORT).show();
                }else if(!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(loginPage.this,"not checked Remember me",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public void openRegister(){
        Intent intent = new Intent(this,registerPage.class);
        startActivity(intent);
    }


    public void loginHandler(){
        String emailStr = email.getText().toString().trim();
        String passStr = password.getText().toString().trim();
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
        fAuth.signInWithEmailAndPassword(emailStr,passStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(loginPage.this,"Logged In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),dashboard.class));
                    finish();
                }
                else{
                    Toast.makeText(loginPage.this,"Error! "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}