import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class Principal extends Algoritmo {
	
	public static int tamPop = 300;
	public static double probCruz = 0.05;
	public static double probMut = 0.9;
	public static int geracoes = 1001;
	public static LinkedList<Double> sMetric;
	public static String pastaPath = "Execucao-";

	public static void main(String[] args) throws Exception {
		int quantExec = 30;
		
		for (int exec = 1; exec <= quantExec; exec++) {
			pastaPath = "Execucao-" + exec;
			sMetric = new LinkedList<Double>();
			/* Cenario1 */
			String c1 = "10", c2 = "100", c3 = "0.01";
			
			/* Cenario2  
			String c1 = "100", c2 = "10", c3 = "0.01";*/
			
			ArrayList<String> objetivos = new ArrayList<String>();
			String nomeObjetiva = "multObjAedes";
			
			/* Objective function one */
			//f(1) = (1/2)*c1*sum(u(1).^2)*(u(3))+(1/2)*c2*sum(u(2).^2)*(u(4)); %função1
			String obj1 = "(1/2)*c1*(_x1^2)*_x3 + (1/2)*c2*(_x2^2)*_x4";
			obj1 = obj1.replaceAll("c1", c1);
			obj1 = obj1.replaceAll("c2", c2);
			
			
			/* % Objective function two */
			//f(2) = (1/2)*c3*trapz(T,Y(:,4).^2);%função2
			String obj2 = "(1/2)*c3*_x1";
			obj2 = obj2.replaceAll("c3", c3);
			
			objetivos.add(obj1);
			objetivos.add(obj2);
			
			int criterioParada = 1;
			int tamanhoPopulacao = tamPop;
			
			Populacao populacaoPai = null;
			Populacao populacaoFilha = null;
			Populacao populacaoUniao = null;
	
			//System.out.println("--------CRIAR POPULACAO---------");
			populacaoPai = new Populacao(tamanhoPopulacao, nomeObjetiva, objetivos);
			//salvarArquivo(populacaoPai, "populacaoInicial");
			
			//System.out.println("------AVALIAR POPULACAO---------");		
			avaliarPopulacaoNSGA2(populacaoPai);
			
			//System.out.println("-----------CRUZAMENTO-----------");
			populacaoFilha = cruzamento(populacaoPai, objetivos);
			
			String nome = "cruzamento" + criterioParada;
			//salvarArquivo(populacaoPai, nome);
			populacaoUniao = new Populacao();
			salvarArquivo(populacaoPai, "popInit-" + nomeObjetiva);
			while(criterioParada < geracoes){
				System.out.println("Geração " + criterioParada);
				//System.out.println("--------PAI + FILHO----------");
				populacaoUniao = unirPaieFilho(populacaoPai, populacaoFilha);
				
				avaliarPopulacaoNSGA2(populacaoPai);
				//salvarArquivo(populacaoPai, "geracaoPai-" + criterioParada);
				avaliarPopulacaoNSGA2(populacaoFilha);
				//salvarArquivo(populacaoFilha, "populacaoFilha-" + criterioParada);
				
				populacaoPai = new Populacao();
				populacaoFilha = new Populacao();
				
				//System.out.println("------AVALIAR POPULACAO---------");
				avaliarPopulacaoNSGA2(populacaoUniao);
				//salvarArquivo(populacaoUniao, "PopulacaoUniao" + criterioParada);
				
				//System.out.println("-------------SELECAO------------");
				populacaoPai = selecaoNSGA2(populacaoUniao);
				//salvarArquivo(populacaoPai, "PopulacaoSelecionada" + criterioParada);
				
				populacaoFilha = cruzamento(populacaoPai, objetivos);
				nome = "cruzamento" + criterioParada;
				
				/** Smetric **/ 
				Smetric smetric = new Smetric();		
				avaliarPopulacaoNSGA2(populacaoPai);
				
				Populacao popSmetric = new Populacao();
				/*for (int pop = 0; pop < popFront.size(); pop ++) {
					if(popFront.get(pop).getFront() == 0){
						paretoSmetric.add(popFront.get(pop));
					}
				}*/
				for (Individuo individuo : populacaoPai.getFront().get(1)) {
					popSmetric.setIndividuo(individuo);
				}
				
				sMetric.add(smetric.somaArea(popSmetric));
				salvarArquivo(populacaoPai, "geracao-" + criterioParada);
				criterioParada++;
			}
			//System.out.println("------AVALIAR POPULACAO---------");
			avaliarPopulacaoNSGA2(populacaoPai);
			for (Individuo i : populacaoPai.getPopulacao()) {
				RungeKutta.rungeKuttaEvalue(i.getGenotipo().get(0), i.getGenotipo().get(1), i.getGenotipo().get(2), i.getGenotipo().get(3));
			}
			salvarArquivo(populacaoPai, "solucao-" + nomeObjetiva);
		
		}
		
	}

	private static void salvarArquivo(Populacao populacao, String nomeArquivo) throws Exception {
		StringBuilder temp = new StringBuilder() ;
		StringBuilder temp1 = new StringBuilder();
		StringBuilder temp3 = new StringBuilder();
		for (Individuo i : populacao.getPopulacao()) {
			//buffWrite.append("scatter(" + i.getFenotipo().get(0) + "," +i.getFenotipo().get(1) + ")");
			//buffWrite.append("\n hold on");
			temp.append(i.getFenotipo().get(0) + ";" +i.getFenotipo().get(1) + System.lineSeparator());
			temp1.append(i.getGenotipo().get(0) + ";" + i.getGenotipo().get(1) + ";" + i.getGenotipo().get(2) + ";" + i.getGenotipo().get(3) + System.lineSeparator());
		}
		for (Double valueSoma : sMetric) {
			temp3.append(valueSoma + " ;" + System.lineSeparator());
		}
		
		File filePasta = new File(pastaPath);
        if (!filePasta.exists()) {
            if (filePasta.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                throw new Exception();
            }
        }
        File fileLog = new File(filePasta, "Fenotipo-"+nomeArquivo+".csv");
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(fileLog, true));
		buffWrite.write(temp.toString());
		buffWrite.close();
        File fileLog1 = new File(filePasta, "Genotipo-"+nomeArquivo+".csv");
		BufferedWriter buffWrite1 = new BufferedWriter(new FileWriter(fileLog1, true));
		buffWrite1.write(temp1.toString());
		buffWrite1.close();
        File fileLog2 = new File(filePasta, "sMetric-"+nomeArquivo+".csv");
		BufferedWriter buffWrite2 = new BufferedWriter(new FileWriter(fileLog2, true));
		buffWrite2.write(temp3.toString());
		buffWrite2.close();
	}
	
	private static void salvarArquivoFrontColor(Populacao populacao, String nomeArquivo) throws IOException {
		for (Individuo i : populacao.getPopulacao()) {
			BufferedWriter buffWrite = new BufferedWriter(new FileWriter(nomeArquivo+".txt", true));
			buffWrite.append("plot(" + i.getFenotipo().get(0) + "," +i.getFenotipo().get(1) + ", '." +i.getColor()+ "')");
			buffWrite.append("\n hold on");
			buffWrite.newLine();
			buffWrite.close();
			System.out.println("Genotipo: " + i.getGenotipo());
			System.out.println("Solucao: " + i.getFenotipo().get(0));
			System.out.println("Solucao: " + i.getFenotipo().get(1));
		}
	}

	
}
