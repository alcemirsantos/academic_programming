package alcemir.tpd.paa.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int M = 1000;
		int graphsize = 50;//100
		int asize = 1;
		int[][] xvm = new int[graphsize - asize][M];
		ArrayList<Integer> idA = new ArrayList<Integer>();
		for (int i = 0; i < M; i++) {
			if (Math.random() > 0.7) {
				idA.add(i);
			}
		}
		double[] degree = new double[graphsize - asize];
		for (int i = 0; i < M; i++) {
			String filename = "grafo_" + i + ".txt";
			generateGraphFile(filename, graphsize);
		}

		ArrayList<Long> reference_results = new ArrayList<Long>();
		ArrayList<Long> my_results = new ArrayList<Long>();
		for (int i = 0; i < 10; i++) {
			reset(M, asize, xvm, degree);
			System.out.println("Kimura Algorithm executing...#"+(i+1));
			reference_results.add(executeReferenceAlgorithms(M, graphsize, asize, xvm, degree));
			reset(M, asize, xvm, degree);
			System.out.println("Santos Algorithm executing...#"+(i+1));
			my_results.add(executeMyOwnAlgorithms(M, graphsize, asize, xvm, degree));
		}
		
		System.out.println("Resultados para "+graphsize+" nós.");
		
		System.out.println("Meu resultados:");
		for (Long l : my_results) {
			System.out.print(l+ " ms, ");
		}
		System.out.println("\nResultados da referencia:");
		for (Long l : reference_results) {
			System.out.print(l+ " ms, ");
		}
		
		
		for (int m1 = 0; m1 < M; m1++) {
			delete("grafo_" + m1 + ".txt");
		}
		

	}

	private static long executeMyOwnAlgorithms(int m, int graphsize, int asize,
			int[][] xvm, double[] degree) {

		// start runtime
		long tempoInicial = System.currentTimeMillis();
		// start algorithm
		HashMap<String, Vertex> partialresult;
		for (int m1 = 0; m1 < m; m1++) {
			// generate the graph
			String filename = "grafo_" + m1 + ".txt";
			Graph g1 = new Graph();
			g1.init(filename, 2);

			// estimate gains
			partialresult = new HashMap<String, Vertex>(
					(HashMap<String, Vertex>) MyAlgorithms.estimateMID(g1));

			// set xvm
			for (Entry<String, Vertex> e : partialresult.entrySet()) {
				int v = Integer.parseInt(e.getKey()) - 2;
				int idegree = e.getValue().influencedegree;
				xvm[v][m1] = idegree;
			}
//			System.out.print("(" + (m1 + 1) + "/" + m + ")");
		}

		System.out.println("");
		for (int i = 0; i < graphsize - asize; i++) {
			for (int m1 = 0; m1 < m; m1++) {
				degree[i] += (1.0 / m) * xvm[i][m1];
			}
		}

		// ends runtime
		long tempoFinal = System.currentTimeMillis();
		long tempototal = tempoFinal - tempoInicial;
		// print values
//		for (int i = 0; i < graphsize - asize; i++) {
//			System.out.println("|F(A U {" + (i + 1) + "};Gr)| = " + degree[i]);
//		}
		return tempototal;
	}

	/**
	 * @param M
	 * @param asize
	 * @param xvm
	 * @param degree
	 */
	private static void reset(int M, int asize, int[][] xvm, double[] degree) {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < asize; j++) {
				xvm[j][i] = 0;
				degree[j] = 0.0;
			}
		}
	}

	/**
	 * @param M
	 * @param graphsize
	 * @param asize
	 * @param xvm
	 * @param degree
	 * @return 
	 * @throws NumberFormatException
	 */
	private static long executeReferenceAlgorithms(int M, int graphsize,
			int asize, int[][] xvm, double[] degree)
			throws NumberFormatException {
		// start runtime
		long tempoInicio = System.currentTimeMillis();
		// start algorithm
		HashMap<String, Vertex> partialresult;
		for (int m = 0; m < M; m++) {
			// generate the graph
			String filename = "grafo_" + m + ".txt";
			Graph g1 = new Graph();
			g1.init(filename, 2);

			// estimate gains
			partialresult = new HashMap<String, Vertex>(
					(HashMap<String, Vertex>) Algorithms
							.estimateMarginalInfluenceDegree(g1));

			// set xvm
			for (Entry<String, Vertex> e : partialresult.entrySet()) {
				int v = Integer.parseInt(e.getKey()) - 2;
				int idegree = e.getValue().influencedegree;
				xvm[v][m] = idegree;
			}
//			System.out.print("(" + (m + 1) + "/" + M + ")");
		}

		System.out.println("");
		for (int i = 0; i < graphsize - asize; i++) {
			for (int m = 0; m < M; m++) {
				degree[i] += (1.0 / M) * xvm[i][m];
			}
		}

		// ends runtime
		long tempoFim = System.currentTimeMillis();
		long tempototal = tempoFim - tempoInicio;
		// print values
//		for (int i = 0; i < graphsize - asize; i++) {
//			System.out.println("|F(A U {" + (i + 1) + "};Gr)| = " + degree[i]);
//		}
		
//		System.out.println("Tempo de execução: " + tempototal + " ms");
		
//		System.out.println("Máximo de memória: "
//				+ Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB");
		return tempototal;
	}

	private static void generateGraphFile(String nomedoarquivo, int qtdenos) {
		int a = 2;
		Combinacoes combinacoes = new Combinacoes(qtdenos, a);
		combinacoes.possiveis(0, qtdenos - a, 0);
		delete(nomedoarquivo);
		int i = 0;
		for (String s : combinacoes.getPossiveis()) {
			if (Math.random() > 0.7) {
				escreve(s, nomedoarquivo);
				i++;
			}
		}
		// System.out.println("Geração do grafo concluída!");
	}

	/**
	 * @throws SingletonException
	 * @throws NotFoundException
	 */
	// private static void testeComGraphStream() throws SingletonException,
	// NotFoundException {
	// Graph g = new DefaultGraph(false,true);
	// GraphViewerRemote gvr;
	//
	// // File file = new File("graph-small.txt");
	// File file = new File("wiki.txt");
	//
	// Scanner s = null;
	// try {
	// s = new Scanner(file);
	// } catch (FileNotFoundException e) {
	// System.err.println("Arquivo não encontrado!");
	// }
	//
	// int i = 0;
	// while(i<100){
	// g.addEdge(""+i, ""+s.nextInt(), ""+s.nextInt(), true);
	// i++;
	// }
	// // Iterator it = g.getNodeIterator();
	// // while(it.hasNext()){
	// // System.out.println(it.toString());
	// // }
	// gvr = g.display();
	// gvr.setQuality(4);
	// }

	public static boolean delete(String nomedoarquivo) {
		boolean exists = (new File(nomedoarquivo)).exists();
		boolean success = true;
		if (exists) {
			success = (new File(nomedoarquivo)).delete();
			if (!success) {
				// Deletion failed
				System.err.println("Falha ao excluir o arquivo!");
			}
		}
		return success;
	}

	/**
	 * Método usado para escrever conteúdo no arquivo necessário nome do arquivo
	 * Necessário conteúdo Necessário de confirmação para adição de conteúdo
	 */
	public static void escreve(String conteudo, String nomedoarquivo) {
		try {

			FileWriter fw;
			fw = new FileWriter(nomedoarquivo, true);
			fw.write(conteudo + "\n");
			fw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
