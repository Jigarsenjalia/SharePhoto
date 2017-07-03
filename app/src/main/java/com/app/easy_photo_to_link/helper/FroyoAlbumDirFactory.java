package com.app.easy_photo_to_link.helper;

import java.io.File;

import android.os.Environment;

import com.app.easy_photo_to_link.helper.AlbumStorageDirFactory;

public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {

	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File(
		  Environment.getExternalStoragePublicDirectory(
		    Environment.DIRECTORY_PICTURES
		  ), 
		  albumName
		);
	}
}
