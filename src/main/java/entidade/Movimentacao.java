package entidade;

import java.time.*;
import javax.persistence.*;

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
	
	public Movimentacao() {
		this.dataTransacao = LocalDateTime.now();
	}
	
	public Movimentacao(Conta conta, TransacaoTipo tipoTransacao, Double valorOperacao, String descricao) {
		this.conta = conta;
		this.tipoTransacao = tipoTransacao;
		this.valorOperacao = valorOperacao;
		this.dataTransacao = LocalDateTime.now();
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
}
