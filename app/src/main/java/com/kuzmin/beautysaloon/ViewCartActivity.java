package com.kuzmin.beautysaloon;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ViewCartActivity extends AppCompatActivity {

    TextView viewCart_pattern, master_Email, viewCart_procedure ;
    public Button btnMenu, btnPlay, btnExit, btnStopPlay;

    //audio переменные
    StorageReference ref;
    FirebaseStorage storage;
    MediaPlayer mediaPlayer;
    Uri uri, uriRef;
    private String sound_id;
    Cart cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_cart_layout);

        Window w= getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        getViewCart();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio();
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewCartActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(ViewCartActivity.this, AuthActivity.class);
                startActivity(in);
            }
        });
        btnStopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    } else {
                        Toast.makeText(ViewCartActivity.this, R.string.note_text_17, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.d("LOG", "Ошибка остановки плейра");

                }
            }
        });

    }
    private void init(){
        viewCart_pattern=(TextView) findViewById(R.id.text_viewcart_pattern);
        master_Email=(TextView) findViewById(R.id.text_viewcart_email);
        viewCart_procedure=(TextView) findViewById(R.id.text_viewcart_procedure);
        btnMenu=(Button) findViewById(R.id.button_menu);
        btnPlay=(Button) findViewById(R.id.button_play_audio);
        btnExit=(Button) findViewById(R.id.button_exit);
        btnStopPlay=(Button) findViewById(R.id.button_stop_play);

        storage = FirebaseStorage.getInstance();


    }

    public void getViewCart(){
        Intent in=getIntent();
        if(in!=null){

            viewCart_procedure.setText(in.getStringExtra("cartview"));
            master_Email.setText(in.getStringExtra("userEmail"));
            sound_id=in.getStringExtra("sound_client");
            Log.d("LOG", "sound  ссылка :"+sound_id);
            uriRef= Uri.parse(sound_id);
        }
    }

    public void playAudio(){
        // воспроизведение
        ref=storage.getReference(uriRef.getPath());
        Log.d("LOG", "myRef  ссылка :"+ref);



        File localFile = null;
        try {
            localFile = File.createTempFile("rock", ".mp3");//создаем файл для загрузки из базы
            Log.d("LOG", "localFile :"+localFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //загружаем файл мр3 из базы в созданный файл.
        ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Log.d("LOG", "Загружено localFile успешно");
                mediaPlayer= MediaPlayer.create(ViewCartActivity.this, uri);
                mediaPlayer.start();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        uri= Uri.parse(localFile.getPath());
        Log.d("LOG", "uri:"+uri);

    }

}
