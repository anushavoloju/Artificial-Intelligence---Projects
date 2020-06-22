public class NumericAttr implements Attr {
	double orgValue;

	private NumericValue val;

	public NumericAttr(double orgValue, NumericValue val) {
		this.orgValue = orgValue;
		this.val = val;
	}

	public String valueAsString() {
		return Double.toString(orgValue);
	}

	public String getName() {
		return val.getAttrName().trim();
	}

	public double valueAsDouble() {
		return orgValue;
	}
}
