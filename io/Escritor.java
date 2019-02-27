package io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Escritor {
	private File local;
	private BufferedWriter save;
	
	public Escritor(){
		try{
			local = new File("C:\\Users\\Sitio\\saveData.txt");
			if(!local.exists()) local.createNewFile();
			save = new BufferedWriter(new FileWriter(local, true));
		}catch(NullPointerException npe){
			System.err.println("Elemento nao instanciado:");
			npe.printStackTrace(System.err);
		}catch(IOException ioe){
			System.err.println("Falha na criacao/abertura do arquivo:");
			ioe.printStackTrace(System.err);
		}
	}
	
	public void escreveDeLimObj(String nomeObj){
		try{
			save.write("obj " + nomeObj);
			save.newLine();
		}catch(IOException ioe){
			System.err.println("Falha ao escrever no arquivo:");
			ioe.printStackTrace(System.err);
		}
	}
	
	public void escreveAtributo(String nomeAtributo, String valor){
		try{
			save.write(nomeAtributo + " " + valor + "\n");
			save.newLine();
		}catch(IOException ioe){
			System.err.println("Falha ao escrever no arquivo:");
			ioe.printStackTrace(System.err);
		}
	}
	
	public void Finalizar(){
		try{
			save.close();
			System.out.println("Salvo com sucesso!");
		}catch(IOException ioe){
			System.err.println("Falha ao fechar o arquivo:");
			ioe.printStackTrace(System.err);
		}
	}
}
