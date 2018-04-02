import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

class CustomizedHashMap implements Comparator<Map.Entry<String, Integer>> {
		@Override
		public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) 
		{
			return -o1.getValue().compareTo(o2.getValue());
		}}