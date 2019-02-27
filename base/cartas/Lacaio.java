package base.cartas;

import java.util.UUID;

import io.Escritor;

public class Lacaio extends Carta {
	private int ataque;
	private int vidaAtual;
	private int vidaMaxima;
	private HabilidadesLacaio habilidade;
	
	public Lacaio(UUID ID, String nome, int ataque, int vidaAtual, int vidaMaxima, int custoMana, HabilidadesLacaio hab) {
		super(ID, nome, custoMana);
		this.ataque = ataque;
		this.vidaAtual = vidaAtual;
		this.vidaMaxima = vidaMaxima;
		this.habilidade = hab;
	}
	
	public Lacaio(String nome, int ataque, int vida, int custoMana) {
		super(nome, custoMana);
		this.ataque = ataque;
		this.vidaAtual = vida;
		this.vidaMaxima = vida;
		this.habilidade = null;
	}
	
	public Lacaio(String nome, int ataque, int vida, int custoMana, HabilidadesLacaio hab) {
		super(nome, custoMana);
		this.ataque = ataque;
		this.vidaAtual = vida;
		this.vidaMaxima = vida;
		this.habilidade = hab;
	}

	public int getAtaque() {
		return ataque;
	}

	public void setAtaque(int ataque) {
		this.ataque = ataque;
	}

	public int getVidaAtual() {
		return vidaAtual;
	}

	public void setVidaAtual(int vidaAtual) {
		this.vidaAtual = vidaAtual;
	}

	public int getVidaMaxima() {
		return vidaMaxima;
	}

	public void setVidaMaxima(int vidaMaxima) {
		this.vidaMaxima = vidaMaxima;
	}
	
	public void setHabilidade(HabilidadesLacaio habilidade){
		this.habilidade = habilidade;
	}
	
	public HabilidadesLacaio getHabilidade(){
		return habilidade;
	}
	
	@Override
	public String toString() {
		return super.toString() + 
				"\nTipo da carta: LACAIO" +
				"\nataque = " + ataque + 
				"\nvidaAtual = " + vidaAtual + 
				"\nvidaMaxima = " + vidaMaxima + "\n" +
				(habilidade != null ? habilidade : "null" );
	}

	@Override
	public void escreveAtributos(Escritor fw){
			fw.escreveDeLimObj("Lacaio");
			fw.escreveAtributo("id", getID().toString());
			fw.escreveAtributo("nome", getNome());
			fw.escreveAtributo("custoMana", String.valueOf(getCustoMana()));
			fw.escreveAtributo("ataque", String.valueOf(ataque));
			fw.escreveAtributo("vidaAtual", String.valueOf(vidaAtual));
			fw.escreveAtributo("vidaMaxima", String.valueOf(vidaMaxima));
			fw.escreveAtributo("habilidade", habilidade.toString());
			fw.escreveDeLimObj("Lacaio");
			fw.Finalizar();
	}
	
	@Override
	public void usar(Carta alvo) {
		Lacaio lacaio = (Lacaio)alvo;
		lacaio.setVidaAtual(lacaio.getVidaAtual() - ataque);
	}
}
