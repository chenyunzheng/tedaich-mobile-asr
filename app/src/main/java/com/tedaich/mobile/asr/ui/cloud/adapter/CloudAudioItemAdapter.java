package com.tedaich.mobile.asr.ui.cloud.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.model.Audio;
import com.tedaich.mobile.asr.ui.recorder.adapter.RecorderAudioItemAdapter;

import java.util.List;

public class CloudAudioItemAdapter extends RecorderAudioItemAdapter {

    public CloudAudioItemAdapter(List<Audio> audioList){
        super(audioList);
    }

    @NonNull
    @Override
    public AudioItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cloud_item_layout,parent,false);
        view.findViewById(R.id.Audio_Player_LinearLayout).setVisibility(View.GONE);
        view.findViewById(R.id.audio_item_more).setVisibility(View.INVISIBLE);
        AudioItemViewHolder viewHolder = new AudioItemViewHolder(view);
        return viewHolder;
    }
}
