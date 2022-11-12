package com.kuzmin.beautysaloon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    Button btn_add_client, btn_find_client, btn_see_base, btn_exit;

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
    }

    private void init(){
        btn_add_client=(Button) findViewById(R.id.button_add);
        btn_find_client=(Button) findViewById(R.id.button_find);
        btn_see_base=(Button) findViewById(R.id.button_base_see);
        btn_exit=(Button) findViewById(R.id.button_exit);
    }
}
