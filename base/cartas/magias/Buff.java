package base.cartas.magias;

import java.util.UUID;

import base.cartas.Carta;
import base.cartas.Lacaio;
import io.Escritor;

public class Buff extends Magia{
	private int aumentoEmAtaque;
	private int aumentoEmVida;
	
	public Buff(UUID ID, String nome, int custoMana, 
				int aumentoEmAtaque, int aumentoEmVida){
		super(ID, nome, custoMana);
		this.aumentoEmAtaque = aumentoEmAtaque;
		this.aumentoEmVida = aumentoEmVida;
	}
	
	public Buff(String nome, int custoMana, 
				int aumentoEmAtaque, int aumentoEmVida){
		super(nome, custoMana);
		this.aumentoEmAtaque = aumentoEmAtaque;
		this.aumentoEmVida = aumentoEmVida;
	}

	public int getAumentoEmAtaque() {
		return aumentoEmAtaque;
	}

	public void setAumentoEmAtaque(int aumentoEmAtaque) {
		this.aumentoEmAtaque = aumentoEmAtaque;
	}

	public int getAumentoEmVida() {
		return aumentoEmVida;
	}

	public void setAumentoEmVida(int aumentoEmVida) {
		this.aumentoEmVida = aumentoEmVida;
	}
	
	@Override
	public String toString(){
		return super.toString() 
			   + "\nTipo da carta: BUFF"
			   + "\naumento em ataque = " + aumentoEmAtaque
			   + "\naumento em vida = " + aumentoEmVida;
	}
	
	public void escreveAtributos(Escritor fw){
			fw.escreveDeLimObj("Buff");
			fw.escreveAtributo("id", getID().toString());
			fw.escreveAtributo("nome", getNome());
			fw.escreveAtributo("custoMana", String.valueOf(getCustoMana()));
			fw.escreveAtributo("aumentoEmAtaque", String.valueOf(aumentoEmAtaque));
			fw.escreveAtributo("aumentoEmVida", String.valueOf(aumentoEmVida));
			fw.escreveDeLimObj("Buff");
			fw.Finalizar();
	}
	
	@Override
	public void usar(Carta alvo){
		Lacaio lacaio = (Lacaio)alvo;
		lacaio.setAtaque(lacaio.getAtaque() + aumentoEmAtaque);
		lacaio.setVidaMaxima(lacaio.getVidaMaxima() + aumentoEmVida);
		lacaio.setVidaAtual(lacaio.getVidaAtual() + aumentoEmVida);
	}
}
