package com.tedaich.mobile.asr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tedaich.mobile.asr.R;

public class UserLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_layout);
        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        String passCode = intent.getStringExtra("passCode");
        EditText phoneValue = findViewById(R.id.cell_phone_value);
        phoneValue.setText(phone);
        EditText passCodeValue = findViewById(R.id.password_value);
        passCodeValue.setText(passCode);
        Toast.makeText(this, "in developing", Toast.LENGTH_SHORT).show();
    }
}
