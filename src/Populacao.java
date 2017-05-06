import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

public class Populacao extends Algoritmo{
	
	public static String fieldSort;
	ArrayList<Individuo> populacao = new ArrayList<Individuo>();
	private int tamPopulacao;
	private int quantGene;
	
	private HashSet<Individuo> dominados = new HashSet<Individuo>();
	HashMap<Integer, ArrayList<Individuo>> front = new HashMap<Integer, ArrayList<Individuo>>();
	
	public Populacao(int tamPopulacao, String nomeObjetiva, ArrayList<String> objetivos) {
		this.setTamPopulacao(tamPopulacao);
		if(nomeObjetiva.equals("multObjAedes")){
			this.setQuantGene(4);
		} 
		gerarPopulacao(tamPopulacao, quantGene, objetivos);
	}
	
	public Populacao() {

	}
	
	private void gerarPopulacao(int tamPopulacao, int quantGene, ArrayList<String> objetivos) {
		populacao = new ArrayList<Individuo>();
		for (int i = 0; i < tamPopulacao; i++) {
			System.out.println("--------------CRIAR INDIVIDUO-------------");
			
			ArrayList<Double> genotipo = new ArrayList<Double>();
			
			for (int j = 0; j < quantGene; j++) {
				if(j == 0){
					double rand = ThreadLocalRandom.current().nextDouble(0, 0.4);
					rand = new BigDecimal(rand).setScale(5, RoundingMode.HALF_UP).doubleValue();
					genotipo.add(rand);
				} else if(j == 1){
					double rand = ThreadLocalRandom.current().nextDouble(0, 1);
					rand = new BigDecimal(rand).setScale(5, RoundingMode.HALF_UP).doubleValue();
					genotipo.add(rand);
				} else {
					double rand = ThreadLocalRandom.current().nextDouble(0, 90);
					rand = new BigDecimal(rand).setScale(5, RoundingMode.HALF_UP).doubleValue();
					genotipo.add(rand);
				}
			}
			
			LinkedHashMap<String, Double> F2y = RungeKutta.rungeKuttaEvalue(genotipo.get(0), genotipo.get(1), genotipo.get(2), genotipo.get(3));
			ArrayList<Double> pontos = new ArrayList<Double>();
			for (Double ponto : F2y.values()) {
				pontos.add(ponto);
			}
			Individuo individuo = calculaFenotipoAedes(genotipo, objetivos, pontos);
			populacao.add(individuo);
		}
	}

	public void setIndividuo(Individuo individuoFilho){
		getPopulacao().add(individuoFilho);
	}

	public int getTamPopulacao(Populacao populacao) {
		return populacao.getPopulacao().size();
	}

	public ArrayList<Individuo> getPopulacao() {
		return populacao;
	}

	public int getTamPopulacao() {
		return tamPopulacao;
	}

	public void setTamPopulacao(int tamPopulacao) {
		this.tamPopulacao = tamPopulacao;
	}

	public void setFront(HashMap<Integer, ArrayList<Individuo>> front, int key) {
		HashMap<Integer, ArrayList<Individuo>> frontAux = getFront();
		if(!frontAux.isEmpty()){
			boolean notExistKey = true;
			for (Entry<Integer, ArrayList<Individuo>> entry : frontAux.entrySet()) {
				if(entry.getKey() == key){
					notExistKey = false;
					ArrayList<Individuo> lista = front.get(key);
					entry.getValue().addAll(lista);
				}
			}
			if(notExistKey == true){
				this.front.putAll(front);
			}
		} else {
			this.front.putAll(front);
		}
	}

	public int getQuantGene() {
		return quantGene;
	}

	public void setQuantGene(int quantGene) {
		this.quantGene = quantGene;
	}

	public void putFront(Integer key, ArrayList<Individuo> obj) {
		front.put(key, obj);
	}
	
	public HashMap<Integer, ArrayList<Individuo>> getFront() {
		return front;
	}
	
	public ArrayList<Individuo> getIndividuosFront() {
		return populacao;	
	}

	public boolean existFront(int k) {
		for (Entry<Integer, ArrayList<Individuo>> entry : getFront().entrySet()) {
			if(entry.getKey() == k){
				for(Individuo aux : entry.getValue()){
					if(aux != null){
						return true;
					}
				}
	        }
		}
		return false;
	}
	
	public void cleanFront() {
		this.front = new HashMap<Integer, ArrayList<Individuo>>();
		
	}
	
	public HashSet<Individuo> getDominados() {
		return dominados;
	}
	
	public void removeDominados(ArrayList<Individuo> listaRemover){
		this.dominados.removeAll(listaRemover);
	}

	public void setDominados(Individuo dominado) {
		this.dominados.add(dominado);
	}
}
