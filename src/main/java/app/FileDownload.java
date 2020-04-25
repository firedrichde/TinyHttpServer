package app;

import handler.FileDownLoadHandler;
import http.Server;
import util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * when the http url matchers the regex pattern "/file/\\w+\\.(zip|txt)",the server will transport the file that stored
 * in the /source/file directory. if the file is not existed, the server will not send the response.
 * Usage: java -jar TinyHttpServer port [aliveTime]
 * the unit of aliveTime is Second
 * if the aliveTime expired, the server will stop.
 *
 * @author lordWang
 * @version 1.0
 * @date 2020/4/25 15:15
 */

public class FileDownload {
    private static String FILE_DOWNLOAD_PATTERN = "/file/\\w+\\.(zip|txt)";

    public static void main(String[] args) {
        String help = "Usage: java -jar TinyHttpServer port [aliveTime]";
        if ("-h".equals(args[0]) || "--help".equals(args[0]) || args.length < 1) {
            System.out.println(help);
            System.exit(1);
        } else {
            int port = 9090;
            long time = 6;
            try {
                port = Integer.parseInt(args[0]);
                if (args.length >= 2) {
                    time = Long.parseLong(args[1]);
                }
            } catch (NumberFormatException e) {
                System.out.println(help);
                System.exit(1);
            }
            int corePoolSize = 2;
            int maxPoolSize = 10;
            long keepAliveTime = 10;
            ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
            Server server = new Server(port, executorService,(int)time*1000);
            server.addHandler(FILE_DOWNLOAD_PATTERN, new FileDownLoadHandler());
            System.out.println("Server is starting...");
            executorService.execute(server);
            try {
                TimeUnit.SECONDS.sleep(time);
                executorService.shutdownNow();
            } catch (InterruptedException e) {
                System.out.println("server stop earlier than expected");
            }finally {
                System.out.println("Server has stopped");
                Logger.close();
                System.exit(0);
            }

        }
    }
}
