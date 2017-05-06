import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class Smetric {

	private double somaArea;
	private ArrayList<Double> genotipo = new ArrayList<Double>();
	private ArrayList<String> objetivos = new ArrayList<String>();
	private ArrayList<Double> areas = new ArrayList<Double>();
	
	public double somaArea(Populacao populacaoPai) {
		Individuo.fieldSort = "fenotipo0";
		Double distancia;
		Collections.sort(populacaoPai.getPopulacao());
		
		for (int i = 0; i < populacaoPai.getPopulacao().size()-1; i++) {
			/*
			String c1 = "10", c2 = "100", c3 = "0.01";
			
			String obj1 = "(1/2)*c1*(_x1^2)*_x3 + (1/2)*c2*(_x2^2)*_x4";
			obj1 = obj1.replaceAll("c1", c1);
			obj1 = obj1.replaceAll("c2", c2);
			
			String obj2 = "(1/2)*c3*_x1";
			obj2 = obj2.replaceAll("c3", c3);
			
			objetivos.add(obj1);
			objetivos.add(obj2);
			
			genotipo.add(0.0);
			genotipo.add(0.0);
			genotipo.add(0.0);
			genotipo.add(0.0);
			LinkedHashMap<String, Double> ponto2 = RungeKutta.rungeKuttaEvalue(0.0, 0.0, 0.0, 0.0);
			
			ArrayList<Double> pontosFilho2 = new ArrayList<Double>();
			for (Double ponto : ponto2.values()) {
				pontosFilho2.add(ponto);
			}
			Individuo indY = Algoritmo.calculaFenotipoAedes(genotipo, objetivos, pontosFilho2);
			genotipo.clear();
			genotipo.add(1.0);
			genotipo.add(1.0);
			genotipo.add(90.0);
			genotipo.add(90.0);
			LinkedHashMap<String, Double> ponto1 = RungeKutta.rungeKuttaEvalue(1.0, 1.0, 90.0, 90.0);
			
			ArrayList<Double> pontosFilho1 = new ArrayList<Double>();
			for (Double ponto : ponto1.values()) {
				pontosFilho1.add(ponto);
			}
			Individuo indX = Algoritmo.calculaFenotipoAedes(genotipo, objetivos, pontosFilho1);
			Individuo ind = new Individuo();
			ArrayList<Double> result = new ArrayList<Double>();
			result.add(indX.getFenotipo().get(0));
			result.add(indY.getFenotipo().get(1));
			ind.setFenotipo(result);
			*/
			
			double pix = populacaoPai.getPopulacao().get(i).getFenotipo().get(0);
			double piy = populacaoPai.getPopulacao().get(i+1).getFenotipo().get(1);
			double pjy = populacaoPai.getPopulacao().get(i).getFenotipo().get(1);
			distancia = (4950.0  - pix) * Math.abs(piy - pjy);
			areas.add(distancia);
			somaArea = somaArea + distancia;
		}
		return somaArea;
	}


}
