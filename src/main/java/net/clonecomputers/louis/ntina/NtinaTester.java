package net.clonecomputers.louis.ntina;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
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
		int a = 0;
		while(outputFile.exists()) {
			outputFile = new File("out-" + ++a + ".csv");
		}
		CSVPrinter p = null;
		try {
			p = new CSVPrinter(new BufferedWriter(new FileWriter(outputFile)), CSVFormat.RFC4180.withRecordSeparator('\n'));
		} catch (IOException e) {
			System.err.println("Error opening writing to " + outputFile);
			e.printStackTrace();
			return;
		}
		try {
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
				for(Entry<Integer, Integer> e : subgraphs.entrySet()) {
					for(int j = 0; j < e.getValue(); ++j) {
						p.print(e.getKey());
					}
				}
				p.println();
			}
			p.flush();
		} catch (IOException e) {
			System.err.println("Error writing to or flushing writer");
			e.printStackTrace();
		}
		try {
			p.close();
		} catch (IOException e) {
			System.err.println("Error closing writer");
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
