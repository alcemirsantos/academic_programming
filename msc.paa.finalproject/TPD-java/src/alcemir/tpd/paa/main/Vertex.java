package alcemir.tpd.paa.main;

import java.util.LinkedList;
import java.util.List;

/**
 *  Represents a vertex in the graph.
 */
public class Vertex
{
    public String     name;   // Vertex name
    public List<Edge> adj;    // Adjacent vertices
    public double     dist;   // Cost
	public boolean status = false;
	public int influencedegree;
	public boolean visited = false;

    public Vertex( String nm )
      { name = nm; adj = new LinkedList<Edge>( ); reset( ); }

    public void reset( )
      { dist = Graph.INFINITY; }    
      
}