package net.clonecomputers.louis.ntina;

import java.io.IOException;

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
		while(running) {
			++i;
			System.out.print(i);
			System.out.println("-tina:");
			Matrix matrix = getNtinaPermutationMatrix(i);
			Matrix identity = Matrix.identity(i, i);
			Matrix lagrangian = identity.minus(matrix);
			int numZeroEigenvales = 0;
			for(double l : lagrangian.eig().getRealEigenvalues()) {
				if(Math.abs(l) < 1e-7) ++numZeroEigenvales;
			}
			System.out.print("\tNumber of subgraphs: ");
			System.out.println(numZeroEigenvales);
			Matrix origMatrix = matrix.copy();
			int period = 1;
			while(!matrix.equals(identity)) {
				++period;
				matrix = matrix.times(origMatrix);
			}
			System.out.print("\tPeriod length: ");
			System.out.println(period);
			System.out.println();
		}
		System.out.print("Stopped after ");
		System.out.print(i);
		System.out.println("-tina");
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
