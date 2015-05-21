package com.coursera.mobile.selfie;

import java.util.Date;

import android.graphics.Bitmap;

public class Photo {
	
	private String mName;
	private String mPath;
	private Date createdDate;
	
	private Bitmap mBitmap;
	
	public Photo(){
		super();
	}
	
	public Photo(String mName, String mpath) {
		super();
		this.mName = mName;
		this.mPath = mpath;
		this.createdDate = new Date();
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Bitmap getBitmap() {
		return mBitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.mBitmap = bitmap;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getPath() {
		return mPath;
	}

	public void setPath(String path) {
		this.mPath = path;
	}
}
