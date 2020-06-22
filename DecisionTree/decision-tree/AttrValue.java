public interface AttrValue {
	boolean isValid(String value);

	String getAttrName();

	Attr createAttr(String value);
}
