package com.kuzmin.beautysaloon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FindShowActivity extends AppCompatActivity {

    FindActivity tempSecondName;
    ListView lw_find_client;
    ArrayList<String> listData;
    ArrayList<Client> listAll;
    ArrayAdapter<String> adapter;
    DatabaseReference myDB;
    private String SALOON_KEY="Saloon"; //ключ к директории базы данных
    public String second_name;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_show_client);

        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        getDataFromDB();
        onClickItem();
    }

    private void init(){

        lw_find_client=(ListView) findViewById(R.id.listView_find_client);
        tempSecondName=new FindActivity();
        lw_find_client=(ListView) findViewById(R.id.listView_find_client);
        myDB= FirebaseDatabase.getInstance().getReference("Saloon/Client");
        listData=new ArrayList<>();
        listAll=new ArrayList<>();
        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        lw_find_client.setAdapter(adapter);
        second_name=tempSecondName.getSecond_name_find();
        Log.d("LOG", "Переданная фамилия "+second_name);
    }

    private void getDataFromDB(){
        ValueEventListener vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listData.size()>0){
                    listData.clear();
                }
                for(DataSnapshot ds: snapshot.getChildren()){
                    Client client=ds.getValue(Client.class);
                    assert client!=null;
                    Log.d("LOG", "Пoлученная фамилия из базы "+client.sec_name);

                    if(second_name.equals(client.sec_name)){

                        listData.add(client.sec_name);
                        listAll.add(client);
                        Log.d("LOG", "отобранная фамилия из базы "+client.sec_name);

                    }


                }
                if(listData.size()==0)
                {
                    Toast.makeText(FindShowActivity.this, "Совпадений нет", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myDB.addValueEventListener(vel);
    }

    private void onClickItem(){
        lw_find_client.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client client=listAll.get(position);
                Intent intent=new Intent(FindShowActivity.this, ShowActivity.class);
                intent.putExtra("id", client.id);
                intent.putExtra("name", client.name);
                intent.putExtra("sec_name", client.sec_name);
                intent.putExtra("tel", client.tel);
                intent.putExtra("photo_id", client.image_id);
                startActivity(intent);

            }
        });
    }
}
