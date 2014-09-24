package net.clonecomputers.louis.ntina;

import java.util.Comparator;
import java.util.Map.Entry;

public class ConjectureEntryComparator implements Comparator<Entry<Integer, ?>> {

	@Override
	public int compare(Entry<Integer, ?> o1, Entry<Integer, ?> o2) {
		if (o1.getKey() == o2.getKey()) return 0;
		return o1.getKey() > o2.getKey() ? 1 : -1;
	}
	
}
