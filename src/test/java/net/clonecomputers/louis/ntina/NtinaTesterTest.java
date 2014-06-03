package net.clonecomputers.louis.ntina;

import static org.junit.Assert.*;
import static junitx.util.PrivateAccessor.invoke;

import org.junit.Test;

public class NtinaTesterTest {

	/* Old test for a method that no longer exists
	@Test
	public void testGetNtinaPermutationMatrix() throws Throwable {
		double[][] monotinaPermutationMatrix = {{1}};
		double[][] bistinaPermutationMatrix = {{0,1},{1,0}};
		double[][] tristinaPermutationMatrix = {
				{0,0,1},
				{1,0,0},
				{0,1,0},
		};
		double[][] sestinaPermutationMatrix = {
				{0,0,0,0,0,1},
				{1,0,0,0,0,0},
				{0,0,0,0,1,0},
				{0,1,0,0,0,0},
				{0,0,0,1,0,0},
				{0,0,1,0,0,0},
		};
		double[][] monotina = ((Matrix) invoke(NtinaTester.class, "getNtinaPermutationMatrix", new Class[]{int.class}, new Object[] {1})).getArray();
		double[][] bistina = ((Matrix) invoke(NtinaTester.class, "getNtinaPermutationMatrix", new Class[]{int.class}, new Object[] {2})).getArray();
		double[][] tristina = ((Matrix) invoke(NtinaTester.class, "getNtinaPermutationMatrix", new Class[]{int.class}, new Object[] {3})).getArray();
		double[][] sestina = ((Matrix) invoke(NtinaTester.class, "getNtinaPermutationMatrix", new Class[]{int.class}, new Object[] {6})).getArray();
		assertArrayEquals(monotinaPermutationMatrix, monotina);
		assertArrayEquals(bistinaPermutationMatrix, bistina);
		assertArrayEquals(tristinaPermutationMatrix, tristina);
		assertArrayEquals(sestinaPermutationMatrix, sestina);
	}*/

}
