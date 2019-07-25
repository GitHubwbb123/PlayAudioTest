package com.wxb.playaudiotest;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private  MediaPlayer mediaPlayer=new MediaPlayer();
private  Button play;
private  Button pause;
private  Button stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play=(Button) findViewById(R.id.play);
        pause=(Button)findViewById(R.id.pause);
        stop=(Button)findViewById(R.id.stop);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        if(AndPermission.hasPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            initMp3Player();
        }
        else{

            AndPermission.with(this)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .requestCode(100)
                    .send();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AndPermission.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    @PermissionYes(100)
    private void getPermission(List<String> grantedPermission){
        initMp3Player();
    }
    @PermissionNo(100)
    private void refusePermission(List<String> grantedPermission){

        Toast.makeText(MainActivity.this,"拒接了权限",Toast.LENGTH_SHORT).show();

    }


    private  void initMp3Player(){
        File file =new File(Environment.getExternalStorageDirectory(),"music.mp3");
        try {
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            case R.id.stop:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    initMp3Player();
                }
                break;
                default:
                    break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();

        }
    }
}
