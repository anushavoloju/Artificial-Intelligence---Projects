import java.util.Hashtable;

public class Data {
	Hashtable<String, Attr> attrs;

	private Attr attr;

	public Data(Hashtable<String, Attr> attributes,
			Attr attr) {
		this.attrs = attributes;
		this.attr = attr;
	}

	public String getAttributeValueAsString(String attributeName) {
		return attrs.get(attributeName).valueAsString();
	}

	public double getAttributeValueAsDouble(String attributeName) {
		Attr attr = attrs.get(attributeName);
		if (attr == null || !(attr instanceof NumericAttr)) {
			throw new RuntimeException(
					"cannot return numerical value for non numeric attribute");
		}
		return ((NumericAttr) attr).valueAsDouble();
	}

	@Override
	public String toString() {
		return attrs.toString();
	}

	public String targetValue() {
		return getAttributeValueAsString(attr.getName());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		Data other = (Data) o;
		return attrs.equals(other.attrs);
	}

	@Override
	public int hashCode() {
		return attrs.hashCode();
	}

	public Data numerize(
			Hashtable<String, Hashtable<String, Integer>> attrValueToNumber) {
		Hashtable<String, Attr> numerizedDataData = new Hashtable<String, Attr>();
		for (String key : attrs.keySet()) {
			Attr attribute = attrs.get(key);
			if (attribute instanceof StringAttr) {
				int correspondingNumber = attrValueToNumber.get(key).get(
						attribute.valueAsString());
				NumericValue spec = new NumericValue(
						key);
				numerizedDataData.put(key, new NumericAttr(
						correspondingNumber, spec));
			} else {// Numeric Attribute
				numerizedDataData.put(key, attribute);
			}
		}
		return new Data(numerizedDataData,
				numerizedDataData.get(attr.getName()));
	}
}
