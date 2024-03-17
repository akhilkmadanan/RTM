package com.example.rtm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognizeRequest;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1p1beta1.SpeechSettings;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class Audio extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 3;
    private static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private static final int REQUEST_CODE = 2;
    private OutputStream outputStream;
    private String out;
    private boolean connected = false;
    private static final UUID HC05 = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private TextView mTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        mTextView = findViewById(R.id.text_view);
        Button coButton = findViewById(R.id.connect_button);
        coButton.setAlpha(0.7f);
        Button seButton = findViewById(R.id.select_button);
        seButton.setAlpha(0.7f);
        Button sdButton = findViewById(R.id.send_button);
        sdButton.setAlpha(0.7f);
        Button clButton = findViewById(R.id.clear_button);
        clButton.setAlpha(0.7f);


        Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        seButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(Audio.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Audio.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_PERMISSION_CODE);
            } else if(connected){
                openFilePicker();
            } else {
                Toast.makeText(Audio.this, "Not connected to bluetooth!", Toast.LENGTH_SHORT).show();
            }
        });

        clButton.setOnClickListener(v -> mTextView.setText(""));

        coButton.setOnClickListener(view -> {
            if(connected) {
                Toast.makeText(Audio.this, "Already connected!", Toast.LENGTH_SHORT).show();
            } else {
                connectToHC05();
            }
        });

        sdButton.setOnClickListener(view -> {
            if(mTextView.getText()!="") {
                CharSequence text = mTextView.getText();
                out = text.toString();
                new OutputTask().execute(out);
            } else {
                Toast.makeText(Audio.this, "No text to send!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connectToHC05() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(Audio.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Audio.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }

        BluetoothDevice hc05Device = bluetoothAdapter.getRemoteDevice("FC:A8:9B:00:1E:16");

        try {
            BluetoothSocket bluetoothSocket = hc05Device.createRfcommSocketToServiceRecord(HC05);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();

            Toast.makeText(this, "Connected to HC-05", Toast.LENGTH_SHORT).show();
            connected = true;
            mTextView.setEnabled(true);
        } catch (IOException e) {
            Toast.makeText(this, "Error connecting to HC-05: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No file manager found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                ByteString audioBytes = ByteString.readFrom(inputStream);

                recognizeSpeech(audioBytes);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void recognizeSpeech(ByteString audioBytes) {
        new Thread(() -> {
            try {
                InputStream inputStream = getResources().openRawResource(R.raw.credentials);
                GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
                SpeechClient speechClient = SpeechClient.create(SpeechSettings.newBuilder().setCredentialsProvider(
                        FixedCredentialsProvider.create(credentials)).build());

                RecognitionConfig config = RecognitionConfig.newBuilder()
                        .setEncoding(RecognitionConfig.AudioEncoding.MP3)
                        .setLanguageCode(Locale.getDefault().getLanguage())
                        .setSampleRateHertz(16000)
                        .build();

                RecognitionAudio audio = RecognitionAudio.newBuilder()
                        .setContent(audioBytes)
                        .build();

                RecognizeRequest request = RecognizeRequest.newBuilder()
                        .setConfig(config)
                        .setAudio(audio)
                        .build();

                RecognizeResponse response = speechClient.recognize(request);

                ArrayList<String> transcriptionList = new ArrayList<>();
                for (SpeechRecognitionResult result : response.getResultsList()) {
                    SpeechRecognitionAlternative alternative = result.getAlternatives(0);
                    transcriptionList.add(alternative.getTranscript());
                }

                runOnUiThread(() -> {
                    String transcription = String.join("\n", transcriptionList);
                    mTextView.setText(transcription);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }


        }).start();
    }

    private class OutputTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String out = params[0];
            char[] ch = new char[out.length()];

            for (int i = 0; i < out.length(); i++) {
                ch[i] = out.charAt(i);
                String output = "" + ch[i];
                if (out.length() != 0) {
                    try {
                        outputStream.write(output.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

}
