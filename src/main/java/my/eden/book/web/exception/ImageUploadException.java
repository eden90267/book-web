package my.eden.book.web.exception;

/**
 * Created by eden90267 on 2016/5/28.
 */
@SuppressWarnings("serial")
public class ImageUploadException extends RuntimeException {

    public ImageUploadException(String message) {
        super(message);
    }

    public ImageUploadException(String message, Throwable cause) {
        super(message, cause);
    }

}