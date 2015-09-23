
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * ************************************** INCOMPLETE *********************************************
 * Notes for Adam: This program is not complete. 
 * There is no check between the topological sort vs the Kosaraju-Sharir algorithm.
 * At the moment the program can correctly do both, but does not do them at the appropriate check.
 * Therefore, regardless if there is a cycle or not, the program will run both.
 * Lastly, in my program, you need to specify the size of the number of vertices within the file.
 * I apologize for the incomplete homework.
 * *********************************************************************************************** 
 * @author darrenchu
 *
 */
public class Toposort 
{
	public List<Integer> res;
	private Stack<Integer> cycle;
	private boolean[] onStack;
	private boolean[] marked;
	private int[] edgeTo;
	private int n;
	
	public Toposort(List<Integer>[] graph)
	{
		this.n = graph.length;	
		res = new ArrayList<>();
		marked = new boolean[n];
		onStack = new boolean[n];
		edgeTo = new int[n];	
	}
	
	private boolean hasParallelEdges(List<Integer>[] graph) {
        marked = new boolean[n];
        for (int v = 0; v < n; v++) {

            // check for parallel edges incident to v
            for (int w : graph[v]) {
                if (marked[w]) 
                {
                    cycle = new Stack<Integer>();
                    cycle.push(v);
                    cycle.push(w);
                    cycle.push(v);
                    return true;
                }
                marked[w] = true;
            }

            // reset so marked[v] = false for all v
            for (int w : graph[v]) 
            {
                marked[w] = false;
            }
        }
        return false;
    }
	
	private boolean hasSelfLoop(List<Integer>[] graph) {
        for (int v = 0; v < n; v++) 
        {
            for (int w : graph[v]) 
            {
                if (v == w) 
                {
                    cycle = new Stack<Integer>();
                    cycle.push(v);
                    cycle.push(v);
                    return true;
                }
            }
        }
        return false;
    }
	
	public boolean check(List<Integer>[] graph)
	{
		if(hasCycle())
		{
			int first = -1, last = -1;
			for (int v : cycle()) 
			{
                if (first == -1) 
                	first = v;
                last = v;
            }
			if(first != last)
			{
				return false;
			}
		}
		return true;
	}
	
	public Iterable<Integer> cycle()
	{
		return cycle;
	}

	public boolean hasCycle()
	{
		return cycle != null;
	}
	
	public List<Integer> topologicalSort(List<Integer>[] graph) {
		int n = graph.length;
		res = new ArrayList<>();
		for (int i = 0; i < n; i++)
		{
			if (!marked[i])
			{
				dfs(graph, marked, res, i);
			}
		}
		Collections.reverse(res);
		return res;
	}

	public List<List<Integer>> scc(List<Integer>[] graph) {
		int n = graph.length;
		res = new ArrayList<>();
		for (int i = 0; i < n; i++)
		{
			if (!marked[i])
			{
				dfs(graph, marked, res, i);
			}
		}

		List<Integer>[] reverseGraph = new List[n];
		for (int i = 0; i < n; i++)
		{
			reverseGraph[i] = new ArrayList<>();
		}
		for (int i = 0; i < n; i++)
		{
			for (int j : graph[i])
			{
				reverseGraph[j].add(i);
			}
		}

		List<List<Integer>> components = new ArrayList<>();
		Arrays.fill(marked, false);
		Collections.reverse(res);

		for (int u : res)
			if (!marked[u]) 
			{
				List<Integer> component = new ArrayList<>();
				dfs(reverseGraph, marked, component, u);
				components.add(component);
			}

		return components;
	}

	public void dfs(List<Integer>[] graph, boolean[] used, List<Integer> res, int u) {
		used[u] = true;
		for (int w : graph[u])
		{
			if(cycle != null)
				return;
			else if (!used[w])
			{
				edgeTo[w] = u;
				dfs(graph, used, res, w);
			}
			else if (onStack[w])
			{
				cycle = new Stack<Integer>();
				for(int x = u; x!= w; x = edgeTo[x])
				{
					cycle.push(x);
				}
				cycle.push(w);
				cycle.push(u);
			}
		}
		res.add(u);
	}
	
	// Usage example
	public static void main(String[] args) {
		int n = 13;
		List<Integer>[] g = new List[n];
		for (int i = 0; i < n; i++) {
			g[i] = new ArrayList<>();
		}
		Scanner scanner = new Scanner(System.in);
    	int j = 0;
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
    				if(line.equals("---"))
    				{
    					try
    					{
    						line = scanner.nextLine();
    					}
    					catch(NoSuchElementException e)
    					{
    						continue;
    					}
    				}
    				if(line.equals(emptyLine))
    				{
    					j++;
    				}
    				if(!line.equals(emptyLine))
    				{
    					String[] adjacent = line.split("\\t");
    					for(int i = 0; i<adjacent.length; i++)
    					{
    						g[j].add(Integer.parseInt(adjacent[i]));
    					}
    					j++;
    				}    					

    			}    		
    		}
    	}
    	catch(FileNotFoundException e) 
    	{
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	System.out.println("Graph is loaded.");
		Toposort t = new Toposort(g);
		Toposort t2 = new Toposort(g);
		List<List<Integer>> components = t.scc(g);
		List<Integer> res = t2.topologicalSort(g);
		System.out.println("Graph contains cycles.");
		System.out.println("Graph has " + components.size() + " strong components:");
		System.out.println(res);
		for(int i = 0; i < components.size(); i++)
		{
			System.out.println(components.get(i));
		}
	}
}