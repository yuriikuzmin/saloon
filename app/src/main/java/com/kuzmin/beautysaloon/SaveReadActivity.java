package com.kuzmin.beautysaloon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class SaveReadActivity extends AppCompatActivity {

    ImageView img_photo_client;
    EditText edNameClient, edNameSecondClient, edTelClient;
    Button btn_add_photo, btn_save_client, btn_main_menu;
    private String SALOON_KEY="SALOON";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage myStor;
    private StorageReference storageRef;
    Uri uploadUri;
    static final int REQUEST_CODE_PHOTO = 1;
    File directory;
    final int TYPE_FOTO=1;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saveread_layout);

        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getPhoto();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, generateFile(TYPE_FOTO));
                startActivityForResult(intent,REQUEST_CODE_PHOTO);

            }
        });

        btn_save_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });

        btn_main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SaveReadActivity.this, MainMenuActivity.class);
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
        btn_main_menu=(Button) findViewById(R.id.button_main_menu);
        myRef=database.getInstance().getReference("Saloon/Client");
        storageRef=myStor.getInstance().getReference("photo");
    }
   /* private void getPhoto(){ //получение фото
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
                Log.d("LOG", "Картинка Uri:"+ data.getData());
                img_photo_client.setImageURI(data.getData());
            }
        }
    }*/
   @Override
   protected void onActivityResult(int requestCode, int resultCode,
                                   Intent intent) {
       super.onActivityResult(requestCode, resultCode, intent);
       if(requestCode==REQUEST_CODE_PHOTO){
           if(resultCode==RESULT_OK){
               if(intent==null){
                   Log.d("LOG", "Intent is null");
               }else {
                   Log.d("LOG", "Photo uri: "+intent.getData());
                   Bundle bndl=intent.getExtras();
                   if(bndl!=null){
                       Object object=intent.getExtras().get("data");
                       if(object instanceof Bitmap){
                           Bitmap bitmap=(Bitmap) object;
                           img_photo_client.setImageBitmap(bitmap);

                       }
                   }
               }
           }else if(resultCode==RESULT_CANCELED){
               Log.d("LOG", "Canceled");
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
                saveClient();



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
                Toast.makeText(this, R.string.note_text_15, Toast.LENGTH_SHORT).show();
                edNameClient.setText("");
                edNameSecondClient.setText("");
                edTelClient.setText("");
                img_photo_client.setImageResource(R.drawable.three_level2);


            }

        } else {
            Toast.makeText(this, R.string.note_text_16, Toast.LENGTH_SHORT).show();
        }
    }





}
