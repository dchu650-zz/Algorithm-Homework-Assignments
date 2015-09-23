import java.util.Comparator;
import java.util.Random;

/**
 * This class represents the edge between the cities
 * @author darrenchu
 *
 */
public class Edge 
{
	protected City neighboringCity;
	protected double distance;
	
	public Edge(City neighboringCity, double weight)
	{
		this.neighboringCity = neighboringCity;
		this.distance = weight;
	}
	
	protected double getWeight()
	{
		return distance;
	}
	
}
