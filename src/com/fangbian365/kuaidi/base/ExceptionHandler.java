package com.fangbian365.kuaidi.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fangbian365.kuaidi.base.log.FrameLog;
import com.fangbian365.kuaidi.base.utils.SysInfo;
import com.fangbian365.kuaidi.mod.dirmanager.DirType;

/**
 * 
 * @author liuruijun
 *
 */

public final class ExceptionHandler implements UncaughtExceptionHandler {
	private static final String TAG = "ExceptionHandler";

	private String mCrashLogPath;
	private static final boolean EASY_READ = true; // 打开的话\n会被替换成\r\n，方便windows同学打开阅读，OOM时候可能没内存操作，全面发布关闭
	private SimpleDateFormat mDateFormat;
	private static StringBuilder logBuilder = new StringBuilder(2 * 1024);
	
	public ExceptionHandler() {
		mCrashLogPath = SysInfo.getDirManager().getDirPath(DirType.CRASH);
		mDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			System.gc();
			Thread.sleep(1000);
			String logString = throwable2String(ex);
			saveErrorLog(logString);
		} catch (Throwable e) {
			// amen
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	private void saveErrorLog(String log) {
		
		String path = mCrashLogPath + File.separator + mDateFormat.format(new Date()) + ".txt";

		if (EASY_READ) {
			log = log.replaceAll("\n", "\r\n");
		}

		final String splitter;
		if (EASY_READ) {
			splitter = "\r\n";
		} else {
			splitter = "\n";
		}

		try {
			logBuilder.append(splitter).append(log);
		} catch (Throwable e) {
			// ...
		}

		try {
			FileOutputStream fos = new FileOutputStream(new File(path));
			try {
				writeString(fos, logBuilder.toString());
			} finally {
				fos.close();
			}

			logBuilder.delete(0, logBuilder.length());
		} catch (Throwable e) {
			// OMG，kill me！
		}
	}

	private String throwable2String(final Throwable tr) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			tr.printStackTrace(pw);
			FrameLog.e(TAG, sw.toString());
			return sw.toString();
		} catch (Throwable e) {
		}
		return "No Memory, throwable2String failed";
	}

	private void writeString(OutputStream out, String s) throws IOException {
		out.write(s.getBytes());
		out.flush();
	}
}
