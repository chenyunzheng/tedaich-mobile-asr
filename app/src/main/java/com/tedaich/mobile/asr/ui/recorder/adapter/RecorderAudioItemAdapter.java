package com.tedaich.mobile.asr.ui.recorder.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.Editable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.dao.DaoSession;
import com.tedaich.mobile.asr.model.Audio;
import com.tedaich.mobile.asr.util.AndroidUtils;
import com.tedaich.mobile.asr.util.AudioUtils;
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
        long audioUserId;

        public AudioItemViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audio_name);
            audioDateTime = itemView.findViewById(R.id.audio_datetime);
            audioDuration = itemView.findViewById(R.id.audio_duration);
            moreBtn = itemView.findViewById(R.id.audio_item_more);
            audioPlayerBtn = itemView.findViewById(R.id.btn_audio_play);
            audioPlayProgress = itemView.findViewById(R.id.audio_play_progress);
        }

        public long getAudioId() {
            return audioId;
        }

        public long getAudioUserId() {
            return audioUserId;
        }

        public String getAudioFilePath() {
            return audioFilePath;
        }
    }

    private Resources resources;
    private List<Audio> audioList;
    protected DaoSession daoSession;
    private View preItemView;

    private boolean isPlaying = false;
    private AudioPlayer[] audioPlayers;

    public RecorderAudioItemAdapter(List<Audio> audioList, DaoSession daoSession){
        this.audioList = audioList;
        this.daoSession = daoSession;
        if (audioList != null){
            this.audioPlayers = new AudioPlayer[audioList.size()];
            for (int i = 0; i < this.audioPlayers.length; i++) {
                this.audioPlayers[i] = new AudioPlayer();
            }
        }
     }

    @NonNull
    @Override
    public AudioItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        resources = parent.getResources();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recorder_item_layout,parent,false);
        view.findViewById(R.id.Audio_Player_LinearLayout).setVisibility(View.GONE);
        AudioItemViewHolder viewHolder = new AudioItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AudioItemViewHolder holder, int position) {
        Audio audio = audioList.get(position);
        holder.audioName.setText(audio.getName());
        holder.audioDateTime.setText(AndroidUtils.formatDateWithoutMillisec(audio.getRecordTime()));
        holder.audioDuration.setText(AudioUtils.getTimerValue(audio.getDuration()));
        holder.audioFilePath = audio.getStorePath() + File.separatorChar + audio.getFileName();
        holder.audioId = audio.getId();
        holder.audioOnCloud = audio.getOnCloud();
        holder.audioStatus = audio.getStatus();
        holder.audioUserId = audio.getUserId();
        float seekBarScale = holder.audioPlayProgress.getMax() / (float)audio.getDuration();
        if (position >= this.audioPlayers.length){
            AudioPlayer[] _audioPlayers = new AudioPlayer[position+1];
            System.arraycopy(this.audioPlayers,0,_audioPlayers,0,this.audioPlayers.length);
            for (int i = this.audioPlayers.length; i <= position; i++){
                _audioPlayers[i] = new AudioPlayer();
            }
            this.audioPlayers = _audioPlayers;
        }
        AudioPlayer audioPlayer = this.audioPlayers[position];

        holder.moreBtn.setOnClickListener(view -> {
            showDialog(view.getContext(), holder, position, audioList);
        });
        holder.audioPlayerBtn.setOnClickListener(view -> {
            ImageButton audioPlayerBtn = (ImageButton)view;
            if (isPlaying){
                audioPlayerBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_play_circle_outline));
                audioPlayer.pause();
            } else {
                audioPlayerBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_pause_circle_outline));
                if (audioPlayer.getStatus() == AudioPlayer.CREATED || audioPlayer.getStatus() == AudioPlayer.COMPLETED){
                    audioPlayer.initialize();
                    audioPlayer.prepare(holder.audioFilePath);
                }
                audioPlayer.play();
            }
            isPlaying = !isPlaying;
        });
        holder.audioPlayProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    audioPlayer.seekTo((long)(progress/seekBarScale));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        audioPlayer.setPlayerCallback(new PlayerCallback() {
            @Override
            public void onStart() { }
            @Override
            public void onProgress(long mills) {
                holder.audioPlayProgress.setProgress((int)(mills * seekBarScale));
            }
            @Override
            public void onPause() { }
            @Override
            public void onSeek(long mills) {
                holder.audioPlayProgress.setProgress((int)(mills * seekBarScale));
            }
            @Override
            public void onComplete() {
                holder.audioPlayerBtn.setImageDrawable(resources.getDrawable(R.drawable.ic_play_circle_outline));
                holder.audioPlayProgress.setProgress(0);
                isPlaying = false;
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

    public List<Audio> getAudioList() {
        return audioList;
    }

    public void release() {
        for (AudioPlayer audioPlayer : audioPlayers) {
            audioPlayer.release();
        }
    }

    private void showDialog(Context context, AudioItemViewHolder holder, int position, List<Audio> audioList) {
        View view = LayoutInflater.from(context).inflate(R.layout.audio_item_more_layout,null,false);
        final AlertDialog moreDialog = new AlertDialog.Builder(context).setView(view).create();
        Button shareBtn = view.findViewById(R.id.audio_share);
        Button renameBtn = view.findViewById(R.id.audio_rename);
        Button deleteBtn = view.findViewById(R.id.audio_delete);

        shareBtn.setOnClickListener(v -> {
            moreDialog.dismiss();
            Toast.makeText(context,"in developing through bluetooth, email, etc", Toast.LENGTH_SHORT).show();
        });
        renameBtn.setOnClickListener(v -> {
            moreDialog.dismiss();
            View renameView = LayoutInflater.from(context).inflate(R.layout.audio_item_rename_dialog, null, false);
            String title = v.getResources().getString(R.string.audio_item_rename_dialog_title);
            AlertDialog renameDialog = new AlertDialog.Builder(context)
                    .setTitle(Html.fromHtml("<font color='#2196F3'>" + title + "</font>"))
                    .setView(renameView)
                    .setPositiveButton(R.string.default_dialog_positive_text, (dialog, which) -> {
                        EditText itemRename = renameView.findViewById(R.id.item_rename);
                        Editable renameText = itemRename.getText();
                        if (renameText != null && !"".equals(renameText.toString().trim())){
                            if (audioList != null){
                                Audio audio = audioList.get(position);
                                audio.setName(renameText.toString().trim());
                                notifyItemChanged(position);
                                //update audio table
                                daoSession.getAudioDao().update(audio);
                            }
                        }
                    })
                    .setNegativeButton(R.string.default_dialog_negative_text, (dialog, which) -> {
                        // nothing
                    }).create();
            renameDialog.setCanceledOnTouchOutside(false);
            renameDialog.show();
            renameDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(v.getResources().getColor(R.color.colorPrimary));
            renameDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(v.getResources().getColor(R.color.colorPrimary));
        });
        deleteBtn.setOnClickListener(v -> {
            moreDialog.dismiss();
            if (audioList != null){
                Audio audio = audioList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, audioList.size() - position);
                //delete from audio table
                daoSession.getAudioDao().deleteByKey(audio.getId());
            }
        });

        moreDialog.show();
        Window dialogWindow = moreDialog.getWindow();
        if (dialogWindow != null){
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
            dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.dimAmount = 0.7f;
            dialogWindow.setBackgroundDrawable(null);
            dialogWindow.setAttributes(lp);
        }
    }


}
