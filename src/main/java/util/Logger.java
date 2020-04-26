package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Logger class is designed to record information of the http server
 * @author lordWang
 * @version 1.0
 * @date 2020/4/5 21:02
 */
public class Logger {
    /**
     * FILE_MAX_LENGTH 10KB
     */
    private static int FILE_MAX_LENGTH = 10*0x1000;
    private static Logger LOGGER;

    /** the logger pool*/
    private static HashMap<String, Logger> LOGGER_MAP;
    private static Lock LOCK = new ReentrantLock();

    public static Logger getInstance() {
        return getInstance("");
    }

    public static Logger getInstance(String filename) {
        Logger item = LOGGER_MAP.get(filename);
        if (item != null) {
            LOGGER = item;
        } else {
            LOGGER = new Logger(filename);
            LOGGER_MAP.put(filename, LOGGER);
        }
        return LOGGER;
    }

    static {
        LOGGER_MAP = new HashMap<>();
    }

    private PrintStream out;

    private DateFormat dataFormat;

    private Logger() {
        out = System.out;
        dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
    }

    private Logger(String filename) {
        this();
        if (!filename.equals("")) {
            try {
                File file = new File(filename);
                if(file.exists() && file.length()>FILE_MAX_LENGTH) {
                    String oldfilename = file.getAbsolutePath()+".1";
                    file.renameTo(new File(oldfilename));
                }
                out = new PrintStream(new FileOutputStream(filename, true));
            } catch (FileNotFoundException e) {
                System.err.println("file: " + filename + " not found");
            }
        }
    }

    public void log(String msg) {
        LOCK.lock();
        try{
        StringBuilder sb = new StringBuilder();
        Date date = new Date();
        sb.append(dataFormat.format(date));
        sb.append("  ");
        sb.append("nu " + Thread.currentThread().getStackTrace()[2].getLineNumber());
        sb.append(" [" + Thread.currentThread().getName() + "]");
        sb.append(" : ");
        sb.append(msg);
        out.println();
        out.print(sb.toString());
        }finally {
            LOCK.unlock();
        }
    }

    public static void close() {
        LOGGER.out.flush();
        LOGGER.out.close();
    }
}
