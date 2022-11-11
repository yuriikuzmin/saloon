package com.kuzmin.beautysaloon;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ShowActivity extends AppCompatActivity {

    private TextView tv_name_client;
    private TextView tv_second_name_client;
    private TextView tv_tel_client;
    Button btn_add_cart_client;
    private ImageView img_photo;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showclient_layout);

        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        getIntentClient();


    }
    private void init(){
        img_photo=(ImageView) findViewById(R.id.img_show_client);
        tv_name_client=(TextView)findViewById(R.id.text_name_show);
        tv_second_name_client=(TextView) findViewById(R.id.text_sec_name_show);
        tv_tel_client=(TextView) findViewById(R.id.tex_tel_show);
        btn_add_cart_client=(Button) findViewById(R.id.button_add_cart);


    }
    private void getIntentClient(){
        Intent i=getIntent();
        if(i!=null){

            Picasso.get().load(i.getStringExtra("photo_id")).into(img_photo);
            tv_name_client.setText(i.getStringExtra("name"));
            tv_second_name_client.setText(i.getStringExtra("sec_name"));
            tv_tel_client.setText(i.getStringExtra("tel"));
        }
    }

}
