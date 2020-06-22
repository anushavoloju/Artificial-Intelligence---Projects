import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Utils {
	public static final String NO = "No";
	public static final String YES = "Yes";
	
	public static double[] normalize(double[] probDist) {
		int len = probDist.length;
		double total = 0.0;
		for (double d : probDist) {
			total = total + d;
		}

		double[] normalized = new double[len];
		if (total != 0) {
			for (int i = 0; i < len; i++) {
				normalized[i] = probDist[i] / total;
			}
		}

		return normalized;
	}

	public static double information(double[] probabilities) {
		double total = 0.0;
		for (double d : probabilities) {
			total += (-1.0 * log2(d) * d);
		}
		return total;
	}
	
	public static double log2(double d) {
		return Math.log(d) / Math.log(2);
	}
	
	public static <T> List<T> removeFrom(List<T> list, T member) {
		List<T> newList = new ArrayList<>(list);
		newList.remove(member);
		return newList;
	}
	
	public static String[] yesno() {
		return new String[] { YES, NO };
	}
	
	public static String ntimes(String s, int n) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < n; i++) {
			builder.append(s);
		}
		return builder.toString();
	}
	
	public static <T> T mode(List<T> l) {
		Hashtable<T, Integer> hash = new Hashtable<>();
		for (T obj : l) {
			if (hash.containsKey(obj)) {
				hash.put(obj, hash.get(obj) + 1);
			} else {
				hash.put(obj, 1);
			}
		}
	
		T maxkey = hash.keySet().iterator().next();
		for (T key : hash.keySet()) {
			if (hash.get(key) > hash.get(maxkey)) {
				maxkey = key;
			}
		}
		return maxkey;
	}
}