package hu.bsmart.framework.communication.data.taskdatatype;

import java.io.Serializable;

import hu.bsmart.framework.communication.annotations.Required;
import hu.bsmart.framework.communication.data.TaskData;
import hu.bsmart.framework.communication.data.quiz.QuizQuestion;

public class QuizTaskData extends TaskData implements Serializable {

	private static final String TYPE_SINGLE = "single";
	private static final String TYPE_MULTI = "multi";

	public enum QuizType {
		SINGLE(TYPE_SINGLE),
		MULTI_SELECT(TYPE_MULTI);

		private String typeString;

		QuizType(String typeString) {
			this.typeString = typeString;
		}

		public static QuizType from(String typeString) {
			for (QuizType quizType : QuizType.values()) {
				if (quizType.typeString.equals(typeString)) {
					return quizType;
				}
			}

			throw new IllegalArgumentException("No QuizType found for typeString: " + typeString);
		}
	}

	@Required
	private QuizType type;

	@Required
	private QuizQuestion[] data;

	public QuizTaskData(int id) {
		super(id);
	}

	@Override
	public Type getType() {
		return Type.QUIZ;
	}

	public QuizType getQuizType() {
		return type;
	}

	public void setQuizType(QuizType quizType) {
		this.type = quizType;
	}

	public QuizQuestion[] getQuizQuestions() {
		return data;
	}

	public void setQuizQuestions(QuizQuestion[] quizQuestions) {
		this.data = quizQuestions;
	}

	@Override
	public void validate() throws ValidationException {
		super.validate();

		if (type == null) {
			throw ValidationException.missingField(this, "type");
		}

		if (data == null) {
			throw ValidationException.missingField(this, "data");
		}

		for (QuizQuestion question : data) {
			question.validate();
		}
	}
}
