import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class represents the city nodes
 * @author darrenchu
 *
 */
public class City {	
	protected ArrayList<Edge> edgeList;
	protected String city;
	protected double latitude;
	protected double longitude;
	protected double g = 0;
	protected double f = 0;
	
	public City(String city, double latitude, double longitude){
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
		edgeList = new ArrayList<Edge>();
	}
	
	public void addAdjacent(City v, double distance){
		edgeList.add(new Edge(v, distance)); 
	}
	
	/**
	 * Get the vertices adjacent to this one
	 * @return the adjacent vertices
	 */
	public ArrayList<Edge> getAdjacencyList(){
		return edgeList;
	}
	
	public double getLat()
	{
		return latitude;
	}
	
	public double getLong()
	{
		return longitude;
	}

	
}