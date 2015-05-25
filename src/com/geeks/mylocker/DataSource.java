package com.geeks.mylocker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.geeks.mylocker.dao.DaoMaster;
import com.geeks.mylocker.dao.DaoMaster.DevOpenHelper;
import com.geeks.mylocker.dao.DaoSession;
import com.geeks.mylocker.dao.FieldDao;
import com.geeks.mylocker.dao.FolderDao;
import com.geeks.mylocker.dao.RecordDao;

public class DataSource {

	protected final String TAG = getClass().getSimpleName();
	
	public final static String DB_NAME = "mylocker-db"; 
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	
	private FolderDao folderDao;
	private RecordDao recordDao;
	private FieldDao fieldDao;
	
	//private Cursor cursor;
	
	public void setup(Context context){
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		folderDao = daoSession.getFolderDao();
		recordDao = daoSession.getRecordDao();
		fieldDao = daoSession.getFieldDao();
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	public RecordDao getRecordDao() {
		return recordDao;
	}

	public void setRecordDao(RecordDao recordDao) {
		this.recordDao = recordDao;
	}

	public FieldDao getFieldDao() {
		return fieldDao;
	}

	public void setFieldDao(FieldDao fieldDao) {
		this.fieldDao = fieldDao;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

	public DaoMaster getDaoMaster() {
		return daoMaster;
	}

	public void setDaoMaster(DaoMaster daoMaster) {
		this.daoMaster = daoMaster;
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}

	public void setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
	}

	public FolderDao getFolderDao() {
		return folderDao;
	}

	public void setFolderDao(FolderDao folderDao) {
		this.folderDao = folderDao;
	}
}
