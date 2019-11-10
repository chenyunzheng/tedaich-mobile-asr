package com.tedaich.mobile.asr.ui.recorder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.tedaich.mobile.asr.MainActivity;
import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.ui.cloud.CloudViewModel;

public class RecorderFragment extends Fragment {

    private FragmentActivity fragmentActivity;
    private RecorderViewModel recorderViewModel;
    private CloudViewModel cloudViewModel;

    private ImageButton iBtnRecorder;
    private boolean isRecording = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recorderViewModel = ViewModelProviders.of(fragmentActivity).get(RecorderViewModel.class);
        cloudViewModel = ViewModelProviders.of(fragmentActivity).get(CloudViewModel.class);

        View root = inflater.inflate(R.layout.fragment_recorder, container, false);
        iBtnRecorder = root.findViewById(R.id.btn_recorder);
        iBtnRecorder.setOnClickListener(this::handleRecordPause);

        return root;
    }

    private void handleRecordPause(View view) {
        ImageButton imageButton = (ImageButton) view;
        if (isRecording){
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_circle_record));
            //show wave view and mask dialog

            //start/resume audio record

        } else {
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_circle_pause));
            //show delete or save icon

            //pause audio record
        }
        isRecording = !isRecording;
    }

}