package com.tedaich.mobile.asr.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

@Entity(nameInDb = "audio_text")
public class AudioText {

    @Id(autoincrement = true)
    private Long id;
    private Long audioId;
    private int type;
    @NotNull
    private String text;
    @NotNull
    private Date createTime;

    @Generated(hash = 1602339964)
    public AudioText(Long id, Long audioId, int type, @NotNull String text,
            @NotNull Date createTime) {
        this.id = id;
        this.audioId = audioId;
        this.type = type;
        this.text = text;
        this.createTime = createTime;
    }
    @Generated(hash = 328810189)
    public AudioText() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getAudioId() {
        return this.audioId;
    }
    public void setAudioId(Long audioId) {
        this.audioId = audioId;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }



}
