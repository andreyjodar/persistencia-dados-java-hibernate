package util;

public class JurosComposto {
	public static Double calcularRendimento(Double saldoInicial, Double taxaJuros, Integer periodo) {
		Double montante = saldoInicial * Math.pow(1 + taxaJuros, periodo);
		return montante - saldoInicial;
	}
}
