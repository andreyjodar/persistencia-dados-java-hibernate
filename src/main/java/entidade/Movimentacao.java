package entidade;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "movimentacao")
public class Movimentacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "id_conta", nullable = false)
	private Conta conta;
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_transacao", nullable = false)
	private TransacaoTipo tipoTransacao;
	@Column(name = "valor_operacao", nullable = false)
	private Double valorOperacao;
	@Column(name = "data_transacao", nullable = false)
	private LocalDateTime dataTransacao;
	@Column(length = 150, name = "descricao", nullable = true)
	private String descricao;
	@Column(name = "cashback", nullable = false)
	private Double cashback;
	
	public Movimentacao() {
		this.dataTransacao = LocalDateTime.now();
		this.cashback = 0.0;
	}
	
	public Movimentacao(Conta conta, TransacaoTipo tipoTransacao, Double valorOperacao, String descricao) {
		this();
		this.conta = conta;
		this.tipoTransacao = tipoTransacao;
		this.valorOperacao = valorOperacao;
		this.descricao = descricao;
	}

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

	public TransacaoTipo getTipoTransacao() {
		return tipoTransacao;
	}

	public void setTipoTransacao(TransacaoTipo tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}
	
	public Conta getConta() {
		return conta;
	}
	
	public void setConta(Conta conta) {
		this.conta = conta;
	}
	
	public Double getCashback() {
		return cashback;
	}
	
	public void setCashback(Double cashback) {
		this.cashback = cashback;
	}
}
