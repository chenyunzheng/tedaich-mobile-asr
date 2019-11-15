package com.tedaich.mobile.asr.ui.recorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.model.Audio;

import java.util.List;

public class RecorderAudioItemAdapter extends Adapter<RecorderAudioItemAdapter.AudioItemViewHolder> {

    protected static class AudioItemViewHolder extends RecyclerView.ViewHolder {
        TextView audioName;
        TextView audioDateTime;
        TextView audioDuration;
        public AudioItemViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audio_name);
            audioDateTime = itemView.findViewById(R.id.audio_datetime);
            audioDuration = itemView.findViewById(R.id.audio_duration);
        }
    }

    private List<Audio> audioList;

    public RecorderAudioItemAdapter(List<Audio> audioList){
        this.audioList = audioList;
    }

    @NonNull
    @Override
    public AudioItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recorder_item_layout,parent,false);
        AudioItemViewHolder viewHolder = new AudioItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AudioItemViewHolder holder, int position) {
        Audio audio = audioList.get(position);
        holder.audioName.setText(audio.getName());
        holder.audioDateTime.setText(audio.getCreateTime().toString());
        holder.audioDuration.setText(audio.getDuration());
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }


}
