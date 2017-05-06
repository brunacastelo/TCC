import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import net.objecthunter.exp4j.ExpressionBuilder;

public class Algoritmo {

	protected static Individuo calculaFenotipoAedesObj1(ArrayList<Double> genotipo, ArrayList<String> objetivos) {
		
		ArrayList<Double> fenotipo = new ArrayList<Double>();
		int i = 0;
		for (String obj : objetivos) {
				if(i==0){
					net.objecthunter.exp4j.Expression e = new ExpressionBuilder(obj)
			        .variables("_x1")
			        .variables("_x2")
			        .variables("_x3")
			        .variables("_x4")
			        .build()
			        .setVariable("_x1", genotipo.get(0))
			        .setVariable("_x2", genotipo.get(1))
			        .setVariable("_x3", genotipo.get(2))
			        .setVariable("_x4", genotipo.get(3));
					double result = e.evaluate();
					fenotipo.add(result);
				}
				i++;
			}
		
		Individuo individuo = new Individuo(genotipo, fenotipo);
		return individuo;
	}	
	
	protected static Individuo calculaFenotipoAedes(ArrayList<Double> genotipo, ArrayList<String> objetivos, ArrayList<Double> pontos) {
		
		ArrayList<Double> fenotipo = new ArrayList<Double>();
		int i = 0;
		for (String obj : objetivos) {
				if(i==0){
					net.objecthunter.exp4j.Expression e = new ExpressionBuilder(obj)
			        .variables("_x1")
			        .variables("_x2")
			        .variables("_x3")
			        .variables("_x4")
			        .build()
			        .setVariable("_x1", genotipo.get(0))
			        .setVariable("_x2", genotipo.get(1))
			        .setVariable("_x3", genotipo.get(2))
			        .setVariable("_x4", genotipo.get(3));
					double result = e.evaluate();
					fenotipo.add(result);
				}
				else if(i==1){
					double somatorio = Integral.resolve(pontos);
					net.objecthunter.exp4j.Expression e = new ExpressionBuilder(obj)
					.variable("_x1")
					.build()
			        .setVariable("_x1", somatorio);
					double result = e.evaluate();
					fenotipo.add(result);
				}
				i++;
			}
		
		Individuo individuo = new Individuo(genotipo, fenotipo);
		return individuo;
	}
	
	public static void avaliarPopulacaoNSGA2(Populacao populacao) throws Exception{
		ArrayList<Individuo> individuos = populacao.getPopulacao();
		for (Individuo individuo : individuos) {
			int nd = 0;
			for (Individuo individuo2 : individuos) {
				if(!individuo.equals(individuo2)){
					if((individuo.getFenotipo().get(0) >= individuo2.getFenotipo().get(0) &&
						individuo.getFenotipo().get(1) >= individuo2.getFenotipo().get(1))){
						nd++;
					}
				}
			}
			individuo.setNd(nd);
		}
		
		
		int k = 1;
		ArrayList<Individuo> dominados = new ArrayList<>();
		for (Individuo individuo : populacao.getPopulacao()) {
			dominados.add((Individuo) individuo.clone()); 	
		}
		
		while (!dominados.isEmpty()) {
			ArrayList<Individuo> newFront = new ArrayList<Individuo>();
			boolean setouNewFront = false;
			for (Individuo indDominados : dominados) {
				if (indDominados.getNd() == 0) {
					newFront.add(indDominados);
					setouNewFront = true;
				}
				if(indDominados.getNd() < 0) {
					throw new Exception("nd < 0");
				}
				indDominados.setNd(indDominados.getNd() - 1);
			}
			if (setouNewFront) {
				dominados.removeAll(newFront);
				populacao.putFront(k, newFront);
				k = k + 1;
			} 
		}
	}

	protected static ArrayList<Individuo> getIndividuosFront(int k, Populacao populacao) {
		HashMap<Integer, ArrayList<Individuo>> front = new HashMap<Integer, ArrayList<Individuo>>();
		front = populacao.getFront();
		for (Entry<Integer, ArrayList<Individuo>> entry : front.entrySet()) {
	        if(entry.getKey() == k){
	        	return entry.getValue();
	        }
		}
		return null;
	}

	public static Populacao selecaoNSGA2(Populacao populacao) {
		Populacao populacaoSolucao = new Populacao();
		int i = 1;
		int quantAPreencher = Principal.tamPop;
		int teste;
		while(populacaoSolucao.getPopulacao().size() != Principal.tamPop){
			ArrayList<Individuo> individuosFront = populacao.getFront().get(i);
			teste = individuosFront.size()-1;
			if(populacao.getFront().get(i).size() <= quantAPreencher){ 
				for (Individuo individuo : individuosFront) {
					populacaoSolucao.setIndividuo(individuo);
					quantAPreencher = quantAPreencher - 1;
				}
			}
			else {
				// distancia da multidao e inserir so a quantidade que falta
					calculaDistanciaMult(individuosFront);
					ordenaIndividuos(individuosFront, "distancia");
					for (int j = quantAPreencher; j > 0; j--) {
						
						populacaoSolucao.setIndividuo(individuosFront.get(teste));
						teste--;
					}
					quantAPreencher = 0;
				
			}
			i = i + 1;
		}
		return populacaoSolucao;
	}
	
	private static void ordenaIndividuos(ArrayList<Individuo> individuosFront, String fieldSort) {
		Individuo.fieldSort = fieldSort;
		Collections.sort(individuosFront);
	}
	
	public static Populacao cruzamento(Populacao populacao, ArrayList<String> objetivos) {
		Collections.shuffle(populacao.getPopulacao());
		Populacao populacaoFilha = new Populacao();
		
		int auxTam = populacao.getPopulacao().size()%2 == 1? populacao.getPopulacao().size()-1 : populacao.getPopulacao().size();
		double probMut = ThreadLocalRandom.current().nextDouble(0, 1);
		if(probMut <= Principal.probCruz){
		for (int i = 0 ; i < auxTam; i=i+2) {
			
			Individuo individuoMae = populacao.getPopulacao().get(i);
			Individuo individuoPai = populacao.getPopulacao().get(i+1);
			
			ArrayList<Double> genotipoFilho1 = new ArrayList<Double>();
			int tamGenotipo = individuoMae.getGenotipo().size();
			
			for (int j = 0; j < tamGenotipo; j++) {
				double rankMae = individuoMae.getGenotipo().get(j);
				double rankPai = individuoPai.getGenotipo().get(j);
				
				double alpha1 = ThreadLocalRandom.current().nextDouble(-0.1, 1.1);
				double u1 = alpha1 * rankMae + (1 - alpha1) * rankPai;
				if(u1 < 0) {
					u1 = 0;
				}
				if (u1 > 1) {
					u1 = 1;
				}
				u1 = reflexao(u1, rankMae, rankPai);
				genotipoFilho1.add(u1);
			}
			LinkedHashMap<String, Double> F2yFilho1 = RungeKutta.rungeKuttaEvalue(genotipoFilho1.get(0), genotipoFilho1.get(1), genotipoFilho1.get(2), genotipoFilho1.get(3));
			ArrayList<Double> genesFilho1 = mutacao(genotipoFilho1);
			ArrayList<Double> pontosFilho1 = new ArrayList<Double>();
			for (Double ponto : F2yFilho1.values()) {
				pontosFilho1.add(ponto);
			}
			Individuo filho1 = calculaFenotipoAedes(genesFilho1, objetivos, pontosFilho1);
			populacaoFilha.setIndividuo(filho1);
			
			ArrayList<Double> genotipoFilho2 = new ArrayList<Double>();
			for (int j = 0; j < tamGenotipo; j++) {
				double rankMae = individuoMae.getGenotipo().get(j);
				double rankPai = individuoPai.getGenotipo().get(j);
				
				double alpha1 = ThreadLocalRandom.current().nextDouble(0, 1);
				double u1 = alpha1 * rankMae + (1 - alpha1) * rankPai;
				if(u1 < 0) {
					u1 = 0;
				}
				if (u1 > 1) {
					u1 = 1;
				}
				u1 = reflexao(u1, rankMae, rankPai);
				genotipoFilho2.add(u1);
			}
			LinkedHashMap<String, Double> F2yFilho2 = RungeKutta.rungeKuttaEvalue(genotipoFilho1.get(0), genotipoFilho1.get(1), genotipoFilho1.get(2), genotipoFilho1.get(3));
			ArrayList<Double> genesFilho2 = mutacao(genotipoFilho2);
			ArrayList<Double> pontosFilho2 = new ArrayList<Double>();
			for (Double ponto : F2yFilho2.values()) {
				pontosFilho2.add(ponto);
			}
			Individuo filho2 = calculaFenotipoAedes(genesFilho2, objetivos, pontosFilho2);
			populacaoFilha.setIndividuo(filho2);
			
		}}
		return populacaoFilha;
	}

	private static double reflexao(double u1, double rankMae, double rankPai) {
		double menor, maior;
		if(rankMae<rankPai){
			menor = rankMae;
			maior = rankPai;
		}
		else{
			menor = rankPai;
			maior = rankMae;
		}
		
		if(u1<menor){
			u1 = menor + Math.abs(u1-menor);
		} 
		if(u1>maior){
			u1 = maior + Math.abs(u1-maior);
		}
		
		return u1;
	}

	private static ArrayList<Double> mutacao(ArrayList<Double> genesMut) {
		double probMut = ThreadLocalRandom.current().nextDouble(0, 1);
		double probValue = ThreadLocalRandom.current().nextDouble(-0.1, 1);
		int geneSortiado = ThreadLocalRandom.current().nextInt(0, genesMut.size());
		double gene = genesMut.get(geneSortiado);
		
		if(probMut <= Principal.probMut){
			//System.out.println("-------------MUTAÇÃO------------");
			gene = gene + gene*probValue; 
		}
		return genesMut;
	}
	
	public static Populacao unirPaieFilho(Populacao populacaoPai, Populacao populacaoFilha) {
		Populacao populacaoUnida = new Populacao();
		
		for (Individuo indPai : populacaoPai.getPopulacao()) {
			populacaoFilha.setIndividuo(indPai);
		}
		for (Individuo indFilho : populacaoFilha.getPopulacao()) {
			populacaoUnida.getPopulacao().add(indFilho);
		}
		
		return populacaoUnida;
	}
	
	public static void calculaDistanciaMult (ArrayList<Individuo> populacao){
		ordenaIndividuos(populacao, "fenotipo0");
		
		for (int i = 0; i < populacao.size(); i++) {
			Individuo individuo = populacao.get(i);
			double distancia = 0;
			for (int posFenotipo = 0; posFenotipo < individuo.getFenotipo().size(); posFenotipo++) {
				if(i == 0 || i == populacao.size()-1){
					distancia = Double.POSITIVE_INFINITY;
				} else {
					Individuo individuoAnt = populacao.get(i-1);
					Individuo individuoPos = populacao.get(i+1);
					
					distancia = distancia + Math.abs((individuoPos.getFenotipo().get(posFenotipo)) - (individuoAnt.getFenotipo().get(posFenotipo)));
				}
			}
			individuo.setDistancia(distancia);
		}
	}
}
