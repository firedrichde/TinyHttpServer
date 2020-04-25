package http;

import handler.FileDownLoadHandler;
import handler.Handler;
import handler.NotFoundHandler;
import util.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lordWang
 * @version 1.0
 * @date 2020/4/24 14:24
 */
public class Connection implements Runnable {
    private static Logger LOGGER = Logger.getInstance("./log/log.log");


    private Socket socket;
    private Request request;
    private Response response;
    private Map<String, Handler> handlerMap;
    private Handler handler;

    public Connection(Socket socket, Map<String, Handler> handlerMap) {
        this.socket = socket;
        this.handler = null;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            request = new Request(inputStream, outputStream);
            handler = choiceHandler();
            response = handler.handle(request);
            handler.makeResponse(response);
            LOGGER.log(String.format("%s from %s, response by %s, %d",request.getRequestLine()!=null?request.getRequestLine():"",
                    socket.getInetAddress().getHostAddress(),handler.getClass().getCanonicalName(),response.getStatusCode().getNumber()));
        } catch (IOException e) {
            LOGGER.log(e.toString());
        } finally {
            close(inputStream, outputStream, socket);
        }
    }

    public Handler choiceHandler() {
        try {
            request.parse();
            Handler handler = handlerMap.get(request.getRelativeUrl());
            if (handler == null) {
                for (String key:
                     handlerMap.keySet()) {
                    Pattern pattern = Pattern.compile(key);
                    Matcher matcher = pattern.matcher(request.getRelativeUrl());
                    if(matcher.matches()){
                        LOGGER.log(String.format("%s match %s",key,request.getRelativeUrl()));
                        return handlerMap.get(key);
                    }
                }
                return new NotFoundHandler();
            } else {
                return handler;
            }

        } catch (Request.RequestParseExcetion e) {
            return new NotFoundHandler();
        }
    }


    public void close(Closeable... closeables) {
        for (Closeable c :
                closeables) {
            try {
                c.close();
            } catch (IOException e) {
                LOGGER.log(e.toString());
            }
        }
    }
}
