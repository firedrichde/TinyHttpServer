package http;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lordWang
 * @version 1.0
 * @date 2020/4/24 14:22
 */
public class Response {
    private String protocol;
    private StatusCode statusCode;
    private Map<String, String> headerMap;
    private OutputStream outputStream;
    private Request request;
    private String content;

    public Response(Request request) {
        this.request = request;
        this.headerMap = new HashMap<>();
        this.outputStream = this.request.getOutputStream();
        this.protocol = request.getProtocol();
    }

    public void setParameter(String key,String value){
        headerMap.put(key,value);
    }

    public void setStatusCode(StatusCode statusCode){
        this.statusCode = statusCode;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResponseHeader(){
        StringBuilder headers = new StringBuilder();
        headers.append(getProtocol()).append(" ").append(statusCode.getNumber()).append(" ").append(statusCode.getDescription()).append("\n");
        for (Map.Entry entry :
                headerMap.entrySet()) {
            headers.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        headers.append("\n");
        return headers.toString();
    }

    public void responseContent(){

    }


    public static class ResponseException extends Exception{
        public ResponseException() {
        }

        public ResponseException(String message) {
            super(message);
        }
    }

}
