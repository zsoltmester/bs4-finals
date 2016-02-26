package hu.bsmart.framework.ui.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

/**
 * Delegate class for handling NFC related operations.
 */
class NfcDelegate {

	private FrameworkActivity activity;
	private NfcAdapter adapter;

	public NfcDelegate(FrameworkActivity activity) {
		this.activity = activity;
		this.adapter = NfcAdapter.getDefaultAdapter(activity);
	}

	/**
	 * Starts listening for NFC tags.
	 */
	public void startTrackingNfc() {
		if (adapter == null) {
			return;
		}

		final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent nfcPendingIntent =
				PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
		adapter.enableForegroundDispatch(activity, nfcPendingIntent, null, null);
	}

	/**
	 * Stops listening for NFC tags.
	 */
	public void stopTrackingNfc() {
		if (adapter == null) {
			return;
		}
		adapter.disableForegroundDispatch(activity);
	}

	public boolean handleNfcIntent(Intent intent) {
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()) ||
				NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			new NdefReaderTask(activity).execute(tag);
			return true;
		}

		return false;
	}

}
