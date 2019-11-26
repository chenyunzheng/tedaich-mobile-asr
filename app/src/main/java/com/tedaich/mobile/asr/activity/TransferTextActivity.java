package com.tedaich.mobile.asr.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tedaich.mobile.asr.App;
import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.dao.DaoSession;
import com.tedaich.mobile.asr.model.AudioText;
import com.tedaich.mobile.asr.util.CloudUtil;
import com.tedaich.mobile.asr.util.Constants;

import java.util.Date;

public class TransferTextActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private DaoSession daoSession;

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        daoSession = ((App) getApplication()).getDaoSession();
        setContentView(R.layout.transfer_text_activity_layout);
        TextView audioNameView = findViewById(R.id.transfer_audio_name);
        View userInputLayout = findViewById(R.id.User_Input_LinearLayout);
        EditText cloudTransText = findViewById(R.id.cloud_transfer_text);
        EditText localUserInput = findViewById(R.id.local_user_input);
        Button userModifyBtn = findViewById(R.id.btn_user_modify);
        Button cancelBtn = findViewById(R.id.btn_cancel);
        Button saveBtn = findViewById(R.id.btn_save);

        Intent transferTextIntent = getIntent();
        long audioId = transferTextIntent.getLongExtra("audio_id", -1);
        String audioName = transferTextIntent.getStringExtra("audio_name");
        String transferAudioText = transferTextIntent.getStringExtra("audio_to_text");
        audioNameView.setText(audioName == null ? getResources().getString(R.string.default_audio_name) : audioName);
        userInputLayout.setVisibility(View.GONE);
        cloudTransText.setEnabled(false);
        cloudTransText.setText(transferAudioText == null ? "" : transferAudioText);
        userModifyBtn.setOnClickListener(v -> {
            v.setVisibility(View.GONE);
            userInputLayout.setVisibility(View.VISIBLE);
            Editable editable = cloudTransText.getText();
            if (editable != null && !"".equals(editable.toString().trim())){
                localUserInput.setText(editable.toString().trim());
            }
        });
        cancelBtn.setOnClickListener(v -> {
            userInputLayout.setVisibility(View.GONE);
            userModifyBtn.setVisibility(View.VISIBLE);
        });
        saveBtn.setOnClickListener(v -> {
            Editable userInput = localUserInput.getText();
            if (userInput != null && !"".equals(userInput.toString().trim())){
                String text = userInput.toString().trim();
                //update local db for transfer text
                if (audioId != -1){
                    AudioText audioText = new AudioText(audioId, 1, text, new Date());
                    daoSession.getAudioTextDao().insert(audioText);
                    Toast.makeText(this, "Cloud service is in developing", Toast.LENGTH_SHORT).show();
                    //update cloud db for transfer text
                    long gUserId = sharedPreferences.getLong("G_USER_ID", -1);
                    if (gUserId != -1){
                        CloudUtil.addUserInputTransferText(gUserId,audioId,text);
                    }
                }
                Intent intent = new Intent();
                setResult(Constants.RESULT_OK, intent);
                finish();
            }
        });

    }
}
