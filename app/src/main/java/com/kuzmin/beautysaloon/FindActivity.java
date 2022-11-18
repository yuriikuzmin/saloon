package com.kuzmin.beautysaloon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FindActivity extends AppCompatActivity {

    EditText editText_second_name;
    Button btn_find_start;
    public static String second_name_find;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_client_layuot);

        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();

        btn_find_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FindActivity.this, FindShowActivity.class);
                if(editText_second_name.getText()!=null){
                    setSecond_name_find(second_name_find);
                    startActivity(intent);

                }else {
                    Toast.makeText(FindActivity.this, R.string.note_text_13, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    private void init(){
        editText_second_name=(EditText) findViewById(R.id.editText_second_name);
        btn_find_start=(Button) findViewById(R.id.button_find_start);
    }
    public void setSecond_name_find(String second_name_find) {
        this.second_name_find = editText_second_name.getText().toString();
    }


    public String getSecond_name_find() {
        Log.d("LOG", "Выводим результат FindActivity get:"+second_name_find);
        return second_name_find;
    }
}
