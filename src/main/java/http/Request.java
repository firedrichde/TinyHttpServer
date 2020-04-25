package http;


import util.Logger;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lordWang
 * @version 1.0
 * @date 2020/4/24 14:22
 */
public class Request {
    private static Logger LOGGER = Logger.getInstance("./log/log.log");
    /**
     * regex pattern for request line, such as "GET /index.html HTTP/1.1"
     */
    private static Pattern REQUEST_LINE_PATTERN = Pattern.compile("([a-zA-Z]+)\\s+(/\\S*)\\s+([A-Z]+/\\d\\.\\d+)");
    /**
     * regex pattern for header line, such as "Connection: close"
     */
    private static Pattern HEADER_LINE_PATTERN = Pattern.compile("(\\S+):(.+)");

    private RequestMethod requestMethod;
    private String protocol;
    private String relativeUrl;
    private List<String> messageList;

    /**
     * entity content when the client uses 'POST' request method
     */
    private List<String> content;
    private InputStream inputStream;

    private OutputStream outputStream;

    /**
     * header line parameter map
     */
    private Map<String, String> headerMap;
    private boolean valid;

//    public Request(String header) {
//        this.header = header;
//        this.inputStream = null;
//    }

    public Request(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.messageList = new ArrayList<>();
        this.headerMap = new HashMap<String, String>();
        this.content = new ArrayList<>();
        this.outputStream = outputStream;
    }

    public void parse() throws RequestParseExcetion {
        if (inputStream != null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                boolean contentArrival = false;
                char[] buffer = new char[1024];
                // if use read() method, then read will be blocked when the header is received
                int offset = reader.read(buffer,0,1024);
                String tmp = new String(buffer,0,offset);
                for(String s:tmp.split("\r\n")){
                    messageList.add(s);
                }
//                if (!contentArrival) {
//                    throw new RequestParseExcetion("no empty line after header lines");
//                }
            } catch (IOException e) {
                LOGGER.log(String.format("%s happened when %s read line", e.getMessage(), reader.toString()));
            }
            for (int i = 0; i < messageList.size(); i++) {
                String msg = messageList.get(i);
                Matcher matcher = null;
                if (i == 0) {
                    matcher = REQUEST_LINE_PATTERN.matcher(msg);
                    if (matcher.matches()) {
                        String method = matcher.group(1);
                        relativeUrl = matcher.group(2);
                        protocol = matcher.group(3);
                        for (RequestMethod rm :
                                RequestMethod.values()) {
                            if (rm.getName().equals(method.toUpperCase())) {
                                requestMethod = rm;
                                break;
                            }
                        }
                        if (requestMethod == null) {
                            throw new RequestParseExcetion(String.format("request method '%s' is illegal", method));
                        }
                    } else {
                        throw new RequestParseExcetion(String.format("request line '%s' is illegal", msg));
                    }
                } else {
                    matcher = HEADER_LINE_PATTERN.matcher(msg);
                    if (matcher.matches()) {
                        String headerKey = matcher.group(1).trim();
                        String headerVal = matcher.group(2).trim();
                        headerMap.put(headerKey, headerVal);
                    } else {
                        throw new RequestParseExcetion(String.format("header line '%s' is illegal", msg));
                    }
                }
            }
            valid = true;
        }
    }

    public String getParameter(String key) {
        if (headerMap.isEmpty()) {
            return null;
        }
        String parameter = headerMap.get(key);
        return parameter;
    }

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public boolean isValid() {
        return valid;
    }

    public String getProtocol() {
        return protocol;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public String getRequestLine(){
        String firstline = messageList.get(0);
        return firstline;
    }

    public static class RequestParseExcetion extends Exception {
        public RequestParseExcetion() {
        }

        public RequestParseExcetion(String message) {
            super(message);
        }
    }

}
