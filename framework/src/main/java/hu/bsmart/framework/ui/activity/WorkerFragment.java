package hu.bsmart.framework.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;

import hu.bsmart.framework.communication.networkhelper.CommunicationError;
import hu.bsmart.framework.communication.networkhelper.NetworkHelper;
import hu.bsmart.framework.communication.networkhelper.NetworkResultListener;
import hu.bsmart.framework.communication.request.NetworkRequest;
import hu.bsmart.framework.communication.response.NetworkResponse;
import hu.bsmart.framework.handler.PauseHandler;
import hu.bsmart.framework.logging.Logger;
import hu.bsmart.framework.logging.LoggerProvider;

public class WorkerFragment extends Fragment {

	public interface RequestStateListener {
		void onRequestStarted(String requestCode, NetworkRequest request);

		void onRequestSucceeded(String requestCode, NetworkResponse response);

		void onRequestFailed(String requestCode, CommunicationError error);
	}

	/**
	 * {@link NetworkResultListener} used for being notified about the results
	 */
	private NetworkResultListener networkListener;

	/**
	 * Handler that runs on the UI thread
	 */
	private PauseHandler mainThreadHandler;

	private RequestStateListener requestStateListener;

	private Logger logger;

	/**
	 * {@link Runnable} class that is responsible for notifying
	 * the actual {@link Activity} about the starting of a request
	 */
	private class StartRunnable implements Runnable {
		private String requestCode;
		private NetworkRequest request;

		public StartRunnable(String requestCode, NetworkRequest request) {
			this.requestCode = requestCode;
			this.request = request;
		}

		@Override
		public void run() {
			if (requestStateListener != null) {
				requestStateListener.onRequestStarted(requestCode, request);
			}
		}
	}

	/**
	 * {@link Runnable} class that is responsible for notifying
	 * the actual {@link Activity} about the success
	 */
	private class SuccessRunnable implements Runnable {

		private String requestCode;
		private NetworkResponse response;

		public SuccessRunnable(String requestCode, NetworkResponse response) {
			this.requestCode = requestCode;
			this.response = response;
		}

		@Override
		public void run() {
			if (requestStateListener != null) {
				requestStateListener.onRequestSucceeded(requestCode, response);
			}
		}
	}

	/**
	 * {@link Runnable} class that is responsible for notifying
	 * the actual {@link Activity} about the failure
	 */
	private class FailRunnable implements Runnable {
		private String requestCode;
		private CommunicationError error;

		public FailRunnable(String requestCode, CommunicationError error) {
			this.requestCode = requestCode;
			this.error = error;
		}

		@Override
		public void run() {
			if (requestStateListener != null) {
				requestStateListener.onRequestFailed(requestCode, error);
			}
		}
	}

	public WorkerFragment() {
	}

	/**
	 * Creates a worker Fragment.
	 *
	 * @return the Fragment instance
	 */
	public static Fragment newInstance() {
		WorkerFragment result = new WorkerFragment();
		return result;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mainThreadHandler = new PauseHandler(Looper.getMainLooper());

		networkListener = new NetworkResultListener() {
			@Override
			public void onSuccess(String requestCode, NetworkResponse response) {
				mainThreadHandler.post(new SuccessRunnable(requestCode, response));
			}

			@Override
			public void onFail(String requestCode, CommunicationError error) {
				mainThreadHandler.post(new FailRunnable(requestCode, error));
			}
		};

		logger = LoggerProvider.createLoggerWithTag("WORKER");
	}

	@Override
	public void onPause() {
		super.onPause();
		mainThreadHandler.pause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mainThreadHandler.resume();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof RequestStateListener)) {
			throw new IllegalStateException("This fragment should have a RequestStateListener!");
		}
		this.requestStateListener = (RequestStateListener) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.requestStateListener = null;
	}

	/**
	 * Starts a network request based on the provided {@link NetworkRequest} object.
	 *
	 * @param requestCode the ID for the request
	 * @param request     the request object
	 */
	protected final void startNetworkRequest(String requestCode, NetworkRequest request) {
		mainThreadHandler.post(new StartRunnable(requestCode, request));
		NetworkHelper.getInstance().startRequest(requestCode, request, networkListener);
	}

}
