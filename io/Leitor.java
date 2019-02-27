package io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import base.ILaMaSerializable;
import base.cartas.HabilidadesLacaio;
import base.cartas.Lacaio;
import base.cartas.magias.Buff;
import base.cartas.magias.Dano;
import base.cartas.magias.DanoArea;

public class Leitor {
	private Scanner load;
	private String nome, tipo;
	private UUID id;
	private int custoMana;
	
	public Leitor(){}
	
	public List<ILaMaSerializable> leObjetos(){
		List<ILaMaSerializable> dados = new ArrayList<ILaMaSerializable>();
		
		try{
			load = new Scanner("C:\\Users\\Sitio\\saveData.txt");
			FileReader fr = new FileReader("C:\\Users\\Sitio\\saveData.txt");
			BufferedReader br = new BufferedReader(fr);
			
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("C:\\Users\\Sitio\\saveData.txt")));
			while(load.hasNext()){
				load.next();
				tipo = load.next();
				load.next(); 
				id = UUID.fromString(load.next());
				load.next(); 
				nome = load.next();
				load.next(); 
				custoMana = Integer.valueOf(load.next());
				
				switch(tipo){
					case "Lacaio":
						load.next(); 
						int ataque = Integer.valueOf(load.next());
						load.next(); 
						int vidaAtual = Integer.valueOf(load.next());
						load.next(); 
						int vidaMaxima = Integer.valueOf(load.next());
						load.next(); 
						HabilidadesLacaio hab = HabilidadesLacaio.valueOf(load.next());
						dados.add(new Lacaio(id, nome, ataque, vidaAtual, vidaMaxima, custoMana, hab));
						break;
					case "Dano":
						load.next();
						int dano = Integer.valueOf(load.next());
						dados.add(new Dano(id, nome, custoMana, dano));
						break;
					case "DanoArea":
						load.next();
						int danoArea = Integer.valueOf(load.next());
						dados.add(new DanoArea(id, nome, custoMana, danoArea));
						break;
					case "Buff":
						load.next();
						int aumentoEmAtaque = Integer.valueOf(load.next());
						load.next();
						int aumentoEmVida = Integer.valueOf(load.next());
						dados.add(new Buff(id, nome, custoMana, aumentoEmAtaque, aumentoEmVida));
						break;
				}
				load.next();
				load.next();
			}
		}catch(FileNotFoundException file404){
			System.err.println("Erro no carregamento - arquivo nao existe:");
			file404.printStackTrace(System.err);
		}finally{
			load.close();
		}
		
		return dados;
	}
}
