package com.tedaich.mobile.asr.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.util.Constants;

public class UserLoginActivity extends AppCompatActivity {

    private boolean isRegister = false;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_layout);
        EditText phoneValue = findViewById(R.id.cell_phone_value);
        EditText passCodeValue = findViewById(R.id.password_value);
        EditText verifyCodeValue = findViewById(R.id.verify_code_value);
        TextView verifyCodeLabel = findViewById(R.id.verify_code_label);
        Button verifyCodeAcquireBtn = findViewById(R.id.verify_code_acquire_btn);
        TextView register = findViewById(R.id.register);
        Button submitBtn = findViewById(R.id.btn_submit);

        verifyCodeLabel.setVisibility(View.GONE);
        verifyCodeValue.setVisibility(View.GONE);
        verifyCodeAcquireBtn.setVisibility(View.GONE);

        register.setOnClickListener(view -> {
            TextView et = (TextView)view;
            if (isRegister){
                verifyCodeLabel.setVisibility(View.VISIBLE);
                verifyCodeValue.setVisibility(View.VISIBLE);
                verifyCodeAcquireBtn.setVisibility(View.VISIBLE);
                et.setText("登录");
                submitBtn.setText("注 册");
            } else {
                verifyCodeLabel.setVisibility(View.GONE);
                verifyCodeValue.setVisibility(View.GONE);
                verifyCodeAcquireBtn.setVisibility(View.GONE);
                et.setText("注册");
                submitBtn.setText("登 录");
            }
            isRegister = !isRegister;
        });
        verifyCodeAcquireBtn.setOnClickListener(view -> {
            Toast.makeText(this, "SMS service is in developing", Toast.LENGTH_SHORT).show();
        });
        submitBtn.setOnClickListener(view -> {
            Toast.makeText(this, "Cloud service is in developing", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("G_USER_ID", -1L);
            setResult(Constants.RESULT_OK, intent);
            finish();
        });
    }
}
