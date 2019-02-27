package base.cartas;

import base.ILaMaSerializable;
import io.Escritor;

import java.util.UUID;

public abstract class Carta implements ILaMaSerializable {
	private UUID ID;
	private String nome;
	private int custoMana;
	
	public Carta(UUID ID, String nome, int custoMana) {
		this.ID = ID;
		this.nome = nome;
		this.custoMana = custoMana;
	}

	public Carta(String nome, int custoMana) {
		ID = UUID.randomUUID();
		this.nome = nome;
		this.custoMana = custoMana;
	}

	public UUID getID() {
		return ID;
	}

	public void setID(UUID ID) {
		this.ID = ID;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getCustoMana() {
		return custoMana;
	}

	public void setCustoMana(int custoMana) {
		this.custoMana = custoMana;
	}

	@Override
	public String toString() {
		return "\nID = "+ID+"\nnome = "+nome+"\ncustoMana = "+ custoMana;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj == null) return false;
		if(this == obj) return true;
		if(obj instanceof Carta)
			if(((Carta) obj).ID == ID)
				return true;
		return false;
	}
	
	public void escreveAtributos(Escritor fw){
			fw.escreveDeLimObj("Carta");
			fw.escreveAtributo("id", ID.toString());
			fw.escreveAtributo("nome", nome);
			fw.escreveAtributo("custoMana", String.valueOf(custoMana));
			fw.escreveDeLimObj("Carta");
			fw.Finalizar();
	}
	
	@Override
	public int hashCode(){
		int hash = ID.hashCode();
		hash += (nome != null ? nome.hashCode() : 0);
		hash *= custoMana;
		return hash;
	}
	
	public abstract void usar(Carta alvo);
	
}
