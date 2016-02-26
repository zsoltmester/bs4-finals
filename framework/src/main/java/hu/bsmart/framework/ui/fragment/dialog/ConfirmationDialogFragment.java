package hu.bsmart.framework.ui.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import hu.bsmart.framework.R;

/**
 * A custom {@code DialogFragment} that asks for confirmation.
 * <p/>
 * The dialog's question can be customized.
 */
public class ConfirmationDialogFragment extends BaseDialogFragment {
	private static final String KEY_QUESTION_STRING = "question_string";
	private static final String KEY_QUESTION_RESOURCE = "question_resource";
	private static final String KEY_CONFIRMATION_ID = "confirmation_id";

	/**
	 * Listener for getting notified about successful confirmation.
	 */
	public interface ConfirmationListener {
		/**
		 * Called when the user confirmed the action.
		 */
		void onConfirmationDone(String confirmationId);
	}

	/**
	 * Creates an {@code ConfirmationDialogFragment} that will
	 * show the given question.
	 *
	 * @param confirmationId a unique ID for the confirmation request
	 * @param question       the question to show
	 * @throws NullPointerException if confirmation ID or question is null
	 */
	@NonNull
	public static ConfirmationDialogFragment newInstance(@NonNull String confirmationId, @NonNull String question) {
		Bundle arguments = createBundleForConfirmationId(confirmationId);

		if (question == null) {
			throw new NullPointerException("You must supply a question!");
		}

		ConfirmationDialogFragment result = new ConfirmationDialogFragment();
		arguments.putString(KEY_QUESTION_STRING, question);
		result.setArguments(arguments);
		return result;
	}

	/**
	 * Creates an {@code ConfirmationDialogFragment} that will
	 * show the given question.
	 *
	 * @param confirmationId   a unique ID for the confirmation request
	 * @param questionResource string resource of the the question to show
	 * @return the new {@code ConfirmationDialogFragment}
	 *
	 * @throws NullPointerException if confirmation ID is null
	 */
	@NonNull
	public static ConfirmationDialogFragment newInstance(@NonNull String confirmationId,
			@StringRes int questionResource) {
		Bundle arguments = createBundleForConfirmationId(confirmationId);
		ConfirmationDialogFragment result = new ConfirmationDialogFragment();
		arguments.putInt(KEY_QUESTION_RESOURCE, questionResource);
		result.setArguments(arguments);
		return result;
	}

	private static Bundle createBundleForConfirmationId(@NonNull String confirmationId) {
		if (confirmationId == null) {
			throw new NullPointerException("You must supply a confirmation ID!");
		}

		Bundle result = new Bundle();
		result.putString(KEY_CONFIRMATION_ID, confirmationId);
		return result;
	}

	private String confirmationId;
	private ConfirmationListener confirmationListener;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		confirmationId = getArguments().getString(KEY_CONFIRMATION_ID);

		String question = getArguments().getString(KEY_QUESTION_STRING);
		if (question == null) {
			question = getString(getArguments().getInt(KEY_QUESTION_RESOURCE, 0));
		}

		DialogInterface.OnClickListener yesButtonListener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				synchronized (ConfirmationDialogFragment.this) {
					if (confirmationListener != null) {
						confirmationListener.onConfirmationDone(confirmationId);
					}
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.confirmation_dialog_title)
				.setMessage(question)
				.setPositiveButton(R.string.confirmation_dialog_yes_button, yesButtonListener)
				.setNegativeButton(R.string.confirmation_dialog_no_button, null);
		builder.setCancelable(true);
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	/**
	 * Sets the confirmation listener for this dialog.
	 *
	 * @param confirmationListener the new listener
	 */
	public synchronized void setConfirmationListener(ConfirmationListener confirmationListener) {
		this.confirmationListener = confirmationListener;
	}
}
