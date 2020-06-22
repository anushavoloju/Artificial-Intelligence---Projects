public class NumericValue implements AttrValue {
	private String name;

	public NumericValue(String name) {
		this.name = name;
	}

	public boolean isValid(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getAttrName() {
		return name;
	}

	public Attr createAttr(String value) {
		return new NumericAttr(Double.parseDouble(value), this);
	}
}
