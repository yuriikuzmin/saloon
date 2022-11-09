package com.kuzmin.beautysaloon;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {

    private EditText edLogin, edPassword;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private TextView tvUserTel;
    private Button button_reg, button_exit, bStart, button_in;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authen_layout);

        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        button_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singIn();
            }
        });
        button_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void  init(){
        edLogin = (EditText)findViewById(R.id.edit_login);
        edPassword = (EditText) findViewById(R.id.edit_name_kod);
        mAuth = FirebaseAuth.getInstance();
        button_in=(Button) findViewById(R.id.button_enter);
        button_reg=(Button) findViewById(R.id.button_registration);
        user = mAuth.getCurrentUser();
    }

    private void singIn(){
        if(!TextUtils.isEmpty(edLogin.getText().toString())&&(!TextUtils.isEmpty(edPassword.getText().toString()))){
            mAuth.signInWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {


                        Toast.makeText(AuthActivity.this, "Пользователь определен, вход выполнен", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AuthActivity.this, "Пользователь не определен", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
        }
    }


}
