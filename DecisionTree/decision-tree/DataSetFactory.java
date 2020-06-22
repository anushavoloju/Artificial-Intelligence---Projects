import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class DataSetFactory {

	public DataSet readFromFile(String filename, DataSetSpecification spec,
			String separator) throws Exception {
		DataSet ds = new DataSet(spec);

		try(BufferedReader reader = new BufferedReader(new InputStreamReader(
				DataResource.class.getResourceAsStream(filename + ".csv")))) {

			String line;

			while ((line = reader.readLine()) != null) {
				ds.add(getDataFromString(line, spec, separator));
			}

		}

		return ds;
	}

	public static Data getDataFromString(String data,
			DataSetSpecification dataSetSpec, String separator) {
		Hashtable<String, Attr> attributes = new Hashtable<String, Attr>();
		List<String> attributeValues = Arrays.asList(data.replaceAll("\\s+","").split(separator));
		if (dataSetSpec.isValid(attributeValues)) {
			List<String> names = dataSetSpec.getAttributeNames();
			Iterator<String> nameiter = names.iterator();
			Iterator<String> valueiter = attributeValues.iterator();
			while (nameiter.hasNext() && valueiter.hasNext()) {
				String name = nameiter.next();
				AttrValue attributeSpec = dataSetSpec
						.getAttributeSpecFor(name);
				Attr attribute = attributeSpec.createAttr(valueiter
						.next());
				attributes.put(name, attribute);
			}
			String targetAttributeName = dataSetSpec.getTarget();
			return new Data(attributes, attributes.get(targetAttributeName));
		} else {
			throw new RuntimeException("Unable to construct Data from "
					+ data);
		}
	}

	public static DataSet getRestaurantDataSet() throws Exception {
		DataSetSpecification spec = createRestaurantDataSetSpec();
		return new DataSetFactory().readFromFile("restaurant", spec, ",");
	}

	public static DataSetSpecification createRestaurantDataSetSpec() {
		DataSetSpecification dss = new DataSetSpecification();
		dss.defineStringAttribute("alternate", Utils.yesno());
		dss.defineStringAttribute("bar", Utils.yesno());
		dss.defineStringAttribute("fri/sat", Utils.yesno());
		dss.defineStringAttribute("hungry", Utils.yesno());
		dss.defineStringAttribute("patrons", new String[] { "None", "Some",
				"Full" });
		dss.defineStringAttribute("price", new String[] { "$", "$$", "$$$" });
		dss.defineStringAttribute("raining", Utils.yesno());
		dss.defineStringAttribute("reservation", Utils.yesno());
		dss.defineStringAttribute("type", new String[] { "French", "Italian",
				"Thai", "Burger" });
		dss.defineStringAttribute("wait_estimate", new String[] { "0-10",
				"10-30", "30-60", ">60" });
		dss.defineStringAttribute("will_wait", Utils.yesno());

		return dss;
	}
}
