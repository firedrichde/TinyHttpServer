package http;

/**
 * @author lordWang
 * @version 1.0
 * @date 2020/4/24 14:27
 */
public enum StatusCode {
    /**
     *
     */
    OK(200, "ok"),
    NOT_MODIFIED(304, "not modified"),
    NOT_FOUND(404, "not found");
    private int number;
    private String description;

    StatusCode(int number, String description) {
        this.number = number;
        this.description = description;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatusCode{");
        sb.append(number);
        sb.append(": '").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }}
