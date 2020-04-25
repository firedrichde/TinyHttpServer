package http;

import handler.FileDownLoadHandler;
import handler.Handler;
import util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lordWang
 * @version 1.0
 * @date 2020/4/24 14:23
 */
public class Server implements Runnable {
    private static Logger LOGGER = Logger.getInstance("./log/log.log");

    private int port;
    private Map<String, Handler> handlerMap;
    /**
     * Key : url describe by regex express, Value: handler
     */
//    private Map<String,Handler> handlerMapRegexUrl;
    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private int timeout;

    public Server(int port, ExecutorService executorService,int timeout) {
        this.port = port;
        this.executorService = executorService;
        this.serverSocket = null;
        this.handlerMap = new HashMap<>();
        this.timeout = timeout;
//        this.handlerMapRegexUrl = new HashMap<>();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port, 10);
            serverSocket.setSoTimeout(timeout);
            while (!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                executorService.execute(new Connection(socket, handlerMap));
            }
        } catch (IOException e) {
            LOGGER.log(e.toString());
        }
    }

    public void addHandler(String url, Handler handler) {
        handlerMap.put(url, handler);
    }


//    public static void main(String[] args){
//        ExecutorService executorService = new ThreadPoolExecutor(10,10,0, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());
//        Server server = new Server(9090,executorService);
//        server.addHandler("/hello.zip",new FileDownLoadHandler());
//        executorService.execute(server);
//        try{
//            TimeUnit.SECONDS.sleep(300);
//            executorService.shutdownNow();
//        }catch (InterruptedException e){
//
//        }
//        System.exit(0);
//
//    }
}
