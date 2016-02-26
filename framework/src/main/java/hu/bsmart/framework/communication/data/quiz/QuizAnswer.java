package hu.bsmart.framework.communication.data.quiz;

import hu.bsmart.framework.communication.data.DataObject;

public class QuizAnswer extends DataObject {
	private boolean right;
	private String text;

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void validate() throws ValidationException {
		if (text == null) {
			throw ValidationException.missingField(this, "text");
		}

		// TODO: validate right boolean
	}

}
