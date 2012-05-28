package com.jessitron.telephonegame.dao;

import java.util.List;
import com.jessitron.telephonegame.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table GAME.
 */
public class Game {

    private Long id;
    /** Not-null value. */
    private String startingText;
    private String endingText;
    /** Not-null value. */
    private java.util.Date startTimestamp;

    /** Used to resolve relations */
    private DaoSession daoSession;

    /** Used for active entity operations. */
    private GameDao myDao;

    private List<Reading> readingList;

    public Game() {
    }

    public Game(Long id) {
        this.id = id;
    }

    public Game(Long id, String startingText, String endingText, java.util.Date startTimestamp) {
        this.id = id;
        this.startingText = startingText;
        this.endingText = endingText;
        this.startTimestamp = startTimestamp;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGameDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getStartingText() {
        return startingText;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStartingText(String startingText) {
        this.startingText = startingText;
    }

    public String getEndingText() {
        return endingText;
    }

    public void setEndingText(String endingText) {
        this.endingText = endingText;
    }

    /** Not-null value. */
    public java.util.Date getStartTimestamp() {
        return startTimestamp;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStartTimestamp(java.util.Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public synchronized List<Reading> getReadingList() {
        if (readingList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReadingDao targetDao = daoSession.getReadingDao();
            readingList = targetDao._queryGame_ReadingList(id);
        }
        return readingList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetReadingList() {
        readingList = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}