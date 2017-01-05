package com.smona.base.onekey.action;

import java.io.File;
import java.util.HashMap;

import com.smona.base.onekey.util.CommonUtil;
import com.smona.base.onekey.util.Constants;
import com.smona.base.onekey.util.FileOperator;
import com.smona.base.onekey.util.ZipFileAction;

public class JavaAction implements IAction {
	private final String FUBEN = "拷贝";
	// private final String FUBEN = "副本";
	private String[] fileSuffix = new String[] { " " + FUBEN + "",
			" " + FUBEN + " 2", " " + FUBEN + " 3", " " + FUBEN + " 4",
			" " + FUBEN + " 5", " " + FUBEN + " 6", " " + FUBEN + " 7",
			" " + FUBEN + " 8", " " + FUBEN + " 9", " " + FUBEN + " 10",
			" " + FUBEN + " 11" };

	HashMap<String, String> mFileFixs = new HashMap<String, String>();
	{
		mFileFixs.put(fileSuffix[0], "720x1280");
		mFileFixs.put(fileSuffix[1], "540x960");
		mFileFixs.put(fileSuffix[2], "480x854");
		mFileFixs.put(fileSuffix[3], "360x640");
		mFileFixs.put(fileSuffix[4], "350x625");
		mFileFixs.put(fileSuffix[5], "270x480");
		mFileFixs.put(fileSuffix[6], "240x427");
		mFileFixs.put(fileSuffix[7], "180x320");
		mFileFixs.put(fileSuffix[8], "160x285");
		mFileFixs.put(fileSuffix[9], "106x190");
		mFileFixs.put(fileSuffix[10], "thumbnail"); // 90x160
	}

	private HashMap<String, String> mFolders = new HashMap<String, String>();
	private int mPicNameLength = -1;

	public void execute(String path) {
		initFolder(path);
		makeReslutionFolders(path);
		copyFiles(path);
		zipFile(path);
	}

	private void initFolder(String path) {
		String copyPath = path + Constants.FILE_SEPARATOR + Constants.COPY_PATH;
		String targetPath = path + Constants.FILE_SEPARATOR
				+ Constants.TARGET_PATH;

		CommonUtil.deleteFolder(copyPath);
		CommonUtil.mkdir(copyPath);
		CommonUtil.deleteFolder(targetPath);
		CommonUtil.mkdir(targetPath);
	}

	private void makeReslutionFolders(String path) {
		String sourcePath = path + Constants.FILE_SEPARATOR
				+ Constants.SOURCE_PATH;
		String copyPath = path + Constants.FILE_SEPARATOR + Constants.COPY_PATH;

		String xmlPath = path + Constants.FILE_SEPARATOR + Constants.XML_PATH;
		File xml = new File(xmlPath + Constants.FILE_SEPARATOR
				+ Constants.XML_TEMPLATE);

		File properties = new File(xmlPath + Constants.FILE_SEPARATOR
				+ Constants.PROPERTIES_TEMPLATE);

		File source = new File(sourcePath);
		File[] picNames = source.listFiles();
		String folderName = null;

		int index = -1;
		String picName = null;
		for (File picFile : picNames) {
			picName = picFile.getName();
			index = picName.indexOf(Constants.DONET);
			if (index != -1) {
				folderName = picName.substring(0, index);
				CommonUtil.mkdir(copyPath + Constants.FILE_SEPARATOR
						+ folderName);
				copyFile(picFile, copyPath + Constants.FILE_SEPARATOR
						+ folderName + Constants.FILE_SEPARATOR + folderName
						+ Constants.JPG);
				copyFile(picFile, copyPath + Constants.FILE_SEPARATOR
						+ folderName + Constants.FILE_SEPARATOR + folderName
						+ Constants.FONT + Constants.JPG);
				if (xml.exists()) {
					copyFile(xml, copyPath + Constants.FILE_SEPARATOR
							+ folderName + Constants.FILE_SEPARATOR
							+ folderName + Constants.XML);
				}
				if (properties.exists()) {
					copyFile(properties, copyPath + Constants.FILE_SEPARATOR
							+ folderName + Constants.FILE_SEPARATOR
							+ folderName + Constants.PROPERTIES);
				}
				mFolders.put(folderName, folderName);
				mPicNameLength = folderName.length();
			}
		}
	}

	private void copyFiles(String path) {
		if (mPicNameLength < 0) {
			return;
		}
		String cutPath = path + Constants.FILE_SEPARATOR + Constants.CUT_PATH;
		String copyPath = path + Constants.FILE_SEPARATOR + Constants.COPY_PATH;

		File source = new File(cutPath);
		File[] picNames = source.listFiles();
		String folderName = null;
		for (File picName : picNames) {
			folderName = getFolderName(picName.getName());
			if (folderName != null) {
				copyJpgAndFolder(copyPath, folderName, picName);
			}
		}
	}

	private String getFolderName(String fileName) {
		int index = fileName.indexOf(" ");
		String folderName = null;
		if (index != -1) {
			folderName = fileName.substring(0, index);
		} else {
			folderName = fileName.substring(0, fileName.length() - 1);
		}
		return mFolders.get(folderName);
	}

	private void copyJpgAndFolder(String dirPath, String folderName,
			File picFile) {
		String fileFix = mFileFixs.get(picFile.getName().substring(
				folderName.length(), picFile.getName().length() - 4));
		String relution = dirPath + Constants.FILE_SEPARATOR + folderName
				+ Constants.FILE_SEPARATOR + fileFix;
		CommonUtil.mkdir(relution);

		copyFile(picFile, relution + Constants.FILE_SEPARATOR + folderName
				+ Constants.JPG);

		copyFile(picFile, relution + Constants.FILE_SEPARATOR + folderName
				+ Constants.FONT + Constants.JPG);
	}

	private void copyFile(File file, String dest) {
		FileOperator.fileChannelCopy(file, dest);
	}

	private void zipFile(String path) {
		String copyPath = path + Constants.FILE_SEPARATOR + Constants.COPY_PATH;
		String targetPath = path + Constants.FILE_SEPARATOR
				+ Constants.TARGET_PATH;

		CommonUtil.deleteFolder(targetPath);
		CommonUtil.mkdir(targetPath);

		File copy = new File(copyPath);
		File[] childs = copy.listFiles();
		ZipFileAction action = new ZipFileAction();
		for (File child : childs) {
			try {
				action.zip(child.getAbsolutePath(), targetPath
						+ Constants.FILE_SEPARATOR + child.getName()
						+ Constants.ZIP);
			} catch (Exception e) {
				CommonUtil.println(e.toString());
				e.printStackTrace();
			}
		}
	}
}
