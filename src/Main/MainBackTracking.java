package Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.designwizard.api.DesignWizard;
import org.designwizard.design.MethodNode;

import code_IMP.Constant;
import code_IMP.RecuperatorClasseIMP;
import code_IMP.RecuperatorMethodIMP;
import exceptions.CodeSelectorExceptions;

public class MainBackTracking {

	static RecuperatorMethodIMP recuperatorMethodIMP = new RecuperatorMethodIMP();
	static RecuperatorClasseIMP recuperatorClasseIMP = new RecuperatorClasseIMP();
	static int count = 0;
	static int niveis = 0;
	static ArrayList<MethodNode> setFinal;
	static int metodos = 0;
	static int metodosPrivates = 0;
	static String nome = "";

	public static void main(String basePath2) throws CodeSelectorExceptions {

		String basePath = basePath2.toString();

		try {
			DesignWizard designWizard = new DesignWizard(basePath);
			Set<MethodNode> meth = designWizard.getAllMethods();
			for (MethodNode methodNode : meth) {
				metodos = metodos + 1;
				setFinal = new ArrayList<>();
				String visib = recuperatorMethodIMP.checksVisibilityMethod(methodNode);

				verificaNivelCallersMethPrivaEProtec(methodNode, visib);

				// HashMap<MethodNode, String> metodosClassificados =
				// classificarPai(callerMethods);

				// System.out.println("\n" + "==> Nome do M�todo: " +
				// recuperatorMethodIMP.getNameMethod(methodNode) + "\n"
				// + "==> Caller: " +
				// recuperatorMethodIMP.getCallersMethodoNode(methodNode) + "\n"
				// + "==> Visibilidade" + visib);

			}

			System.out.println("\n" + "Quantidade de metodos p�blicos: " + metodos);
			System.out.println("Quantidade de metodos privados: " + metodosPrivates);

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void verificaNivelCallersMethPrivaEProtec(MethodNode methodNode, String visib) {
		ArrayList<MethodNode> callers = new ArrayList<>();
		if (condicaoVerificaSeEhPrivOuProtect(methodNode)) {

			metodosPrivates = metodosPrivates + 1;

			Set<MethodNode> callerMethods = recuperatorMethodIMP.getCallersMethodoNode(methodNode);

			callers = backTracking(callerMethods);

			count = count + 1;
			// Imprime resutado dos metodos privados

			imprimindoOResultado(methodNode, visib, callers);

			// imprimindoOResultado(methodNode, visib, metodosClassificados);

			niveis = count;

			count = 0;
		}

	}

	private static void imprimindoOResultado(MethodNode methodNode, String visib, ArrayList<MethodNode> callers) {
		System.out.println("\n" + "Methodos " + methodNode + "Visibilidade: " + visib + "\n" + "N�veis: " + count + "\n"
				+ "Callers: " + callers.toString());

		classificarPai(callers);
	}

	private static void classificarPai(ArrayList<MethodNode> callers) {

		if (!callers.isEmpty()) {

			for (MethodNode methodNode : callers) {
				if (recuperatorMethodIMP.checksVisibilityMethod(methodNode).contains(Constant.PUBLIC)) {
					System.out.println("Esse m�todos tem caller desses M�todo � PUBLIC" + "\n" + "\n");
				} else {
					System.out.println(
							"Esse m�todos n�o possui caller PUBLIC. Ou seja, � PRIVATE ou PROTECTED, podendo ser incalcan��vel"
									+ "\n" + "\n");
				}
			}

		} else {

			System.out.println("O caller desses m�todo � NULL ou VAZIO, pode ser incalcan��vel" + "\n" + "\n");

		}
	}

	private static void imprimindoOResultado(MethodNode methodNode, String visib, HashMap<MethodNode, String> callers) {

		System.out
				.println("\n" + "Methodos " + methodNode + "Visibilidade: " + visib + "\n" + "N�veis: " + count + "\n");
		System.out.println("Callers: " + callers.toString());
	}

	private static ArrayList<MethodNode> backTracking(Set<MethodNode> callerMethods) {
		for (MethodNode methodNode : callerMethods) { // intera no array de
			// callers
			if (condicaoVerificaSeEhPrivOuProtect(methodNode)) {
				count++;
				backTracking(recuperatorMethodIMP.getCallersMethodoNode(methodNode));

			}
			setFinal.add(methodNode);
			return setFinal;
		}

		return setFinal;

	}

	private static boolean condicaoVerificaSeEhPrivOuProtect(MethodNode methodNode) {
		return recuperatorMethodIMP.checksVisibilityMethod(methodNode).contains(Constant.PRIVATE)
				|| recuperatorMethodIMP.checksVisibilityMethod(methodNode).contains(Constant.PROTECTED);
	}

	private static boolean condicaoVerificaSeEhNull(MethodNode methodNode) {
		return recuperatorMethodIMP.checksVisibilityMethod(methodNode).contains(Constant.NULL)
				|| recuperatorMethodIMP.checksVisibilityMethod(methodNode).contains(Constant.VAZIO);
	}
}
