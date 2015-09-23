/**
 * The QFUnionFinder class represents the class that uses the quick find implementation
 * Inherits the UnionFinder
 * @author darrenchu
 * @date January 28, 2015
 */
public class QFUnionFinder extends UnionFinder{
	
	 /**
     * Initializes an empty data structure inherited from the UnionFinder
     * @param N the number of objects
     */
	public QFUnionFinder(int N) {
		super(N);
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
		int pID = find(p);
		int qID = find(q);
		
		
		if(pID == qID) return;


		for (int i = 0; i < id.length; i++)
		{
			if(id[i]==pID)
			{
				id[i] = qID;
			}
		}

		count --;
	}

	/**
     * Simply returns the identifier component to int p
     * @param p int represents one integer component
     * @return the component identifier for the component p
     */
	public int find(int p) {
		return id[p];
	}

	
	/**
	 * The method that becomes invoked if F is the first argument in the UnionFinder  
	 * @param args
	 */
	public static void main(String[] args)
	{
		String fileName = args[0];
		args = new String[]{"F", fileName};
		UnionFinder.main(args);
	}
	
	
}
