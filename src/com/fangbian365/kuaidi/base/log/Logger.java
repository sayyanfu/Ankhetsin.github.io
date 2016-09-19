package com.fangbian365.kuaidi.base.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.os.Process;
import android.util.Log;

public class Logger {
	 /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;
    
    private Logger(final String fileName, int level) {
    	this.mLogFileName = fileName ;
    	this.mLevel = level;
    }
    
    public static Logger getLogger(final String fileName) {
    	return getLogger(fileName, INFO);
    }
    private static Map<String, Logger> sLoggers = null;
    public synchronized static Logger getLogger(final String fileName, int level) {
    	
    	if (fileName == null || fileName.length() == 0 || level <VERBOSE || level > ASSERT ) {
    		throw new IllegalArgumentException("invalid parameter fileName or level");
    	}
    	
    	Logger logger = null;
    	if (sLoggers == null ) {
    		sLoggers = new HashMap<String, Logger>(); 
    	} else {
    		logger = sLoggers.get(fileName);
    	}
    	
    	if ( logger == null) {
    		logger = new Logger(fileName, level);
    		sLoggers.put(fileName, logger);
    	}
    	return logger;
    }
    
    private int mLevel = INFO;
    protected  boolean mTrace = false;
    
    protected void traceOff() {
    	mTrace = false;
    }
    
    protected void traceOn() {
    	mTrace = true;
    }
    
    public boolean isTracing() {
    	return mTrace;
    }
    
    public void setLevel(int level) {
    	this.mLevel = level;
    }
    
    public  int v(String tag, String msg) {
    	return mTrace? Log.v(tag,msg) : println(VERBOSE, tag, msg);
    }
    
    public  int v(String tag, Throwable tr) {
    	return v( tag, getStackTraceString(tr));
    }
    
    public int d(String tag, String msg) {
    	return mTrace? Log.d(tag,msg) : println(DEBUG, tag, msg);
    }
    
    public  int d(String tag, Throwable tr) {
    	return d(tag, getStackTraceString(tr));
    }
    
    public int w(String tag, String msg) {
    	return mTrace? Log.w(tag,msg) : println(WARN, tag, msg);
    }
    
    public int w(String tag, Throwable tr) {
    	return w(tag, getStackTraceString(tr));
    }
    
    public int i(String tag, String msg) {
    	return mTrace? Log.i(tag,msg) : println(INFO, tag, msg);
    }
    
    public int i(String tag, Throwable tr) {
    	return i(tag, getStackTraceString(tr));
    }
    
    public int e(String tag, String msg) {
    	return mTrace? Log.e(tag,msg) : println(ERROR, tag, msg);
    }
    
    public int e(String tag, Throwable tr) {
    	return e(tag, getStackTraceString(tr));
    }
    
    public String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }
    
    private final Object mLogLock = new Object();    
    private String mLogFileName = null;
    
    private int println(int priority, String tag, String msg){    	
    	if (priority < mLevel){
    		return 0;
    	}
    	
    	String[] ps = {"","","V","D","I","W","E","A"};
    	SimpleDateFormat df = new SimpleDateFormat("[MM-dd hh:mm:ss.SSS]");
    	String time = df.format(new Date());
    	StringBuilder sb = new StringBuilder();
    	sb.append(time);
    	sb.append("\t");
    	sb.append(ps[priority]);
    	sb.append("/");
    	sb.append(tag);
    	int pid = Process.myPid();
    	sb.append("(");
    	sb.append(pid);
    	sb.append("):");
    	sb.append(msg);
    	sb.append("\n");
    	FileWriter writer = null;
    	
    	synchronized (mLogLock) {    		
			try {
				File file = new File(mLogFileName);
				// not exist
				if (!file.exists()) {
					file.createNewFile();
				}
				writer = new  FileWriter(file, true);
				writer.write(sb.toString());
			} catch (FileNotFoundException e) {
				return -1;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
					}
				}				
			}    		
		}
    	
    	return 0;
    }
}
