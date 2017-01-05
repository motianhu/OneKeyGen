package com.smona.base.onekey;

import com.smona.base.onekey.action.IAction;
import com.smona.base.onekey.action.JavaAction;
import com.smona.base.onekey.action.VBAction;
import com.smona.base.onekey.util.CommonUtil;
import com.smona.base.onekey.util.Logger;

public class Main {

	public static void main(String[] args) {
		String encode = System.getProperty("file.encoding");
		CommonUtil.println(encode);
		Logger.init();
		String path = System.getProperty("user.dir");
		CommonUtil.println(path);

		action(args == null || args.length == 0 ? "cut" : args[0], path);
	}

	private static void action(String cmd, String path) {
		IAction action = null;
		if ("cut".equals(cmd)) {
			action = new VBAction();
			action.execute(path);
		} else if ("zip".equals(cmd)) {
			action = new JavaAction();
			action.execute(path);
		}
	}

}
