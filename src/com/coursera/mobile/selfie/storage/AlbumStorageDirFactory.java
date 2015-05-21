package com.coursera.mobile.selfie.storage;

import java.io.File;

public abstract class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
}
