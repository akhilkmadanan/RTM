package com.example.rtm;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class Document extends AppCompatActivity {

    private static final int REQUEST_FILE_CODE = 2;
    private static final int PERMISSION_REQUEST_CODE = 3;
    private OutputStream outputStream;
    private Button connectButton;
    private TextView fileTextView;
    private boolean connected = false;
    private static final UUID HC05_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        connectButton = findViewById(R.id.connect_button);
        connectButton.setAlpha(0.7f);
        Button sendButton = findViewById(R.id.send_button);
        sendButton.setAlpha(0.7f);
        fileTextView = findViewById(R.id.file_text_view);

        connectButton.setOnClickListener(view -> {
            if(connected) {
                Toast.makeText(Document.this, "Already connected!", Toast.LENGTH_SHORT).show();
            } else {
                connectToHC05();
            }
        });

        sendButton.setOnClickListener(view -> {
            if(connected) {
                chooseFile();
            } else {
                Toast.makeText(Document.this, "Not connected to bluetooth!", Toast.LENGTH_SHORT).show();
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

        if (ContextCompat.checkSelfPermission(Document.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Document.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }

        BluetoothDevice hc05Device = bluetoothAdapter.getRemoteDevice("FC:A8:9B:00:1E:16");

        try {
            BluetoothSocket bluetoothSocket = hc05Device.createRfcommSocketToServiceRecord(HC05_UUID);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();

            Toast.makeText(this, "Connected to HC-05", Toast.LENGTH_SHORT).show();
            connected = true;
            connectButton.setEnabled(false);
            fileTextView.setEnabled(true);
        } catch (IOException e) {
            Toast.makeText(this, "Error connecting to HC-05: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        startActivityForResult(intent, REQUEST_FILE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == REQUEST_FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    fileTextView.setText(stringBuilder.toString());
                    CharSequence text = fileTextView.getText();
                    String out = text.toString();

                    new OutputTask().execute(out);

                    reader.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
