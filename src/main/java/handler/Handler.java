package handler;

import http.Request;
import http.Response;

/**
 * @author lordWang
 * @version 1.0
 * @date 2020/4/24 14:22
 */
public interface Handler {
    /**
     *
     * @param request http request
     * @return http response
     */
    public Response handle(Request request);

    public void makeResponse(Response response);

    public String getName();
}
