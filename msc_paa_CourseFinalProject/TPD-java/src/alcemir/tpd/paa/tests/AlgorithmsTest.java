package alcemir.tpd.paa.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

import alcemir.tpd.paa.main.Edge;
import alcemir.tpd.paa.main.Graph;
import alcemir.tpd.paa.main.Vertex;
import alcemir.tpd.paa.main.Algorithms;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class AlgorithmsTest extends TestCase {

	private Graph g;
	private ArrayList<Vertex> A;
	private ArrayList<Vertex> FAGr;
	private ArrayList<Vertex> VrA;
	private Graph GrA;
	private ArrayList<Vertex> U;
	private ArrayList<Vertex> VrA_U;
	private Vertex u;
	private ArrayList<Vertex> FuGrA;
	private ArrayList<Vertex> BuGrA;
	private ArrayList<Vertex> CuGrA;
	private ArrayList<Vertex> FAGr_A;
	private HashMap<String, Vertex> numbersFAvGr;

	@BeforeClass
	public void setUp() {
		g = new Graph();
		A = new ArrayList<Vertex>();
		FAGr = new ArrayList<Vertex>();
		VrA = new  ArrayList<Vertex>();
		GrA = new Graph();
		U = new ArrayList<Vertex>();
		VrA_U = new ArrayList<Vertex>();
		FuGrA = new ArrayList<Vertex>();
		CuGrA = new ArrayList<Vertex>();
		FAGr_A = new ArrayList<Vertex>();
		numbersFAvGr = new HashMap<String, Vertex>();
		g.init();
	}

	@Test
	public void testEstimateMarginalInfluenceDegree(){
		assertThat("Não há arestas!", g.getV().size(), is(greaterThan(0)));
		assertThat("Não há arestas!", g.getE().size(), is(greaterThan(0)));
		
		A = Algorithms.getActiveNodes(g);
		// (E1)
		FAGr = Algorithms.findFAGr(A);
		// (E2)
		FAGr_A = Algorithms.findFAGr_A(FAGr, A);
		for (Vertex v: FAGr_A) {
			v.influencedegree = FAGr.size();
			numbersFAvGr.put(v.name, v);  
		}
		// (E3)
		VrA = Algorithms.findVrA(g.getV(), FAGr);
		GrA = Algorithms.findGrA(VrA);
		// (E5)
		VrA_U = Algorithms.findVrAmenosU(VrA,U);
		// (E6)
		u = Algorithms.peekNode(VrA_U);
		// (E7)
		FuGrA = Algorithms.findFuGr(u,VrA);
		// (E8)
		BuGrA = Algorithms.findBuGrA(GrA,u);
		CuGrA = Algorithms.findCuGrA(FuGrA,BuGrA);
		// (E9)
		u.influencedegree = FuGrA.size();
		for (Vertex v : CuGrA) {
			v.influencedegree = FAGr.size()+u.influencedegree;
			numbersFAvGr.put(v.name, v);  
		}
		// (E10)
		for (Vertex v : CuGrA){
			U.add(v);
		}
		assertEquals("O número de graus de influÊncia não foi calculado corretamente.", g.getV().size()-A.size(), numbersFAvGr.size());
	}
	
	@Test
	public void testExtractActiveNodes() {
		// quando
		A = Algorithms.getActiveNodes(g);
		// então
		for (Vertex v : A) {
			assertTrue("Vértice não ativo no conjunto de ativos.", v.status);
		}
		assertEquals("Número errado de nós ativos no conjunto.", A.size(), g.numberOfActiveNodes);
	}

	@Test
	public void testFindFinalSetOfVertexReachableByActiveNodes(){
		A = Algorithms.getActiveNodes(g);
		FAGr = Algorithms.findFAGr(A);
		
		assertThat("O conjunto final de nós tem menos nós qe o conjunto de ativos.", FAGr.size(), is(greaterThanOrEqualTo(A.size())));
		Queue<Vertex> q = new LinkedList<Vertex>();
		for (Vertex v : A) {
			q.offer(v);
		}
		
		while (!q.isEmpty()) {
			Vertex temp = q.poll();
			if (temp.visited == false) {
				for (Edge e : temp.adj) {
					q.add(e.dest);
					assertTrue("Este elemento não existe no conjunto.", FAGr.contains(e.dest));
				}
				temp.visited = true;
			}
		}	
	}
	
	// (E2)
	@Test
	public void testFindFAGr_A(){
		A = Algorithms.getActiveNodes(g);
		FAGr = Algorithms.findFAGr(A);
		
		FAGr_A = Algorithms.findFAGr_A(FAGr, A);
		
		assertEquals("O conjunto final de nós tem menos nós qe o conjunto de ativos.", FAGr_A.size(), FAGr.size()-A.size());
		for (Vertex v : A) {
			assertFalse("Existe um elemento do conjunto de ativos no conjunto.", FAGr_A.contains(v));
		}		
	}
	
	// (E3)
	@Test
	public void testFindGraphVertexesMinusFinalSetOfVertexReachableByActiveNodes(){

		A = Algorithms.getActiveNodes(g);
		FAGr = Algorithms.findFAGr(A);
		
		// (E3)
		ArrayList<Vertex> array = (ArrayList<Vertex>) g.getV();

		VrA = Algorithms.findVrA(array, FAGr);
		
		assertEquals("O conjunto final de nós tem menos nós qe o conjunto de ativos.", VrA.size(), array.size() - FAGr.size());
		for (Vertex vertex : FAGr) {
			assertFalse("Existe um elemento do conjunto FAGr no VrA.", VrA.contains(vertex));
		}
		
	}
	
	@Test
	public void testFindInducedGraphByVrA(){
		A = Algorithms.getActiveNodes(g);
		FAGr = Algorithms.findFAGr(A);
		ArrayList<Vertex> array = (ArrayList<Vertex>) g.getV();
		VrA = Algorithms.findVrA(array, FAGr);
		
		GrA = Algorithms.findGrA(VrA);
		
		assertNotNull("O conjunto de vertices não foi estanciado.", GrA.getV());
		assertNotNull("O conjunto de arestas não foi estanciado.", GrA.getE());
		assertThat("O conjunto de vertices está vazio.", GrA.getV().size(), is(greaterThan(0)));
		assertThat("O conjunto de arestas está vazio.", GrA.getE().size(), is(greaterThan(0)));
		for(Vertex v : VrA){
			assertTrue("Um vértice que deveria, não está presente no grafo.", GrA.getV().contains(v));
			for (Edge e : v.adj) {
				assertTrue("Uma aresta que deveria, não está no grafo.", GrA.getE().contains(e));
			}
		}
	}

	// (E5)
	@Test
	public void testFindVrAmenosU(){
		A = Algorithms.getActiveNodes(g);
		FAGr = Algorithms.findFAGr(A);
		ArrayList<Vertex> array = (ArrayList<Vertex>) g.getV();
		VrA = Algorithms.findVrA(array, FAGr);		
		GrA = Algorithms.findGrA(VrA);
		U.add(VrA.get(0));
		U.add(VrA.get(VrA.size()-1));
		
		// (E5)
		VrA_U = Algorithms.findVrAmenosU(VrA,U);
		
		assertNotNull("O conjunto de vértices não foi estanciado.", VrA_U);
		assertThat("O conjunto de vértices está vazio.", VrA_U.size(), is(greaterThan(0)));
		for(Vertex v : U){
			assertFalse("Um vértice que não deveria, está presente no grafo.", VrA_U.contains(v));
		}
		assertEquals("O conjunto final de nós tem menos nós qe o conjunto de ativos.", VrA_U.size(), VrA.size() - U.size());
	
	}
	// (E6)
	@Test
	public void testPeekNode(){
		A = Algorithms.getActiveNodes(g);
		FAGr = Algorithms.findFAGr(A);
		ArrayList<Vertex> array = (ArrayList<Vertex>) g.getV();
		VrA = Algorithms.findVrA(array, FAGr);		
		GrA = Algorithms.findGrA(VrA);
		U.add(VrA.get(0));
		U.add(VrA.get(VrA.size()-1));
		VrA_U = Algorithms.findVrAmenosU(VrA,U);

		// (E6)
		int oldsize = VrA_U.size();
		u = Algorithms.peekNode(VrA_U);
		
		assertNotNull("O conjunto de vértices não foi estanciado.", u);
		assertThat("O conjunto de vértices está vazio.", u, is(instanceOf(Vertex.class)));
		assertFalse("Um vértice que não deveria, está presente no grafo.", VrA_U.contains(u));
		assertEquals("O conjunto final de nós tem menos nós qe o conjunto de ativos.", oldsize, VrA_U.size()+1);

	}
	// (E7)
	@Test
	public void testFindFuGr(){
		A = Algorithms.getActiveNodes(g);
		FAGr = Algorithms.findFAGr(A);
		ArrayList<Vertex> array = (ArrayList<Vertex>) g.getV();
		VrA = Algorithms.findVrA(array, FAGr);		
		GrA = Algorithms.findGrA(VrA);
		U.add(VrA.get(0));
		U.add(VrA.get(VrA.size()-1));
		VrA_U = Algorithms.findVrAmenosU(VrA,U);
		u = Algorithms.peekNode(VrA_U);

		// (E7)
		FuGrA = Algorithms.findFuGr(u,VrA);
		
		assertNotNull("O conjunto de vértices não foi estanciado.", FuGrA);
		assertThat("O conjunto de vértices está vazio.", FuGrA.size(), is(greaterThan(0)));
		
	}
	// (E8)
	
	@Test 
	public void testFindBuGrA(){
		A = Algorithms.getActiveNodes(g);
		FAGr = Algorithms.findFAGr(A);
		ArrayList<Vertex> array = (ArrayList<Vertex>) g.getV();
		VrA = Algorithms.findVrA(array, FAGr);		
		GrA = Algorithms.findGrA(VrA);
		U.add(VrA.get(0));
		U.add(VrA.get(VrA.size()-1));
		VrA_U = Algorithms.findVrAmenosU(VrA,U);
		u = Algorithms.peekNode(VrA_U);

		// (E7)
		BuGrA = Algorithms.findBuGrA(GrA,u);
		
		assertNotNull("O conjunto de vértices não foi estanciado.", BuGrA);
		assertThat("O conjunto de vértices está vazio.", BuGrA.size(), is(greaterThan(0)));
	}
	
	@Test
	public void testFindCuGrA(){
		A = Algorithms.getActiveNodes(g);
		FAGr = Algorithms.findFAGr(A);
		ArrayList<Vertex> array = (ArrayList<Vertex>) g.getV();
		VrA = Algorithms.findVrA(array, FAGr);		
		GrA = Algorithms.findGrA(VrA);
		U.add(VrA.get(0));
		U.add(VrA.get(VrA.size()-1));
		VrA_U = Algorithms.findVrAmenosU(VrA,U);
		u = Algorithms.peekNode(VrA_U);
		FuGrA = Algorithms.findFuGr(u,VrA);
		BuGrA = Algorithms.findBuGrA(GrA,u);
		
		// (E8)
		CuGrA = Algorithms.findCuGrA(FuGrA,BuGrA);
		for (Vertex v : CuGrA) {
			assertTrue("Existe nó no conjunto que não deveria.", FuGrA.contains(v));
			assertTrue("Existe nó no conjunto que não deveria.", BuGrA.contains(v));
		}
		
	}
}
