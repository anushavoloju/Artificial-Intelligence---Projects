
public interface Learner {
	void train(DataSet ds);

	String predict(Data e);

	int[] test(DataSet ds);
}
