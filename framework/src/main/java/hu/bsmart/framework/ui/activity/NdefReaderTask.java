package hu.bsmart.framework.ui.activity;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import hu.bsmart.framework.logging.Logger;
import hu.bsmart.framework.logging.LoggerProvider;

/**
 * AsyncTask that reads the records from scanned NFC tags and
 * provides the scanned data as plain text.
 */
class NdefReaderTask extends AsyncTask<Tag, Void, String> {

	/**
	 * Interface for handling result of NFC tag scanning.
	 */
	interface NfcResultHandler {
		/**
		 * This method is called when an NFC tag has been successfully scanned.
		 *
		 * @param data the data that has been read from the tag
		 */
		public void handleNfcResult(String data);
	}

	private final Logger logger;
	private final NfcResultHandler nfcResultHandler;

	NdefReaderTask(NfcResultHandler nfcResultHandler) {
		if (nfcResultHandler == null) {
			throw new NullPointerException("You need to supply a valid result handler!");
		}

		this.nfcResultHandler = nfcResultHandler;
		logger = LoggerProvider.createLoggerForClass(getClass());
	}

	@Override
	protected String doInBackground(Tag... params) {
		Tag tag = params[0];

		Ndef ndef = Ndef.get(tag);
		if (ndef == null) {
			return null;
		}

		NdefMessage ndefMessage = ndef.getCachedNdefMessage();

		NdefRecord[] records = ndefMessage.getRecords();
		for (NdefRecord ndefRecord : records) {
			if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN
					&& Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
				try {
					return readText(ndefRecord);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					logger.e("Failed to read text from NDEF record!", e);
				}
			}
		}

		return null;
	}

	private String readText(NdefRecord record)
			throws UnsupportedEncodingException {

		byte[] payload = record.getPayload();
		String textEncoding = ((payload[0] & 128) == 0) ? ("UTF-8") : ("UTF-16");

		int languageCodeLength = payload[0] & 0063;

		return new String(payload, languageCodeLength + 1, payload.length
				- languageCodeLength - 1, textEncoding);
	}

	@Override
	protected void onPostExecute(String result) {
		if (result != null) {
			nfcResultHandler.handleNfcResult(result);
		}
	}
}