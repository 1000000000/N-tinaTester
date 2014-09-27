package net.clonecomputers.louis.ntina;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class NtinaTester extends Thread {
	
	private boolean running = true;

	public static void main(String[] args) throws IOException {
		NtinaTester ntinaTester = new NtinaTester();
		ntinaTester.start();
		System.in.read();
		ntinaTester.running = false;
		
	}
	
	@Override
	public void run() {
		int n = 0;
		int numGood = 0;
		File outputFile = new File("out.csv");
		File coutputFile = new File("conjectures.csv");
		int a = 0;
		while(outputFile.exists()) {
			outputFile = new File("out-" + ++a + ".csv");
		}
		a = 0;
		while (coutputFile.exists()) {
			coutputFile = new File("conjectures-" + ++a + ".csv");
		}
		CSVPrinter p, cp = null;
		try {
			p = new CSVPrinter(new BufferedWriter(new FileWriter(outputFile)), CSVFormat.RFC4180.withRecordSeparator('\n'));
			cp = new CSVPrinter(new BufferedWriter(new FileWriter(coutputFile)), CSVFormat.RFC4180.withRecordSeparator('\n'));
		} catch (IOException e) {
			System.err.println("Error opening writing to " + outputFile + " or " + coutputFile);
			e.printStackTrace();
			return;
		}
		try {
			Map<Integer, List<Conjecture>> conjectures = new HashMap<Integer, List<Conjecture>>();
			while(running) {
				++n;
				p.print(n);
				System.out.print(n);
				System.out.println("-tina:");
				boolean[] visited = new boolean[n];
				int subgraphLength = 0;
				int numSubgraphs = 0;
				Map<Integer, Integer> subgraphs = new HashMap<Integer, Integer>();
				for(int i = 0; i < n; ++i) {
					if(subgraphLength == 0 && visited[i]) continue;
					if(visited[i]) {
						++numSubgraphs;
						Integer newNum = subgraphs.get(subgraphLength);
						subgraphs.put(subgraphLength, newNum != null ? newNum + 1 : 1);
						subgraphLength = 0;
					} else {
						visited[i] = true;
						++subgraphLength;
						i = getNextLoc(n, i) - 1;
					}
				}
				p.print(numSubgraphs);
				System.out.print("\tNumber of subgraphs: ");
				System.out.println(numSubgraphs);
				System.out.print("\tSubgraphs: ");
				System.out.println(subgraphs);
				int period = lcm(subgraphs.keySet());
				if(period == n) numGood++;
				p.print(period);
				System.out.print("\tPeriod length: ");
				System.out.println(period);
				System.out.println();
				for(Entry<Integer, List<Conjecture>> conjecture : conjectures.entrySet()) {
					Integer num = subgraphs.get(conjecture.getKey()); // How many of a size of this ntina has
					int num2 = num != null ? num : 0;
					for (int i = 0; i < conjecture.getValue().size(); ++i) {
						Conjecture aConjecture = conjecture.getValue().remove(i); // This is so that if the first of a family of conjectures is not valid we do not keep it
						aConjecture = aConjecture.addData(n, num2); // Returns either the same of its child if the conjecture is not valid
						conjecture.getValue().add(i, aConjecture);
					}
					if (num2 > 0) { //For a particular subgroup if it appears at least once in an ntina then we need to add a new conjecture for it in the set
						conjecture.getValue().add(new Conjecture(n));
					}
				}
				for(Entry<Integer, Integer> e : subgraphs.entrySet()) {
					// This is for adding new subgroups to the conjecture thing
					if (!conjectures.containsKey(e.getKey())) { //If we are not yet paying attention to subgroup of size e.getKey()
						List<Conjecture> newSubgroup = new ArrayList<Conjecture>();
						newSubgroup.add(new Conjecture(n)); // n is the first one that works for that subgroup
						conjectures.put(e.getKey(), newSubgroup);
					}
					for(int j = 0; j < e.getValue(); ++j) {
						p.print(e.getKey());
					}
				}
				p.println();
			}
			List<Entry<Integer, List<Conjecture>>> entries = new ArrayList<Map.Entry<Integer,List<Conjecture>>>(conjectures.entrySet());
			Collections.sort(entries, new ConjectureEntryComparator());
			for (Entry<Integer, List<Conjecture>> entry : entries) {
				cp.print(entry.getKey()); // Print the subgroup size the conjectures pretain to
				for (int i = 0; i < entry.getValue().size(); ++i) {
					Conjecture base = entry.getValue().get(i);
					if (base.getScore() < 0) {
						entry.getValue().remove(i);
						--i;
						continue;
					}
					while (base != null) {
						for (int j = i; j < entry.getValue().size(); ++j) {
							Conjecture toTest = entry.getValue().get(j);
							while (toTest != null) {
								if (toTest.getScore() < 0 || (toTest != base && base.isSupersetOf(toTest))) {
									if (toTest.getAncestor() == null) {
										if (toTest.getChild() != null) {
											entry.getValue().set(j, toTest.getChild());
										} else {
											entry.getValue().remove(j);
											--j;
										}
									}
									toTest = toTest.remove();
								} else {
									toTest = toTest.getChild();
								}
							} // end while
						} // end for
						cp.print(base);
						base = base.getChild();
					} // end while
				} // end for
				cp.println();
			} // end for
			cp.flush();
			p.flush();
		} catch (IOException e) {
			System.err.println("Error writing to or flushing writers");
			e.printStackTrace();
		}
		try {
			cp.close();
			p.close();
		} catch (IOException e) {
			System.err.println("Error closing writers");
			e.printStackTrace();
		}
		System.out.print("Stopped after ");
		System.out.print(n);
		System.out.println();
		System.out.printf("%1$d/%2$d are valid n-tinas\n", numGood, n);
	}
	
	private static int lcm(Set<Integer> nums) {
		int lcm = 1;
		for(int num : nums) {
			lcm = (num/gcd(lcm, num)) * lcm;
		}
		return lcm;
	}
	
	private static int gcd(int n1, int n2) {
		while(n2 > 0) {
			int temp = n2;
			n2 = n1 % n2;
			n1 = temp;
		}
		return n1;
	}
	
	private static int getNextLoc(int size, int currentLoc) {
		if(currentLoc < size/2) return 2*currentLoc + 1;
		else return 2*(size - currentLoc - 1);
	}

}
