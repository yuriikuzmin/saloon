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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadBaseActivity extends AppCompatActivity {

    ListView listView_base;
    ArrayList <String> listClients;
    ArrayAdapter<String> adapter;
    ArrayList <Client> listTemp;
    private String SALOON_KEY="Saloon";
    DatabaseReference myDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_layout);

        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        getClientFromDB();
        setOnClickItem();


    }
    private void init(){
        listView_base=(ListView) findViewById(R.id.list_client_base);
        myDB=FirebaseDatabase.getInstance().getReference("Saloon/Client");
        listClients=new ArrayList<>();
        listTemp=new ArrayList<>();
        adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, listClients);
        listView_base.setAdapter(adapter);
    }
    public void getClientFromDB(){
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listClients.size()>0){
                    listClients.clear();
                }
                for(DataSnapshot ds: snapshot.getChildren()){
                    Client client=ds.getValue(Client.class);
                    assert client!=null;
                    listClients.add(client.sec_name);
                    listTemp.add(client);
                    Log.d("LOG", "Получаем из базы пользователя и вставляем в список");

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myDB.addValueEventListener(valueEventListener);
    }
    private void setOnClickItem(){
        listView_base.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client client=listTemp.get(position);
                Intent in=new Intent(ReadBaseActivity.this, ShowActivity.class);
                in.putExtra("photo_id", client.image_id);
                in.putExtra("name", client.name);
                in.putExtra("sec_name", client.sec_name);
                in.putExtra("tel", client.tel);
                startActivity(in);

            }
        });
    }

}
