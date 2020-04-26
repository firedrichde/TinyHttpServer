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
import java.util.Map;

/**
 * @author lordWang
 * @version 1.0
 * @date 2020/4/24 19:44
 */
public class NotFoundHandler implements Handler {
    private static Logger LOGGER = Logger.getInstance("./log/log.log");

    private String name = "NotFoundHandler";
    public NotFoundHandler() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response(request);
        response.setStatusCode(StatusCode.NOT_FOUND);
        response.setParameter("Connection", "close");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        response.setParameter("Date", dateFormat.format(date));
        StringBuilder content = new StringBuilder();
        content.append("<html>");
        content.append("<head>");
        content.append("<title>Not Found</title>");
        content.append("</head>");
        content.append("<body>Page not found</body>");
        content.append("</html>");
        String contentMsg = content.toString();
        response.setContent(contentMsg);
        response.setParameter("Content-Length", String.valueOf(contentMsg.length()));
        response.setParameter("Content-Type", "text/html;charset=UTF-8");
        return response;
    }

    @Override
    public void makeResponse(Response response) {
        OutputStream outputStream = response.getOutputStream();
        BufferedWriter writer = null;
        try{
        writer = new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));
        }catch (UnsupportedEncodingException e){
            LOGGER.log(e.toString());
        }
        StringBuilder headers = new StringBuilder();
        Map<String, String> headerMap = response.getHeaderMap();
        StatusCode statusCode = response.getStatusCode();
        headers.append(response.getProtocol()).append(" ").append(statusCode.getNumber()).append(" ").append(statusCode.getDescription()).append("\n");
        for (Map.Entry entry :
                headerMap.entrySet()) {
            headers.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        headers.append("\n");

        String headerMsg = headers.toString();
        String contentMsg = response.getContent();
        try {
            writer.write(headerMsg, 0, headerMsg.length());
            writer.write(contentMsg, 0, contentMsg.length());
            writer.flush();
        } catch (IOException e) {
            LOGGER.log(e.toString());
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                LOGGER.log(e.toString());
            }
        }
    }
}
