import java.util.ArrayList;

public class Integral {
	
	static double resolve(ArrayList<Double> pontos) {
		int i;
	    double integral = 0.0;
	    double deltat = 0.01;
	    
	    ArrayList<Double> ptsElevados = new ArrayList<Double>();
	    for(i = 0; i < pontos.size(); i++)
	    {
	    	ptsElevados.add(pontos.get(i)*pontos.get(i)); // elevando F2 ao quadrado
	    }
	    for(i = 0; i < pontos.size()-1; i++){ // integracao trapezio composto
	        integral = integral + (deltat/2.0)*(ptsElevados.get(i)+ptsElevados.get(i+1));
	    }
	    return integral;
	}
}