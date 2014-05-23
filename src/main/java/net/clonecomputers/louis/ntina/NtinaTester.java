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

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

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
		int i = 0;
		int numGood = 0;
		File outputFile = new File("out.csv");
		int n = 0;
		while(outputFile.exists()) {
			outputFile = new File("out-" + ++n + ".csv");
		}
		CSVPrinter p = null;
		try {
			p = new CSVPrinter(new BufferedWriter(new FileWriter(outputFile)), CSVFormat.RFC4180);
		} catch (IOException e) {
			System.err.println("Error opening writing to " + outputFile);
			e.printStackTrace();
			return;
		}
		try {
			while(running) {
				++i;
				p.print(i);
				System.out.print(i);
				System.out.println("-tina:");
				Matrix matrix = getNtinaPermutationMatrix(i);
				Matrix identity = Matrix.identity(i, i);
				Matrix lagrangian = identity.minus(matrix);
				int numZeroEigenvales = 0;
				EigenvalueDecomposition eigs = lagrangian.eig();
				Matrix eigenvectors = eigs.getV();
				double[] eigenvalues = eigs.getRealEigenvalues();
				Map<Integer, Integer> subgraphs = new HashMap<Integer, Integer>();
				for(int j = 0; j < eigenvalues.length; ++j) {
					if(Math.abs(eigenvalues[j]) <= 1e-10) {
						++numZeroEigenvales;
						double[] eigenvector = eigenvectors.getColumnVector(j);
						int acc = 0;
						double bound = 0.5/Math.sqrt(i);
						for(double d : eigenvector) {
							if(Math.abs(d) > bound) ++acc;
						}
						if(subgraphs.containsKey(acc)) {
							subgraphs.put(acc, subgraphs.get(acc) + 1);
						} else {
							subgraphs.put(acc, 1);
						}
					}
				}
				p.print(numZeroEigenvales);
				System.out.print("\tNumber of subgraphs: ");
				System.out.println(numZeroEigenvales);
				System.out.print("\tSubgraphs: ");
				System.out.println(subgraphs);
				int period = lcm(subgraphs.keySet());
				if(period == i) numGood++;
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
		System.out.print(i);
		System.out.println();
		System.out.printf("%1$d/%2$d are valid n-tinas\n", numGood, i);
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
	
	private static Matrix getNtinaPermutationMatrix(int size) {
		double[][] matrixArray = new double[size][size];
		for(int j = 0; j < size/2; ++j) {
			matrixArray[2*j+1][j] = 1;
		}
		for(int j = size/2; j < size; ++j) {
			matrixArray[2*(size-j-1)][j] = 1;
		}
		return new Matrix(matrixArray);
	}

}
