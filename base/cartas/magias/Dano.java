package base.cartas.magias;

import java.util.UUID;

import base.cartas.Carta;
import io.Escritor;

public class Dano extends Magia {
	private int dano;

	public Dano(UUID ID, String nome, int custoMana, int dano) {
		super(ID, nome, custoMana);
		this.dano = dano;
	}

	public Dano(String nome, int custoMana, int dano) {
		super(nome, custoMana);
		this.dano = dano;
	}

	public int getDano() {
		return dano;
	}

	public void setDano(int dano) {
		this.dano = dano;
	}

	@Override
	public String toString() {
		return super.toString() + 
				"\nTipo da carta: DANO" +
				"\ndano = " + dano;
	}

	public void escreveAtributos(Escritor fw){
			fw.escreveDeLimObj("Dano");
			fw.escreveAtributo("id", getID().toString());
			fw.escreveAtributo("nome", getNome());
			fw.escreveAtributo("custoMana", String.valueOf(getCustoMana()));
			fw.escreveAtributo("dano", String.valueOf(dano));
			fw.escreveDeLimObj("Dano");
			fw.Finalizar();
	}
	
	@Override
	public void usar(Carta alvo) {
		
	}
}
