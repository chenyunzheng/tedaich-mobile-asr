package com.tedaich.mobile.asr.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tedaich.mobile.asr.R;

public class UserLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_layout);
        EditText phoneValue = findViewById(R.id.cell_phone_value);
        EditText passCodeValue = findViewById(R.id.password_value);
        EditText verifyCodeValue = findViewById(R.id.verify_code_value);
        TextView verifyCodeLabel = findViewById(R.id.verify_code_label);
        TextView register = findViewById(R.id.register);

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        String passCode = intent.getStringExtra("passCode");
        phoneValue.setText(phone);
        passCodeValue.setText(passCode);
        verifyCodeLabel.setVisibility(View.GONE);
        verifyCodeValue.setVisibility(View.GONE);

        register.setOnClickListener(view -> {
            TextView et = (TextView)view;
            if ("注册".equals(et.getText().toString())){
                verifyCodeLabel.setVisibility(View.VISIBLE);
                verifyCodeValue.setVisibility(View.VISIBLE);
                et.setText("登录");
            } else {
                verifyCodeLabel.setVisibility(View.GONE);
                verifyCodeValue.setVisibility(View.GONE);
                et.setText("注册");
            }
        });
        Toast.makeText(this, "in developing", Toast.LENGTH_SHORT).show();
    }
}
