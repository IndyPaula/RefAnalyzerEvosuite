package code_IMP;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import exceptions.CodeSelectorExceptions;

public class ValidadorIMP {
	static RecuperatorMethodIMP recuperatorMethodIMP = new RecuperatorMethodIMP();

	public ValidadorIMP() {
		// TODO Auto-generated constructor stub
	}

	public String vericaMetodoByModelo(int visiPub, int paramCom, int retCom) {

		double vp = 0.3646 * visiPub;
		double pc = 0.2637 * paramCom;
		double rc = 0.3304 * retCom;
		double e = (1.1758 - (vp) - (pc) - (rc));
		double resultModel = 1 / (1 + (1 / Math.pow(2.7182, e)));

		BigDecimal valorFinalPorc = new BigDecimal(resultModel).setScale(2, RoundingMode.HALF_DOWN);

		double doubValue = valorFinalPorc.doubleValue();
		int porcFinal = (int) (doubValue * 100);

		if (resultModel > 0.5) {
			// System.out.println(
			// " Esse método tem uma grande probabilidade ("+ resultModel +"%)
			// de ser coberto pelas ferramentas de geração automática"
			// + " de testes de unidade (Randoop e Evosuite), ou seja, muito
			// provavelmente elas conseguiram gera testes para ele.");
			//
			System.out.println("===========================");
			System.out.println("ALTA: ---- Alta probabilidade " + porcFinal
					+ "% de conseguir gerar testes que detectam uma falta de refatorameto.");
			System.out.println("===========================");

			return Constant.DETECTOU;
		}
		double diff = Math.abs(1 - porcFinal);
		// System.out.println(
		// " Esse método tem uma grande probabilidade ("+ diff +"%) de Não ser
		// coberto pelas ferramentas de geração automática, ou seja, muito
		// provavelmente elas não conseguiram gera testes para ele.");
		System.out.println("===========================");
		System.out.println("BAIXA: ---- Há baixa probabilidade de " + diff
				+ "% de NÃO conseguir gerar testes que detectam uma falta de refatorameto. ");
		System.out.println("===========================");

		return Constant.NAODECTECTA;
	}

	public int[] vericaParamMetodoByModelo(Method method) {

		int visiPub = 0;
		int paramCom = 0;
		int retCom = 0;
		ArrayList<String> atributesMeth;
		int[] nada = {};
		try {
			atributesMeth = recuperatorMethodIMP.checksAttributePrimitive(method);
			String retMeth = recuperatorMethodIMP.checksReturnPrimitive(method);
			String vis = recuperatorMethodIMP.checksVisibilityMethod(method);

			if (vis != null && atributesMeth != null && retMeth != null) {

				if (vis.equals("PUBLIC")) {
					visiPub = 1;
				} else if (atributesMeth.contains(Constant.PARAM_COMP)) {
					paramCom = 1;
				} else if (retMeth.contains(Constant.PARAM_COMP)) {
					retCom = 1;
				}
			} else {
				System.err
						.println("Falhou no verificador da visibilidade, parametro ou retorno da Classe ValidadosIMP");
				return null;
			}

			int[] a = { visiPub, paramCom, retCom };

			return a;

		} catch (CodeSelectorExceptions e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nada;
	}

	public void showAllProblematicMethods(ArrayList<Method> metodosProblematicos) {
		System.out.println("======================================================== ");
		System.out.println("====== Métodos problemáticos para as ferramentas======== ");
		System.out.println("======================================================== " + "\n");
		
		System.out.println(metodosProblematicos.size());
		

		for (Method method : metodosProblematicos) {
			System.out.println(
					"- Nome do Metodo:" + method.getName().toString() + ",    - Assinatura: " + method.toString());
		}

		System.out.println(
				"==> NOTA IMPORTANTE: Os métodos abaixo possuem características de que dificultam a geração automática de testes para estes, onde estas podem está relacionadas com a visibilidade do mesmo, os parametros de entrada e/ou tipo de retorno. "
						+ "\n");
	}

}
