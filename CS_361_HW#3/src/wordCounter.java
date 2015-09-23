import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * The wordCounter class is the the helper class to the wordFreq class.
 * This class gets the an file input and take the user's word input.
 * The class will then return the frequency of the requested word.
 * @author darrenchu
 * @date March 8th, 2015
 */
public class wordCounter 

{
	/**
	 * Main method for the wordFreq class
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException 
	{ 
		Scanner scanner = new Scanner(System.in);
		String fileName = args[0];
		wordFreq<String, Integer> wf = new wordFreq();
		try 
		{
			scanner = new Scanner(new File(fileName));
			{
				scanner.useDelimiter("[^A-Za-z0-9'_]+");
				while(scanner.hasNext())
				{
					String word = scanner.next().toLowerCase();
					while(word.length()>0&&word.charAt(0)=='\'')
					{
						word = word.substring(1);
					}
					while(word.length()>0&&word.charAt(word.length()-1) == '\'')
					{
						word = word.substring(0,word.length()-1);
					}
					if(wf.contains(word))
					{
						wf.put(word, (Integer) wf.get(word) + 1);
					}
					else
					{
						wf.put(word, 1);
					}
				}
//				System.out.println(wf.keys()); testing
				System.out.println("This text contains " + wf.size() + " distinct words.");
				System.out.println("Please enter a word to get its frequency or hit enter to leave.");
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				String s = bufferRead.readLine().toLowerCase();
				while(!s.equals(""))
				{
					if(s.charAt(0)=='-')
					{
						wf.delete(s.substring(1, s.length()));
						System.out.println("The word " + s.substring(1, s.length()) + " has been deleted");
					}
					else
					{
						if((Integer)wf.get(s) == null)
						{
							System.out.println(s + " does not appear.");
						}
						else
						{
							System.out.println(s + " appears " + wf.get(s) + " times.");
						}
					}
					if(wf.isEmpty())
					{
						System.out.println("There are no words!");
					}
					scanner = new Scanner(System.in);
					s = scanner.nextLine().toLowerCase();
				}
			}
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
