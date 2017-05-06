import java.util.ArrayList;

public class Individuo implements Comparable<Individuo>, Cloneable {

	public static String fieldSort = "rank";

	private int rank;
	private double distancia;
	private int front;
	private int nd;
	private String color;

	private ArrayList<Individuo> dominados = new ArrayList<Individuo>();
	private ArrayList<Double> genotipo = new ArrayList<Double>();
	private ArrayList<Double> fenotipo = new ArrayList<Double>();

	public Individuo(ArrayList<Double> genotipo, ArrayList<Double> fenotipo) {
		this.setGenotipo(genotipo);
		this.setFenotipo(fenotipo);
		this.rank = 1;
	}

	public Individuo() {

	}

	public void incrementaRank() {
		rank = rank + 1;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}

	public void setZeroFit() {
		this.rank = 0;
	}

	public int compareTo(Individuo outroIndividuo) {
		if (fieldSort.equals("distancia")) {
			if (outroIndividuo == null
					|| this.distancia < outroIndividuo.getDistancia()) {
				return -1;
			} else if (this.distancia > outroIndividuo.getDistancia()) {
				return 1;
			} else {
				return 0;
			}
		}
		if (fieldSort.equals("fenotipo0")) {
			if (outroIndividuo == null
					|| this.fenotipo.get(0) < outroIndividuo.getFenotipo().get(
							0)) {
				return -1;
			} else if (this.fenotipo.get(0) > outroIndividuo.getFenotipo().get(
					0)) {
				return 1;
			} else {
				return 0;
			}
		}
		if (fieldSort.equals("fenotipo1")) {
			if (outroIndividuo == null
					|| this.fenotipo.get(1) < outroIndividuo.getFenotipo().get(1)) {
				return -1;
			} else if (this.fenotipo.get(1) > outroIndividuo.getFenotipo().get(1)) {
				return 1;
			} else {
				return 0;
			}
		}
		return 1;
	}

	public ArrayList<Double> getGenotipo() {
		return genotipo;
	}

	public void setGenotipo(ArrayList<Double> genotipo) {
		this.genotipo.addAll(genotipo);
	}

	public ArrayList<Double> getFenotipo() {
		return fenotipo;
	}

	public void setFenotipo(ArrayList<Double> fenotipo) {
		this.fenotipo.addAll(fenotipo);
	}

	public int getFront() {
		return front;
	}

	public void setFront(int front) {
		this.front = front;
	}

	public ArrayList<Individuo> getDominados() {
		return dominados;
	}

	public void setDominados(Individuo dominado) {
		this.dominados.add(dominado);
	}

	public int getNd() {
		return nd;
	}

	public void setNd(int nd) {
		this.nd = nd;
	}

	public void cleanDominados() {
		this.dominados = new ArrayList<Individuo>();
		this.nd = 0;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String toString() {
		String retorno = "";
		retorno += "X: " + genotipo.toString() + " ";
		retorno += "Y: " + fenotipo.toString() + " ";
		retorno += "nd: " + nd + " ";
		retorno += "distancia: " + distancia + " ";
		return retorno;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Cloning not allowed.");
			return this;
		}
	}

}
