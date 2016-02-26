package hu.bsmart.framework.communication.data.quiz;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.data.DataObject;
import hu.bsmart.framework.communication.data.FailingType;

import static hu.bsmart.framework.communication.data.Constants.UNSET_INTEGER;

public class QuizQuestion extends DataObject {

	@Required
	private int index = UNSET_INTEGER;

	@Required
	private String question = null;

	@Required
	private QuizAnswer[] answers = null;

	@Required
	private FailingType failingType = null;

	private int retryNumber = UNSET_INTEGER;

	private int failPenalty = UNSET_INTEGER;

	@Required
	private int score = UNSET_INTEGER;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public QuizAnswer[] getAnswers() {
		return answers;
	}

	public void setAnswers(QuizAnswer[] answers) {
		this.answers = answers;
	}

	public FailingType getFailingType() {
		return failingType;
	}

	public void setFailingType(FailingType failingType) {
		this.failingType = failingType;
	}

	public int getRetryNumber() {
		return retryNumber;
	}

	public void setRetryNumber(int retryNumber) {
		this.retryNumber = retryNumber;
	}

	public int getFailPenalty() {
		return failPenalty;
	}

	public void setFailPenalty(int failPenalty) {
		this.failPenalty = failPenalty;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public void validate() throws ValidationException {
		if (index == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "index");
		}

		if (question == null) {
			throw ValidationException.missingField(this, "question");
		}

		if (answers == null) {
			throw ValidationException.missingField(this, "answers");
		}

		for (QuizAnswer answer : answers) {
			answer.validate();
		}

		if (failingType == null) {
			throw ValidationException.missingField(this, "failingType");
		}

		if (failingType != FailingType.SILENT_FAIL) {
			if (failPenalty == UNSET_INTEGER) {
				throw ValidationException.missingField(this, "failPenalty");
			}

			if (failPenalty <= 0) {
				throw new ValidationException("FailPenalty must be positive if failingType != SILENT_FAIL!");
			}
		}

		if (failingType == FailingType.LIMITED_FAIL) {
			if (retryNumber == UNSET_INTEGER) {
				throw ValidationException.missingField(this, "retryNumber");
			}

			if (retryNumber <= 0) {
				throw new ValidationException("RetryNumber must be positive if failingType == LIMITED_FAIL!");
			}
		}

		if (score == UNSET_INTEGER) {
			throw ValidationException.missingField(this, "score");
		}

	}
}
