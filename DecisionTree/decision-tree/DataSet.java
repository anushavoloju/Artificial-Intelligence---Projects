import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DataSet {
	protected DataSet() {
	}

	public List<Data> dataList;

	public DataSetSpecification specification;

	public DataSet(DataSetSpecification spec) {
		dataList = new LinkedList<Data>();
		this.specification = spec;
	}

	public void add(Data e) {
		dataList.add(e);
	}

	public int size() {
		return dataList.size();
	}

	public Data getExample(int number) {
		return dataList.get(number);
	}

	public DataSet removeExample(Data e) {
		DataSet ds = new DataSet(specification);
		for (Data eg : dataList) {
			if (!(e.equals(eg))) {
				ds.add(eg);
			}
		}
		return ds;
	}

	public double getInformationFor() {
		String attributeName = specification.getTarget();
		Hashtable<String, Integer> counts = new Hashtable<String, Integer>();
		for (Data e : dataList) {

			String val = e.getAttributeValueAsString(attributeName);
			if (counts.containsKey(val)) {
				counts.put(val, counts.get(val) + 1);
			} else {
				counts.put(val, 1);
			}
		}

		double[] data = new double[counts.keySet().size()];
		Iterator<Integer> iter = counts.values().iterator();
		for (int i = 0; i < data.length; i++) {
			data[i] = iter.next();
		}
		data = Utils.normalize(data);

		return Utils.information(data);
	}

	public Hashtable<String, DataSet> splitByAttribute(String attributeName) {
		Hashtable<String, DataSet> results = new Hashtable<String, DataSet>();
		for (Data e : dataList) {
			String val = e.getAttributeValueAsString(attributeName);
			if (results.containsKey(val)) {
				results.get(val).add(e);
			} else {
				DataSet ds = new DataSet(specification);
				ds.add(e);
				results.put(val, ds);
			}
		}
		return results;
	}

	public double calculateGainFor(String parameterName) {
		Hashtable<String, DataSet> hash = splitByAttribute(parameterName);
		double totalSize = dataList.size();
		double remainder = 0.0;
		for (String parameterValue : hash.keySet()) {
			double reducedDataSetSize = hash.get(parameterValue).dataList
					.size();
			remainder += (reducedDataSetSize / totalSize)
					* hash.get(parameterValue).getInformationFor();
		}
		return getInformationFor() - remainder;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		DataSet other = (DataSet) o;
		return dataList.equals(other.dataList);
	}

	@Override
	public int hashCode() {
		return 0;
	}

	public Iterator<Data> iterator() {
		return dataList.iterator();
	}

	public DataSet copy() {
		DataSet ds = new DataSet(specification);
		for (Data e : dataList) {
			ds.add(e);
		}
		return ds;
	}

	public List<String> getAttributeNames() {
		return specification.getAttributeNames();
	}

	public String getTargetAttributeName() {
		return specification.getTarget();
	}

	public DataSet emptyDataSet() {
		return new DataSet(specification);
	}

	/**
	 * @param specification
	 *            The specification to set. USE SPARINGLY for testing etc ..
	 *            makes no semantic sense
	 */
	public void setSpecification(DataSetSpecification specification) {
		this.specification = specification;
	}

	public List<String> getPossibleAttributeValues(String attributeName) {
		return specification.getPossibleAttributeValues(attributeName);
	}

	public DataSet matchingDataSet(String attributeName, String attributeValue) {
		DataSet ds = new DataSet(specification);
		for (Data e : dataList) {
			if (e.getAttributeValueAsString(attributeName).equals(
					attributeValue)) {
				ds.add(e);
			}
		}
		return ds;
	}

	public List<String> getNonTargetAttributes() {
		return Utils.removeFrom(getAttributeNames(), getTargetAttributeName());
	}
}
