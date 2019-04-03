package eu.tsystems.mms.tic.testframework.mobile;

/**
 * Created by rnhb on 04.02.2016.
 */
public class PageFactoryException extends RuntimeException{

    public PageFactoryException() {
    }

    public PageFactoryException(String message) {
        super(message);
    }

    public PageFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageFactoryException(Throwable cause) {
        super(cause);
    }

    public PageFactoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
