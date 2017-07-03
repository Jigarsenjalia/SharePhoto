package com.app.easy_photo_to_link.helper;

import java.io.File;

import android.os.Environment;

import com.app.easy_photo_to_link.helper.AlbumStorageDirFactory;

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

	// Standard storage location for digital camera files
	private static final String CAMERA_DIR = "/dcim/";

	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File (
				Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ albumName
		);
	}
}
