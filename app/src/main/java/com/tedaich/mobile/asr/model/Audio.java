package com.tedaich.mobile.asr.model;

import com.tedaich.mobile.asr.dao.AudioDao;
import com.tedaich.mobile.asr.dao.AudioTextDao;
import com.tedaich.mobile.asr.dao.DaoSession;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;

@Entity(nameInDb = "audio")
public class Audio {

    @Id(autoincrement = true)
    private Long id;
    private Long userId;
    @NotNull
    private String name;
    @NotNull
    private String fileName;
    @NotNull
    private Date createTime;
    @NotNull
    private String duration;
    @NotNull
    private float fileSize;
    @NotNull
    private String storePath;
    @NotNull
    private int status;
    private boolean onCloud;
    private String audioToText;
    @ToMany(referencedJoinProperty = "audioId")
    private List<AudioText> audioTextList;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 226033729)
    private transient AudioDao myDao;
    @Generated(hash = 427671841)
    public Audio(Long id, Long userId, @NotNull String name,
            @NotNull String fileName, @NotNull Date createTime,
            @NotNull String duration, float fileSize, @NotNull String storePath,
            int status, boolean onCloud, String audioToText) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.fileName = fileName;
        this.createTime = createTime;
        this.duration = duration;
        this.fileSize = fileSize;
        this.storePath = storePath;
        this.status = status;
        this.onCloud = onCloud;
        this.audioToText = audioToText;
    }
    @Generated(hash = 1642629471)
    public Audio() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getDuration() {
        return this.duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public float getFileSize() {
        return this.fileSize;
    }
    public void setFileSize(float fileSize) {
        this.fileSize = fileSize;
    }
    public String getStorePath() {
        return this.storePath;
    }
    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public boolean getOnCloud() {
        return this.onCloud;
    }
    public void setOnCloud(boolean onCloud) {
        this.onCloud = onCloud;
    }
    public String getAudioToText() {
        return this.audioToText;
    }
    public void setAudioToText(String audioToText) {
        this.audioToText = audioToText;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1661056904)
    public List<AudioText> getAudioTextList() {
        if (audioTextList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AudioTextDao targetDao = daoSession.getAudioTextDao();
            List<AudioText> audioTextListNew = targetDao
                    ._queryAudio_AudioTextList(id);
            synchronized (this) {
                if (audioTextList == null) {
                    audioTextList = audioTextListNew;
                }
            }
        }
        return audioTextList;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 71032301)
    public synchronized void resetAudioTextList() {
        audioTextList = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1261206123)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAudioDao() : null;
    }

}
