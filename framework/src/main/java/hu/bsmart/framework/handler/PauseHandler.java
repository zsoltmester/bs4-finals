package hu.bsmart.framework.handler;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PauseHandler extends Handler {

	public PauseHandler() {
	}

	public PauseHandler(Callback callback) {
		super(callback);
	}

	public PauseHandler(Looper looper) {
		super(looper);
	}

	public PauseHandler(Looper looper, Callback callback) {
		super(looper, callback);
	}

	private final List<Runnable> pendingRequests = Collections.synchronizedList(new ArrayList<Runnable>());

	private boolean isResumed = false;

	public synchronized void resume() {
		isResumed = true;

		while (pendingRequests.size() > 0) {
			post(pendingRequests.remove(0));
		}
	}

	public synchronized void pause() {
		isResumed = false;
	}

	public synchronized void postRequest(Runnable request) {
		if (isResumed) {
			post(request);
		} else {
			pendingRequests.add(request);
		}
	}

}
