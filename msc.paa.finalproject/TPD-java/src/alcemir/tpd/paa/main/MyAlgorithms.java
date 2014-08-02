package alcemir.tpd.paa.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

public class MyAlgorithms {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long tempoInicial= System.currentTimeMillis();
		Graph g = new Graph();
		
		g.init("graph-big.txt", 3);
//		init("gr.txt", 2,g);

		HashMap<String, Vertex> result = estimateMID(g);
		
		for(Entry<String, Vertex> e : result.entrySet()){
			System.out.println("|F(A U {"+e.getKey()+"};Gr)| = "+e.getValue());
		}
		
		long tempoFinal = System.currentTimeMillis();
		long tempototal = tempoFinal - tempoInicial;
		System.out.println("Tempo de execução: "+tempototal+" ms" );
		System.out.println("Máximo de memória: "+Runtime.getRuntime().maxMemory()/(1024*1024)+" MB");
	}

	/**
	 * 
	 * @param gr
	 * @return 
	 */
	public static HashMap<String, Vertex> estimateMID(Graph gr) {
		HashMap<String, Vertex> numbersFAvGr = new HashMap<String, Vertex>();
		ArrayList<Vertex> A = Algorithms.getActiveNodes(gr);
		ArrayList<Vertex> V_A = new ArrayList<Vertex>(gr.getV());
		ArrayList<Vertex> Av;
		
		extractANodes(V_A,A);
		
		for (Vertex vertex : V_A) {
			Av = new ArrayList<Vertex>(A);
			Av.add(vertex);
			ArrayList<Vertex> finnal = null;
			for (Vertex v : Av) {
				finnal = Algorithms.findFuGr(v, (ArrayList<Vertex>)gr.getV());
			}
			vertex.influencedegree = finnal.size();
			numbersFAvGr.put(vertex.name, vertex);
		}
		
		ArrayList<Vertex> FAGr = Algorithms.findFAGr(A);
		ArrayList<Vertex> FAGr_A = Algorithms.findFAGr_A(FAGr, A);
		for (Vertex v : FAGr_A) {
			v.influencedegree = FAGr.size();
			numbersFAvGr.put(v.name, v);
		}
				
		return numbersFAvGr;
	}

	private static void extractANodes(ArrayList<Vertex> v_A, ArrayList<Vertex> a) {
		ArrayList<Vertex> copy = new ArrayList<Vertex>(v_A);
		for (Vertex vertex : copy) {
			if (a.contains(vertex)) {
				v_A.remove(vertex);
			}
		}
	}

	public static  void init(String filename, int numparamoffile, Graph g){
		
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
					if (!g.hasVertex(dest)){ 
						d = new Vertex(dest);
						g.addVertex(d);
					}else
						d = g.getVertex(dest);
					
					if (!g.hasVertex(source)){
						src = new Vertex(source);
						g.addVertex(src);
					}else
						src = g.getVertex(source);			
					
					src.adj.add(new Edge(d, cost));
					g.addEdge( src, d, cost );
				}
				catch( NumberFormatException e )
				{ System.err.println( "Skipping ill-formatted line " + line ); }
			}
			
		}catch( IOException e )
		{ System.err.println( e ); }
		
		g.setActive("V1");
//        g.setActiveNodesRandom(0.8);
		for (Vertex v : g.getV()) {
			if (v.status==true) {
				g.numberOfActiveNodes++;
			}
		}
		System.out.println("Inicialização concluida!");
		System.out.println("Número de nós: "+g.getV().size());
		System.out.println("Número de arestas: "+g.getE().size());
	}
}
