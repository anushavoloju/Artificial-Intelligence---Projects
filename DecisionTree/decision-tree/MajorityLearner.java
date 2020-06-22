import java.util.ArrayList;
import java.util.List;

public class MajorityLearner implements Learner {

	private String result;

	public void train(DataSet ds) {
		List<String> targets = new ArrayList<String>();
		for (Data e : ds.dataList) {
			targets.add(e.targetValue());
		}
		result = Utils.mode(targets);
	}

	@Override
	public String predict(Data e) {
		return result;
	}

	public int[] test(DataSet ds) {
		int[] results = new int[] { 0, 0 };

		for (Data e : ds.dataList) {
			if (e.targetValue().equals(result)) {
				results[0] = results[0] + 1;
			} else {
				results[1] = results[1] + 1;
			}
		}
		return results;
	}
}