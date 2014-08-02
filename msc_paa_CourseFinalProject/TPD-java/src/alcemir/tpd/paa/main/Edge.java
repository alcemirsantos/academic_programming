package alcemir.tpd.paa.main;

/**
 *  Represents an edge in the graph.
 */
public class Edge{
	public Vertex origin;
    public Vertex dest;   // Second vertex in Edge
    public double cost;   // Edge cost
    public boolean status;
    
    public Edge( Vertex d, double c ){
        dest = d;
        cost = c;
        status = false;
    }
    
    public Edge( Vertex o, Vertex d, double c ){
    	origin = o;
        dest = d;
        cost = c;
        status = false;
    }

	public Edge(String origem, String destino, int i) {
		origin = new Vertex(origem);
		dest = new Vertex(destino);
		cost = i;
	}
}