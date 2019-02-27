package base;

import java.util.List;
import java.util.UUID;

import base.cartas.HabilidadesLacaio;
import base.cartas.Lacaio;
import base.cartas.magias.Buff;
import base.cartas.magias.Dano;
import base.cartas.magias.DanoArea;
import io.Escritor;
import io.Leitor;

public class Main {
	public static void main(String[] args) {
		Lacaio l = new Lacaio(UUID.randomUUID(), "Soldado", 10, 5, 8, 3, HabilidadesLacaio.INVESTIDA);
		Dano d = new Dano(UUID.randomUUID(), "Sniper", 12, 6);
		DanoArea da = new DanoArea(UUID.randomUUID(), "Granada", 15, 10); 
		Buff b = new Buff(UUID.randomUUID(), "Antibiotico", 5, 4, 7);
		
		l.escreveAtributos(new Escritor());
		d.escreveAtributos(new Escritor());
		da.escreveAtributos(new Escritor());
		b.escreveAtributos(new Escritor());
		
		Leitor leitor = new Leitor();
		List<ILaMaSerializable> dados = leitor.leObjetos();
		
		for(ILaMaSerializable c : dados){
			if(c instanceof Lacaio){
				l = (Lacaio) c;
				System.out.println(l.toString());
			}else if(c instanceof Dano){
				d = (Dano) c;
				System.out.println(d.toString());
			}else if(c instanceof DanoArea){
				da = (DanoArea) c;
				System.out.println(da.toString());
			}else{
				b = (Buff) c;
				System.out.println(b.toString());
			}
		}
		
	}
}
