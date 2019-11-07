package com.tedaich.mobile.asr.ui.recorder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.tedaich.mobile.asr.R;

public class RecorderFragment extends Fragment {

    private RecorderViewModel recorderViewModel;

    private ImageButton iBtnRecorder;
    private boolean isRecording = false;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recorderViewModel = ViewModelProviders.of(this).get(RecorderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_recorder, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        recorderViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        iBtnRecorder = root.findViewById(R.id.btn_recorder);
        iBtnRecorder.setOnClickListener(this::handleRecordPause);
        return root;
    }

    private void handleRecordPause(View view) {
        ImageButton imageButton = (ImageButton) view;
        if (isRecording){
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_circle_record));
        } else {
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.btn_circle_pause));
        }
        isRecording = !isRecording;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton recorder = getActivity().findViewById(R.id.btn_recorder);
        System.out.println("onActivityCreated");

    }
}