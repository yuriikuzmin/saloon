package com.kuzmin.beautysaloon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenuActivity extends AppCompatActivity {

    Button btn_add_client, btn_find_client, btn_see_base, btn_exit, btn_delete_user;

    AuthActivity textEmail;
    private  static  String emailUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu_layout);

        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();




        btn_add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainMenuActivity.this, SaveReadActivity.class);
                startActivity(intent);

            }
        });

        btn_find_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, FindActivity.class);
                startActivity(intent);

            }
        });

        btn_see_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainMenuActivity.this, ReadBaseActivity.class);
                startActivity(intent);

            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainMenuActivity.this, AuthActivity.class);
                startActivity(intent);
            }
        });

        btn_delete_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainMenuActivity.this, DeleteUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init(){
        btn_add_client=(Button) findViewById(R.id.button_add);
        btn_find_client=(Button) findViewById(R.id.button_find);
        btn_see_base=(Button) findViewById(R.id.button_base_see);
        btn_exit=(Button) findViewById(R.id.button_exit);
        btn_delete_user=(Button) findViewById(R.id.button_delete_user);
        textEmail=new AuthActivity();
        textEmail();

    }

    public void textEmail(){//получение почты/телефона мастера который вошел в приложение
        emailUser=textEmail.getTextEmail();
        Log.d("LOG", "textEmail ="+emailUser);
        assert emailUser!=null;
        if(emailUser.equals("poshta.kuzmin@gmail.com")){
            btn_delete_user.setVisibility(View.GONE);
        }else {
            btn_delete_user.setVisibility(View.GONE);}
    }
}
