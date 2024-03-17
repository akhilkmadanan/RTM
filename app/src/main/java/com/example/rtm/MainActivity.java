package com.example.rtm;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int playbackPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView videoview = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bg);
        videoview.setVideoURI(uri);
        videoview.start();

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoview.start(); // restart the video
            }
        });


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        TextView textView = (TextView) findViewById(R.id.dev);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Akhil, Vijoy, Gokul, Arthur ", Toast.LENGTH_SHORT).show();
            }
        });

        Button button= (Button) findViewById(R.id.button1);
        button.setAlpha(0.7f);
        button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Document.class)));

        Button button2= (Button) findViewById(R.id.button2);
        button2.setAlpha(0.7f);
        button2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Voice.class)));

        Button button3= (Button) findViewById(R.id.button3);
        button3.setAlpha(0.7f);
        button3.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Audio.class)));

    }
    @Override
    protected void onPause() {
        super.onPause();
        VideoView videoview = (VideoView) findViewById(R.id.videoView);
        playbackPosition = videoview.getCurrentPosition();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bg);
        VideoView videoview = (VideoView) findViewById(R.id.videoView);
        videoview.setVideoURI(uri);
        videoview.seekTo(playbackPosition);
        videoview.start();
    }


}