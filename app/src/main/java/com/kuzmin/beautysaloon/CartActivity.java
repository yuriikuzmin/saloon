package com.kuzmin.beautysaloon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    TextView txtTel, txtName, txtSecName, status;
    EditText editTextProced;
    ListView listProced;
    Button btn_start_record, btn_stop_record, btn_save_proced;
    DatabaseReference mDB;
    public static String textEmail ;
    Cart txtProsed;
    AuthActivity clientTel;
    private String fileClient;
    ArrayAdapter<String> adapterCart;//адаптер для отображения данных из базы в виде списка на экране
    List<String> listDataCart;//переменная для сохранения списка данных карт из базы для разворачивания в textCart
    List<Cart> listTempCart; //для сохранения всех данных класса Cart

    //audio переменные
    MediaRecorder mediaRecorder;
    File outFile;
    String name;
    SimpleDateFormat sdf;
    StorageReference myRef;
    StorageReference storage;
    Task uploadTask;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carta_layout);

        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        Intent intent=getIntent();
        txtTel.setText(intent.getStringExtra("tel"));
        txtName.setText(intent.getStringExtra("name"));
        txtSecName.setText(intent.getStringExtra("sec_name"));
        mDB= FirebaseDatabase.getInstance().getReference("Saloon/Cart"+fileClient);
        fileClient=intent.getStringExtra("tel");

        getDataCart(); //получение списка процедур из базы по карте клиента
        setOnClickItem();//выбор процедуры для перехода на просмотр в другом окне
        TextEmail();//получение почты/телефона мастера который вошел в приложение

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);



        btn_start_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAudio();
            }
        });

        btn_stop_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaRecorder != null) {
                    mediaRecorder.stop();
                    Log.d("LOG", "Запись остановлена");
                    recordFireBase();
                    status.setText("Запись остановлена");
                }
            }
        });

        btn_save_proced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProsed();
            }
        });
    }
    private void init(){
        txtTel=(TextView) findViewById(R.id.text_cart_tel);
        txtName=(TextView) findViewById(R.id.text_cart_name);
        txtSecName=(TextView) findViewById(R.id.text_cart_sec_name);
        status=(TextView)findViewById(R.id.text_recorders);
        editTextProced=(EditText) findViewById(R.id.editText_procedure);
        listProced=(ListView) findViewById(R.id.listView_proced);
        btn_save_proced=(Button) findViewById(R.id.button_save_proced);
        btn_start_record=(Button) findViewById(R.id.button_start_record);
        btn_stop_record=(Button) findViewById(R.id.button_stop_record);
        listDataCart=new ArrayList<>();
        listTempCart=new ArrayList<>();
        adapterCart=new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, listDataCart);
        listProced.setAdapter(adapterCart);
        clientTel=new AuthActivity();
        outFile=null;
        try {
            outFile=File.createTempFile("audio", name);
            Log.d("LOG", "outFile:"+ outFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProsed(){ //запись процедуры в базу с остальными данными

        String id=mDB.push().getKey();
        String textProsed=editTextProced.getText().toString();
        String sound_id=storage.toString();
        Log.d("LOG", "sound_id ="+sound_id);
        txtProsed=new Cart(id, textProsed, textEmail, sound_id);

        if(!TextUtils.isEmpty(textProsed)){
            if(id!=null){
                Log.d("LOG", "id процедуру ="+id);
                mDB.child(id).setValue(txtProsed);
                Toast.makeText(this, "Запись новой процедуры сохранена", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Процедура не сохранена", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void getDataCart(){ // получение списка процедур из базы по карте клиента
        ValueEventListener eventListenerCart=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listDataCart.size()>0){
                    listDataCart.clear();
                }
                for(DataSnapshot dsCart: snapshot.getChildren()){
                    Cart cart=dsCart.getValue(Cart.class);
                    assert cart!=null;
                    listDataCart.add(cart.textProsed);
                    Log.d("LOG", "procedure :"+listDataCart.toString());
                    listTempCart.add(cart);
                }
                adapterCart.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDB.addValueEventListener(eventListenerCart);
    }

    public void TextEmail(){//получение почты/телефона мастера который вошел в приложение
        textEmail=clientTel.getTextEmail();
        Log.d("LOG", "textEmail ="+textEmail);
    }

    public void recordAudio(){//создание медиа записующего устройства

        try {
            mediaRecorder=new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outFile.toString());
            mediaRecorder.prepare();
            mediaRecorder.start();
            status.setText("Идет запись");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordFireBase(){//запись аудио записи с временного файла базу
        sdf = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
        name = sdf.format(new Date(System.currentTimeMillis()));

        storage= FirebaseStorage.getInstance().getReference("sound/"+name);

        Uri file = Uri.fromFile(new File(outFile.toString()));

        uploadTask=storage.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("LOG", "Файл не загружен на Firebase");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Log.d("LOG", "загружен на Firebase");
            }
        });
    }
    public void setOnClickItem(){
        listProced.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override  //выбор процедуры для перехода на просмотр в другом окне
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cart cart=listTempCart.get(position);
                Intent intent=new Intent(CartActivity.this, ViewCartActivity.class);
                intent.putExtra("id", cart.id);
                intent.putExtra("cartview", cart.textProsed);
                intent.putExtra("userEmail", cart.textEmail);
                intent.putExtra("sound_client", cart.sound_id);
                startActivity(intent);
            }
        });
    }

}
