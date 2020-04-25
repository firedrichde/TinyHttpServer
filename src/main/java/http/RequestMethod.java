package http;

/**
 * @author lordWang
 * @version 1.0
 * @date 2020/4/24 14:29
 */
public enum RequestMethod {
    /**
     * GET The GET method requests a representation of the specified resource
     * POST The POST method is used to submit an entity to the specified resource,
     *      often causing a change in state or side effects on the serve
     */
    GET("GET"),
    POST("POST");
    private String name;

    RequestMethod(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RequestMethod{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
