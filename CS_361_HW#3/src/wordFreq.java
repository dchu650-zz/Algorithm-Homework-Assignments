import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author darrenchu
 * @date March 8th, 2015
 * This wordFreq class receives a file input and returns the number of time a word is present in the file.
 * This class uses a linear probing hash table.
 * @param <Key, Integer> 
 */
public class wordFreq<Key, Value> extends SymbolTable
{	
	private int numKeys=0;           // current number of items within the hash table
	private int tableSize;           // size of linear probing table
	private Key[] keys;      // key[] that contains the word
	private Value[] vals;    // int[] that contains the number of occurrences 
	private Stack stack = new Stack(); // Stack used to return the list of distinct words from the text
	private static final int INITIAL_CAPACITY = 997;
	
	/**
	 * Constructor for the wordFreq class
	 * Sets the initial capacity as 997
	 */
	public wordFreq()
	{
		this(INITIAL_CAPACITY);
	}

	/**
	 * Constructor for the wordFreq class
	 * @param capacity set the size of the table
	 */
	public wordFreq(int capacity)
	{
		tableSize = capacity;
		keys = (Key[])   new Object[tableSize];
		vals = (Value[]) new Object[tableSize];
	}

	/**
	 * Method used to get the value associated with the string
	 * Uses linear probing to search through the table of keys
	 */
	@Override
	public Object get(Object key) 
	{
		for (int i = hash(key); keys[i] != null; i = (i + 1) % tableSize) 
		{
			if (keys[i].equals(key))
			{
				return vals[i];
			}
		}
		return null;
	}

	/**
	 * Method used to delete the key and value.
	 * Once the key and value are deleted, rehash the keys within the table
	 */
	@Override
	public void delete(Object key) 
	{
		if (!this.contains(key))  //check is the key exists
		{
			return;
		}
		
		int i = hash(key); //find the hash value of the key
		while (!key.equals(keys[i]))  //locate the key if the key is not in the original hash position 
		{
			i = (i + 1) % tableSize;
		}

		keys[i] = null; //delete the key and value
		vals[i] = null;

		i = (i + 1) % tableSize; //rehash the keys and values that were after the deleted key-value pair and reinsert them back into the table
		while (keys[i] != null) 
		{
			Key   keyToRehash = keys[i];
			Value valToRehash = vals[i];
			keys[i] = null;
			vals[i] = null;
			numKeys--;  
			put(keyToRehash, valToRehash);
			i = (i + 1) % tableSize;
		}

		numKeys--;        
		if (numKeys > 0 && numKeys <= tableSize/4) //resize the table
		{
			resize(tableSize/2);
		}
		assert check(); //perform the check method
	}

	/**
	 * Method that returns the hash table 
	 */
	@Override
	public int size() 
	{
		return numKeys;
	}

	/**
	 * Method puts all of the keys into a stack and returns the list of keys
	 */
	@Override
	public Iterable<Key> keys() 
	{
		Stack<Key> stack = new Stack<Key>();
		for(int i = 0; i<tableSize; i++)
		{
			if(keys[i] != null)
			{
				stack.push(keys[i]);
			}
		}
		return stack;	
	}

	/**
	 * Method that inserts the key-value pair into the hash table
	 */
	@Override
	public void put(Object key, Object value) {
		// TODO Auto-generated method stub
		if (value == null) //if there is no value, delete the key
		{
			delete(key);
			return;
		}
		
		if (numKeys >= tableSize/2) //resize the table if the table becomes halfway filled
		{
			resize(tableSize*2);
		}

		int i;
		for (i = hash(key); keys[i] != null; i = (i + 1) % tableSize) //linear probe and add items into the table
		{
			if (keys[i].equals(key)) 
			{ 
				vals[i] = (Value) value;
				return; 
			}
		}
		keys[i] = (Key) key;
		vals[i] = (Value) value;
		numKeys++;
	}

	/**
	 * Method used to hash and mod the key
	 * @param key
	 * @return the hashed value for the table
	 */
	private int hash(Object key) 
	{
		return (key.hashCode() & 0x7fffffff) % tableSize;
	}

	/**
	 * Method used to resize the table
	 * Creates a new table based on the new size and copies all the data into the new table
	 * @param capacity
	 */
	private void resize(int capacity) 
	{
		wordFreq<Key, Value> temp = new wordFreq<Key, Value>(capacity);
		for (int i = 0; i < tableSize; i++) 
		{
			if (keys[i] != null) 
			{
				temp.put(keys[i], vals[i]);
			}
		}
		keys = temp.keys;
		vals = temp.vals;
		tableSize  = temp.tableSize;
	}


	/**
	 * Method for used within the delete method
	 * @return True if the table can find each key with the get method
	 */
	private boolean check() 
	{

		if (tableSize < 2*numKeys) //checks if the table is halfway filled 
		{
			return false;
		}

		for (int i = 0; i < tableSize; i++) //get() checker
		{
			if (keys[i] == null) 
			{
				continue;
			}
			else if (get(keys[i]) != vals[i]) 
			{
				return false;
			}
		}
		return true;
	}

}
//	/**
//	 * 
//	 * @param args
//	 * @throws IOException
//	 */
//	public static void main(String[] args) throws IOException 
//	{ 
//		Scanner scanner = new Scanner(System.in);
//		String fileName = args[0];
//		wordFreq wf = new wordFreq();
//		try 
//		{
//			scanner = new Scanner(new File(fileName));
//			{
//				scanner.useDelimiter("[^A-Za-z0-9'_]+");
//				while(scanner.hasNext())
//				{
//					String word = scanner.next().toLowerCase();
//					while(word.length()>0&&word.charAt(0)=='\'')
//					{
//						word = word.substring(1);
//					}
//					while(word.length()>0&&word.charAt(word.length()-1) == '\'')
//					{
//						word = word.replace("'", "");
//					}
//					if(wf.contains(word))
//					{
//						wf.put(word, wf.get(word)+1);
//					}
//					else
//					{
//						wf.put(word, 1);
//					}
//				}
//				System.out.println("This text contains " + wf.size() + " distinct words.");
//				System.out.println("Please enter a word to get its frequency or hit enter to leave.");
//				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//				String s = bufferRead.readLine().toLowerCase();
//				while(!s.equals(""))
//				{
//					if(s.charAt(0)=='-')
//					{
//						wf.delete(s.substring(1, s.length()));
//						System.out.println("The word " + s.substring(1, s.length()) + " has been deleted");
//					}
//					else
//					{
//						if(wf.get(s) == -1)
//						{
//							System.out.println(s + " does not appear.");
//						}
//						else
//						{
//						System.out.println(s + " appears " + wf.get(s) + " times.");
//						}
//					}
//					scanner = new Scanner(System.in);
//					s = scanner.nextLine().toLowerCase();
//				}
//			}
//		} 
//		catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//}