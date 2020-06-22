import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataSetSpecification {
	List<AttrValue> attrValueList;

	private String targetAttribute;

	public DataSetSpecification() {
		this.attrValueList = new ArrayList<AttrValue>();
	}

	public boolean isValid(List<String> uncheckedAttributes) {
		if (attrValueList.size() != uncheckedAttributes.size()) {
			throw new RuntimeException("size mismatch specsize = "
					+ attrValueList.size() + " attrbutes size = "
					+ uncheckedAttributes.size());
		}
		Iterator<AttrValue> attributeSpecIter = attrValueList
				.iterator();
		Iterator<String> valueIter = uncheckedAttributes.iterator();
		while (valueIter.hasNext() && attributeSpecIter.hasNext()) {
			if (!(attributeSpecIter.next().isValid(valueIter.next()))) {
				return false;
			}
		}
		return true;
	}

	public String getTarget() {
		return targetAttribute;
	}

	public List<String> getPossibleAttributeValues(String attributeName) {
		for (AttrValue as : attrValueList) {
			if (as.getAttrName().equals(attributeName)) {
				return ((StringValue) as)
						.getPossibleAttributeValues();
			}
		}
		throw new RuntimeException("No such attribute" + attributeName);
	}

	public List<String> getAttributeNames() {
		List<String> names = new ArrayList<String>();
		for (AttrValue as : attrValueList) {
			names.add(as.getAttrName());
		}
		return names;
	}

	public void defineStringAttribute(String name, String[] attributeValues) {
		attrValueList.add(new StringValue(name,
				attributeValues));
		setTarget(name);
	}

	public void setTarget(String target) {
		this.targetAttribute = target;
	}

	public AttrValue getAttributeSpecFor(String name) {
		for (AttrValue spec : attrValueList) {
			if (spec.getAttrName().equals(name)) {
				return spec;
			}
		}
		throw new RuntimeException("no attribute spec for  " + name);
	}

	public void defineNumericAttribute(String name) {
		attrValueList.add(new NumericValue(name));
	}

	public List<String> getNamesOfStringAttributes() {
		List<String> names = new ArrayList<String>();
		for (AttrValue spec : attrValueList) {
			if (spec instanceof StringValue) {
				names.add(spec.getAttrName());
			}
		}
		return names;
	}
}
