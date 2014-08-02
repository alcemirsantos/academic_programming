package alcemir.tpd.paa.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.StringTokenizer;

import javax.print.CancelablePrintJob;

public class Algorithms {
	public static void main(String args[]){
		long tempoInicial = System.currentTimeMillis();		
		Graph gr = new Graph();
		HashMap<String, Vertex> result;
		
//		init(gr);
		gr.init("graph-medium.txt",3);
		
		result = (HashMap<String, Vertex>) estimateMarginalInfluenceDegree(gr);
		
		for (Entry<String, Vertex> e : result.entrySet()) {
			System.out.println("|F(A U {"+e.getValue().name+"};Gr)| = "+e.getValue().influencedegree);
		}
		long tempoFinal = System.currentTimeMillis();
		long tempototal = tempoFinal - tempoInicial;
		System.out.println("Tempo de execução: "+tempototal+" ms" );
		System.out.println("Máximo de memória: "+Runtime.getRuntime().maxMemory()/(1024*1024)+" MB");
	}
	
	public static void init(Graph g){
		String filename = "wiki.txt";
		int numparamoffile = 2;
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

//        g.setActive("V1");
        g.setActiveNodesRandom(0.8);
//        for (Vertex v : g.getV()) {
//			System.out.println("O nó "+v.name+" está "+v.status);
//		}
        System.out.println("Inicialização concluida!");
        System.out.println("Número de nós: "+g.getV().size());
        System.out.println("Número de arestas: "+g.getE().size());
	}
	
	/*
	 * Main method 
	 */
	
	/**
	 * 
	 * @param Gr
	 * @return 
	 */
	public static Map<String, Vertex> estimateMarginalInfluenceDegree(Graph Gr) {
		Graph GrA;
		ArrayList<Vertex> A;
		ArrayList<Vertex> VrA;
		ArrayList<Vertex> U;
		ArrayList<Vertex> FAGr;
		ArrayList<Vertex> VrA_U;
		ArrayList<Vertex> CuGrA;
		ArrayList<Vertex> BuGrA;

		// (E1) Find the subset F (A; Gr ) of V.
		A = getActiveNodes(Gr);
		FAGr = findFAGr(A);

		// (E2) Set |F (A ∪ {v}; Gr )| ← |F (A; Gr )| for all v ∈ F (A; Gr ) \ A
		ArrayList<Vertex> FAGr_A;
		FAGr_A = findFAGr_A(FAGr, A);
		Map<String, Vertex> numbersFAvGr = new HashMap<String, Vertex>();
		for (Vertex v : FAGr_A) {
			v.influencedegree = FAGr.size();
			numbersFAvGr.put(v.name, v);  
		}
		// (E3) Find the subset VrA = V \ F (A; Gr) of V , and the induced graph GrA of Gr to VrA.
		VrA = findVrA(Gr.getV(),FAGr);
		GrA = findGrA(VrA);

		// (E4) Set U ← ∅.
		U = new ArrayList<Vertex>();

		// (E5) while VrA \ U = ∅ do
		VrA_U = findVrAmenosU(VrA, U);
		ArrayList<Vertex> FuGrA = new ArrayList<Vertex>();
		while (!VrA_U.isEmpty()) {
			// (E6) Pick a Vertex u ∈ VrA \ U .
			Vertex u = peekNode(VrA_U);
			// (E7) Find the subset F (u; GrA ) of VrA .
			FuGrA = findFuGr(u, VrA);
			// (E8) Find the subset SCC(u) = C(u;GrA) = B(u;GrA) ∩ F(u;GrA) of F(u;GrA)
			BuGrA = findBuGrA(GrA, u);
			CuGrA = findCuGrA(FuGrA, BuGrA);
			// (E9) Set |F (A∪{v}; Gr )| ← |F (u; GA )| + |F (A; Gr )| for all v ∈ C(u; GA ).
			for (Vertex v : CuGrA) {
				v.influencedegree = FuGrA.size() + FAGr.size();
				numbersFAvGr.put(v.name, v);
			}
			// (E10) Set U ← U ∪ C(u; GrA ).
			for (Vertex v : CuGrA){
				U.add(v);
			}
			VrA_U = findVrAmenosU(VrA, U);
		}
		return numbersFAvGr;
	}

	/*
	 * Called methods in EstimateMarginalInfluenceDegree Function 
	 */
	
	/**
	 * Returns tha Active nodes set
	 * 
	 * @param g
	 * @return
	 */
	public static ArrayList<Vertex> getActiveNodes(Graph g) {
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		for (Vertex v : g.getV()) {
			if (v.status == true) {
				result.add(v);
			}
		}
		return result;
	}
	
	/**
	 * Finds all Vertex that Vertex of A can reach;
	 * 
	 * @param Vertexs
	 */
	public static ArrayList<Vertex> findFAGr(
			ArrayList<Vertex> a) {
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		Queue<Vertex> q = new LinkedList<Vertex>();

		for (Vertex vertex : a) {
			q.offer(vertex);
			result.add(vertex);
		}
		while (!q.isEmpty()) {
			Vertex temp = q.poll();
			if (temp.visited == false) {
				for (Edge e : temp.adj) {
					q.add(e.dest);
					if (!result.contains(e.dest)) {
						result.add(e.dest);
					}

				}
				temp.visited = true;
			}
		}
		for (Vertex vertex : result) {
			vertex.visited = false;
		}
		return result;
	}
	/**
	 * Returns difference between FAGr e A
	 * @param fAGr
	 * @param a
	 * @return
	 */
	public static ArrayList<Vertex> findFAGr_A(ArrayList<Vertex> fAGr,
			ArrayList<Vertex> a) {
		ArrayList<Vertex> result = new ArrayList<Vertex>(fAGr);
		
		for (Vertex vertex : fAGr) {
			if (a.contains(vertex)) {
				result.remove(vertex);
			}
		}
		return result;
	}

	/**
	 * Finds the s
	 * 
	 * @param gr
	 * @return
	 */
	public static ArrayList<Vertex> findVrA(
			List<Vertex> list, ArrayList<Vertex> fAGr) {
		ArrayList<Vertex> result = new ArrayList<Vertex>(list);
		
		for (Vertex vertex : list) {
			if (fAGr.contains(vertex)) {
				result.remove(vertex);
			}
		}
		return result;
	}
	
	/**
	 * Returns the induced graph by VrA.
	 * @param vrA
	 * @return
	 */
	public static Graph findGrA(ArrayList<Vertex> vrA) {
		Graph result = new Graph();
		result.setV(new ArrayList<Vertex>(vrA));
		ArrayList<Edge> aux = new ArrayList<Edge>();
		
		for (Vertex v : vrA) {
			for (Edge e : v.adj) {
				e.origin = v;
				aux.add(e);
			}
		}
		for (Edge ed : aux) {
			if (vrA.contains(ed.dest)) {
				result.addEdge(ed);
			}
		}
		return result;
	}
	
	/**
	 * Returns the difference between VrA e U.
	 * @param vrA
	 * @param u
	 * @return
	 */
	public static ArrayList<Vertex> findVrAmenosU(ArrayList<Vertex> vrA,
			ArrayList<Vertex> u) {
		ArrayList<Vertex> result = new ArrayList<Vertex>(vrA);
		
		for (Vertex vertex : vrA) {
			if (u.contains(vertex)) {
				result.remove(vertex);
			}
		}
		return result;
	}
	
	/**
	 * Returns a node extracted from VrA_U.
	 * @param vrA_U
	 * @return
	 */
	public static Vertex peekNode(ArrayList<Vertex> vrA_U) {
		Vertex v = null;
		if (vrA_U.size()>0) {
			v = vrA_U.get(0);
			vrA_U.remove(0);
		}		
		return v;
	}
	
	/**
	 * Returns the Final vertex set reachable from u
	 * @param u
	 * @param vrA
	 * @return
	 */
	public static ArrayList<Vertex> findFuGr(Vertex u, ArrayList<Vertex> vrA) {
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		Queue<Vertex> q = new LinkedList<Vertex>();

		q.offer(u);
		result.add(u);
		while (!q.isEmpty()) {
			Vertex temp = q.poll();
			if (temp.visited == false) {
				for (Edge e : temp.adj) {
					if (vrA.contains(e.dest)) {
						q.add(e.dest);
						if (!result.contains(e.dest)) {
							result.add(e.dest);
						}
					}
				}
				temp.visited = true;
			}
		}
		for (Vertex vertex : result) {
			vertex.visited = false;
		}
		return result;
	}

	/**
	 * Finds the strogly connected component that contains u.
	 * @param fuGrA
	 * @param buGrA
	 * @return
	 */
	public static ArrayList<Vertex> findCuGrA(ArrayList<Vertex> fuGrA, ArrayList<Vertex> buGrA) {
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		
		for (Vertex no : buGrA) {
			if(fuGrA.contains(no)){
				result.add(no);
			}
		}
		return result;
	}

	/**
	 * Returns the Final vertex set that can reach u.
	 * @param grA
	 * @param u
	 * @return
	 */
	public static ArrayList<Vertex> findBuGrA(Graph grA, Vertex u) {
		ArrayList<Vertex> result = new ArrayList<Vertex>();
//		Queue<Vertex> stack = new LinkedList<Vertex>();

//		Graph gt = transposeGraph(grA);
//		ArrayList<Vertex> a = (ArrayList<Vertex>) gt.getV();
//
//		for (Vertex v : a) {
//			if (v.name.equals(u.name)) {
//				stack.offer(v);
//				result.add(v);
//			}
//		}
//
//		while (!stack.isEmpty()) {
//			Vertex temp = stack.poll();
//			if (temp.visited == false) {
//				for (Edge e : temp.adj) {
//					stack.add(e.dest);
//					if (!result.contains(e.dest)) {
//						result.add(e.dest);
//					}
//				}
//				temp.visited = true;
//			}
//		}
		for (Vertex v : grA.getV()) {
			v.visited = false;
		}
		for (Vertex v : grA.getV()) {
			if (v.name.equals(u.name)) {
				result.add(v);
			}else if (canReaches(v, u)) {
				result.add(v);
			}
		}
		
		return result;
	}
	
	private static boolean canReaches(Vertex v , Vertex u){
		boolean result = false;
		Queue<Vertex> q = new LinkedList<Vertex>();
		q.offer(v);
		
		while(!q.isEmpty()){
			Vertex temp = q.poll();
			if (temp.visited == false) {				
				for (Edge e : temp.adj) {
					if (e.dest.name.equals(u.name)) {
						result = true;
						q.clear();
						break;
					}else{
						q.offer(e.dest);
					}
				}
				temp.visited = true;
			}
		}
		q.offer(v);
		while(!q.isEmpty()){
			Vertex temp = q.poll();
			if (temp.visited == true) {				
				for (Edge e : temp.adj) {
					if (e.dest.visited == true) {
						e.dest.visited = false;
						q.offer(e.dest);
					}
				}
				temp.visited = false;
			}
		}
		return result;		
	}
	/*
	 * Additional Methods 
	 */

	
//	/**
//	 * Transpose a graph g.
//	 * 
//	 * @param g
//	 * @return
//	 */
//	private static Graph transposeGraph(Graph g) {
//		ArrayList<Edge> e = (ArrayList<Edge>) g.getE();
//		ArrayList<Vertex> v = (ArrayList<Vertex>) g.getV();
//		ArrayList<Edge> et;
//		ArrayList<Vertex> vt;
//
//		vt = transposeVertexAdj(v);
//		g.setVertexSet(vt);
//		
//		et = transposeEdge(e);
//		g.setEdgeSet(et);
//		return g;
//	}
//
//	private static ArrayList<Vertex> transposeVertexAdj(ArrayList<Vertex> v) {
//		ArrayList<Vertex> result = new ArrayList<Vertex>();
//		
//		HashMap<String, HashMap<String, String>> arrows = new HashMap<String, HashMap<String,String>>();		
//		for (Vertex vertex : v) {
//			HashMap<String, String> teste = new HashMap<String, String>();
//			int x =0;
//			for (Edge e : vertex.adj) {
//				if (v.contains(e.dest)) {
//					teste.put(""+x, e.dest.name);
//				}				
//				x++;
//			}
//			arrows.put(vertex.name, teste);
//		}
//		ArrayList<Vertex> arr = new ArrayList<Vertex>();
//		for (int i = 0; i < arrows.keySet().size(); i++) {
//			arr.add(new Vertex((String)arrows.keySet().toArray()[i]));
//		}
//		
//		for (Entry<String, HashMap<String, String>> e : arrows.entrySet()) {
//			ArrayList<Edge> adj = new ArrayList<Edge>();
//			String vname = (String) e.getKey();
//			Vertex vx;
//			for (Entry<String, String> ent : e.getValue().entrySet()) {
//				vx = getVertex(ent.getValue(), arr);
//				vx.adj.add(new Edge(ent.getValue(), vname,0));					
//			}
//		}
//		return arr;
//	}
//
//	
//	private static Vertex getVertex(String vname, ArrayList<Vertex> result) {
//
//		Vertex vx =null;
//		for (Vertex v : result) {
//			if (v.name.equals(vname)) {
//				vx = v;
//				break;
//			}
//		}
//		if (vx==null)
//			return new Vertex(vname);
//		else
//			return vx;
//		
//	}
//
//	/**
//	 * Change the way of each edge of e.
//	 * 
//	 * @param e
//	 * @return
//	 */
//	private static ArrayList<Edge> transposeEdge(ArrayList<Edge> e) {
//		ArrayList<Edge> result = new ArrayList<Edge>();
//		Vertex aux;
//
//		for (Edge edge : e) {
//			aux = edge.origin;
//			edge.origin = edge.dest;
//			edge.dest = aux;
//			result.add(edge);
//		}
//		return result;
//	}
//	
}// fim da classe.
