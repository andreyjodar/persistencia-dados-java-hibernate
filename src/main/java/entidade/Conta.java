package entidade;

import java.time.*;
import javax.persistence.*;

@Entity
@Table (name = "conta")
public class Conta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "id_cliente", nullable = false)
	private Cliente cliente;
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_conta", nullable = false)
	private ContaTipo contaTipo;
	@Column(name = "data_abertura", nullable = false)
	private LocalDateTime dataAbertura;
	@Column(name = "saldo", nullable = false)
	private Double saldo;
	@Column(name = "cashback_acumulado", nullable = false)
	private Double cashbackAcumulado;
	@Column(name = "ultima_atualizacao_cashback")
	private LocalDate ultimaAtualizacaoCashback;

	public Conta() {
		this.saldo = 0.0;
		this.cashbackAcumulado = 0.0;
	}
	
	public Conta(Cliente cliente, ContaTipo contaTipo, LocalDateTime dataAbertura) {
		this.cliente = cliente;
		this.contaTipo = contaTipo;
		this.dataAbertura = dataAbertura;
		this.saldo = 0.0;
		this.cashbackAcumulado = 0.0;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public ContaTipo getContaTipo() {
		return contaTipo;
	}
	
	public void setContaTipo(ContaTipo contaTipo) {
		this.contaTipo = contaTipo;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}
	
	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
	
	public Double getSaldo() {
		return saldo;
	}
	
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	
	public Double getCashBackAcumulado() {
		return cashbackAcumulado;
	}
	
	public void setCashBackAcumulado(Double cashbackAcumulado) {
		this.cashbackAcumulado = cashbackAcumulado;
	}
	
	public LocalDate getUltimaAtualizacaoCashback() {
		return this.ultimaAtualizacaoCashback;
	}
	
	public void setUltimaAtualizaçãoCashback(LocalDate ultimaAtualizacaoCashback) {
		this.ultimaAtualizacaoCashback = ultimaAtualizacaoCashback;
	}
}
