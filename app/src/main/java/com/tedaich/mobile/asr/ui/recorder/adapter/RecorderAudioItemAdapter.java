package com.tedaich.mobile.asr.ui.recorder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.model.Audio;
import com.tedaich.mobile.asr.util.player.AudioPlayer;
import com.tedaich.mobile.asr.util.player.PlayerCallback;

import java.io.File;
import java.util.List;

public class RecorderAudioItemAdapter extends Adapter<RecorderAudioItemAdapter.AudioItemViewHolder> {

    protected static class AudioItemViewHolder extends RecyclerView.ViewHolder {
        TextView audioName;
        TextView audioDateTime;
        TextView audioDuration;
        ImageButton moreBtn;
        ImageButton audioPlayerBtn;
        SeekBar audioPlayProgress;

        long audioId;
        String audioFilePath;
        boolean audioOnCloud;
        int audioStatus;

        public AudioItemViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audio_name);
            audioDateTime = itemView.findViewById(R.id.audio_datetime);
            audioDuration = itemView.findViewById(R.id.audio_duration);
            moreBtn = itemView.findViewById(R.id.audio_item_more);
            audioPlayerBtn = itemView.findViewById(R.id.btn_audio_play);
            audioPlayProgress = itemView.findViewById(R.id.audio_play_progress);
        }
    }

    private List<Audio> audioList;
    private View preItemView;

    private boolean isPlaying = false;
    private AudioPlayer[] audioPlayers;

    public RecorderAudioItemAdapter(List<Audio> audioList){
        this.audioList = audioList;
        if (audioList != null){
            this.audioPlayers = new AudioPlayer[audioList.size()];
        }

     }

    @NonNull
    @Override
    public AudioItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recorder_item_layout,parent,false);
        view.findViewById(R.id.Audio_Player_LinearLayout).setVisibility(View.GONE);
        AudioItemViewHolder viewHolder = new AudioItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AudioItemViewHolder holder, int position) {
        Audio audio = audioList.get(position);
        holder.audioName.setText(audio.getName());
        holder.audioDateTime.setText(audio.getCreateTime().toString());
        holder.audioDuration.setText(audio.getDuration());
        holder.audioFilePath = audio.getStorePath() + File.separatorChar + audio.getFileName();
        holder.audioId = audio.getId();
        holder.audioOnCloud = audio.getOnCloud();
        holder.audioStatus = audio.getStatus();
        AudioPlayer audioPlayer = this.audioPlayers[position];

        holder.moreBtn.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "in developing, including rename, delete", Toast.LENGTH_SHORT).show();
        });
        holder.audioPlayerBtn.setOnClickListener(view -> {
            ImageButton audioPlayerBtn = (ImageButton)view;
            if (isPlaying){
                audioPlayerBtn.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_play_circle_outline));
                audioPlayer.pause();
            } else {
                audioPlayerBtn.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_pause_circle_outline));
//                audioPlayer.
                audioPlayer.play();
            }
            isPlaying = !isPlaying;
        });
        holder.audioPlayProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    float rate = progress / seekBar.getMax();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        audioPlayer.setPlayerCallback(new PlayerCallback() {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(long mills) {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onPause() {

            }

            @Override
            public void onSeek(long mills) {

            }
        });
        holder.itemView.setOnClickListener(itemView -> {
            if (preItemView != null && preItemView != itemView){
                preItemView.findViewById(R.id.Audio_Player_LinearLayout).setVisibility(View.GONE);
            }
            preItemView = itemView;
            View audioPlayerLayout = itemView.findViewById(R.id.Audio_Player_LinearLayout);
            if (audioPlayerLayout.getVisibility() == View.GONE){
                audioPlayerLayout.setVisibility(View.VISIBLE);
                holder.audioPlayProgress.setProgress(0);
            } else {
                audioPlayerLayout.setVisibility(View.GONE);

            }


        });

    }

    @Override
    public int getItemCount() {
        return audioList == null ? 0 : audioList.size();
    }

}
