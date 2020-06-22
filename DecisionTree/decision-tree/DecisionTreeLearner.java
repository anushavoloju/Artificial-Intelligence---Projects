import java.util.Iterator;
import java.util.List;

public class DecisionTreeLearner implements Learner {
	private DTree tree;

	private String defaultValue;

	public DecisionTreeLearner() {
		this.defaultValue = "Unable To Classify";

	}

	public DecisionTreeLearner(DTree tree, String defaultValue) {
		this.tree = tree;
		this.defaultValue = defaultValue;
	}

	@Override
	public void train(DataSet ds) {
		List<String> attributes = ds.getNonTargetAttributes();
		this.tree = decisionTreeLearning(ds, attributes,
				new DecisionTree(defaultValue));
	}

	@Override
	public String predict(Data e) {
		return (String) tree.predict(e);
	}

	@Override
	public int[] test(DataSet ds) {
		int[] results = new int[] { 0, 0 };

		for (Data e : ds.dataList) {
			if (e.targetValue().equals(tree.predict(e))) {
				results[0] = results[0] + 1;
			} else {
				results[1] = results[1] + 1;
			}
		}
		return results;
	}

	public DTree getDecisionTree() {
		return tree;
	}

	private DTree decisionTreeLearning(DataSet ds,
			List<String> attributeNames, DecisionTree defaultTree) {
		if (ds.size() == 0) {
			return defaultTree;
		}
		if (allExamplesHaveSameClassification(ds)) {
			return new DecisionTree(ds.getExample(0).targetValue());
		}
		if (attributeNames.size() == 0) {
			return majorityValue(ds);
		}
		String chosenAttribute = chooseAttribute(ds, attributeNames);

		DTree tree = new DTree(chosenAttribute);
		DecisionTree m = majorityValue(ds);

		List<String> values = ds.getPossibleAttributeValues(chosenAttribute);
		for (String v : values) {
			DataSet filtered = ds.matchingDataSet(chosenAttribute, v);
			List<String> newAttribs = Utils.removeFrom(attributeNames,
					chosenAttribute);
			DTree subTree = decisionTreeLearning(filtered, newAttribs, m);
			tree.addNode(v, subTree);

		}

		return tree;
	}

	private DecisionTree majorityValue(DataSet ds) {
		Learner learner = new MajorityLearner();
		learner.train(ds);
		return new DecisionTree(learner.predict(ds.getExample(0)));
	}

	private String chooseAttribute(DataSet ds, List<String> attributeNames) {
		double greatestGain = 0.0;
		String attributeWithGreatestGain = attributeNames.get(0);
		for (String attr : attributeNames) {
			double gain = ds.calculateGainFor(attr);
			if (gain > greatestGain) {
				greatestGain = gain;
				attributeWithGreatestGain = attr;
			}
		}

		return attributeWithGreatestGain;
	}

	private boolean allExamplesHaveSameClassification(DataSet ds) {
		String classification = ds.getExample(0).targetValue();
		Iterator<Data> iter = ds.iterator();
		while (iter.hasNext()) {
			Data element = iter.next();
			if (!(element.targetValue().equals(classification))) {
				return false;
			}

		}
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		DataSet dataset = DataSetFactory.getRestaurantDataSet();
		DecisionTreeLearner decisionTreeLearner = new DecisionTreeLearner();
		decisionTreeLearner.train(dataset);
		DTree trainedDecisionTree = decisionTreeLearner.getDecisionTree();
		System.out.println(trainedDecisionTree.toString());
	}
}
