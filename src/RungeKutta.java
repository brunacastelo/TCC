import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class RungeKutta {

	/** Constantes */
	private static double phi;		 // Taxa de oviposição por unidade de individuo
	private static double k;		 // Capacidade do meio relacionada com o numero de nutrientes, espaço, etc
	private static double miE;		 // Taxa de mortalidade na população de ovos
	private static double miA;		 // Taxa por unidade de indivíduo de mortalidade natural na fase aquática
	private static double miF1; 	 // Taxa por unidade de indivíduo de mortalidade natural da população de fêmeas pré-repasto sanguíneo
	private static double miF2; 	 // Taxa por unidade de indivíduo de mortalidade natural da população de fêmeas pos-repasto sanguíneo
	private static double alpha1;	 // porcentagem de mosquito que transforma da fase aquatica para a fase alada.
	private static double alpha2;	 // taxa por unidade de indivíduo em que o vetor passa da população imatura para a população de fêmeas pré-repasto sanguíneo
	private static double alpha3;	 // taxa por unidade de indivíduo com que fêmeas pré-repasto sanguíneo transforma em fêmeas pós-repasto sanguíneo

	public static LinkedHashMap<String, Double> rungeKuttaEvalue (double u1,double u2, double u3, double u4) {	
		DecimalFormat df = new DecimalFormat("000.00");
		LinkedHashMap<String, Double> Ey = new LinkedHashMap<String, Double>(); 
		LinkedHashMap<String, Double> Ay = new LinkedHashMap<String, Double>(); 
		LinkedHashMap<String, Double> F1y = new LinkedHashMap<String, Double>(); 
		LinkedHashMap<String, Double> F2y = new LinkedHashMap<String, Double>(); 
		
		double deltat = 0.01;
		String tAnterior = null; 
		for (double t = 0.0; t <= 360.0; t = t + deltat) {
			String tString = df.format(t); 
			if(0 <= t  && t <= 90){
				//setVariaveisFavoraveis();
				//setVariaveisIntermediaria();
				setVariaveisDesfavoraveis();
			}
			else if(90 < t  && t <= 180){
				//setVariaveisFavoraveis();
				setVariaveisIntermediaria();
				//setVariaveisDesfavoraveis();
			}
			else if(180 < t  && t <= 270){
				setVariaveisFavoraveis();
				//setVariaveisIntermediaria();
				//setVariaveisDesfavoraveis();
			}
			else if(270 < t  && t <= 360){
				//setVariaveisFavoraveis();
				setVariaveisIntermediaria();
				//setVariaveisDesfavoraveis();
			}
			
			if (t == 0){
				/*y0=[96.3      263.5     192.2    1345.13];%favorável*/
				/*Ey.put(tString, 96.3);
				Ay.put(tString, 263.5);
				F1y.put(tString, 192.2);
				F2y.put(tString, 1345.13);*/
				/*y0=[88.6550   324.79    48.265   164.103];%desfavorável
				Ey.put(tString, 88.6550);
				Ay.put(tString, 324.79);
				F1y.put(tString, 48.265);
				F2y.put(tString, 164.103);*/
				/*y0=[94.1539   268.609   100.829  504.147];%intermediário*/			  				
				Ey.put(tString, 94.1539);
				Ay.put(tString, 268.609);
				F1y.put(tString, 100.829);
				F2y.put(tString, 504.147);
				
				tAnterior = tString;
				continue;
			}
			
			double aux1 = 0;
			double aux2 = 0;
			
			if(t <= u3){
				aux1 = u1;
			}
			if(t <= u4){
				aux2 = u2;
			}
			
			double ce = aux1; // taxa por unidade de indivíduo de mortalidade adicional por causa do controle na população de ovos
			double ca = aux1; // taxa por unidade de indivíduo de mortalidade adicional por causa do controle na população aquática
			double cf1 = aux2; // taxa por unidade de indivíduo de mortalidade adicional por causa do controle na população de fêmeas pré-repasto sanguíneo
			double cf2 = aux2; // taxa por unidade de indivíduo de mortalidade adicional por causa do controle na população de fêmeas pós-repasto sanguíneo

			
			double e = 0, a = 0, f1 = 0, f2 = 0;
			/** Solver K1 */
			try {
				e = Ey.get(tAnterior);
				a = Ay.get(tAnterior);
				f1 = F1y.get(tAnterior);
				f2 = F2y.get(tAnterior);
				
			} catch (Exception exp) {
				// TODO: handle exception
				System.out.print("Vish");
			}
			

			// Eggs phase (eggs)
			double Ek1 = phi*(1 - (e/k))*f2 - alpha1*e - miE*e - ce*e;
			// Aquatic phase (larvae and pulp)
			double Ak1 = alpha1*e - alpha2*a - miA*a - ca*a;
			// Winged phase (pre-repast females)
			double F1k1 = alpha2*a - alpha3*f1 - miF1*f1 - cf1*f1;
			// Winged phase (post-repast females)
			double F2k1 = alpha3*f1 - miF2*f2 - cf2*f2;

			/** Solver K2 */
			e = Ey.get(tAnterior) + (deltat / 2.0) * Ek1;
			a = Ay.get(tAnterior) + (deltat / 2.0) * Ak1;
			f1 = F1y.get(tAnterior) + (deltat / 2.0) * F1k1;
			f2 = F2y.get(tAnterior) + (deltat / 2.0) * F2k1;
			
			// Eggs phase (eggs)
			double Ek2 = phi*(1 - (e/k))*f2 - alpha1*e - miE*e - ce*e;
			// Aquatic phase (larvae and pulp)
			double Ak2 = alpha1*e - alpha2*a - miA*a - ca*a;
			// Winged phase (pre-repast females)
			double F1k2 = alpha2*a - alpha3*f1 - miF1*f1 - cf1*f1;
			// Winged phase (post-repast females)
			double F2k2 = alpha3*f1 - miF2*f2 - cf2*f2;

			/** Solver K3 */
			e = Ey.get(tAnterior) + (deltat / 2.0) * Ek2;
			a = Ay.get(tAnterior) + (deltat / 2.0) * Ak2;
			f1 = F1y.get(tAnterior) + (deltat / 2.0) * F1k2;
			f2 = F2y.get(tAnterior) + (deltat / 2.0) * F2k2;
			
			// Eggs phase (eggs)
			double Ek3 = phi*(1 - (e/k))*f2 - alpha1*e - miE*e - ce*e;
			// Aquatic phase (larvae and pulp)
			double Ak3 = alpha1*e - alpha2*a - miA*a - ca*a;
			// Winged phase (pre-repast females)
			double F1k3 = alpha2*a - alpha3*f1 - miF1*f1 - cf1*f1;
			// Winged phase (post-repast females)
			double F2k3 = alpha3*f1 - miF2*f2 - cf2*f2;
			
			/** Solver K4 */
			e = Ey.get(tAnterior) + deltat * Ek3;
			a = Ay.get(tAnterior) + deltat * Ak3;
			f1 = F1y.get(tAnterior) + deltat * F1k3;
			f2 = F2y.get(tAnterior) + deltat * F2k3;
			
			// Eggs phase (eggs)
			double Ek4 = phi*(1 - (e/k))*f2 - alpha1*e - miE*e - ce*e;
			// Aquatic phase (larvae and pulp)
			double Ak4 = alpha1*e - alpha2*a - miA*a - ca*a;
			// Winged phase (pre-repast females)
			double F1k4 = alpha2*a - alpha3*f1 - miF1*f1 - cf1*f1;
			// Winged phase (post-repast females)
			double F2k4 = alpha3*f1 - miF2*f2 - cf2*f2;
			
	        // yn+1 = yn + h/6*(k1+2*(k2+k3)+k4)
            			         
			Ey.put(tString, (Ey.get(tAnterior) + (deltat/6.0)*(Ek1 + 2.0*(Ek2 + Ek3) + Ek4)));
            Ay.put(tString, (Ay.get(tAnterior) + (deltat/6.0)*(Ak1 + 2.0*(Ak2 + Ak3) + Ak4)));
            F1y.put(tString, (F1y.get(tAnterior) + (deltat/6.0)*(F1k1 + 2.0*(F1k2 + F1k3) + F1k4)));
            F2y.put(tString, (F2y.get(tAnterior) + (deltat/6.0)*(F2k1 + 2.0*(F2k2 + F2k3) + F2k4)));
        
            //System.out.println("Valor k:" +i+ ":" +Ey+","+Ay+","+F1y+","+F2y);
            tAnterior = tString;
			
		}
		
		/*if(Principal.geracoes == 300 - 1){
			salvarArquivo(Ey, Ay, F1y, F2y);
		}*/
		return F2y;
 
	}
	
	private static void salvarArquivo(LinkedHashMap<String, Double> Ey, HashMap<String, Double> Ay, HashMap<String, Double> F1y, HashMap<String, Double> F2y) {
		try {
			BufferedWriter buffWriteEy = new BufferedWriter(new FileWriter("Ey.csv", true));
			String tempEy = "";
			for (Entry<String, Double> valueEy : Ey.entrySet()) {
				tempEy += valueEy.getValue() + ";";
				tempEy += "\r\n"; 
			}	
			buffWriteEy.newLine();
			buffWriteEy.write(tempEy);
			buffWriteEy.close();
			
			BufferedWriter buffWriteAy = new BufferedWriter(new FileWriter("Ay.csv", true));
			String tempAy = "";
			for (Entry<String, Double> valueAy : Ay.entrySet()) {
				tempAy += valueAy.getValue() + ";";
				tempAy += "\r\n"; 
			}	
			buffWriteAy.newLine();
			buffWriteAy.write(tempAy);
			buffWriteAy.close();
			
			BufferedWriter buffWriteF1y = new BufferedWriter(new FileWriter("F1y.csv", true));
			String tempF1y = "";
			for (Entry<String, Double> valueF1y : F1y.entrySet()) {
				tempF1y += valueF1y.getValue() + ";";
				tempF1y += "\r\n"; 
			}	
			buffWriteF1y.newLine();
			buffWriteF1y.write(tempF1y);
			buffWriteF1y.close();
			
			BufferedWriter buffWriteF2y = new BufferedWriter(new FileWriter("F2y.csv", true));
			String tempF2y = "";
			for (Entry<String, Double> valueF2y : F2y.entrySet()) {
				tempF2y += valueF2y.getValue() + ";";
				tempF2y += "\r\n"; 
			}	
			buffWriteF2y.newLine();
			buffWriteF2y.write(tempF2y);
			buffWriteF2y.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public static void setVariaveisFavoraveis(){
		phi = 1.0;
		k = 100.0;
		miE = 1.0 / 100.0;
		miA = 1.0 / 62.0;
		miF1 = 1.0 / 35.0;
		miF2 = 1.0 / 35.0;
		alpha1 = 1.0 / 2.0;
		alpha2 = 1.0 / 6.0;
		alpha3 = 1.0 / 5.0;
	}
		
	public static void setVariaveisIntermediaria(){
		phi = 1.0;
		k = 100.0;
		miE = 1.0 / 100.0;
		miA = 1.0 / 62.0;
		miF1 = 1.0 / 25.0;
		miF2 = 1.0 / 25.0;
		alpha1 = 1.0 / 3.3;
		alpha2 = 1.0 / 11.1;
		alpha3 = 1.0 / 5.0;
	}
	
	public static void setVariaveisDesfavoraveis(){
		phi = 1.0;
		k = 100.0;
		miE = 1.0 / 100.0;
		miA = 1.0 / 62.0;
		miF1 = 1.0 / 17.0;
		miF2 = 1.0 / 17.0;
		alpha1 = 1.0 / 5.0;
		alpha2 = 1.0 / 26.0;
		alpha3 = 1.0 / 5.0;
	}
}
