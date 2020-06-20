package com.example.punchontime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    final boolean success=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        Button btnSignIn= findViewById(R.id.btnSignIn);



        final Context context = this;
        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signIn();

            }
        });

    }

    private void signIn() {
        EditText inputCorreo= findViewById(R.id.etCorreo);
        EditText inputPassword= findViewById(R.id.etPassword);
        String email = inputCorreo.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { finish();
                    FirebaseUser user = mAuth.getCurrentUser();
                    //Toast.makeText(SignUpActivity.this, "User UID: ", Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(SignUpActivity.this,MainScreen.class));
                    Intent intent = new Intent(SignUpActivity.this, MainScreen.class);
                    intent.putExtra("UID", user.getUid());
                    //Toast.makeText(SignUpActivity.this,"Antes de: " + user.getUid(), Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
                }
            });
        }

     private void getIn(){

     }
    }


