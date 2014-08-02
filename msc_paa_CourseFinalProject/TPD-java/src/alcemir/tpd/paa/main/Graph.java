package alcemir.tpd.paa.main;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 *  Graph class
 *  
 *  CONSTRUCTION: with no parameters.
 *  
 ******************PUBLIC OPERATIONS**********************
 *  void addEdge( String v, String w, double cvw )
 *                               --> Add additional edge
 ******************ERRORS*********************************
 * Some error checking is performed to make sure graph is ok,
 * and to make sure graph satisfies properties needed by each
 * algorithm.  Exceptions are thrown if errors are detected.
 * 
 */
public class Graph
{
    public static final double INFINITY = Double.MAX_VALUE;
    private List<Vertex> v = new ArrayList<Vertex>();
    private List<Edge> e = new ArrayList<Edge>();
	public int numberOfActiveNodes = 0;

    public Graph(List<Vertex> v, List<Edge> e){
    	this.v = v;
    	this.e = e;
    }
    public Graph() {}
    
    public Graph(Graph g1) {
    	this.e = new ArrayList<Edge>(g1.getE());
    	this.v = new ArrayList<Vertex>(g1.getV());
    	this.numberOfActiveNodes = g1.numberOfActiveNodes;
    	
	}
	/**
     * Constructor
     * @param v
     */
	public void addVertex(Vertex v) {
		this.v.add(v);
	}

    /**
     * If vertexName is not present, add it to vertexMap.
     * In either case, return the Vertex.
     */
    public Vertex getVertex( String vertexName ){
        Vertex vertex = null;
    	for (Iterator it = this.v.iterator(); it.hasNext();) {
			vertex = (Vertex) it.next();
			if(vertex.name.equals(vertexName)){
				return vertex;
			}
        }        
//        if( vertex == null )
//        {
//            vertex = new Vertex( vertexName );
//            this.v.add(vertex);
//        }
        return vertex;
    }
	/**
     * Add a new edge to the graph.
     */
    public void addEdge( String sourceName, String destName, double cost )
    {
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName );
        v.adj.add( new Edge( w, cost ) );
        this.e.add(new Edge(v, w, cost));
    }
    /**
     * Add a new edge to the graph.
     */
    public void addEdge(Edge e)
    {
        this.e.add(e);
    }
    /**
     * Remove vertex.
     * @param sourceName 
     */
    public void removeVertex(String sourceName){
    	Vertex vx = null;
    	for (Iterator it = this.v.iterator(); it.hasNext();) {
			vx = (Vertex) it.next();
			if(vx.name.equals(sourceName)){
				break;
			}			
		}    	
    	this.v.remove(vx);
    	removeAllEdgesTo(sourceName);
    }

    /**
     * 
     * @param sourceName
     */
    private void removeAllEdgesTo(String sourceName) {
    	
    	for (Iterator it = this.v.iterator(); it.hasNext();) {
			Vertex v = (Vertex) it.next();			
			for (Edge ob : v.adj) {
				if (ob.dest.name.equals(sourceName)) {
					v.adj.remove(ob);
				}
			}
		}
    	ArrayList<Edge> arr = new ArrayList<Edge>();
    	for (Iterator itr = this.e.iterator(); itr.hasNext();) {
			Edge e = (Edge) itr.next();
			if (e.dest.name.equals(sourceName)) {
				arr.add(e);
			}
		}
    	for (Iterator itr = arr.iterator(); itr.hasNext();) {
			Edge e = (Edge) itr.next();
			this.e.remove(e);
		}
	}
    
    /**
     * Returns true if there is a Vertex with name vertexName 
     */
    public boolean hasVertex( String vertexName )
    {
        for (Iterator it = this.v.iterator(); it.hasNext();) {
			Vertex vertex = (Vertex) it.next();
			if(vertex.name.equals(vertexName)){
				return true;
			}
        } 
        return false;
    }

    
//    /**
//     * Initializes the vertex output info prior to running
//     * any shortest path algorithm.
//     */
//    private void clearAll( ){
//        for( Iterator itr = vertexMap.values( ).iterator( ); itr.hasNext( ); )
//            ( (Vertex)itr.next( ) ).reset( );
//    }


	public void init() {
		try{
        	//String  name = args[0];
            FileReader fin = new FileReader( "graph-medium.txt" );
            BufferedReader graphFile = new BufferedReader( fin );

            // Read the edges and insert
            String line;
            while( ( line = graphFile.readLine( ) ) != null ){
                StringTokenizer st = new StringTokenizer( line );
                try{
                    if( st.countTokens( ) != 3 ){
                        System.err.println( "Skipping ill-formatted line " + line );
                        continue;
                    }
                    String source  = st.nextToken( );
                    String dest    = st.nextToken( );
                    int    cost    = Integer.parseInt( st.nextToken( ) );
                    Vertex src;
                    
                    Vertex d;
                    if (!hasVertex(dest)){ 
                    	d = new Vertex(dest);
                    	this.v.add(d);
                    }else
                    	d = getVertex(dest);
                    
                    if (!hasVertex(source)){
                    	src = new Vertex(source);
                    	this.v.add(src);
                    }else
                    	src = getVertex(source);			

                    src.adj.add(new Edge(d, cost));
                    this.addEdge( src, d, cost );
//                    System.out.println(source +" to "+dest);
                }
                catch( NumberFormatException e )
                  { System.err.println( "Skipping ill-formatted line " + line ); }
             }
//            System.out.println( "File read..." );
         
        }catch( IOException e )
           { System.err.println( e ); }

        activateNodes(0.8);
//         System.out.println( this.v.size( ) + " vertex found." );
         this.numberOfActiveNodes = countActiveNodes();
//         printGraph();
	}

	/**
	 * Ativa os nós randomicamente;
	 */
	private void activateNodes(double threshold) {
		int i=0;
		for (Iterator it = this.v.iterator(); it.hasNext();) {
			Vertex v = (Vertex) it.next();
			if (Math.random()>threshold) {
				v.status = true;
				i++;
			}
		}
		if (i==0) {
			this.v.get(0).status = true;
		}
	}
	/**
	 * Conta o número de nós ativos.
	 * @return
	 */
	private int countActiveNodes() {
		int num=0;
		for (Iterator it = this.v.iterator(); it.hasNext();) {
			Vertex v = (Vertex) it.next();
			if (v.status==true) {
				num++;
			}
		}
		return num;
	}
	/**
	 * Adiciona aresta.
	 * @param src
	 * @param d
	 * @param cost
	 */
	public void addEdge(Vertex src, Vertex d, int cost) {
		this.e.add(new Edge(src, d, cost));
	}
	
	/**
	 * Imprime o gráfico no console
	 */
	private void printGraph() {
		System.out.println("Existem os vértices: ");
		for (Iterator it = this.v.iterator(); it.hasNext();) {
			Vertex v = (Vertex) it.next();
			System.out.print(v.name+" ,");
		}
		System.out.println("Existem as arestas: ");
		for (Iterator itr = this.e.iterator(); itr.hasNext();) {
			Edge e = (Edge) itr.next();
			System.out.println(e.origin.name+" to "+e.dest.name);			
		}
	}
	/**
	 * Returns all the to the vertex with name 'vertexname'.
	 * @param vertexname
	 * @return
	 */
	public Map<String, Edge> getEdgesTo(String vertexname) {
		Vertex vertex;
		Edge ed;
		Map<String, Edge> map = new HashMap<String, Edge>();
		
		for (Iterator it = this.e.iterator(); it.hasNext();) {
			ed = (Edge) it.next();
			if ( ed.dest.name.equals(vertexname) ){
				map.put(ed.origin.name, ed);
			}					
		}            
		return map;
	}

	
	/**
	 * Returns the Vertex Set of the Graph.
	 * @return
	 */
	public Set<Vertex> getVertexSet() {
		Set<Vertex> vertexset = new HashSet<Vertex>();
		
		for (Iterator it = this.v.iterator(); it.hasNext();) {
			Vertex vertex = (Vertex) it.next();
			vertexset.add( vertex );			
        } 		
		return vertexset;
	}
	/**
	 * Returns an iterable Vertex Set of the Graph.
	 * @return
	 */
	public List<Vertex> getV() {
		return this.v;
	}
	
	
	/**
	 * Returns the number of Edges of this Graph.
	 * @return
	 */
	public Integer getNumberOfEdges() {
		return this.e.size();
	}

	/**
	 * Returns the Edges set of the Graph.
	 * @return
	 */
	public Set<Edge> getEdgesSet() {
		Set<Edge> edgeset = new HashSet<Edge>();
		
		for (Edge edge : this.e) {
			edgeset.add(edge);			
		}
		return edgeset;
	}
	/**
	 * Returns an iterable Edge Set of the Graph.
	 * @return
	 */
	public List<Edge> getE() {
		return this.e;
	}
	public void setEdgeSet(ArrayList<Edge> et) {
		this.e = et;
	}
	public void setV(ArrayList<Vertex> arrayList) {
		this.v = arrayList;
	}
	
	public void setActive(String string) {
		for (Vertex v : this.v) {
			if (v.name.equals(string)) {
				v.status = true;
				break;
			}
		}		
	}
	public void setVertexSet(ArrayList<Vertex> vt) {
		this.v = vt;
	}
	public void setActiveNodesRandom(double threshold) {
		for (Vertex v : this.v) {
			if (Math.random()>threshold) {
				v.status = true;
			}
		}
	}
	
	public  void init(String filename, int numparamoffile){
		
		try{
            FileReader fin = new FileReader( filename );
            BufferedReader graphFile = new BufferedReader( fin );

            // Read the edges and insert
            String line;
            while( ( line = graphFile.readLine( ) ) != null ){
                StringTokenizer st = new StringTokenizer( line );
                try{
                    if( st.countTokens( ) != numparamoffile){
                        System.err.println( "Skipping ill-formatted line " + line );
                        continue;
                    }
                    String source  = st.nextToken( );
                    String dest    = st.nextToken( );
                    int    cost    = 0;
                    Vertex src;
                    Vertex d;
                    if (!this.hasVertex(dest)){ 
                    	d = new Vertex(dest);
                    	this.addVertex(d);
                    }else
                    	d = this.getVertex(dest);
                    
                    if (!this.hasVertex(source)){
                    	src = new Vertex(source);
                    	this.addVertex(src);
                    }else
                    	src = this.getVertex(source);			

                    src.adj.add(new Edge(d, cost));
                    this.addEdge( src, d, cost );
                   
                }
                catch( NumberFormatException e )
                  { System.err.println( "Skipping ill-formatted line " + line ); }
             }
            fin.close();
            graphFile.close();
        }catch( IOException e )
           { System.err.println( e ); }

        this.setActive("1");
//        this.setActiveNodesRandom(0.8);
        for (Vertex v : this.getV()) {
			if (v.status==true) {
				this.numberOfActiveNodes++;
			}
		}
//        System.out.println("Inicialização concluida!");
//        System.out.println("Número de nós: "+this.getV().size());
//        System.out.println("Número de arestas: "+this.getE().size());
	}

}
