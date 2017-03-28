import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Principal extends Algoritmo {
	
	public static int tamPop = 200;
	public static double probCruz = 0.15;
	public static double probMut = 0.8;
	public static int geracoes = 300;

	public static void main(String[] args) throws Exception {
		
		/* Cenario1 */
		String c1 = "10", c2 = "100", c3 = "0.01";
		
		/* Cenario2 
		String c1 = "100", c2 = "10", c3 = "0.01";
		*/
		
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

		System.out.println("--------CRIAR POPULACAO---------");
		populacaoPai = new Populacao(tamanhoPopulacao, nomeObjetiva, objetivos);
		//salvarArquivo(populacaoPai, "populacaoInicial");
		
		System.out.println("------AVALIAR POPULACAO---------");		
		avaliarPopulacaoNSGA2(populacaoPai);
		
		System.out.println("-----------CRUZAMENTO-----------");
		populacaoFilha = cruzamento(populacaoPai, objetivos);
		
		String nome = "cruzamento" + criterioParada;
		//salvarArquivo(populacaoPai, nome);
		populacaoUniao = new Populacao();
		salvarArquivo(populacaoPai, "popInit-" + nomeObjetiva);
		while(criterioParada < geracoes){
			
			System.out.println("--------PAI + FILHO----------");
			populacaoUniao = unirPaieFilho(populacaoPai, populacaoFilha);
			
			avaliarPopulacaoNSGA2(populacaoPai);
			//salvarArquivo(populacaoPai, "geracaoPai-" + criterioParada);
			avaliarPopulacaoNSGA2(populacaoFilha);
			//salvarArquivo(populacaoFilha, "populacaoFilha-" + criterioParada);
			
			populacaoPai = new Populacao();
			populacaoFilha = new Populacao();
			
			System.out.println("------AVALIAR POPULACAO---------");
			avaliarPopulacaoNSGA2(populacaoUniao);
			//salvarArquivo(populacaoUniao, "PopulacaoUniao" + criterioParada);
			
			System.out.println("-------------SELECAO------------");
			populacaoPai = selecaoNSGA2(populacaoUniao);
			//salvarArquivo(populacaoPai, "PopulacaoSelecionada" + criterioParada);
			
			populacaoFilha = cruzamento(populacaoPai, objetivos);
			nome = "cruzamento" + criterioParada;
			
			//salvarArquivo(populacaoFilha, nome);
			criterioParada++;
		}
		System.out.println("------AVALIAR POPULACAO---------");
		avaliarPopulacaoNSGA2(populacaoPai);
		for (Individuo i : populacaoPai.getPopulacao()) {
			RungeKutta.rungeKuttaEvalue(i.getGenotipo().get(0), i.getGenotipo().get(1), i.getGenotipo().get(2), i.getGenotipo().get(3));
		}
		salvarArquivo(populacaoPai, "solucao-" + nomeObjetiva);
		
	}

	private static void salvarArquivo(Populacao populacao, String nomeArquivo) throws IOException {
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(nomeArquivo+".txt", true));
		String temp = "fenotipo = [";
		String temp1 = "genotipo = [";
		for (Individuo i : populacao.getPopulacao()) {
			//buffWrite.append("scatter(" + i.getFenotipo().get(0) + "," +i.getFenotipo().get(1) + ")");
			//buffWrite.append("\n hold on");
			temp += i.getFenotipo().get(0) + " " +i.getFenotipo().get(1) + ";";
			temp1 += i.getGenotipo().get(0) + /*" " +i.getGenotipo().get(1) + */";";
		}
		temp += "];";
		temp1 += "];";
		buffWrite.newLine();
		buffWrite.write(temp);
		buffWrite.newLine();
		buffWrite.write(temp1);
		buffWrite.close();
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
