package com.kuzmin.beautysaloon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

public class SaveReadActivity extends AppCompatActivity {

    ImageView img_photo_client;
    EditText edNameClient, edNameSecondClient, edTelClient;
    Button btn_add_photo, btn_save_client, btn_read_base;
    private String SALOON_KEY="SALOON";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage myStor;
    private StorageReference storageRef;
    Uri uploadUri;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saveread_layout);

        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
                uploadPhoto();
            }
        });

        btn_save_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClient();
            }
        });

        btn_read_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SaveReadActivity.this, ReadBaseActivity.class);
                startActivity(intent);
            }
        });

    }
    public void init(){
        img_photo_client=(ImageView)findViewById(R.id.image_photo_client);
        edNameClient=(EditText) findViewById(R.id.edit_name_client);
        edNameSecondClient=(EditText) findViewById(R.id.edit_secondName_client);
        edTelClient=(EditText) findViewById(R.id.edit_phone_client);
        btn_add_photo=(Button) findViewById(R.id.button_add_photo_client);
        btn_save_client=(Button) findViewById(R.id.button_save_client);
        btn_read_base=(Button) findViewById(R.id.button_read_base);
        myRef=database.getInstance().getReference("Saloon/Client");
        storageRef=myStor.getInstance().getReference("photo");
    }
    private void getPhoto(){ //получение фото
        Intent intentChooser =new Intent();
        intentChooser.setType("image/*");//определяем тим сущности структуру пути к картинке
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);//определяем назначение этой сущности
        startActivityForResult(intentChooser, 1);//запускаем активность с этой сущностью, присваиваем код запроса
        Log.d("LOG", "Картинку выбрали");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("LOG", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        if(requestCode==1 && data!=null && data.getData()!=null)
        {
            if(resultCode==RESULT_OK ){
                img_photo_client.setImageURI(data.getData());
            }
        }
    }

    private void uploadPhoto(){
        Bitmap bitmap=((BitmapDrawable)img_photo_client.getDrawable()).getBitmap();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte[]byteArray=baos.toByteArray();
        StorageReference myR=storageRef.child(System.currentTimeMillis()+"myClient");
        UploadTask up=myR.putBytes(byteArray);
        Task<Uri> task=up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return myR.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri=task.getResult();
                Log.d("LOG", "Ссылка на storage"+uploadUri.toString());


            }
        });

    }
    private void saveClient() {
        String id = myRef.push().getKey();
        String name = edNameClient.getText().toString();
        String sec_name = edNameSecondClient.getText().toString();
        String tel = edTelClient.getText().toString();
        Client newClient = new Client(id, name, sec_name, tel, uploadUri.toString());
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(sec_name) && !TextUtils.isEmpty(tel)) {

            if (id != null) {
                myRef.child(id).setValue(newClient);
                Toast.makeText(this, "Запись нового пользователя сохранена", Toast.LENGTH_SHORT).show();
                edNameClient.setText("");
                edNameSecondClient.setText("");
                edTelClient.setText("");
                img_photo_client.setImageResource(R.drawable.three_level2);


            }

        } else {
            Toast.makeText(this, "Заполните поля", Toast.LENGTH_SHORT).show();
        }
    }





}
