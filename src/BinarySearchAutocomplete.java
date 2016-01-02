
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 
 * Using a sorted array of Term objects, this implementation uses binary search to find the
 * top term(s).
 * 
 * @author Austin Lu, adapted from Kevin Wayne
 *
 */
public class BinarySearchAutocomplete implements Autocompletor {

	Term[] _terms;

	/**
	 * Given arrays of words and weights, initialize _terms to a corresponding
	 * array of Terms sorted lexicographically.
	 * 
	 * This constructor is written for you, but you may make modifications to 
	 * it.
	 * 
	 * @param terms - A list of words to form terms from
	 * @param weights - A corresponding list of weights, such that
	 * terms[i] has weight[i].
	 * @return a BinarySearchAutocomplete whose _terms object
	 * has _terms[i] = a Term with word terms[i] and weight weights[i].
	 * @throws a NullPointerException if either argument passed in is
	 * null
	 */
	public BinarySearchAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		_terms = new Term[terms.length];
		for (int i = 0; i < terms.length; i++) {
			_terms[i] = new Term(terms[i], weights[i]);
		}
		Arrays.sort(_terms);
	}

	/**Uses binary search to find the index of the first Term in the passed in 
	 * array which is considered equivalent by a comparator to the given key.
	 * This method should not call comparator.compare() more than 1+log n times,
	 * where n is the size of a.
	 * 
	 * @param a - The array of Terms being searched
	 * @param key - The key being searched for.
	 * @param comparator - A comparator, used to determine equivalency
	 * between the values in a and the key.
	 * @return The first index i for which comparator considers a[i] and key
	 * as being equal. If no such index exists, return -1 instead.
	 */
	public static int firstIndexOf(Term[] a, Term key, Comparator<Term> comparator) {
		//uses binary search to find the firstIndexOf a matching term in the argument list
		if(a == null || key == null || a.length == 0){
			return -1;
		}
		int start = -1;
		int end = a.length-1;
		int middle;
		while(end-start>1){
			middle = (start + end)/2;
			int comparison = comparator.compare(a[middle], key);
			if(comparison==0){
				end = middle;
			}
			else if(comparison>0){
				end = middle;
			}
			else if(comparison<0){
				start = middle;
			}				
		}
		
		if(start!=-1 && comparator.compare(a[start], key)==0){
			return start;
		}
		else if(comparator.compare(a[end], key)==0){
			return end;
		}
		return -1;
	}

	/**The same as firstIndexOf, but instead finding the index of the
	 * last Term.
	 * 
	 * @param a - The array of Terms being searched
	 * @param key - The key being searched for.
	 * @param comparator - A comparator, used to determine equivalency
	 * between the values in a and the key.
	 * @return The last index i for which comparator considers a[i] and key
	 * as being equal. If no such index exists, return -1 instead.
	 */
	public static int lastIndexOf(Term[] a, Term key, Comparator<Term> comparator) {
		//uses binary search to find the lastIndexOf a matching term in the argument list
		if(a == null || key == null || a.length == 0){
			return -1;
		}
		int start = -1;
		int end = a.length-1;
		int middle = (start + end)/2;
		while(end-start>1){
			middle = (start + end)/2;
			int comparison = comparator.compare(a[middle], key);
			if(comparison==0){
				start = middle;
			}
			else if(comparison>0){
				end = middle;
			}
			else if(comparison<0){
				start = middle;
			}				
		}
		
		if(comparator.compare(a[end], key)==0){
			return end;
		}
		else if(start!=-1 && comparator.compare(a[start], key)==0){
			return start;
		}
		return -1;
	}

	/**
	 * Required by the Autocompletor interface.
	 * Returns an array containing the k words in _terms with the largest weight
	 * which match the given prefix, in descending weight order. If less than k
	 * words exist matching the given prefix (including if no words exist),
	 * then the array instead contains all those words.
	 * e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then topKMatches("b", 2)
	 * should return {"bell", "bat"}, but topKMatches("a", 2) should return
	 * {"air"}
	 * 
	 * @param prefix - A prefix which all returned words must start with
	 * @param k - The (maximum) number of words to be returned
	 * @return An array of the k words with the largest weights among all
	 * words starting with prefix, in descending weight order.
	 * 	If less than k such words exist, return an array containing all those 
	 *  words
	 * 	If no such words exist, reutrn an empty array
	 * @throws a NullPointerException if prefix is null
	 */
	public String[] topKMatches(String prefix, int k) {
		//uses firstIndexOf and lastIndexOf to find all of the matching terms in the list
		//Then places those matching terms in a priority queue sorted by reverseWeightOrder
		//And then extracts the largest K or less weighted terms
		if(prefix==null){
			throw new NullPointerException("no prefix");
		}
		int r = prefix.length();
		Term searchTerm = new Term(prefix, 0);
		Comparator<Term> prefixOrd = new Term.PrefixOrder(r);
		int firstIndex = firstIndexOf(_terms, searchTerm, prefixOrd);
		int lastIndex = lastIndexOf(_terms, searchTerm, prefixOrd);		
		int numFound = lastIndex - firstIndex+1;
		if(firstIndex ==-1 || lastIndex==-1){
			return new String[0];
		}
		PriorityQueue<Term> matchTerms = new PriorityQueue<Term>(new Term.ReverseWeightOrder());
		for(int i = firstIndex; i<lastIndex+1; i++){
			matchTerms.add(_terms[i]);
		}		
		if(numFound>k){
			numFound = k;			
		}
		String[] matchStrings = new String[numFound];
		for(int j =0; j<numFound; j++){
			matchStrings[j] =  matchTerms.poll().getWord();
		}
		return matchStrings;
	}

	@Override
	/**
	 * Given a prefix, returns the largest-weight word in _terms starting with 
	 * that prefix. 
	 * e.g. for {air:3, bat:2, bell:4, boy:1}, topMatch("b") would return "bell".
	 * If no such word exists, return an empty String.
	 * 
	 * @param prefix - the prefix the returned word should start with
	 * @return The word from _terms with the largest weight starting with 
	 * prefix, or an empty string if none exists
	 * @throws a NullPointerException if the prefix is null
	 * 
	 */
	public String topMatch(String prefix) {
		//creates an ArrayList of matching terms from _terms, and then finds the max weighted term by
		//iterating the matching terms once and returning
		if(prefix==null){
			throw new NullPointerException("no prefix");
		}
		int r = prefix.length();
		Term searchTerm = new Term(prefix, 0);
		Comparator<Term> prefixOrd = new Term.PrefixOrder(r);
		int firstIndex = firstIndexOf(_terms, searchTerm, prefixOrd);
		int lastIndex = lastIndexOf(_terms, searchTerm, prefixOrd);
		if(firstIndex ==-1 || lastIndex==-1){
			return "";
		}
		ArrayList<Term> matchTerms = new ArrayList<Term>();
		for(int i = firstIndex; i<lastIndex+1; i++){
			matchTerms.add(_terms[i]);
		}
		double maxWeight = -1;
		String maxTerm = "";
		for (Term t : matchTerms) {
			if (t.getWeight() > maxWeight) {
				maxTerm = t.getWord();
				maxWeight = t.getWeight();
			}
		}
		return maxTerm;
	}

	public static void main(String[] args){
		//for testing
		Term[] terms = new Term[] { new Term("ape", 6), new Term("app", 4), new Term("ban", 2), new Term("bat", 3),
				new Term("bee", 5), new Term("car", 7), new Term("cat", 1), new Term("cool", 10), new Term("crow", 8) };
		String[] names = { "ape", "app", "ban", "bat", "bee", "car", "cat", "cool" ,"crow"};
		double[] weights = { 6, 4, 2, 3, 5, 7, 1, 10, 8 };
		Autocompletor ac = new BinarySearchAutocomplete(names, weights);
		System.out.println(Arrays.toString(ac.topKMatches("", 0)));
	}

}
