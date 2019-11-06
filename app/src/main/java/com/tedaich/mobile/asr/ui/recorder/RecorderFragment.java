package com.tedaich.mobile.asr.ui.recorder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.util.AndroidUtils;

public class RecorderFragment extends Fragment {

    private RecorderViewModel recorderViewModel;

    private Button btnRecorder;
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
        btnRecorder = root.findViewById(R.id.btn_recorder);
        btnRecorder.setOnClickListener(this::handleRecordPause);
        return root;
    }

    private void handleRecordPause(View view) {
        Button button = (Button) view;
        if (isRecording){
            button.setText("");
            button.setBackground(getResources().getDrawable(R.drawable.btn_circle_record));
        } else {
            button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            AndroidUtils.addButtonIcon(button, R.drawable.ic_pause, null, "");
            button.setBackground(getResources().getDrawable(R.drawable.btn_circle_pause));
        }
        isRecording = !isRecording;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button recorder = getActivity().findViewById(R.id.btn_recorder);
        System.out.println("onActivityCreated");

    }
}