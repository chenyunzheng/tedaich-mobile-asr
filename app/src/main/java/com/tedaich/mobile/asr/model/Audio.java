package com.tedaich.mobile.asr.model;

import java.util.Date;
import java.util.List;

public class Audio {

    private int id;
    private int userId;
    private String name;
    private Date dateTime;
    private String duration;
    private float fileSize;
    private String storePath;
    private int status;
    private boolean onCloud;
    private String audioToText;
    private List<AudioText> audioTextList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public float getFileSize() {
        return fileSize;
    }

    public void setFileSize(float fileSize) {
        this.fileSize = fileSize;
    }

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isOnCloud() {
        return onCloud;
    }

    public void setOnCloud(boolean onCloud) {
        this.onCloud = onCloud;
    }

    public String getAudioToText() {
        return audioToText;
    }

    public void setAudioToText(String audioToText) {
        this.audioToText = audioToText;
    }

    public List<AudioText> getAudioTextList() {
        return audioTextList;
    }

    public void setAudioTextList(List<AudioText> audioTextList) {
        this.audioTextList = audioTextList;
    }
}
