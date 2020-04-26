package handler;

import http.Request;
import http.Response;
import http.StatusCode;
import util.Logger;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author lordWang
 * @version 1.0
 * @date 2020/4/25 10:32
 */
public class FileDownLoadHandler implements Handler {
    private static String FILE_RESOURCE_DIR = "./source";
    private static Logger LOGGER = Logger.getInstance("./log/log.log");

    private File file;

    private String name ="FileDownLoadHandler";
    public FileDownLoadHandler() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response(request);
        String url = request.getRelativeUrl();
        response.setStatusCode(StatusCode.OK);
        response.setParameter("Connection", "close");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        response.setParameter("Date", dateFormat.format(date));
        file = new File(FILE_RESOURCE_DIR + url);
        response.setParameter("Content-Length", String.valueOf(file.length()));
        response.setParameter("Content-Type", "application/zip");
        return response;
    }

    @Override
    public void makeResponse(Response response) {
        OutputStream outputStream = response.getOutputStream();
        InputStream inputStream = null;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        String headers = response.getResponseHeader();
        try {
            inputStream = new FileInputStream(file);
            writer.write(headers, 0, headers.length());
            writer.flush();
            int ch;
            while ((ch=inputStream.read())!=-1){
                outputStream.write(ch);
            }
            LOGGER.log(String.format("transport a file %s",file.getCanonicalPath()));
        }catch (IOException e){
            LOGGER.log(e.toString());
        }
    }

}
