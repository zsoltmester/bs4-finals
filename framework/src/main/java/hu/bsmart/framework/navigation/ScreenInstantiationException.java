package hu.bsmart.framework.navigation;

/**
 * Runtime exception indicating something serious happened during
 * screen instantiation.
 */
public class ScreenInstantiationException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public ScreenInstantiationException() {
	}

	public ScreenInstantiationException(String msg) {
		super(msg);
	}
}