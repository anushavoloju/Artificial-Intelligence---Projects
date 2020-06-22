public class DecisionTree extends DTree {
	private String value;

	public DecisionTree(String value) {
		this.value = value;
	}

	@Override
	public void addLeaf(String attributeValue, String decision) {
		throw new RuntimeException("cannot add Leaf to ConstantDecisonTree");
	}

	@Override
	public void addNode(String attributeValue, DTree tree) {
		throw new RuntimeException("cannot add Node to ConstantDecisonTree");
	}

	@Override
	public Object predict(Data e) {
		return value;
	}

	@Override
	public String toString() {
		return "DECISION -> " + value;
	}

	@Override
	public String toString(int depth, StringBuffer buf) {
		buf.append(Utils.ntimes("\t", depth + 1));
		buf.append("DECISION -> " + value + "\n");
		return buf.toString();
	}
}
