package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private List<Album>allAlbum;
	private SimpleDirectedWeightedGraph<Album, DefaultWeightedEdge>graph;
	private ItunesDAO dao;
	
	private List<Album>bestPath;//campi globali per la ricorsione
	private int bestScore;
	
	public Model() {
		super();
		this.allAlbum = new ArrayList<>();
		
		this.dao = new ItunesDAO();
	}
	
	public List<BilancioAlbum>getAdiacenti(Album a){
		List<Album>successori = Graphs.successorListOf(this.graph, a);//da tutti gli archi successori del vertice dato
		List<BilancioAlbum>bilancioSuccessori = new ArrayList<>();
		for(Album b: successori) {
			bilancioSuccessori.add(new BilancioAlbum(b, getBilancio(b)));
		}
		Collections.sort(bilancioSuccessori);
		return bilancioSuccessori;
	}
	
	public void buildGraph(int n) {
		this.loadNodes(n);
		//creo grafo
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		Graphs.addAllVertices(graph, this.allAlbum);
		System.out.println(this.graph.vertexSet().size());
		//archi
		for(Album a1 : this.allAlbum) {
			for(Album a2 : this.allAlbum) {
				int peso = a1.getNumSongs()-a2.getNumSongs();
				if(peso > 0) {//se a1 ha piu canzoni di a2
					Graphs.addEdgeWithVertices(this.graph, a2, a1, peso);
				}
			}
		}
		System.out.println(this.graph.edgeSet().size());
	}
	
	private int getBilancio(Album a) {
		int bilancio = 0;
		List<DefaultWeightedEdge> edgesIn = new ArrayList<>(this.graph.incomingEdgesOf(a));//archi entranti in un vertice
		List<DefaultWeightedEdge> edgesOut = new ArrayList<>(this.graph.outgoingEdgesOf(a));//archi uscenti da un vertice
		
		for(DefaultWeightedEdge edge : edgesIn) {
			bilancio += this.graph.getEdgeWeight(edge);
		}
		
		for(DefaultWeightedEdge edge : edgesOut) {
			bilancio -= this.graph.getEdgeWeight(edge);
		}
		return bilancio;
	}
	
	public List<Album>getVertices(){
		List<Album>allVertices = new ArrayList<>(graph.vertexSet());
		return allVertices;
		
	}
	
	private void loadNodes(int n) {
		if(this.allAlbum.isEmpty())
			this.allAlbum = this.dao.getFilteredAlbums(n);
	}

	public int getNumVertici() {
		return this.graph.vertexSet().size();
	}

	public int getNumEdges() {
		return this.graph.edgeSet().size();
	}
	
	public List<Album> getPath(Album source, Album target, int threshold) {
		List<Album>parziale = new ArrayList<>();
		this.bestPath = new ArrayList<>();
		this.bestScore = 0;
		parziale.add(source);
		
		ricorsione(parziale, target, threshold);
		
		return this.bestPath;
	}

	private void ricorsione(List<Album> parziale, Album target, int threshold) {
		Album current = parziale.get(parziale.size()-1);
		if(current.equals(target)) {//condiz di uscita
			//controllo se soluzione e meglio di best
			if(getScore(parziale) > this.bestScore) {
				this.bestScore = getScore(parziale);
				this.bestPath = new ArrayList<>(parziale);//non mettere bestPath = parziale (senno non creo copia della lista)
			}
			return;
		}
		//continuo ad aggiungere elementi a parziale
		List<Album> successors = Graphs.successorListOf(this.graph, current);
		for(Album a : successors) {
			if(this.graph.getEdgeWeight(this.graph.getEdge(current, a))>= threshold) {//solo archi >=x
				parziale.add(a);
				ricorsione(parziale, target, threshold);
				parziale.remove(a);
			}
			
		}
	}

	private int getScore(List<Album> parziale) {
	
		int score = 0;
		Album source = parziale.get(0);
		for(Album x : parziale) {
			if(getBilancio(x)> this.getBilancio(source)) {
				score += 1;
			}
		}
		return score;
	}
	
	
	
	
	
	
}
