package net.clonecomputers.louis.ntina;


public class Conjecture {
	
	private Conjecture ancestor = null;
	private Conjecture child = null;
	private boolean lockedIn = false;
	
	private Integer initial;
	private Integer diff;
	
	private int score = -1;
	
	public Conjecture(int n) {
		initial = n;
		diff = n;
	}
	
	private Conjecture(Conjecture ancestor) {
		this.ancestor = ancestor;
		initial = ancestor.initial;
		diff = ancestor.diff + 1;
	}
	
	/**
	 * Adds data to try and complete the conjecture or,
	 * if the conjecture is complete, checks it against the new data
	 * @param n
	 * @param num
	 * @return false if and only if this conjecture is a complete conjecture and 
	 */
	public Conjecture addData(int n, int num) {
		if (child != null) child.addData(n, num);
		if (doesInclude(n)) {
			if (num <= 0) {
				if (lockedIn) {
					return remove();
				} else {
					++diff;
				}
			} else {
				if (!lockedIn) {
					lockedIn = true;
					child = new Conjecture(this);
				}
				++score;
			}
		}
		return this;
	}
	
	public boolean doesInclude(int n) {
		return (n - 1) % diff == (initial - 1); //This minus one thing is because 0 < initial <= diff while 0 <= n % diff < diff
	}
	
	public Conjecture remove() {
		child.ancestor = ancestor;
		if (ancestor != null) ancestor.child = child;
		return child;
	}
	
	public Conjecture getAncestor() {
		return ancestor;
	}
	
	public Conjecture getChild() {
		return child;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getDiff() {
		return diff;
	}
	
	public String toString() {
		return "{" + (initial % diff) + " mod " + diff + " (" + score + ")}";
	}
	
}
