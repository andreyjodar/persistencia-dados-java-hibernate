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

	public Conta() {
		this.dataAbertura = LocalDateTime.now();
	}
	
	public Conta(Cliente cliente, ContaTipo contaTipo) {
		this();
		this.cliente = cliente;
		this.contaTipo = contaTipo;
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
	
}
