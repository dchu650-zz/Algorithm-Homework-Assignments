/**
 * The WQUUnionFinder class represents the class that uses the weighted quick union implementation
 * Inherits the UnionFinder
 * @author darrenchu
 * @date January 28, 2015
 */
public class WQUUnionFinder extends UnionFinder{

	private int[] size; //the size of the subtree

	/**
     * Initializes an empty data structure inherited from the UnionFinder
     * Initialize the size of the tree
     * @param N the number of objects
     */
	public WQUUnionFinder(int N) {
		super(N);
		size = new int[N];
		for(int i = 0; i<N; i++) size[i]=1;
			
	}

	/**
     * Returns the number of components.
     * @return the number of components (between 1 and N)
     */
	public int count() {
		return count;
	}

	/**
     * Checks if int p and int q are in the same component
     * @param p int represents one integer to be compared
     * @param q int represents one integer to be compared
     * @return returns true if both p and q are in the same component and false if otherwise
     */
	public boolean connected(int p, int q) {
		
		return find(p)==find(q);
	}
	
	/**
	 * Combines the components of int p with the component of int q 
     * 
     * @param p int represents one integer to be combined
     * @param q int represents one integer to be combined
	 */
	public void union(int p, int q) {
		int i = find(p);
		int j = find(q);
		
		
		if(i == j) return;

		// make smaller root point to larger one
		if(size[i] < size[j])
		{
			id[i] = j;
			size[j] += size[i];
		}		
		else
		{
			id[j] = i; 
			size[i] += size[j];
		}
			count --;
	}

	/**
     * Simply returns the identifier component to int p
     * @param p int represents one integer component
     * @return the component identifier for the component p
     */
	public int find(int p) {
		while(p != id[p])
			p = id[p];
		return p;
	}

	/**
	 * The method that becomes invoked if W is the first argument in the UnionFinder  
	 * @param args
	 */
	public static void main(String[] args)
	{
		String fileName = args[0];
		args = new String[]{"W", fileName};
		UnionFinder.main(args);
	}
	
}