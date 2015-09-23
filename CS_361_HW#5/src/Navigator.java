import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * This class is the main navigator class that determines whether there is a path between cities
 * @author darrenchu
 *
 */
public class Navigator 
{
	public Navigator()
	{
		
	}
	protected HashMap<String, City> cities = new HashMap<String, City>();
	
	public class cityComparator implements Comparator<City>{
		
		public cityComparator(){}		
		public int compare(City city1, City city2) {
			if(city1.f == city2.f)
				return 0;
			if(city1.f < city2.f) //if the other vertex is less than this one we indicate it
				return -1;
			else 
				return 1;
		}	
	}
	public boolean isPath(City openCity, City destination)
	{
		PriorityQueue<City> openList = new PriorityQueue<City>(50, new cityComparator());
		ArrayList<City> closedList = new ArrayList<City>();
		openList.add(openCity);
		Hashtable<String, String> previousCities = new Hashtable<String,String>();
		previousCities.put(openCity.city, "");
		double tempG = 0;
		openCity.f = openCity.g + this.calcH(openCity, destination);
		while(!openList.isEmpty())
		{
			City activeCity = openList.poll();
			if(activeCity.equals(destination))
			{
				return addPath(previousCities, activeCity.city);
			}
			closedList.add(activeCity);
			for(Edge e:activeCity.getAdjacencyList())
			{
				City neighbor = e.neighboringCity;
				if(closedList.contains(neighbor))
				{
					continue;
				}
				double tempG2 = activeCity.g + e.distance;
				if(!openList.contains(neighbor) || tempG2<neighbor.g)
				{
					previousCities.put(neighbor.city, activeCity.city);
					neighbor.g = tempG2;
					neighbor.f = neighbor.g + this.calcH(neighbor, destination);
					if(!openList.contains(neighbor))
					{
						openList.add(neighbor);
					}
				}
			}
			closedList.add(activeCity);
		}
		return false;
	}
	
	/**
	 * Method used to build a path
	 * @param previousCities A hashtable of key value pairs
	 * @param activeCity
	 * @return true if there is a path
	 */
	public boolean addPath(Hashtable<String,String> previousCities, String activeCity)
	{
		ArrayList<String> path = new ArrayList<String>();
		String nextCity = activeCity;
		do
		{
			path.add(nextCity);
			nextCity = previousCities.get(nextCity);	
		}
		while(!nextCity.equals(""));
		System.out.print("Path Found: ");
		for (int i = path.size()-1; i>=0; i--)
		{
			System.out.print(path.get(i) + " - ");
		}
		System.out.println("(" + cities.get(activeCity).g + "km)");
		System.out.println("Please enter your query:");
		System.out.println("");
		return true;
	}
	
	/**
	 * Method used to add cities
	 * @param city name of the city
	 * @param latitude latitude location of city
	 * @param longitude longitude location of city
	 */
	public void addCity(String city, double latitude, double longitude)
	{
		cities.put(city, new City(city, latitude, longitude));
	}
	
	/**
	 * Method used to calculate the heuristic
	 * @param openNode first node in question
	 * @param goalNode goal node in question
	 * @return the distance between the 2 nodes
	 */
	public double calcH(City openNode, City goalNode)
	{
		double toRads = Math.PI/180;
		double sin = Math.sin(openNode.getLat()*toRads) * Math.sin(goalNode.getLat()*toRads);
		double cos = Math.cos(openNode.getLat()*toRads) * Math.cos(goalNode.getLat()*toRads) * Math.cos(openNode.getLong()*toRads- goalNode.getLong()*toRads);
		return Math.acos(sin + cos)  * 6371;
	}
	
	public static void main(String[] args){		
		Scanner scanner = new Scanner(System.in);
		Navigator navigator = new Navigator();
		String line = "";
		String emptyLine = "";
		String file = args[0];
		try
		{
			scanner = new Scanner(new File(file));
			{
				while(scanner.hasNextLine())
				{
					line = scanner.nextLine();
					if(!line.equals(emptyLine))
					{
						String[] cityName = line.split("\\t");
						String name = cityName[0];
						double latitude = Double.parseDouble(cityName[1]);
						double longitude = Double.parseDouble(cityName[2]);
						navigator.addCity(name, latitude, longitude);
					}
					else
					{
						while(scanner.hasNextLine())
						{
							line = scanner.nextLine();
							String[] cityName2 = line.split("\\t");
							City city1 = navigator.cities.get(cityName2[0]);
							City city2 = navigator.cities.get(cityName2[1]);
							double weight = Double.parseDouble(cityName2[2]);
							city1.addAdjacent(city2, weight);
							city2.addAdjacent(city1, weight);
						}
						scanner.close();
						System.out.println("File \"US-capitals.geo\" has been loaded.");
						System.out.println("Please enter your query:");
						System.out.println("The first letter of the city must be capitalized");
						System.out.println("");
						scanner = new Scanner(System.in);
						String cityInputs;
						do{
							cityInputs = scanner.nextLine();
							String[] cityPath = cityInputs.split("-");
							if(cityPath.length !=2)
							{
								System.out.println("Please enter the name of two cities separated by a dash.");
								System.out.println("Please reenter your query:");
								System.out.println("");
								continue;
							}
							City start = navigator.cities.get(cityPath[0]);
							City destination = navigator.cities.get(cityPath[1]);
							
							if(start==null)
							{
								System.out.println("The city " + cityPath[0] + " was either inserted incorrectly or does not exist.");
								System.out.println("Please try again.");
								System.out.println("Please enter a new query:");
								System.out.println("");
								continue;
							}
							if(destination == null){

								System.out.println("The city " + cityPath[1] + " was either inserted incorrectly or does not exist.");
								System.out.println("Please try again.");
								System.out.println("Please enter a new query:");
								System.out.println("");
								continue;
							}

							boolean pathExists = navigator.isPath(start, destination);

							if(!pathExists)
							{
								System.out.println("I'm sorry--there's not a path from " + start.city + " and " + destination.city);
								System.out.println("Please enter a new query:");
								System.out.println("");
							}

						}
						while(!cityInputs.equals(""));
					}
				}
			}
		}
		catch(FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
