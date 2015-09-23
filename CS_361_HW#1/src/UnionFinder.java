import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The class is a abstract class that contains the abstract methods used in the QFUnionFinder, QUUnionFinder and WQUUnionFinder.
 * The main() function in this class allows for the QFUnionFinder, QUUnionFinder and WQUUnionFinder classes to inherit from this one.
 *  
 * @author darrenchu
 * @date January 28, 2015
 */
public abstract class UnionFinder {
	protected int[] id;
	protected int count;

	public UnionFinder(int N)
	{
		count = N;
		id = new int[N];
		for(int i = 0; i < N; i++)
			id[i] = i;
	}
	
	
	public abstract int count(); // list of abstract methods to be inherited 
	public abstract boolean connected(int p, int q);
	public abstract void union(int p, int q);
	public abstract int find(int p);
	public final void readInt() {
	}

	
	public static void main(String[] args)
	{
		String implementer = args[0]; //First item to be declared in terminal
		String fileName = args[1]; //Second item to be declared in terminal
		UnionFinder uf;
		try 
		{
			Scanner source = new Scanner(new File(fileName));
			
			
			//Selecting which Finder to use
			if(implementer.equals("F")) //If first string input in terminal is F, use QFUnionFinder
			{
				uf = new QFUnionFinder(source.nextInt());
			}
			else if(implementer.equals("U")) //If first string input in terminal is U, use QUUnionFinder
			{
				uf = new QUUnionFinder(source.nextInt());
			}
			else if(implementer.equals("W")) //If first string input in terminal is W, use WQUUnionFinder
			{
				uf = new WQUUnionFinder(source.nextInt());
			}
			else //If first string input in terminal is not F, U, or W, print error statement
			{
				System.out.println("Error: You have provided an invalid argument. Please input a correct argument.");
				return;
			}
			
			
			
			long time = System.currentTimeMillis(); //Start timer when search begins
			{
				while(source.hasNextInt())
				{
					int p = source.nextInt();
					int q = source.nextInt();
					
					if(!uf.connected(p, q))
					{
						uf.union(p, q);
						System.out.println(p + " " + q);
					}
				}
			}
			time = System.currentTimeMillis() - time; //Adjust the time
			System.out.println(uf.count() + " components " + time + " time");
		}
		
		
		
		catch(FileNotFoundException e) 
		{
			System.out.println("Error loading from file: "+ fileName + ". Unable to locate the File.");//catch the exception if there is one thrown
		}
	}
}