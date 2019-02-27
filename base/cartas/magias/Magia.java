package base.cartas.magias;

import java.util.UUID;

import base.cartas.Carta;
import io.Escritor;

public abstract class Magia extends Carta {
	public Magia(UUID ID, String nome, int custoMana){
		super(ID, nome, custoMana);
	}
	
	public Magia(String nome, int custoMana){
		super(nome, custoMana);
	}
	
	@Override
	public void escreveAtributos(Escritor fw){}
	
	@Override
	public String toString(){
		return super.toString();
	}
}
