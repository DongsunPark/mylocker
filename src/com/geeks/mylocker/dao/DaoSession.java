package com.geeks.mylocker.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.geeks.mylocker.dao.Group;
import com.geeks.mylocker.dao.Record;
import com.geeks.mylocker.dao.Field;

import com.geeks.mylocker.dao.GroupDao;
import com.geeks.mylocker.dao.RecordDao;
import com.geeks.mylocker.dao.FieldDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig groupDaoConfig;
    private final DaoConfig recordDaoConfig;
    private final DaoConfig fieldDaoConfig;

    private final GroupDao groupDao;
    private final RecordDao recordDao;
    private final FieldDao fieldDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        groupDaoConfig = daoConfigMap.get(GroupDao.class).clone();
        groupDaoConfig.initIdentityScope(type);

        recordDaoConfig = daoConfigMap.get(RecordDao.class).clone();
        recordDaoConfig.initIdentityScope(type);

        fieldDaoConfig = daoConfigMap.get(FieldDao.class).clone();
        fieldDaoConfig.initIdentityScope(type);

        groupDao = new GroupDao(groupDaoConfig, this);
        recordDao = new RecordDao(recordDaoConfig, this);
        fieldDao = new FieldDao(fieldDaoConfig, this);

        registerDao(Group.class, groupDao);
        registerDao(Record.class, recordDao);
        registerDao(Field.class, fieldDao);
    }
    
    public void clear() {
        groupDaoConfig.getIdentityScope().clear();
        recordDaoConfig.getIdentityScope().clear();
        fieldDaoConfig.getIdentityScope().clear();
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public RecordDao getRecordDao() {
        return recordDao;
    }

    public FieldDao getFieldDao() {
        return fieldDao;
    }

}
