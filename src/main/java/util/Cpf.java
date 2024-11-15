package util;

public class Cpf {
	
	public static boolean verificarCpf(String cpf) {
		if(cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
			return false;
		}
		
		String numBase = cpf.substring(0,9);
		int primeiroDigito = calcularDigitoVerificador(numBase, 10);
		int segundoDigito = calcularDigitoVerificador(numBase + primeiroDigito, 11);
		return cpf.equals(numBase + primeiroDigito + segundoDigito);
	}
	
	public static int calcularDigitoVerificador(String numBase, int pesoInicial) {
		int soma = 0;
		for (int i = 0; i < numBase.length(); i++) {
			int numero = Character.getNumericValue(numBase.charAt(i));
			soma += numero * pesoInicial--;
		}
		
		if (soma % 11 > 2) {
			return 11 - (soma % 11);
		} 
		return 0;
	}
}
