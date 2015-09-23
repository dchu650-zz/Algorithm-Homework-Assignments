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
public class wordCounter2 

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
		wordfreq2<String, Integer> wf = new wordfreq2();
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
						if(!word.equals(""))
							wf.put(word, (Integer) wf.get(word) + 1);
					}
					else
					{
						if(!word.equals(""))
							wf.put(word, 1);
					}
				}
//				System.out.println(wf.keys()); testing
				System.out.println("This text contains " + wf.size() + " distinct words.");
				System.out.println("Please enter a word to get its frequency or hit enter to leave.");
				System.out.println("Other Instructions:");
				System.out.println("Enter < or > followed by a word to find its predecessor or successor.");
				System.out.println("Enter - followed by a word to delete a word.");
				System.out.println("Enter & followed by a word to find its rank.");
				System.out.println("Enter * followed by a number to find the word at that rank. The first word starts at 0.");
				BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
				String s = bufferRead.readLine().toLowerCase();
				while(!s.equals(""))
				{
					if(s.charAt(0)=='&')
					{
						System.out.println(wf.findRank(s.substring(1, s.length())));
					}
					if(s.charAt(0)=='*')
					{
						try
						{
						System.out.println(wf.select(Integer.parseInt(s.substring(1, s.length()))));
						}
						catch(NumberFormatException e)
						{
							System.err.println("Please follow the * command with an integer!");
						}
					}
					if(s.charAt(0) == '<')
					{
						if(s.length() == 1)
						{
							System.out.println("The alphabetically-first word in the text is "+wf.getMinKey()+".");
						}
						else
						{
							System.out.println("The word " + wf.findPredecessor(s.substring(1,s.length())) + " comes before " + s.substring(1,s.length()));
						}
					}
					if(s.charAt(0) == '>')
					{
						if(s.length() == 1)
						{
							System.out.println("The alphabetically-last word in the text is "+wf.getMaxKey()+".");
						}
						else
						{
						System.out.println("The word " + wf.findSuccessor(s.substring(1,s.length())) + " comes after " +  s.substring(1,s.length()));
						}
					}
					if(s.charAt(0)=='-')
					{
						wf.delete(s.substring(1, s.length()));
						System.out.println("The word " + s.substring(1, s.length()) + " has been deleted");
					}
					else
					{
						if((Integer)wf.get(s) == null)
						{
							if(s.charAt(0) == '<' || s.charAt(0) == '>' ||s.charAt(0)=='&' || s.charAt(0)=='*')
							{
								
							}
							else
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
				System.out.println("Goodbye!");
			}
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
