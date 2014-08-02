package alcemir.tpd.paa.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import alcemir.tpd.paa.main.Graph;
import junit.framework.TestCase;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class GraphTest extends TestCase {

	private Graph g = new Graph();
	String vertexname;	
	
	@BeforeClass
	public void setUp(){
		g.init();
	}
	
	@Test
	public void testIfGraphIsCreated(){
		assertThat("Não há arestas!", g.getV().size(), is(greaterThan(0)));
		assertThat("Não há arestas!", g.getE().size(), is(greaterThan(0)));
	}
	
	@Test
	public void testRemoveVertex(){
		vertexname = "A";		
		assertNotNull("There is no vertex called: "+vertexname, g.getVertex(vertexname));
		
		g.removeVertex("A");
		
		assertFalse("There is a Vertex with this name.", g.getVertex(vertexname) == null);	
	}
	
	@Test
	public void testThereIsNoEdgeToTheVertexRemoved(){
		vertexname = "B";
		assertNotNull("There is no vertex called: "+vertexname, g.getVertex(vertexname));
		
		g.removeVertex(vertexname);
		
		assertThat("There is an edge to the vertex removed.", g.getEdgesTo(vertexname).size(), is(lessThanOrEqualTo(0)));
	}
	
	@Test
	public void testGetVertexSet(){
		assertThat("Não há arestas!", g.getEdgesSet().size(), is(greaterThan(0)));
		
		assertThat("There is no vertx on this graph.", g.getVertexSet().size(), is(greaterThan(0)));
		assertFalse("The Vertex set is empty.", g.getVertexSet().isEmpty());
	}
	
	@Test
	public void testGetEdgeSet(){
		vertexname = "C";
		assertThat("Não há arestas!", g.getNumberOfEdges() , is(greaterThan(0)));
		
		assertThat("There is no vertx on this graph.", g.getEdgesSet().size(), is(greaterThan(0)));
		assertFalse("The Vertex set is empty.", g.getVertexSet().isEmpty());		
	}
	
	@AfterClass
	public void tearDown(){
		
	}
	
}
