package com.fangbian365.kuaidi.mod.dirmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class CHBDirContext extends DirectoryContext {

	public CHBDirContext(String appName) {
		super(Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + appName);
	}

	@Override
	protected List<Directory> initDirectories() {
		ArrayList<Directory> children = new ArrayList<Directory>();

		AddChild(children, DirType.CACHE, "cache");
		AddChild(children, DirType.PICTURE, "picture");
		AddChild(children, DirType.TEMP, "temp");
		AddChild(children, DirType.CRASH, "crash");
		AddChild(children, DirType.DB, "db");
		AddChild(children, DirType.DOWNLOAD, "download");

		return children;
	}

	private Directory AddChild(ArrayList<Directory> children, DirType type, String name) {
		Directory child = new Directory();
		child.setType(type);
		child.setName(name);
		children.add(child);
		return child;
	}
}
