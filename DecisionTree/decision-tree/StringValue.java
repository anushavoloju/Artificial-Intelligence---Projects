import java.util.Arrays;
import java.util.List;

public class StringValue implements AttrValue {
	String name;

	List<String> attrPossibleValues;

	public StringValue(String name,
			List<String> attrPossibleValues) {
		this.name = name;
		this.attrPossibleValues = attrPossibleValues;
	}

	public StringValue(String attributeName,
			String[] attributePossibleValues) {
		this(attributeName, Arrays.asList(attributePossibleValues));
	}

	public boolean isValid(String value) {
		return (attrPossibleValues.contains(value));
	}

	public String getAttrName() {
		return name;
	}

	public List<String> getPossibleAttributeValues() {
		return attrPossibleValues;
	}

	public Attr createAttr(String rawValue) {
		return new StringAttr(rawValue, this);
	}
}
