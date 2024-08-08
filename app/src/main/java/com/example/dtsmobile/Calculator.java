package com.example.dtsmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Calculator extends AppCompatActivity {

    private EditText angka1, angka2;
    Button tambah, kurang, kali, bagi, bersihkan;
    private TextView hasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        angka1 = findViewById(R.id.angka1);
        angka2 = findViewById(R.id.angka2);
        tambah = findViewById(R.id.tambah);
        kurang = findViewById(R.id.kurang);
        kali = findViewById(R.id.kali);
        bagi = findViewById(R.id.bagi);
        bersihkan = findViewById(R.id.bersihkan);
        hasil = findViewById(R.id.hasil);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitung("+");
            }
        });

        kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitung("-");
            }
        });

        kali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitung("*");
            }
        });

        bagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitung("/");
            }
        });

        bersihkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                angka1.setText("");
                angka2.setText("");
                angka1.requestFocus();
                hasil.setText("0");
            }
        });
    }

    private void hitung(String operasi) {
        String input1 = angka1.getText().toString();
        String input2 = angka2.getText().toString();

        if (input1.isEmpty() || input2.isEmpty()) {
            Toast.makeText(this, "Mohon masukkan kedua angka", Toast.LENGTH_SHORT).show();
            return;
        }

        double angka1 = Double.parseDouble(input1);
        double angka2 = Double.parseDouble(input2);
        double result = 0;

        switch (operasi) {
            case "+":
                result = angka1 + angka2;
                break;
            case "-":
                result = angka1 - angka2;
                break;
            case "*":
                result = angka1 * angka2;
                break;
            case "/":
                if (angka2 == 0) {
                    Toast.makeText(this, "Tidak bisa dibagi dengan nol", Toast.LENGTH_SHORT).show();
                    return;
                }
                result = angka1 / angka2;
                break;
        }

        hasil.setText(String.valueOf(result));
    }
}