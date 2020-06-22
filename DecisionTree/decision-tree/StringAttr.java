public class StringAttr implements Attr {
	private StringValue val;

	private String orgValue;

	public StringAttr(String orgValue, StringValue val) {
		this.val = val;
		this.orgValue = orgValue;
	}

	public String valueAsString() {
		return orgValue.trim();
	}

	public String getName() {
		return val.getAttrName().trim();
	}
}
