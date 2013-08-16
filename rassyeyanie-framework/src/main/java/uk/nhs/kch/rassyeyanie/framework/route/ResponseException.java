package uk.nhs.kch.rassyeyanie.framework.route;

public class ResponseException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResponseException(String s) {
        super(s);
    }

    public ResponseException(Throwable throwable) {
        super(throwable);
    }
}
