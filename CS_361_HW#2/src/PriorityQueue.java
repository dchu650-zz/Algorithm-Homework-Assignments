import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author darrenchu
 * @date Feb 13th 2015
 * 
 * This PriorityQueue class replicates a manager of a sports team.
 * The team inserts a new player into the game based on the player's skill.
 * 
 * @param <T>
 */
public class PriorityQueue<T> {
	private T[] playerRoster;
	private int[] values;
	private static int currentSize;
	private static int incSize;
	private static int resizeCounter = 0;
	private int startingSize;
	private int nodesRemoved;
	private int nodesAdded;
	private int size = 0;
	private static Stack stack;

	@SuppressWarnings("unchecked")
	
	/**
	 * The constructor of the PriorityQueue
	 * @param initialSize The variable for setting the initial size for the player roster list
	 * @param stepSize The variable for setting the increment size
	 */
	public PriorityQueue(int initialSize, int stepSize)
	{
		playerRoster = (T[]) new Object[initialSize]; //set as a constant value at the moment!
		values = new int[initialSize];
		startingSize = initialSize;
		incSize = stepSize+1;
	}

	/**
	 * Method for inserting the player into the roster list.
	 */
	public void insert(T p, int score) 
	{
		playerRoster[size+1] = p;
		values[size+1] = score;
		swim(size+1);
		size++;
	}

	/**
	 * Method for removing the player from the roster list and add them into the game
	 * @return The player with the highest skill.
	 */
	public T remove()
	{
		T max = playerRoster[0];
		exch(0,size);
		playerRoster[size] = null;
		values[size] = -1;
		sink(0);
		size = size-1;
		return max;
	}

	/**
	 * Method for returning the number of resizes in the roster list.
	 * @return The number of resizes
	 */
	public int getNumResizes()
	{
		return resizeCounter;
	}
	
	/**
	 * Method that returns the number of remaining players on the bench
	 * @return The number of remaining players
	 */
	public int remainingPlayers()
	{
		return nodesAdded - nodesRemoved;
	}

	/**
	 * Method that returns a boolean true when the player roster is 0
	 * @return True when no remaining players on roster
	 */
	public boolean isEmpty()
	{
		return playerRoster.length==0;
	}


	/**
	 * Method that compares the score of each player from the parent and the root
	 * @param i The parent integer
	 * @param j The root integer
	 * @return Returns true if the parent is smaller than the root
	 */
	private boolean less(int i,int j)
	{
		return (values[i])<(values[j]);
	}
	
	/**
	 * Method that exchanges the parent with their root
	 * @param i The parent integer
	 * @param j The root integer
	 */
	private void exch(int i, int j)
	{
		T t = playerRoster[i];
		int v = values[i];
		playerRoster[i] = playerRoster[j];
		values[i] = values[j];
		playerRoster[j] = t; 
		values[j] = v;
	}

	/**
	 * Recursive method that exchanges the root with the parent if the root is larger than the parent
	 * @param k The root variable position
	 */
	private void swim(int k)
	{
		while(k > 1 && less(k/2, k))
		{
			exch(k/2, k);
			swim(k/2);
		}
	}
	
	/**
	 * Method that exchanges the parent with their root if the parent is less than the root 
	 * @param k The parent in question
	 */
	private void sink(int k)
	{
		while (2*k <= size+1)
		{
			int j = 2*k;
			if(j < size+1 && less(j, j+1)) 
				j++;
			if(!less(k,j))
				break;
			exch(k,j);
			k = j;
		}
	}

	
	/**
	 * 
	 */
	private void resize()
	{
		int replacedSize = 0;
		if(size+1 == playerRoster.length)
		{
			replacedSize = incSize + size;
			T[] tempList =(T[]) new Object[playerRoster.length];
			int[] tempNums = new int[values.length];
			tempList = Arrays.copyOf(playerRoster, playerRoster.length);
			tempNums = Arrays.copyOf(values, values.length);
			playerRoster = (T[]) new Object[replacedSize];
			values = new int[replacedSize];
			for(int i = 0; i<tempList.length;i++)
			{
				playerRoster[i] = tempList[i];
				values[i] = tempNums[i];
			}
			resizeCounter++;
		}
		if(replacedSize == playerRoster.length-1 - 2*incSize)
		{
			if(size < startingSize)
				size = startingSize;
			else
				size = size - incSize;
			resizeCounter--;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	/**
	 * Method for loading the text file.
	 * This method also contains the insertion and removal methods of players into the roster.
	 * This method also contains the resizing function within the method
	 * @param fileName
	 */
	private void loadList(String fileName)
	{
		Scanner source;
		try 
		{
			source = new Scanner(new File(fileName));
			{

				while(source.hasNextLine())
				{
					String[] tokens = source.nextLine().split("/");

					if(!tokens[0].equals("GO!"))
					{
						this.insert((T)tokens[0], Integer.parseInt(tokens[1]));
						if(size == playerRoster.length-1)
						{
							this.resize();
						}
						nodesAdded++;
					}

					else
					{
						stack.push(this.remove());
						nodesRemoved++;
					}
				}
			}
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * The main method
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException
	{
		String fileName = args[0];
		try
		{
			currentSize = Integer.parseInt(args[1]); // integer values for the initial size of the roster
			incSize =  Integer.parseInt(args[2]); //initial increment size
		}
		catch (NumberFormatException e) {
	        System.err.println("Argument args[1] and args[2] must be integers. Please input two positive integer values.");
	        System.exit(1);
	    }
		catch(IndexOutOfBoundsException e) 
		{
			currentSize = 30;
			incSize = 10;
		}
		
		PriorityQueue p = new PriorityQueue(currentSize, incSize);
		p.loadList(fileName);
		for(Object t: stack) //my first item is always null for some reason. I couldn't figure out why so I made a bit of a quick solution.
		{					//not an answer to the problem, but still works
			try
			{
				if(!t.equals(null))
					System.out.println(t + " enters the game.");
			}
			catch(NullPointerException e)
			{

			}
		}
		if(p.isEmpty())
		{
			System.out.println("No one is ready!");
		}

		System.out.println("At the end, there were " + p.remainingPlayers() + " players left.");
		System.out.println("The array was resized " + resizeCounter +" times.");
	}
}