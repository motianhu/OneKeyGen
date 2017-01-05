package com.smona.base.onekey.action;

import java.io.IOException;

import com.smona.base.onekey.util.CommonUtil;
import com.smona.base.onekey.util.Constants;

public class VBAction implements IAction {

	public void execute(String path) {
		initFolder(path);
		executeVBS();
	}

	private void initFolder(String path) {
		String cutPath = path + Constants.FILE_SEPARATOR + Constants.CUT_PATH;
		CommonUtil.deleteFolder(cutPath);
		CommonUtil.mkdir(cutPath);
	}

	private void executeVBS() {
		try {
			Runtime.getRuntime().exec("cscript VBScript.vbs");
		} catch (IOException e) {
			CommonUtil.println("ERROR[executeVBS] e=" + e);
			e.printStackTrace();
		}
	}
}
