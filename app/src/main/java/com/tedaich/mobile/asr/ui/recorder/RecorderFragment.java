package com.tedaich.mobile.asr.ui.recorder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tedaich.mobile.asr.App;
import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.dao.DaoSession;
import com.tedaich.mobile.asr.model.Audio;
import com.tedaich.mobile.asr.service.RecordAudioTask;
import com.tedaich.mobile.asr.service.TimerAudioService;
import com.tedaich.mobile.asr.ui.recorder.adapter.RecorderAudioItemAdapter;
import com.tedaich.mobile.asr.util.AndroidUtils;
import com.tedaich.mobile.asr.util.AudioUtils;
import com.tedaich.mobile.asr.util.Constants;
import com.tedaich.mobile.asr.widget.AudioWaveView;

import java.io.File;
import java.util.Date;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class RecorderFragment extends Fragment {

    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;//音频输入源
    private static final int SAMPLE_RATE_IN_HZ = 16000;//音频采样频率
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;//通道数
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;//数据位宽

    private FragmentActivity fragmentActivity;
    private RecorderViewModel recorderViewModel;
    private SharedPreferences sharedPreferences;

    private ImageButton iBtnRecorder;
    private ImageButton iBtnDelete;
    private ImageButton iBtnSave;
    private TextView recorderTimer;
    private AudioWaveView audioWaveView;
    private LinearLayout audioWaveLinearLayout;
    private RecyclerView fixedListRecyclerView;
    private RecorderAudioItemAdapter recorderAudioItemAdapter;
    private boolean isRecording = false;

    //audio record
    private boolean requireNewRecordTask;
    private int recBufSize;
    private AudioRecord audioRecord;
    private RecordAudioTask recordAudioTask;
    private TimerAudioService timerAudioService;

    private DaoSession daoSession;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        daoSession = ((App) fragmentActivity.getApplication()).getDaoSession();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecorderFragmentPermissionsDispatcher.createAudioDirectoryWithPermissionCheck(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recorderViewModel = ViewModelProviders.of(fragmentActivity).get(RecorderViewModel.class);
        recorderViewModel.setDaoSession(daoSession);

        View root = inflater.inflate(R.layout.fragment_recorder, container, false);
        iBtnRecorder = root.findViewById(R.id.btn_recorder);
        iBtnDelete = root.findViewById(R.id.btn_delete);
        iBtnSave = root.findViewById(R.id.btn_save);
        recorderTimer = root.findViewById(R.id.recorder_timer);
        audioWaveView = root.findViewById(R.id.audio_wave_view);
        audioWaveLinearLayout = root.findViewById(R.id.AudioWave_LinearLayout);
        fixedListRecyclerView = root.findViewById(R.id.fixedlist_recycler_view);
        prepareForAudioRecord();
        return root;
    }

    private void prepareForAudioRecord() {
        iBtnRecorder.setOnClickListener(view -> RecorderFragmentPermissionsDispatcher.handleRecordPauseWithPermissionCheck(this, view));
        iBtnDelete.setVisibility(View.INVISIBLE);
        iBtnDelete.setOnClickListener(this::handleRecordDelete);
        iBtnSave.setVisibility(View.INVISIBLE);
        iBtnSave.setOnClickListener(this::handleRecordSave);
        audioWaveLinearLayout.setVisibility(View.INVISIBLE);

        fixedListRecyclerView.setHasFixedSize(true);
        fixedListRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(fragmentActivity, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.recyclerview_divider));
        fixedListRecyclerView.addItemDecoration(itemDecoration);
        int userId = (int)sharedPreferences.getLong("CURRENT_USER_ID", -1);
        recorderViewModel.getAudioList(userId, getResources().getInteger(R.integer.recycler_view_fixed_item_count)).observe(this, audioList -> {
            if (recorderAudioItemAdapter != null){
                recorderAudioItemAdapter.release();
            }
            recorderAudioItemAdapter = new RecorderAudioItemAdapter(audioList, daoSession);
            fixedListRecyclerView.setAdapter(recorderAudioItemAdapter);
        });
        recBufSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT);
    }

    @NeedsPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void createAudioDirectory(){
        String audioDirName = getResources().getString(R.string.default_audio_directory_name);
        AudioUtils.createAudioDirectory(audioDirName);
    }

    @NeedsPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    void handleRecordPause(View view) {
        ImageButton imageButton = (ImageButton) view;
        if (isRecording){
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_circle_record));
            //pause audio record
            recordAudioTask.getIsRecording().set(false);

        } else {
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_circle_pause));
            //show delete or save icon
            iBtnDelete.setVisibility(View.VISIBLE);
            iBtnSave.setVisibility(View.VISIBLE);
            audioWaveLinearLayout.setVisibility(View.VISIBLE);
            //start/resume audio record
            if (requireNewRecordTask){
                String defaultAudioName = getResources().getString(R.string.default_audio_name) + "_" + AndroidUtils.formatDate(new Date());
                String audioPath = AudioUtils.getAudioDirectory() + File.separatorChar + defaultAudioName;
                DaoSession daoSession = this.daoSession;
                audioRecord = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT, recBufSize);
                System.out.println("========= audioRecord new instance");
                recordAudioTask = new RecordAudioTask(audioRecord, recBufSize, audioWaveView, audioPath, daoSession);
                recordAudioTask.setAudioSaveCompletedListener((audio) -> {
                    if (recorderAudioItemAdapter != null){
                        List<Audio> audioList = recorderAudioItemAdapter.getAudioList();
                        audioList.add(0, audio);
                        recorderAudioItemAdapter.notifyItemInserted(0);
                        recorderAudioItemAdapter.notifyItemRangeChanged(0, audioList.size());
                    }
                });
                if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED){
                    recordAudioTask.execute();
                    timerAudioService = new TimerAudioService(recorderTimer, recordAudioTask);
                    timerAudioService.start();
                } else {
                    Toast.makeText(this.getContext(), getResources().getString(R.string.error_audiorecord_uninitialized), Toast.LENGTH_LONG).show();
                }
                requireNewRecordTask = !requireNewRecordTask;
            } else {
                if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED){
                    recordAudioTask.getIsRecording().set(true);
                }
            }
        }
        isRecording = !isRecording;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RecorderFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        requireNewRecordTask = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireNewRecordTask = true;
    }

    @Override
    public void onStop() {
        if (audioRecord != null && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING){
            audioRecord.stop();
        }
        if (recordAudioTask != null){
            recordAudioTask.getIsRecording().set(false);
            recordAudioTask.getIsDelete().set(true);
        }
        if (timerAudioService != null){
            timerAudioService.stop();
        }
        super.onStop();
    }

    private void handleRecordSave(View view) {
        final boolean preIsRecording = isRecording;
        if (isRecording){
            recordAudioTask.getIsRecording().set(false);
            iBtnRecorder.setImageDrawable(getResources().getDrawable(R.drawable.btn_circle_record));
            isRecording = false;
        }
        String title = getResources().getString(R.string.app_name);
        String message = getResources().getString(R.string.default_audio_save_alert_message);
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.default_dialog_positive_text, (dialog, which) -> {
                    recordAudioTask.getIsSave().set(true);
                    timerAudioService.stop();
                    requireNewRecordTask = true;
                    isRecording = false;
                    recorderTimer.setText(R.string.recorder_timer);
                    audioWaveLinearLayout.setVisibility(View.INVISIBLE);
                    iBtnDelete.setVisibility(View.INVISIBLE);
                    iBtnSave.setVisibility(View.INVISIBLE);
                })
                .setNegativeButton(R.string.default_dialog_negative_text, (dialog, which) -> {
                    if (preIsRecording) {
                        recordAudioTask.getIsRecording().set(true);
                        iBtnRecorder.setImageDrawable(getResources().getDrawable(R.drawable.btn_circle_pause));
                        isRecording = true;
                    }
                }).create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void handleRecordDelete(View view) {
        final boolean preIsRecording = isRecording;
        if (isRecording){
            recordAudioTask.getIsRecording().set(false);
            iBtnRecorder.setImageDrawable(getResources().getDrawable(R.drawable.btn_circle_record));
            isRecording = false;
        }
        String title = getResources().getString(R.string.app_name);
        String message = getResources().getString(R.string.default_audio_delete_alert_message);
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.default_dialog_positive_text, (dialog, which) -> {
                    recordAudioTask.getIsDelete().set(true);
                    timerAudioService.stop();
                    requireNewRecordTask = true;
                    isRecording = false;
                    recorderTimer.setText(R.string.recorder_timer);
                    audioWaveLinearLayout.setVisibility(View.INVISIBLE);
                    iBtnDelete.setVisibility(View.INVISIBLE);
                    iBtnSave.setVisibility(View.INVISIBLE);
                })
                .setNegativeButton(R.string.default_dialog_negative_text, (dialog, which) -> {
                    if (preIsRecording) {
                        recordAudioTask.getIsRecording().set(true);
                        iBtnRecorder.setImageDrawable(getResources().getDrawable(R.drawable.btn_circle_pause));
                        isRecording = true;
                    }
                }).create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}