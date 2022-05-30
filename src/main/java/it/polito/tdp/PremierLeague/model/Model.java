package it.polito.tdp.PremierLeague.model;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	PremierLeagueDAO dao;
	Graph<Match, DefaultWeightedEdge> grafo;
	Map<Integer, Match> allMatchMap = new HashMap<>(); 
	
	public Model() {
		dao = new PremierLeagueDAO();
		dao.listAllMatches(allMatchMap);
	}
	
	public String creaGrafo(int mese, int minuti) {
		grafo = new SimpleWeightedGraph<Match, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Match> vertici = dao.getVertici(mese);
		Graphs.addAllVertices(this.grafo, vertici);
		
		for(Adiacenza a : dao.getAdiacenti(minuti, allMatchMap)) {
			if(vertici.contains(a.getM1()) && vertici.contains(a.getM2()))
				Graphs.addEdgeWithVertices(this.grafo, a.getM1(), a.getM2(), a.getPeso());
		}
		
		String result = "Grafo creato!\n" + "#Vertici: " + grafo.vertexSet().size() + "\n#Archi: " + grafo.edgeSet().size();
		return result;
	}
	
	public List<Adiacenza> getConnessioneMax() {
		int max = 0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e) > max)
				max = (int)grafo.getEdgeWeight(e);
		}
		
		List<Adiacenza> result = new ArrayList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e) == max) {
				Adiacenza adiacenza = new Adiacenza(grafo.getEdgeSource(e), grafo.getEdgeTarget(e), (int)grafo.getEdgeWeight(e));
				result.add(adiacenza);
			}
		}
		return result;
	}
	
	public Graph<Match, DefaultWeightedEdge> getGrafo() {
		return this.grafo;
	}
	
	public List<Match> calcolaCollegamento(Match sorgente, Match destinazione) {
		List<Match> visitati = new LinkedList<>();
		GraphIterator<Match, DefaultWeightedEdge> bfi = new BreadthFirstIterator<>(this.grafo, sorgente);
		while(bfi.hasNext()) {
			if(!visitati.contains(bfi.next())) {
				visitati.add(bfi.next());
				if(bfi.next().equals(destinazione)) {
					break;
				}
			}
		}
		return visitati;
	}
}
