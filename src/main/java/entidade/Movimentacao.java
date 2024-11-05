package entidade;

import java.time.*;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "conta")
public class Movimentacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "valor_operacao")
	private Double valorOperacao;
	private LocalDateTime dataTransacao;
	@Column(length = 150, name = "descricao", nullable = true, unique = false)
	private String descricao;
	private Double saldo;
	private String tipoTransacao;
	private String nomeCorrentista;
	private String cpfCorrentista;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValorOperacao() {
		return valorOperacao;
	}

	public void setValorOperacao(Double valorOperacao) {
		this.valorOperacao = valorOperacao;
	}

	public LocalDateTime getDataTransacao() {
		return dataTransacao;
	}

	public void setDataTransacao(LocalDateTime dataTransacao) {
		this.dataTransacao = dataTransacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipoTransacao() {
		return tipoTransacao;
	}

	public void setTipoTransacao(String tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}

	public String getNomeCorrentista() {
		return nomeCorrentista;
	}

	public void setNomeCorrentista(String nomeCorrentista) {
		this.nomeCorrentista = nomeCorrentista;
	}

	public String getCpfCorrentista() {
		return cpfCorrentista;
	}

	public void setCpfCorrentista(String cpfCorrentista) {
		this.cpfCorrentista = cpfCorrentista;
	}
	
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	
	public Double getSaldo() {
		return saldo;
	}

	@Override
	public String toString() {
		return "Conta [id=" + id + ", valorOperacao=" + valorOperacao + ", dataTransacao=" + dataTransacao
				+ ", descricao=" + descricao + ", tipoTransacao=" + tipoTransacao + ", nomeCorrentista="
				+ nomeCorrentista + ", cpfCorrentista=" + cpfCorrentista + "]";
	}

}
