import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Anusha
 * 
 */
public class DTree {
	private String attrib;

	private Hashtable<String, DTree> childs;

	protected DTree() {

	}

	public DTree(String attributeName) {
		this.attrib = attributeName;
		childs = new Hashtable<String, DTree>();

	}

	public void addLeaf(String attributeValue, String decision) {
		childs.put(attributeValue, new DecisionTree(decision));
	}

	public void addNode(String attributeValue, DTree tree) {
		childs.put(attributeValue, tree);
	}

	public Object predict(Data e) {
		String attrValue = e.getAttributeValueAsString(attrib);
		if (childs.containsKey(attrValue)) {
			return childs.get(attrValue).predict(e);
		} else {
			throw new RuntimeException("no node exists for attribute value "
					+ attrValue);
		}
	}

	public static DTree getStumpFor(String attributeName,
			String attributeValue, String returnValueIfMatched,
			List<String> unmatchedValues, String returnValueIfUnmatched) {
		DTree dt = new DTree(attributeName);
		dt.addLeaf(attributeValue, returnValueIfMatched);
		for (String unmatchedValue : unmatchedValues) {
			dt.addLeaf(unmatchedValue, returnValueIfUnmatched);
		}
		return dt;
	}

	public static List<DTree> getStumpsFor(DataSet ds,
			String returnValueIfMatched, String returnValueIfUnmatched) {
		List<String> attributes = ds.getNonTargetAttributes();
		List<DTree> trees = new ArrayList<DTree>();
		for (String attribute : attributes) {
			List<String> values = ds.getPossibleAttributeValues(attribute);
			for (String value : values) {
				List<String> unmatchedValues = Utils.removeFrom(
						ds.getPossibleAttributeValues(attribute), value);

				DTree tree = getStumpFor(attribute, value,
						returnValueIfMatched, unmatchedValues,
						returnValueIfUnmatched);
				trees.add(tree);

			}
		}
		return trees;
	}

	public String getAttributeName() {
		return attrib;
	}

	@Override
	public String toString() {
		return toString(1, new StringBuffer());
	}

	public String toString(int depth, StringBuffer buf) {

		if (attrib != null) {
			buf.append(Utils.ntimes("\t", depth));
			buf.append(Utils.ntimes("***", 1));
			buf.append(attrib + " \n");
			for (String attributeValue : childs.keySet()) {
				buf.append(Utils.ntimes("\t", depth + 1));
				buf.append("+" + attributeValue);
				buf.append("\n");
				DTree child = childs.get(attributeValue);
				buf.append(child.toString(depth + 1, new StringBuffer()));
			}
		}

		return buf.toString();
	}
}
