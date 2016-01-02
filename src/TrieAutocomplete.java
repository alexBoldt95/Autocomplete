import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * General trie/priority queue algorithm for implementing Autocompletor
 * 
 * @author Austin Lu
 *
 */
public class TrieAutocomplete implements Autocompletor {

	/**
	 * Root of entire trie
	 */
	protected Node myRoot;

	/**
	 * Constructor method for TrieAutocomplete. Should initialize the trie
	 * rooted at myRoot, as well as add all nodes necessary to represent the
	 * words in terms.
	 * 
	 * @param terms
	 *            - The words we will autocomplete from
	 * @param weights
	 *            - Their weights, such that terms[i] has weight weights[i].
	 * @throws a
	 *             NullPointerException if either argument is null
	 */
	public TrieAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		// Represent the root as a dummy/placeholder node
		myRoot = new Node('-', null, 0);

		for (int i = 0; i < terms.length; i++) {
			add(terms[i], weights[i]);
		}
	}

	/**
	 * Add the word with given weight to the trie. If word already exists in the
	 * trie, no new nodes should be created, but the weight of word should be
	 * updated.
	 * 
	 * In adding a word, this method should do the following: Create any
	 * necessary intermediate nodes if they do not exist. Update the
	 * subtreeMaxWeight of all nodes in the path from root to the node
	 * representing word. Set the value of myWord, myWeight, isWord, and
	 * mySubtreeMaxWeight of the node corresponding to the added word to the
	 * correct values
	 * 
	 * @throws a
	 *             NullPointerException if word is null
	 * @throws an
	 *             IllegalArgumentException if weight is negative.
	 * 
	 */
	private void add(String word, double weight) {
		//Add terms to the trie. Accounts for duplicate adds with lower weights than previous adds.
		Node t = myRoot;

		for (int k = 0; k < word.length(); k++) {
			char ch = word.charAt(k);
			if(weight>t.mySubtreeMaxWeight){
				t.mySubtreeMaxWeight=weight;
			}
			Node child = t.getChild(ch);
			if (child == null) {				
				child = new Node(ch, t, weight);
				t.children.put(ch, child);
			}
			t = child;
		}
		//This loop checks if the last node is already a word (i.e. a duplicate) and iterates up through the
		//parents and updates the mySubtreeMaxWeight if it was equal to the old max weight
		if(t.isWord){
			double oldMaxWeight = t.mySubtreeMaxWeight;
			Node p = t.parent;
			while(p!=null){
				if(p.mySubtreeMaxWeight==oldMaxWeight){
					p.mySubtreeMaxWeight = weight;
				}
				p = p.parent;
			}
		}
		t.setWeight(weight);
		t.setWord(word);
		t.isWord = true; // walked down path, mark this as a word
	}


	@Override
	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in the trie with the largest weight which match the given prefix,
	 * in descending weight order. If less than k words exist matching the given
	 * prefix (including if no words exist), then the array instead contains all
	 * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
	 * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
	 * 2) should return {"air"}
	 * 
	 * @param prefix
	 *            - A prefix which all returned words must start with
	 * @param k
	 *            - The (maximum) number of words to be returned
	 * @return An array of the k words with the largest weights among all words
	 *         starting with prefix, in descending weight order. If less than k
	 *         such words exist, return an array containing all those words If
	 *         no such words exist, return an empty array
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public String[] topKMatches(String prefix, int k) {
		//Uses the a priority queue and arraylist of nodes to return the K highest weighted words.
		if(prefix==null){
			throw new NullPointerException("prefix is null");
		}
		//new
		if(k==0){
			return new String[0];
		}
		PriorityQueue<Node> nodes = new PriorityQueue<Node>(new Node.ReverseSubtreeMaxWeightComparator());
		ArrayList<Node> result = new ArrayList<Node>();
		Node t = myRoot;
		//navigate to the end of the prefix
		for(int i=0; i<prefix.length(); i++){
			char ch = prefix.charAt(i);
			t=t.getChild(ch);
			if(t==null){
				return new String[0];
			}
		}
		nodes.add(t);
		while(nodes.size()!=0){
			//As soon as the result ArrayList is at least k big, we must check to see if it contains the top k
			//matches. This is done by seeing if the mySubtreeMaxWeight of the head of the priority queue
			//is less than the kth element in the sorted arraylist. If this is true, that must mean that we
			//cannot possibly see a larger weighted in the trie, so we have found the top K, and we then break
			//the while loop.
			if(result.size()>=k){
				Collections.sort(result, Collections.reverseOrder());
				if(nodes.peek().mySubtreeMaxWeight < result.get(k-1).getWeight()){
					break;
				}
			}
			//maintenance of the priority queue and result arraylist.
			t=nodes.poll();
			if(t.isWord){
				result.add(t);
			}
			for(Node child:t.children.values()){
				nodes.add(child);
			}
		}
		//returning the k (or less) highest weighted words.
		Collections.sort(result, Collections.reverseOrder());
		int size =result.size();
		if(size>k){
			size=k;
		}
		String[] ret = new String[size];
		for(int j=0; j<size; j++){
			ret[j] = result.get(j).getWord();
		}
		return ret;
	}

	@Override
	/**
	 * Given a prefix, returns the largest-weight word in the trie starting with
	 * that prefix.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from _terms with the largest weight starting with
	 *         prefix, or an empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 * 
	 */
	public String topMatch(String prefix) {
		//Find the topmatch by navigating from the prefix down the trie, following the mySubtreeMaxWeight
		//down until the word with that weight is found.
		if(prefix==null){
			throw new NullPointerException("prefix is null");
		}
		Node t = myRoot;
		for(int k=0; k<prefix.length(); k++){
			char ch = prefix.charAt(k);
			t=t.getChild(ch);
			if(t==null){
				return "";
			}
		}
		double maxWeight = t.mySubtreeMaxWeight;
		while(!t.isWord){
			for(Node n: t.children.values()){
				if(n.mySubtreeMaxWeight==maxWeight){
					t=n;
					break;
				}
			}
		}
		return t.getWord();
	}
	public static void main(String[] args){
		//for testing
		String[] names = { "ape", "app", "ban", "bat", "bee", "car", "cat", "bee"};
		double[] weights = { 6, 4, 2, 3, 5, 7, 1, 1};
		Autocompletor ac = new TrieAutocomplete(names, weights);
		System.out.println(Arrays.toString(ac.topKMatches("b", 0)));
	}

}
