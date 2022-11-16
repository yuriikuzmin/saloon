package com.kuzmin.beautysaloon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DeleteUserActivity extends AppCompatActivity {

    EditText emailUsers;
    Button btn_user_delete;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_exit_layaut);

        emailUsers=(EditText) findViewById(R.id.text_user_email);
        btn_user_delete=(Button) findViewById(R.id.button_delete);

        btn_user_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



}
