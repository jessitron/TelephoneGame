package com.jessitron.telephonegame.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.SqlUtils;
import de.greenrobot.dao.Query;
import de.greenrobot.dao.QueryBuilder;

import com.jessitron.telephonegame.dao.Reading;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table READING.
*/
public class ReadingDao extends AbstractDao<Reading, Long> {

    public static final String TABLENAME = "READING";

    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property GameId = new Property(1, long.class, "gameId", false, "GAME_ID");
        public final static Property CompleteTimestamp = new Property(2, java.util.Date.class, "completeTimestamp", false, "COMPLETE_TIMESTAMP");
        public final static Property StartingText = new Property(3, String.class, "startingText", false, "STARTING_TEXT");
        public final static Property EndingText = new Property(4, String.class, "endingText", false, "ENDING_TEXT");
    };

    private DaoSession daoSession;

    private Query<Reading> game_ReadingListQuery;

    public ReadingDao(DaoConfig config) {
        super(config);
    }
    
    public ReadingDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String sql = "CREATE TABLE " + (ifNotExists? "IF NOT EXISTS ": "") + "'READING' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'GAME_ID' INTEGER NOT NULL ," + // 1: gameId
                "'COMPLETE_TIMESTAMP' INTEGER NOT NULL ," + // 2: completeTimestamp
                "'STARTING_TEXT' TEXT NOT NULL ," + // 3: startingText
                "'ENDING_TEXT' TEXT NOT NULL );"; // 4: endingText
        db.execSQL(sql);
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'READING'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Reading entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getGameId());
        stmt.bindLong(3, entity.getCompleteTimestamp().getTime());
        stmt.bindString(4, entity.getStartingText());
        stmt.bindString(5, entity.getEndingText());
    }

    @Override
    protected void attachEntity(Reading entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Reading readEntity(Cursor cursor, int offset) {
        Reading entity = new Reading( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // gameId
            new java.util.Date(cursor.getLong(offset + 2)), // completeTimestamp
            cursor.getString(offset + 3), // startingText
            cursor.getString(offset + 4) // endingText
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Reading entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setGameId(cursor.getLong(offset + 1));
        entity.setCompleteTimestamp(new java.util.Date(cursor.getLong(offset + 2)));
        entity.setStartingText(cursor.getString(offset + 3));
        entity.setEndingText(cursor.getString(offset + 4));
     }
    
    @Override
    protected Long updateKeyAfterInsert(Reading entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Reading entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "readingList" to-many relationship of Game. */
    public synchronized List<Reading> _queryGame_ReadingList(long gameId) {
        if (game_ReadingListQuery == null) {
            QueryBuilder<Reading> queryBuilder = queryBuilder();
            queryBuilder.where(Properties.GameId.eq(gameId));
            queryBuilder.orderRaw("COMPLETE_TIMESTAMP ASC");
            game_ReadingListQuery = queryBuilder.build();
        } else {
            game_ReadingListQuery.setParameter(0, gameId);
        }
        return game_ReadingListQuery.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getGameDao().getAllColumns());
            builder.append(" FROM READING T");
            builder.append(" LEFT JOIN GAME T0 ON T.'GAME_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Reading loadCurrentDeep(Cursor cursor, boolean lock) {
        Reading entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Game game = loadCurrentOther(daoSession.getGameDao(), cursor, offset);
         if(game != null) {
            entity.setGame(game);
        }

        return entity;    
    }

    public Reading loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Reading> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Reading> list = new ArrayList<Reading>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Reading> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Reading> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}