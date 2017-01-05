package com.smona.base.onekey.util;

import java.io.File;

public class CommonUtil {

	public static void deleteFolder(String path) {
		File dirFile = new File(path);
		FileOperator.deleteDirectory(dirFile);
	}

	public static void mkdir(String target) {
		File tempDir = new File(target);
		tempDir.mkdir();
	}

	public static void println(String msg) {
		Logger.printDetail(msg);
	}
}
