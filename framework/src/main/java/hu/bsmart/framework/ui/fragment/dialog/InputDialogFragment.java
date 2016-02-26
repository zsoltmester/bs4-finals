package hu.bsmart.framework.ui.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import hu.bsmart.framework.R;

/**
 * A custom {@code DialogFragment} that shows a view for inputting data.
 * <p/>
 * The input view can be customized.
 */
public class InputDialogFragment extends BaseDialogFragment {
	public interface SubmitListener {
		/**
		 * Called when the user presses the OK button on the dialog.
		 * This method is responsible for validating the input data.
		 *
		 * @param inputRequestId  request ID for the input
		 * @param filledInputView the dialog's view, now filled with the input data
		 * @return {@code true} on successful validation, {@code false} on failure
		 */
		boolean onValidate(String inputRequestId, View filledInputView);

		/**
		 * Called when the user presses the OK button on the dialog.
		 *
		 * @param inputRequestId  request ID for the input
		 * @param filledInputView the dialog's view, now filled with the input data
		 */
		void onSubmit(String inputRequestId, View filledInputView);
	}

	private static final String KEY_INPUT_REQUEST_ID = "input_request_id";
	private static final String KEY_TITLE_RESOURCE = "title_resource";
	private static final String KEY_INPUT_VIEW_RESOURCE = "input_view_resource";

	/**
	 * Creates an {@code InputDialogFragment} that will
	 * show the given input view with the given title.
	 *
	 * @param inputRequestId          request ID for the input
	 * @param titleResource           the string resource for the dialog title
	 * @param inputViewLayoutResource the layout of the input view
	 * @return the new InputDialogFragment
	 *
	 * @throws NullPointerException if inputRequestId is null
	 */
	@NonNull
	public static InputDialogFragment newInstance(@NonNull String inputRequestId, @StringRes int titleResource,
			@LayoutRes int inputViewLayoutResource) {
		if (inputRequestId == null) {
			throw new NullPointerException("You must supply an input request ID!");
		}

		Bundle arguments = new Bundle();
		arguments.putString(KEY_INPUT_REQUEST_ID, inputRequestId);
		arguments.putInt(KEY_TITLE_RESOURCE, titleResource);
		arguments.putInt(KEY_INPUT_VIEW_RESOURCE, inputViewLayoutResource);

		InputDialogFragment result = new InputDialogFragment();
		result.setArguments(arguments);
		return result;
	}

	private String inputRequestId;
	private View dialogView;
	private SubmitListener submitListener;

	private AlertDialog inputDialog;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		inputRequestId = getArguments().getString(KEY_INPUT_REQUEST_ID);

		int layoutResource = getArguments().getInt(KEY_INPUT_VIEW_RESOURCE);
		int titleResource = getArguments().getInt(KEY_TITLE_RESOURCE);

		dialogView = LayoutInflater.from(getActivity()).inflate(layoutResource, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(titleResource).setView(dialogView)
				.setCancelable(true)
				.setNegativeButton(R.string.input_dialog_cancel_button, null)
				.setPositiveButton(R.string.input_dialog_ok_button, null);
		inputDialog = builder.create();
		inputDialog.setCanceledOnTouchOutside(true);
		inputDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				Button okButton = inputDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				okButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						synchronized (InputDialogFragment.this) {
							if (submitListener == null) {
								inputDialog.dismiss();
							}

							if (submitListener.onValidate(inputRequestId, dialogView)) {
								submitListener.onSubmit(inputRequestId, dialogView);
								inputDialog.dismiss();
							}
						}
					}
				});
			}
		});
		return inputDialog;
	}

	/**
	 * Sets the submit listener for this dialog.
	 *
	 * @param submitListener the new listener
	 */
	public synchronized void setSubmitListener(SubmitListener submitListener) {
		this.submitListener = submitListener;
	}
}
