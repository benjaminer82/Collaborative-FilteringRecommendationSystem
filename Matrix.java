import java.util.*;
import java.io.*;

class Matrix {

    // a matrix of CF ratings.  you can....

    // Create a new matrix by reading from a Reader
    public Matrix(BufferedReader breader) throws IOException {
	this();
	breader.readLine(); // discard header row
	String line;
	int U = 0;
	while ((line=breader.readLine()) != null) {
	    String[] ratings = line.split(","); // if the last rating is null, we won't see it; but that's OK: it's null!
	    for (int I = 0; I<ratings.length; I++) {
		if (ratings[I].length() > 0) { // empty string means not rated
		    put(U, I, Double.parseDouble(ratings[I]));
		}
	    }
	    U++;
	}
    }

    // Fetch a rating
    public double get(int U, int I) {
	Double result = (Double) ratings.get(new Pair(U,I));
	return result==null ? MISSING : result.doubleValue();
    }

    // Ask how many users are known
    public int nusers() {
	return 1+maxuser;
    }

    // Ask how many items are known
    public int nitems() {
	return 1+maxitem;
    }

    // Ask how many ratings are known
    public int nratings() {
	return ratings.size();
    }

    // this is the special rating value meaning "not rated"
    public final static double MISSING = -1;

    // everything else is PRIVATE

    private Matrix() {
	ratings = new HashMap();
    }

    private void put(int U, int I, double R) {
	maxuser = Math.max(maxuser, U);
	maxitem = Math.max(maxitem, I);
	ratings.put(new Pair(U,I), new Double(R));
    }

    private Map ratings; // mapping from a (U,I) Pair --> Double

    private int maxuser;

    private int maxitem;

    class Pair {
	int U;
	int I;
	Pair(int _U, int _I) {
	    U = _U;
	    I = _I;
	}
	public boolean equals(Object o) {
	    Pair pair = (Pair) o;
	    return U==pair.U && I==pair.I;
	}
	public int hashCode() {
	    return U*I; // yes, this is a dumb hash code
	}
    }

    // just for debugging

    public String toString() {
	return "<Matrix: " + nusers() + " users; " + nitems() + " items; " + nratings() + " ratings>";
    }

    
}
