package com.tedaich.mobile.asr.ui.cloud.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.tedaich.mobile.asr.R;
import com.tedaich.mobile.asr.activity.TransferTextActivity;
import com.tedaich.mobile.asr.dao.AudioTextDao;
import com.tedaich.mobile.asr.dao.DaoSession;
import com.tedaich.mobile.asr.model.Audio;
import com.tedaich.mobile.asr.model.AudioText;
import com.tedaich.mobile.asr.ui.recorder.adapter.RecorderAudioItemAdapter;
import com.tedaich.mobile.asr.util.CloudUtil;
import com.tedaich.mobile.asr.util.Constants;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.util.Date;
import java.util.List;

public class CloudAudioItemAdapter extends RecorderAudioItemAdapter {

    private SharedPreferences sharedPreferences;
    private Fragment fragment;

    public CloudAudioItemAdapter(List<Audio> audioList, DaoSession daoSession,
                                 SharedPreferences sharedPreferences, Fragment fragment){
        super(audioList, daoSession);
        this.sharedPreferences = sharedPreferences;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AudioItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        super.resources = parent.getResources();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cloud_item_layout,parent,false);
        view.findViewById(R.id.Audio_Player_LinearLayout).setVisibility(View.GONE);
        view.findViewById(R.id.audio_item_more).setVisibility(View.INVISIBLE);
        AudioItemViewHolder viewHolder = new AudioItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AudioItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Button cloudUploadBtn = holder.itemView.findViewById(R.id.audio_cloud_upload);
        if (holder.isAudioOnCloud()){
            cloudUploadBtn.setEnabled(false);
        }
        Button cloudTransferTextBtn = holder.itemView.findViewById(R.id.audio_cloud_transfer_text);
        Button cloudMoreOptionBtn = holder.itemView.findViewById(R.id.cloud_item_more_option);

        cloudUploadBtn.setOnClickListener(v -> {
            pauseAudioPlayer(holder, position);
            long gUserId = sharedPreferences.getLong("G_USER_ID", -1);
            if (gUserId != -1){
                List<Audio> audioList = getAudioList();
                Audio audio = audioList.get(position);
                CloudUtil.uploadFile(gUserId, JSON.toJSON(audio).toString(), holder.getAudioFilePath());
            } else {
                Toast.makeText(v.getContext(), "Login first (in developing)", Toast.LENGTH_SHORT).show();
            }
        });
        cloudTransferTextBtn.setOnClickListener(v -> {
            pauseAudioPlayer(holder, position);
            List<Audio> audioList = getAudioList();
            Audio audio = audioList.get(position);
            Intent intent = new Intent(v.getContext(), TransferTextActivity.class);
            intent.putExtra("audio_id", audio.getId());
            intent.putExtra("audio_name", audio.getName());
            QueryBuilder<AudioText> audioTextQueryBuilder = daoSession.queryBuilder(AudioText.class).where(AudioTextDao.Properties.AudioId.eq(audio.getId()),
                    AudioTextDao.Properties.Type.eq(0)).orderDesc(AudioTextDao.Properties.CreateTime);
            List<AudioText> audioTextList = audioTextQueryBuilder.list();
            intent.putExtra("audio_to_text", audioTextList.size() > 0 ? audioTextList.get(0).getText() : "");
            fragment.startActivityForResult(intent, Constants.REQUEST_CODE_TRANSFER_TEXT);
        });
        cloudMoreOptionBtn.setOnClickListener(v -> {
            pauseAudioPlayer(holder, position);
            showDialog(v.getContext(), holder, position, super.getAudioList());
        });

    }

    private void showDialog(Context context, AudioItemViewHolder holder, int position, List<Audio> audioList){
        View view = LayoutInflater.from(context).inflate(R.layout.cloud_item_more_layout,null,false);
        final AlertDialog moreDialog = new AlertDialog.Builder(context).setView(view).create();
        Button cloudSyncBtn = view.findViewById(R.id.cloud_sync);
        Button cloudRenameBtn = view.findViewById(R.id.cloud_rename);
        Button cloudDeleteBtn = view.findViewById(R.id.cloud_delete);

        cloudSyncBtn.setOnClickListener(v -> {
            moreDialog.dismiss();
            Toast.makeText(context, "in developing and mainly sync the latest transfer text from cloud to local", Toast.LENGTH_SHORT).show();
            long gUserId = sharedPreferences.getLong("G_USER_ID", -1);
            if (gUserId != -1){
                long audioId = holder.getAudioId();
                String text = CloudUtil.getLatestTransferText(gUserId,audioId);
                //compare with local transfer text
                QueryBuilder<AudioText> audioTextQueryBuilder = daoSession.queryBuilder(AudioText.class).where(AudioTextDao.Properties.AudioId.eq(audioId),
                        AudioTextDao.Properties.Type.eq(0)).orderDesc(AudioTextDao.Properties.CreateTime);
                List<AudioText> audioTextList = audioTextQueryBuilder.list();
                if (audioTextList.size() == 0){
                    //update local db
                    AudioText audioText = new AudioText(audioId, 0, text, new Date());
                    daoSession.getAudioTextDao().insert(audioText);
                } else {
                    if (!text.equals(audioTextList.get(0).getText())){
                        //update local db
                        AudioText audioText = new AudioText(audioId, 0, text, new Date());
                        daoSession.getAudioTextDao().insert(audioText);
                    }
                }
            }
        });
        cloudRenameBtn.setOnClickListener(v -> {
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
                                String newName = renameText.toString().trim();
                                Audio audio = audioList.get(position);
                                audio.setName(newName);
                                notifyItemChanged(position);
                                //update audio table
                                daoSession.getAudioDao().update(audio);
                                //update cloud audio table
                                long gUserId = sharedPreferences.getLong("G_USER_ID", -1);
                                if (gUserId != -1){
                                    CloudUtil.renameFile(gUserId,audio.getId(),newName);
                                }
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
        cloudDeleteBtn.setOnClickListener(v -> {
            moreDialog.dismiss();
            if (audioList != null){
                Audio audio = audioList.remove(position);
                adjustAudioPlayers(Constants.DELETE, position);
                preAudioItemViewHolder = null;
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, audioList.size() - position);
                //delete from audio table + local audio file
                daoSession.getAudioDao().deleteByKey(audio.getId());
                File file = new File(holder.getAudioFilePath());
                if (file.exists()){
                    file.delete();
                }
                //delete from cloud audio table
                long gUserId = sharedPreferences.getLong("G_USER_ID", -1);
                if (gUserId != -1){
                    if (audio.getOnCloud()){
                        CloudUtil.deleteFile(gUserId,audio.getId(),true);
                    }
                }
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
