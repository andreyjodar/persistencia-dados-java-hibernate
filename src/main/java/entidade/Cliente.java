package entidade;

import java.time.LocalDate;
import javax.persistence.*;

@Entity
@Table (name = "cliente")
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 150, name = "nome", nullable = false)
	private String nome;
	@Column(length = 11, name = "cpf", nullable = false, unique = true)
	private String cpf;
	@Column(name = "data_nascimento", nullable = false)
	private LocalDate dataNascimento;
	
	public Cliente() {
		
	}
	
	public Cliente(String nome, String cpf, LocalDate dataNascimento) {
		this.nome = nome;
		this.cpf = cpf;
		this.dataNascimento = dataNascimento;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
}
