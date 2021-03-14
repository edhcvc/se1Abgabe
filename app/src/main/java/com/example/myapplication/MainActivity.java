package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText number;
    private Button btn;
    private TextView txt;
    private Button btn2;


    public void berechnen(String matriNr){
        int[] arr = new int[matriNr.length()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = matriNr.charAt(i) - '0';
        }

        List<Integer> primeList = new ArrayList<>();
        List<Integer> noprimeList = new ArrayList<>();
        for (int i = 2; i < arr.length; i++) {
            if (arr[i] % i == 0 || arr[i] % 2 == 0 && arr[i] > 2) {
                noprimeList.add(arr[i]);
            } else {
                primeList.add(arr[i]);
            }
        }
        String result = primeList.toString();
        txt.setText(result);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number = findViewById(R.id.editTextNumber);
        btn = findViewById(R.id.button);
        txt = findViewById(R.id.textView2);
        btn2 = findViewById(R.id.button2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matriklNr = number.getText().toString();
                berechnen(matriklNr);

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matriklNr = number.getText().toString();
                TCPServer server = new TCPServer(matriklNr);
                server.start();
                try {
                    server.join();
                }
                catch (InterruptedException ie)
                {}
                txt.setText(server.getAnswer());
            }
        });

    }


    class TCPServer extends Thread {
        String matrNr;
        String string1;

        public TCPServer(String matrNr) {
            this.matrNr = matrNr;

        }

        public void run() {
            try {
                BufferedReader inFormUser = new BufferedReader(new InputStreamReader(System.in));
                Socket clientSocket = new Socket("se2-isys.aau.at", 53212);

                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());

                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                outToServer.writeBytes(matrNr + '\n');
                string1 = inFromServer.readLine();


                clientSocket.close();
            } catch (Exception e) {
                System.out.println("Crash");
            }
        }

        public String getAnswer() {
            return string1;
        }


    }
}