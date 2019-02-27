package base.cartas.magias;

import java.util.List;
import java.util.UUID;

import base.cartas.Carta;
import base.cartas.Lacaio;
import io.Escritor;

public class DanoArea extends Dano {

	public DanoArea(UUID ID, String nome, int custoMana, int dano) {
		super(ID, nome, custoMana, dano);
	}

	public DanoArea(String nome, int custoMana, int dano) {
		super(nome, custoMana, dano);
	}

	@Override
	public String toString() {
		return super.toString() + "\nTipo da carta: DANO_AREA";
	}
	
	@Override
	public void escreveAtributos(Escritor fw){
			fw.escreveDeLimObj("DanoArea");
			fw.escreveAtributo("id", getID().toString());
			fw.escreveAtributo("nome", getNome());
			fw.escreveAtributo("custoMana", String.valueOf(getCustoMana()));
			fw.escreveAtributo("dano", String.valueOf(getDano()));
			fw.escreveDeLimObj("DanoArea");
			fw.Finalizar();
	}
	
	public void usar(List<Carta> alvos){
		int i;
		Lacaio alvo;
		
		for(i = 0 ; i < alvos.size() ; ++i){
			alvo = (Lacaio) alvos.get(i);
			alvo.setVidaAtual(alvo.getVidaAtual() - super.getDano());
		}
	}
}
