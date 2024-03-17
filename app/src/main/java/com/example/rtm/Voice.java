package com.example.rtm;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class Voice extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 3;
    private OutputStream outputStream;
    private Button connectButton;
    private String out;
    private boolean connected = false;
    private static final UUID HC05 = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private TextView resultText;
    private final int REQUEST_CODE_SPEECH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        connectButton = findViewById(R.id.connect_button);
        connectButton.setAlpha(0.7f);
        Button sendButton = findViewById(R.id.send_button);
        sendButton.setAlpha(0.7f);
        Button speakButton = findViewById(R.id.speak_button);
        speakButton.setAlpha(0.7f);
        resultText = findViewById(R.id.result_text);

        connectButton.setOnClickListener(view -> {
            if(connected) {
                Toast.makeText(Voice.this, "Already connected!", Toast.LENGTH_SHORT).show();
            } else {
                connectToHC05();
            }
        });

        sendButton.setOnClickListener(view -> {
            if(resultText.getText()!="") {
                CharSequence text = resultText.getText();
                out = text.toString();
                new OutputTask().execute(out);
            } else {
                Toast.makeText(Voice.this, "No text to send!", Toast.LENGTH_SHORT).show();
            }
        });

        speakButton.setOnClickListener(v -> {
            if(connected) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your message");
                startActivityForResult(intent, REQUEST_CODE_SPEECH);
            } else {
                Toast.makeText(Voice.this, "Not connected to bluetooth!", Toast.LENGTH_SHORT).show();
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

        if (ContextCompat.checkSelfPermission(Voice.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Voice.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }

        BluetoothDevice hc05Device = bluetoothAdapter.getRemoteDevice("FC:A8:9B:00:1E:16");

        try {
            BluetoothSocket bluetoothSocket = hc05Device.createRfcommSocketToServiceRecord(HC05);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();

            Toast.makeText(this, "Connected to HC-05", Toast.LENGTH_SHORT).show();
            connected = true;
            connectButton.setEnabled(false);
            resultText.setEnabled(true);
        } catch (IOException e) {
            Toast.makeText(this, "Error connecting to HC-05: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String transcribedText = results.get(0);
            resultText.setText(transcribedText);
        } else {
            Toast.makeText(this, "Speech recognition failed", Toast.LENGTH_SHORT).show();
        }
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
