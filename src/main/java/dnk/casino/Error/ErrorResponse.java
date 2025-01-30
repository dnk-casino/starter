package dnk.casino.Error;

public class ErrorResponse {

    private String title;
    private String message;

    public ErrorResponse(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }
}
