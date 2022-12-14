package com.kuzmin.beautysaloon;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
    public static FirebaseUser user;
    private TextView tvUserEmail;
    private Button button_in, button_reg, button_enter_main_menu, button_exit;
    public static String textEmail;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authen_layout);

        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        onStart();

        button_in.setOnClickListener(new View.OnClickListener() {//кнопка входа после вводе логина и пароля
            @Override
            public void onClick(View v) {
                singIn();
            }
        });
        button_reg.setOnClickListener(new View.OnClickListener() {//кнопка регистрации после ввода логина и пароля
            @Override
            public void onClick(View v) {
                singUp();
            }
        });
        button_exit.setOnClickListener(new View.OnClickListener() {//кнопка выхода из аккаунта
            @Override
            public void onClick(View v) {
                user=null;
                showSigNot();
            }
        });
        button_enter_main_menu.setOnClickListener(new View.OnClickListener() {//кнопка добавить клиента после входа в аккаунт
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AuthActivity.this,MainMenuActivity.class);
                startActivity(intent);
            }
        });


    }

    private void  init(){
        edLogin = (EditText)findViewById(R.id.edit_login);
        edPassword = (EditText) findViewById(R.id.edit_name_kod);
        tvUserEmail=(TextView)findViewById(R.id.text_auth_enter);
        mAuth = FirebaseAuth.getInstance();
        button_in=(Button) findViewById(R.id.button_enter);
        button_reg=(Button) findViewById(R.id.button_registration);
        button_enter_main_menu=(Button) findViewById(R.id.button_enter_main_menu);
        button_exit=(Button)findViewById(R.id.button_exit);
        user = null;
    }

    private void singIn(){
        if(!TextUtils.isEmpty(edLogin.getText().toString())&&(!TextUtils.isEmpty(edPassword.getText().toString()))){
            mAuth.signInWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        singInVer();

                        Toast.makeText(AuthActivity.this, R.string.note_text_2, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AuthActivity.this, R.string.note_text_3, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(this, R.string.note_text_1, Toast.LENGTH_SHORT).show();
        }
    }
    private void showSigIn(){ //дисплей после успешного входа в аккаунт
        tvUserEmail.setVisibility(View.VISIBLE);
        button_enter_main_menu.setVisibility(View.VISIBLE);
        button_exit.setVisibility(View.VISIBLE);

        edLogin.setVisibility(View.GONE);
        edPassword.setVisibility(View.GONE);
        button_reg.setVisibility(View.GONE);
        button_in.setVisibility(View.GONE);
    }
    private void showSigNot(){ //дисплей если вход в аккаунт не выполнен
        tvUserEmail.setVisibility(View.VISIBLE);
        button_enter_main_menu.setVisibility(View.GONE);
        button_exit.setVisibility(View.GONE);

        edLogin.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.VISIBLE);
        button_reg.setVisibility(View.VISIBLE);
        button_in.setVisibility(View.VISIBLE);
        tvUserEmail.setText(R.string.text_auth);
        edLogin.setText(null);
        edPassword.setText(null);
    }
    private void sendEmailVer(){//Проверка существования E-mail пользователя
        user=mAuth.getCurrentUser();
        assert user!=null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AuthActivity.this, R.string.note_text_4, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AuthActivity.this, R.string.note_text_5, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void singInVer(){
        user=mAuth.getCurrentUser();
        assert user!=null;
        if(user.isEmailVerified()){
            showSigIn();
            tvUserEmail.setText(user.getEmail());
            textEmail=tvUserEmail.getText().toString();
            Log.d("LOG", "рез E-mail"+textEmail);
        }else{
            Toast.makeText(AuthActivity.this, R.string.note_text_6, Toast.LENGTH_SHORT).show();
        }
    }

    public void singUp(){
        if(!TextUtils.isEmpty(edLogin.getText().toString())&&(!TextUtils.isEmpty(edPassword.getText().toString()))){
            mAuth.createUserWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("LOG", "Проверка результата регистрации");
                        sendEmailVer();
                        Log.d("LOG", "Проверка результата E-mail");

                        singInVer();

                        Toast.makeText(AuthActivity.this, R.string.note_text_7, Toast.LENGTH_SHORT).show();
                    } else {
                        showSigNot();
                        Toast.makeText(AuthActivity.this, R.string.note_text_8, Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }else {
            Toast.makeText(this, R.string.note_text_9, Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onStart() {
        super.onStart();

        if (user!= null) {
            showSigIn();
        } else {
            showSigNot();
        }

    }
    public String getTextEmail() {
        Log.d("LOG", "рез E-mail get"+textEmail);
        return textEmail;
    }
   public void onBackPressed(){
        Intent intent=new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
    }


}
