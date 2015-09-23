/**
 * @author darrenchu
 * @date March 22nd, 2015
 * This is a search and sort algorithm that utilizes the red-black tree schematic
 * 
 */
public class wordfreq2 <Key, Value> extends OrderedSymbolTable
{
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	private Node root;
	
	/**
	 * 
	 * @author darrenchu
	 * Create a node class to utilize within the red-black tree
	 */
	public class Node 
	{
		private Key key;
		private Value value;
		private Node left, right;
		private boolean color;
		private int subTreeSize;

		public Node(Comparable key, Object value, boolean color, int subTreeSize)
		{
			this.key = (Key) key;
			this.value = (Value) value;
			this.color = color;
			this.subTreeSize = subTreeSize;
		}

//		public void setColor(boolean color) 
//		{
//			// TODO Auto-generated method stub
//			root.color = color;
//		}
		
		/**
		 * Recalculate the size of the subTreeSize
		 * @return The new subTreeSize
		 */
		public int recalcSize() 
		{
			// TODO Auto-generated method stub
			return subTreeSize = size(left) + size(right) + 1;
		}

	}
	
	/*
	 *  NODE METHODS
	 */
	
	/**
	 * Method used to check whether a node is red or black
	 * @param node The node in question
	 * @return boolean True if node is red, False if node is black
	 */
	private boolean isRed (Node node)
	{
		if(node == null)
		{
			return false;
		}
		return node.color == RED;
	}
	
	@Override
	/**
	 * Method used to return the size of the tree
	 * @return int size of the subtree
	 */
	public int size() {
		// TODO Auto-generated method stub
		return size(root);
	}
	
	/**
	 * Method used to return the size of the tree located at the given node
	 * @param node The node in question (will be the root) 
	 * @return int Size of the size of the tree located at the given node
	 */
	private int size(Node node)
	{
		if(node == null)
		{
				return 0;
		}
		return node.subTreeSize;
	}
	
	@Override
	/**
	 * Method used to add nodes into the tree
	 * The root color is always black, therefore we must enforce the root to be black
	 * @param key The word that is given from the text file
	 * @param value The number of times the word appears within the text file
	 */
	public void put(Comparable key, Object value) {
		// TODO Auto-generated method stub
		root = findAndAdd(root, key, value);
		root.color = BLACK;
	}
	
	/**
	 * Method used to add nodes into the tree. This method recursively adds the key and value pairs to a node in the red-black tree in the proper position. 
	 * @param node The node in question
	 * @param key The word that is given from the text file
	 * @param value The number of times the word appears within the text file
	 * @return node Returns the root node after all of the flips and adjustments have been made
	 */
	public Node findAndAdd(Node node, Comparable key, Object value)
	{
		if(node == null)
		{
			return new Node(key, value, RED, 1);
		}
		int cmp = key.compareTo(node.key);
		if (cmp<0) 
		{
			node.left = findAndAdd(node.left, key, value); //traverse left
		}
		else if (cmp>0) 
		{
			node.right = findAndAdd(node.right, key, value); //traverse right
		}
		else 
		{
			node.value = (Value) value;
		}
		// going up--fix things
		if (isRed(node.right) && !isRed(node.left)) 
		{
			node = rotateLeft(node);
		}
		if (isRed(node.left) && isRed(node.left.left)) 
		{
			node = rotateRight(node);
		}
		if (isRed(node.right) && isRed(node.left)) 
		{
			flipColors(node);
		}
		// adjust the size & go home
		//node.subTreeSize = size(node.left) + size(node.right) + 1; //recalcSize() method.
		node.subTreeSize = node.recalcSize();
		return node;
	}


	/*
	 *  SUPPORT METHODS FOR findAndAdd METHOD
	 */
	/**
	 * Method used to flip the colors of a given node and its children
	 * If the color is red, it becomes black
	 * If the color is black, it becomes red
	 * @param node The parent node that we want to start the color flip.
	 */
	private void flipColors(Node node) {
		// TODO Auto-generated method stub
		node.color = !node.color; //flip the colors for each node
		node.left.color = !node.left.color;
		node.right.color = !node.right.color;
	}

	/**
	 * Method used to rotate the nodes around the given node counterclockwise 
	 * @param node The given node that is going to be rotated at
	 * @return node The new parent node after the rotation
	 */
	private Node rotateLeft(Node node) {
		// TODO Auto-generated method stub
		Node temp = node.right; 
		node.right = temp.left;
		temp.left = node;
		temp.color = temp.left.color;
		temp.left.color = RED;
		temp.subTreeSize = node.subTreeSize;
		node.subTreeSize = size(node.left) + size(node.right) + 1;
		return temp;
	}
	
	/**
	 * Method used to rotate the nodes around the given node clockwise
	 * @param node The given node that is going to be rotated at
	 * @return node The new parent node after the rotation
	 */
	private Node rotateRight(Node node) {
		// TODO Auto-generated method stub
		Node temp = node.left;
		node.left = temp.right;
		temp.right = node;
		temp.color = temp.right.color;
		temp.right.color = RED;
		temp.subTreeSize = node.subTreeSize;
		node.subTreeSize = size(node.left) + size(node.right) + 1;
		return temp;
	}
	
	
	@Override
	/**
	 * A get method that returns the number of times the given key appears
	 * @param key The word that is given by the user
	 * @return The value associated with the given key
	 */
	public Object get(Comparable key) {
		// TODO Auto-generated method stub
		return get(root, key);
	}
	
	/**
	 * The method used to return the number of times the given key appears
	 * @param node The node that will traverse down the tree
	 * @param key The word that is given by the user
	 * @return The value associated with the given key
	 */
	private Value get(Node node, Comparable key)
	{
		while(node != null)
		{
			int cmp = ((Comparable) key).compareTo(node.key);
			if(cmp < 0)
			{
				node = node.left;
			}
			else if(cmp > 0)
			{
				node = node.right;
			}
			else
			{
				return node.value;
			}
		}
		return null;
	}
	
	@Override
	/**
	 * Method used to delete when given a key
	 * @param key The key that the user wants to delete
	 */
	public void delete(Comparable key) {
		// TODO Auto-generated method stub
		if(!contains(key))
			{
				System.err.println("This symbol table does not contain " + key);
				return;
			}
		if(!isRed(root.left) && !isRed(root.right))
		{
			root.color = RED;
		}
		root = delete(root, key);
		if(!isEmpty())
		{
			//root.color = RED;  
			root.color = BLACK;
		}
	}
	
	/**
	 * Method used to delete when given a key
	 * @param node The node that will traverse the tree for the deletion
	 * @param key The key that we are going to delete from the tree
	 * @return node The root after fixing the balance and size of tree
	 */
	private Node delete(Node node, Comparable key)
	{
		if(key.compareTo(node.key) < 0)
			{
				if(!isRed(node.left) && !isRed(node.left.left))
				{
					flipColors(node);
					if (isRed(node.right.left)) 
					{ 
						node.right = rotateRight(node.right);
						node = rotateLeft(node);
						flipColors(node);
					}
				}
				node.left = delete(node.left,key);
			}
		else
		{
			if(isRed(node.left))
			{
				node = rotateRight(node);
			}
			if(key.compareTo(node.key) == 0 && (node.right == null))
			{
				return null;
			}
			if(!isRed(node.right) && !isRed(node.right.left))
			{
				flipColors(node);
				if (isRed(node.left.left)) 
				{ 
					node = rotateRight(node);
					flipColors(node);
				}
			}
			if(key.compareTo(node.key) == 0)
			{
				Node temp = minNode(node.right);
				node.key = temp.key;
				node.value = temp.value;
				node.right = deleteMin(node.right);
			}
			else
			{
				node.right = delete(node.right, key);
			}
		}
		return balanceTree(node);
	}
	
	/**
	 * Method used to delete the smallest node
	 * @param node The node that we will use to traverse the subtree
	 * @return 
	 */
	private Node deleteMin(Node node) 
	{ 
        if (node.left == null)
        {
            return null;
        }

        if (!isRed(node.left) && !isRed(node.left.left))
        {
        	flipColors(node);
        	if (isRed(node.right.left)) 
        	{ 
        		node.right = rotateRight(node.right);
        		node = rotateLeft(node);
        		flipColors(node);
        	}
        }
        node.left = deleteMin(node.left);
        return balanceTree(node);
    }
	
	/**
	 * Method used to return the balance and fix the tree to make sure the tree follows the red-black tree rules
	 * @param node The node in question that requires balancing
	 * @return node The node after the balance has been corrected
	 */
	private Node balanceTree(Node node) 
	{

        if (isRed(node.right))   
        {
        	node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) 
        {
        	node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right))   
        {
        	flipColors(node);
        }
        //node.subTreeSize = size(node.left) + size(node.right) + 1;
        node.subTreeSize = node.recalcSize();
        return node;
    }
	
	
	@Override
	/**
	 * Method used to get the minimum key
	 */
	public Comparable getMinKey() {
		// TODO Auto-generated method stub
		if(isEmpty())
		{
			return null;
		}
		return (Comparable) minNode(root).key;
	}
	
	/**
	 * Method used to get the minimum node. (Traverse down the left side of the tree until the node can no longer traverse left)
	 * @param node The node in question
	 * @return node return the smallest node
	 */
	private Node minNode(Node node)
	{
		if(node.left == null)
		{
			return node;
		}
		else
		{
			return minNode(node.left);
		}
	}
	
	
	@Override
	/**
	 * Method used to get the maximum key
	 */
	public Comparable getMaxKey() {
		// TODO Auto-generated method stub
		if(isEmpty())
		{
			return null;
		}
		return (Comparable) maxNode(root).key;
	}
	
	/**
	 * Method used to get the maximum node. (Traverse down the right side of the tree until the node can no longer traverse right)
	 * @param node The node in question
	 * @return node return the largest node
	 */
	public Node maxNode(Node node)
	{
		if(node.right == null)
		{
			return node;
		}
		else
		{
			return maxNode(node.right);
		}
	}
	
	@Override
	/**
	 * Method used to find the predecessor of the given key
	 * @param key The key in question
	 * @return The predecessor to the given key
	 */
	public Comparable findPredecessor(Comparable key) {
		// TODO Auto-generated method stub
		Node node = findPredecessor(root, key);
		if(key == null)
		{
			return null;
		}
		else
		{
			if(key.equals(this.getMinKey()))
			{
				return this.getMaxKey();
			}
			return (Comparable) node.key;
		}
	}
	
	/**
	 * Method used to find the predecessor of the given key
	 * @param node The node that will traverse the tree
	 * @param key The key in question
	 * @return The node that the predecessor is located
	 */
	private Node findPredecessor(Node node, Comparable key)
	{
		if(node == null)
		{
			return null;
		}
		int cmp = key.compareTo(node.key);
		if(cmp <= 0)
		{
			return findPredecessor(node.left, key);
		}
		Node right = findPredecessor(node.right, key);
		if(right != null)
		{
			return right;
		}
		else
		{
			return node;
		}
	}
	
	@Override
	/**
	 * Method use to find the successor of the given key
	 * @param key The key in question 
	 * @return The successor of the given key
	 */
	public Comparable findSuccessor(Comparable key) {
		// TODO Auto-generated method stub
		Node node = findSuccessor(root, key);
		if(key == null)
		{
			return null;
		}
		else
		{
			if(key.equals(this.getMaxKey()))
				{
					return this.getMinKey();
				}
			return (Comparable) node.key;
		}
	}
	
	/**
	 * Method used to find the successor of the given key
	 * @param node The node that will traverse the tree
	 * @param key The key in question
	 * @return The node that the successor is located
	 */
	private Node findSuccessor(Node node, Comparable key)
	{
		if(node == null)
		{
			return null;
		}
		int cmp = key.compareTo(node.key);
		if(cmp >= 0)
		{
			return findSuccessor(node.right, key);
		}
		Node left = findSuccessor(node.left, key);
		if(left != null)
		{
			return left;
		}
		else
		{
			return node;
		}
	}
	
	@Override
	/**
	 * Method used to find the rank of a given key
	 * @param key The key in question
	 * @return int The rank of the key
	 */
	public int findRank(Comparable key) {
		// TODO Auto-generated method stub
		return findRank(key, root);
	}
	
	/**
	 * Method used to find the rank of a given key
	 * @param key The key in question
	 * @param node The node that will traverse the tree
	 * @return int The rank of the tree (the size of the subtree where the key is located)
	 */
	private int findRank(Comparable key, Node node)
	{
		if(node == null)
		{
			return 0;
		}
		int cmp = key.compareTo(node.key);
		if(cmp < 0)
		{
			return findRank(key, node.left);
		}
		else if(cmp > 0)
		{
			return 1 + size(node.left) + findRank(key, node.right);
		}
		else
		{
			return size(node.left);
		}
	}
	
	@Override
	/**
	 * Method used to find the key at the given rank  
	 * @param int The rank of the key that the user is looking for
	 * @return Comparable The key associated at the given rank
	 */
	public Comparable select(int rank) {
		// TODO Auto-generated method stub
		if(rank >= size() || rank<0)
		{
			return null;
		}
		Node node = select(root, rank);
		return (Comparable) node.key;
	}
	
	/**
	 * Method used to find the key at the given rank
	 * @param node The node that will traverse the red-black tree
	 * @param rank The rank in question
	 * @return node The node with the rank that the key is located  
	 */
	private Node select(Node node, int rank)
	{
		int sizeLeft = size(node.left);
		if(sizeLeft>rank)
		{
			return select(node.left, rank);
		}
		else if(sizeLeft<rank)
		{
			return select(node.right, rank-sizeLeft-1);
		}
		else
		{
			return node;
		}
	}
	
}
