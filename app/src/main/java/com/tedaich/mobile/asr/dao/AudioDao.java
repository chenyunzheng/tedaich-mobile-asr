package com.tedaich.mobile.asr.dao;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import com.tedaich.mobile.asr.model.Audio;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "audio".
*/
public class AudioDao extends AbstractDao<Audio, Long> {

    public static final String TABLENAME = "audio";

    /**
     * Properties of entity Audio.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, Long.class, "userId", false, "USER_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property FileName = new Property(3, String.class, "fileName", false, "FILE_NAME");
        public final static Property RecordTime = new Property(4, java.util.Date.class, "recordTime", false, "RECORD_TIME");
        public final static Property Duration = new Property(5, Long.class, "duration", false, "DURATION");
        public final static Property FileSize = new Property(6, float.class, "fileSize", false, "FILE_SIZE");
        public final static Property StorePath = new Property(7, String.class, "storePath", false, "STORE_PATH");
        public final static Property Status = new Property(8, int.class, "status", false, "STATUS");
        public final static Property OnCloud = new Property(9, boolean.class, "onCloud", false, "ON_CLOUD");
        public final static Property AudioToText = new Property(10, String.class, "audioToText", false, "AUDIO_TO_TEXT");
    }

    private DaoSession daoSession;

    private Query<Audio> user_AudioListQuery;

    public AudioDao(DaoConfig config) {
        super(config);
    }
    
    public AudioDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"audio\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" INTEGER," + // 1: userId
                "\"NAME\" TEXT NOT NULL ," + // 2: name
                "\"FILE_NAME\" TEXT NOT NULL ," + // 3: fileName
                "\"RECORD_TIME\" INTEGER NOT NULL ," + // 4: recordTime
                "\"DURATION\" INTEGER NOT NULL ," + // 5: duration
                "\"FILE_SIZE\" REAL NOT NULL ," + // 6: fileSize
                "\"STORE_PATH\" TEXT NOT NULL ," + // 7: storePath
                "\"STATUS\" INTEGER NOT NULL ," + // 8: status
                "\"ON_CLOUD\" INTEGER NOT NULL ," + // 9: onCloud
                "\"AUDIO_TO_TEXT\" TEXT);"); // 10: audioToText
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"audio\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Audio entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(2, userId);
        }
        stmt.bindString(3, entity.getName());
        stmt.bindString(4, entity.getFileName());
        stmt.bindLong(5, entity.getRecordTime().getTime());
        stmt.bindLong(6, entity.getDuration());
        stmt.bindDouble(7, entity.getFileSize());
        stmt.bindString(8, entity.getStorePath());
        stmt.bindLong(9, entity.getStatus());
        stmt.bindLong(10, entity.getOnCloud() ? 1L: 0L);
 
        String audioToText = entity.getAudioToText();
        if (audioToText != null) {
            stmt.bindString(11, audioToText);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Audio entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(2, userId);
        }
        stmt.bindString(3, entity.getName());
        stmt.bindString(4, entity.getFileName());
        stmt.bindLong(5, entity.getRecordTime().getTime());
        stmt.bindLong(6, entity.getDuration());
        stmt.bindDouble(7, entity.getFileSize());
        stmt.bindString(8, entity.getStorePath());
        stmt.bindLong(9, entity.getStatus());
        stmt.bindLong(10, entity.getOnCloud() ? 1L: 0L);
 
        String audioToText = entity.getAudioToText();
        if (audioToText != null) {
            stmt.bindString(11, audioToText);
        }
    }

    @Override
    protected final void attachEntity(Audio entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Audio readEntity(Cursor cursor, int offset) {
        Audio entity = new Audio( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // userId
            cursor.getString(offset + 2), // name
            cursor.getString(offset + 3), // fileName
            new java.util.Date(cursor.getLong(offset + 4)), // recordTime
            cursor.getLong(offset + 5), // duration
            cursor.getFloat(offset + 6), // fileSize
            cursor.getString(offset + 7), // storePath
            cursor.getInt(offset + 8), // status
            cursor.getShort(offset + 9) != 0, // onCloud
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // audioToText
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Audio entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setName(cursor.getString(offset + 2));
        entity.setFileName(cursor.getString(offset + 3));
        entity.setRecordTime(new java.util.Date(cursor.getLong(offset + 4)));
        entity.setDuration(cursor.getLong(offset + 5));
        entity.setFileSize(cursor.getFloat(offset + 6));
        entity.setStorePath(cursor.getString(offset + 7));
        entity.setStatus(cursor.getInt(offset + 8));
        entity.setOnCloud(cursor.getShort(offset + 9) != 0);
        entity.setAudioToText(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Audio entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Audio entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Audio entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "audioList" to-many relationship of User. */
    public List<Audio> _queryUser_AudioList(Long userId) {
        synchronized (this) {
            if (user_AudioListQuery == null) {
                QueryBuilder<Audio> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.UserId.eq(null));
                user_AudioListQuery = queryBuilder.build();
            }
        }
        Query<Audio> query = user_AudioListQuery.forCurrentThread();
        query.setParameter(0, userId);
        return query.list();
    }

}
