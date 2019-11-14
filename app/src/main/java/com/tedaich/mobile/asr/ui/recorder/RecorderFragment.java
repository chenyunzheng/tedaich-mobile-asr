package com.tedaich.mobile.asr.ui.recorder;

import android.Manifest;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.model.Audio;
import com.tedaich.mobile.asr.service.RecordAudioTask;
import com.tedaich.mobile.asr.ui.cloud.CloudViewModel;
import com.tedaich.mobile.asr.ui.recorder.adapter.RecorderAudioItemAdapter;
import com.tedaich.mobile.asr.util.AudioUtils;
import com.tedaich.mobile.asr.widget.AudioWaveView;

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
    private CloudViewModel cloudViewModel;

    private ImageButton iBtnRecorder;
    private ImageButton iBtnDelete;
    private ImageButton iBtnSave;
    private AudioWaveView audioWaveView;
    private LinearLayout audioWaveLinearLayout;
    private RecyclerView fixedListRecyclerView;
    private boolean isRecording = false;

    //audio record
    private int recBufSize;
    private AudioRecord audioRecord;
    private RecordAudioTask recordAudioTask;
    private String mFileName = "test";//文件名

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecorderFragmentPermissionsDispatcher.createAudioDirectoryWithPermissionCheck(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recorderViewModel = ViewModelProviders.of(fragmentActivity).get(RecorderViewModel.class);
        cloudViewModel = ViewModelProviders.of(fragmentActivity).get(CloudViewModel.class);

        View root = inflater.inflate(R.layout.fragment_recorder, container, false);
        iBtnRecorder = root.findViewById(R.id.btn_recorder);
        iBtnDelete = root.findViewById(R.id.btn_delete);
        iBtnSave = root.findViewById(R.id.btn_save);
        audioWaveView = root.findViewById(R.id.audio_wave_view);
        audioWaveLinearLayout = root.findViewById(R.id.AudioWave_LinearLayout);
        fixedListRecyclerView = root.findViewById(R.id.fixedlist_recycler_view);

        prepareForAudioRecord();

        return root;
    }

    private void prepareForAudioRecord() {
        final RecorderFragment recorderFragment = this;
        iBtnRecorder.setOnClickListener(view -> RecorderFragmentPermissionsDispatcher.handleRecordPauseWithPermissionCheck(recorderFragment, view));
        iBtnDelete.setVisibility(View.INVISIBLE);
        iBtnDelete.setOnClickListener(this::handleRecordDelete);
        iBtnSave.setVisibility(View.INVISIBLE);
        iBtnSave.setOnClickListener(this::handleRecordSave);
        audioWaveLinearLayout.setVisibility(View.INVISIBLE);

        fixedListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        fixedListRecyclerView.setLayoutManager(layoutManager);
        cloudViewModel.getAudioList().observe(this, new Observer<List<Audio>>() {
            @Override
            public void onChanged(List<Audio> audioList) {
                RecorderAudioItemAdapter adapter = new RecorderAudioItemAdapter(audioList);
                fixedListRecyclerView.setAdapter(adapter);
            }
        });
        recBufSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT);
        audioRecord = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT, recBufSize);

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
//            if (recordAudioTask != null){
//                recordAudioTask.cancel(true);
//                System.out.println("=======canceled");
//            }
            recordAudioTask.getIsRecording().set(false);

        } else {
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_circle_pause));
            //show delete or save icon
            iBtnDelete.setVisibility(View.VISIBLE);
            iBtnSave.setVisibility(View.VISIBLE);
            audioWaveLinearLayout.setVisibility(View.VISIBLE);
            //start/resume audio record
            if (recordAudioTask == null){
                recordAudioTask = new RecordAudioTask(audioRecord, recBufSize, view);
                if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED){
                    recordAudioTask.execute();
                } else {
                    Toast.makeText(this.getContext(), getResources().getString(R.string.error_audiorecord_uninitialized), Toast.LENGTH_SHORT).show();
                }
            } else {
                recordAudioTask.getIsRecording().set(true);
            }
        }
        isRecording = !isRecording;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RecorderFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }



    private void handleRecordSave(View view) {
        Toast.makeText(getContext(), "developing", Toast.LENGTH_SHORT).show();
    }

    private void handleRecordDelete(View view) {
        Toast.makeText(getContext(), "developing", Toast.LENGTH_SHORT).show();
    }
}