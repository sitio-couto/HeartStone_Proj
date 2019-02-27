import java.util.ArrayList;
/**
 * RESUMO DA ESTRATEGIA ADOTADA.
 * 
 * Inicialmente, o codigo analisa qual sera a abordagem adotada no turno.
 * Caso ainda seja um dos primeiros 8 turnos, a estrategia adotada sera a 
 * CURVA DE MANA, pois visa o melhor proveito da mana	Caso contrario, 
 * se o heroi tive ao menos 5 pontos de vida a mais do que o heroi 
 * do oponente e pelo menos dois lacaios a mais em mesa prontos para uso, 
 * adota-sem uma abordagem AGRESSIVA, devido ao fato de que a vantagem de
 * vida, em mesa e a mana maior que 8 permite um comportamento menos cuidadoso 
 * no turno e mais ofensivo. Como caso padrao (turno > 8 e sem vantagem) 
 * adota-se a abordagem de CONTROLE esta eh a base pois garante o equilibrio
 * entre o poder de ataque do jogador e de seu adversario.
 * Para cada turno, o programa faz a combinacao de cartas possives a serem 
 * usadas, levando em conta a mana do jogador, e seleciona a mais adequada de
 * acordo com a abordagem selecionada.	A selecao eh feita a partir de uma 
 * pontuacao que prioriza determinados atributos para cada um das abordagems
 * possiveis.	No caso da CURVA DE MANA, ganha a selecao de cartas que 
 * deixar menos mana sobrando, no caso de empate, a preferencia eh de 
 * baixar lacaios de maior vida pra promover mais vantagem em mesa deixando
 * uma gama de alvos maior para o jogador adversario.	No caso do CONTROLE, 
 * Testa-se qual combinacao de cartas pode realizar mais trocas favoraveis 
 * com os lacaios inimigos, esta abordagem permite maior controle da mesa de 
 * jogo dado que foca os atacantes inimigos. Magias que eliminam lacaios 
 * fortes ou multiplos possuem peso alto neste caso. O ultimo caso eh o 
 * AGRESSIVO. Neste, ganha a combinacao de cartas que possibilita o conjunto
 * de jogadas que desferem o maior dano possivel no heroi adversario. Todas as 
 * ofensivas sao focadas no heroi adversario, eh irreleante o bom proveito
 * de magias e lacaios neste caso, e lacaios sao preferiveis a magia para
 * exercer superioridade em mesa como segunda prioridade.
 * Ha uma abordagem agressiva que eh testada a parte chamada ELIMINAR HEROI.
 * Nesta particularidade eh testado se o poder de ataque atual (respeitando
 * o limite de mana) eh o sufuciente para executar o heroi adversario, caso
 * seja, esse metodo retorna o conjunto de jogadas que executam o heroi.
 */

/**
 * Esta classe representa um Jogador aleatório (realiza jogadas de maneira
 * aleatória) para o jogo LaMa (Lacaios & Magias).
 * 
 * @see java.lang.Object
 * @author Rafael Arakaki - MC302
 */
public class JogadorRA188115 extends Jogador {
	private ArrayList<CartaLacaio> lacaios;
	private ArrayList<CartaLacaio> lacaiosOponente;
	private enum Abordagens {
		AGRESSIVO, CONTROLE, CURVA_MANA;
	}
	private enum Relevancia {
		DANO, VIDA, MANA, TROCAS;
	}
	private Abordagens abordagem;

	/**
	 * O método construtor do JogadorAleatorio.
	 * 
	 * @param maoInicial
	 *            Contém a mão inicial do jogador. Deve conter o número de
	 *            cartas correto dependendo se esta classe Jogador que está
	 *            sendo construída é o primeiro ou o segundo jogador da
	 *            partida.
	 * @param primeiro
	 *            Informa se esta classe Jogador que está sendo construída é
	 *            o primeiro jogador a iniciar nesta jogada (true) ou se é o
	 *            segundo jogador (false).
	 */
	public JogadorRA188115(ArrayList<Carta> maoInicial, boolean primeiro) {
		primeiroJogador = primeiro;

		mao = maoInicial;
		lacaios = new ArrayList<CartaLacaio>();
		lacaiosOponente = new ArrayList<CartaLacaio>();

		// Mensagens de depuração:
		System.out.println("*Classe JogadorRAxxxxxx* Sou o " + (primeiro ? "primeiro" : "segundo")
				+ " jogador (classe: JogadorAleatorio)");
		System.out.println("Mao inicial:");
		for (int i = 0; i < mao.size(); i++)
			System.out.println("ID " + mao.get(i).getID() + ": " + mao.get(i));
	}

	/**
	 * Um método que processa o turno de cada jogador. Este método deve
	 * retornar as jogadas do Jogador decididas para o turno atual (ArrayList de
	 * Jogada).
	 * 
	 * @param mesa
	 *            O "estado do jogo" imediatamente antes do início do turno
	 *            corrente. Este objeto de mesa contém todas as informações
	 *            'públicas' do jogo (lacaios vivos e suas vidas, vida dos
	 *            heróis, etc).
	 * @param cartaComprada
	 *            A carta que o Jogador recebeu neste turno (comprada do
	 *            Baralho). Obs: pode ser null se o Baralho estiver vazio ou o
	 *            Jogador possuir mais de 10 cartas na mão.
	 * @param jogadasOponente
	 *            Um ArrayList de Jogada que foram os movimentos utilizados pelo
	 *            oponente no último turno, em ordem.
	 * @return um ArrayList com as Jogadas decididas
	 */
	public ArrayList<Jogada> processarTurno(Mesa mesa, Carta cartaComprada, ArrayList<Jogada> jogadasOponente) {
		int minhaMana, minhaVida, vidaOponente;

		if (cartaComprada != null)
			mao.add(cartaComprada);
		if (primeiroJogador) {
			minhaMana = mesa.getManaJog1();
			minhaVida = mesa.getVidaHeroi1();
			lacaios = mesa.getLacaiosJog1();
			vidaOponente = mesa.getVidaHeroi2();
			lacaiosOponente = mesa.getLacaiosJog2();
			// System.out.println("--------------------------------- Começo de
			// turno pro jogador1");
		} else {
			minhaMana = mesa.getManaJog2();
			minhaVida = mesa.getVidaHeroi2();
			lacaios = mesa.getLacaiosJog2();
			vidaOponente = mesa.getVidaHeroi1();
			lacaiosOponente = mesa.getLacaiosJog1();
			// System.out.println("--------------------------------- Começo de
			// turno pro jogador2");
		}

		ArrayList<Jogada> minhasJogadas = new ArrayList<Jogada>();

		if (primeiroJogador) {
			lacaios = mesa.getLacaiosJog1();
			lacaiosOponente = mesa.getLacaiosJog2();
		} else {
			lacaios = mesa.getLacaiosJog2();
			lacaiosOponente = mesa.getLacaiosJog1();
		}

		ArrayList<Carta> cartasSelecionadas = new ArrayList<Carta>();
		ArrayList<Carta> cartasEmAnalise = new ArrayList<Carta>();
		
		if(eliminarHeroi(minhaMana, vidaOponente, minhasJogadas))
			return minhasJogadas;
		
		/*define a abordagem para o turno.*/
		abordagem = defineAbordagem(minhaVida, vidaOponente, minhaMana);
	
		/*analisa recursivamente todas as combinacoes possiveis com a mana atual.*/
		for (int i = 0; i < mao.size(); ++i) {
			if (mao.get(i).getMana() <= minhaMana)
				selecaoDeCartas(i, minhaMana, cartasSelecionadas, cartasEmAnalise, minhaMana);
		}
		
		/*remove as cartas que serao utilizadas da mao do jogador.*/
		for(Carta c : cartasSelecionadas)
			mao.remove(c);
		
		/*baixa os lacaios da selecao a mesa.*/
		for(int i = 0; i < cartasSelecionadas.size(); ++i){	
			minhaMana -= cartasSelecionadas.get(i).getMana();		/*remove a mana utilizada na mao selecionada*/
			if(cartasSelecionadas.get(i) instanceof CartaLacaio){
				minhasJogadas.add(new Jogada(TipoJogada.LACAIO, cartasSelecionadas.get(i), null));
				cartasSelecionadas.remove(i);
				--i;
			}
		}
		
		/*define as jogadas de ataque.*/
		ofensivas(abordagem, cartasSelecionadas, minhasJogadas);	/*escolhe os ataques a serem feitos com as magias selecionadas e os lacaios em mesa.*/
		
		return minhasJogadas;
	}

	/**
	 * Um metodo que define quais das trs abordagems possiveis deve ser adotada de acordo
	 * com aspectos do turno atual.
	 * 
	 * @param minhaVida
	 * 			Vida do Heroi do jogador.
	 * @param vidaOponente
	 * 			Vida do heroi oponente.
	 * @return	enumerador com a abordagem escolhida.
	 */
	private Abordagens defineAbordagem(int minhaVida, int vidaOponente, int minhaMana){
		Abordagens abordagemEscolhida = null;
		int vantagemDeQuantidade = lacaios.size() - lacaiosOponente.size();	/*maior o valor, maior a vantagem*/
		int vantagemDeVida = minhaVida - vidaOponente;	/*maior o valor, maior a vantagem*/
		
		if(minhaMana <= 8)
			abordagemEscolhida = Abordagens.CURVA_MANA;
		else if(vantagemDeQuantidade >= 2 && vantagemDeVida >= 5)
			abordagemEscolhida = Abordagens.AGRESSIVO;
		else
			abordagemEscolhida = Abordagens.CONTROLE;
		
		return abordagemEscolhida;
	}
	
	/**
	 * Um metodo recursivo que cria todas as combinacoes possiveis de cartas 
	 * diferente e as submete a teste para ver qual eh a mais adequada de 
	 * acordo com a abordagem escolhida.
	 * 
	 * @param start
	 * 			inteiro que define a posicao da carta que deve tentar adicionar a
	 * 			selecao de cartas se houver mana.
	 * @param minhaMana
	 * 			mana disponivel para a selecao de cartas.
	 * @param cartasSelecionadas
	 * 			cartas escolhidas como mais adequadas para o turno.
	 * @param cartasEmAnalise
	 * 			combinacao que sera comparada com a melhor selecao ate o momento.
	 * @param manaRestante
	 * 			mana remanescente para usar mais cartas em mao.
	 */
	private void selecaoDeCartas(int start, int minhaMana, ArrayList<Carta> cartasSelecionadas,
			ArrayList<Carta> cartasEmAnalise, int manaRestante) {
		
		cartasEmAnalise.add(mao.get(start));
		manaRestante -= mao.get(start).getMana();
		
		/*percorre cartas recursivamente para serem adicionadas a selecao.*/
		for (int i = (start + 1); i < mao.size(); ++i) 
			if(mao.get(i).getMana() <= manaRestante)   /*testa se ha mana para a proxima carta.*/
				selecaoDeCartas(i, minhaMana, cartasSelecionadas, cartasEmAnalise, manaRestante);
		
		/* testa a qualidade da selecao de acordo com a abordagem escolhida.*/
		ponderarSelecaoDeCartas(cartasEmAnalise, cartasSelecionadas);
		
		cartasEmAnalise.remove(cartasEmAnalise.size() - 1);

		return;
	}
	
	/**
	 * Um metodo que pondera se a selecao atual e melhor ou pior que a nova combinacao
	 * de acordo com a abordagem escolhida.
	 * 
	 * @param cartasEmAnalise
	 * 			Nova combinacao a ser ponderada em relacao a melhor combinacao atual.
	 * @param cartasSelecionadas
	 * 			Melhor combinacao de cartas atual.
	 */
	private void ponderarSelecaoDeCartas(ArrayList<Carta> cartasEmAnalise, ArrayList<Carta> cartasSelecionadas) {
		Relevancia primaria = null, secundaria = Relevancia.VIDA;
		int limiteDeLacaios = (7 - lacaios.size());
		
		/*testa se ha espaco em mesa para os lacaios da selecao, se nao, retorna sem fazer nada.*/
		for(Carta c : cartasEmAnalise)
			if(c instanceof CartaLacaio)
				--limiteDeLacaios;
		if(limiteDeLacaios < 0) return;
		
		
		switch (abordagem) {
		case AGRESSIVO:    /*Prioriza a selecao de maior dano, caso o dano seja igual, prioriza a vida*/
			primaria = Relevancia.DANO;
			break;
		case CURVA_MANA:   /*Prioriza a selecao de maior custo de mana, se igual, prioriza a vida*/
			primaria = Relevancia.MANA;
			break;
		case CONTROLE:     /*prioriza trocas favoraveis, se igual, prioriza maior vida*/
			primaria = Relevancia.TROCAS;
			secundaria = Relevancia.DANO;
			break;
		}
		
		if (soma(primaria, cartasSelecionadas) < soma(primaria, cartasEmAnalise)) {
			cartasSelecionadas.clear();
			cartasSelecionadas.addAll(cartasEmAnalise);
		} else if (soma(primaria, cartasSelecionadas) == soma(primaria, cartasEmAnalise)) {
			/*OBS: deixar a vida como relevancia secundaria prioriza lacaios sobre magias.*/
			if (soma(secundaria, cartasSelecionadas) < soma(secundaria, cartasEmAnalise)) { 
				cartasSelecionadas.clear();
				cartasSelecionadas.addAll(cartasEmAnalise);
			}
		}
		
		return;
	}

	/**
	 * Um metodo que soma varios aspectos das cartas visando qualificar a qualidade
	 * da selecao atual em relacao a nova combinacao de cartas.
	 * 
	 * @param relevancia
	 * 			Enumerador que define qual aspecto da carta deve ser priorizado
	 * @param cartas
	 * 			Conjunto de cartas as quais serao qualificadas.
	 * @return	retorna um inteiro que qualifica as cartas.
	 */
	private int soma(Relevancia relevancia, ArrayList<Carta> cartas) {
		boolean cartaMagiaDesperdicada;	/*garante que as cartas magias seja usadas em situacoes beneficas*/
		int somatoria = 0;
		CartaMagia magia;

		for (Carta c : cartas) {
			switch (relevancia) {
			case DANO:		/*calcula o dano total de uma selecao de cartas.*/
				if (c instanceof CartaMagia) {
					magia = (CartaMagia) c;
					switch (magia.getMagiaTipo()) {
					case BUFF:
						if (lacaios.isEmpty())	/*zera selecao se nao houer alvo para o buff.*/
							return 0;
					default:
						somatoria += magia.getMagiaDano();
					}
				} else
					somatoria += ((CartaLacaio) c).getAtaque();
				break;
			case VIDA:		/*calcula a vida total de uma selecao de cartas.*/
				if (c instanceof CartaMagia) {
					magia = (CartaMagia) c;
					switch (magia.getMagiaTipo()) {
					case BUFF:
						if (lacaios.isEmpty())	/*zera selecao se nao houer alvo para o buff.*/
							return 0;
						else
							somatoria += magia.getMagiaDano();
					default:
						somatoria += 0;	/*magias que nao sao de buff nao contribuem para a vida total.*/
					}
				} else
					somatoria += ((CartaLacaio) c).getVidaMaxima();
				break;
			case TROCAS:	/*calcula o total de trocas favoraveis de uma selecao.*/
				if (c instanceof CartaMagia) {
					magia = (CartaMagia) c;
					switch (magia.getMagiaTipo()) {
					case ALVO:
						cartaMagiaDesperdicada = true;
						for(CartaLacaio l : lacaiosOponente){
							int danoExcesso = magia.getMagiaDano() - l.getVidaAtual();
							if(danoExcesso >= 0 && danoExcesso <= 1){	/*certifica que o dano da magia sera bem aproveitado.*/
								++somatoria;
								cartaMagiaDesperdicada = false;	/*executa ao menos um lacaio.*/
							}
						}
						if(cartaMagiaDesperdicada) return 0; /*nao executa lacaios.*/
					case AREA:
						cartaMagiaDesperdicada = true;
						if(lacaiosOponente.size() < 3 ) return 0;
						for(CartaLacaio l : lacaiosOponente){
							if(magia.getMagiaDano() >= l.getVidaAtual()){
								++somatoria;
								cartaMagiaDesperdicada = false;	/*executa ao menos um lacaio.*/
							}
						}
						if(cartaMagiaDesperdicada) return 0;	/*nao executa lacaios.*/
						break;
					case BUFF:
						cartaMagiaDesperdicada = true;
						if(lacaios.isEmpty())
							return 0;
						else{
							for(CartaLacaio l :lacaios)
								if(l.getVidaAtual() > 4){		/*buffa somenta lacaios de vida alta (mais durabilidade).*/
									++somatoria;
									cartaMagiaDesperdicada = false;	/*ha um lacaio de vida alta para buffar*/
									break;
								}
							}
						if(cartaMagiaDesperdicada) return 0;	/*nao ha lacaios de vida alta para buffar*/
						break;
					}
				} else{
					for(CartaLacaio l : lacaiosOponente){
						if(trocaFavoravel(l, (CartaLacaio) c))
							++somatoria;
					}
				}
				break;
			case MANA:		/*calcula o proveito total da mana usada na selecao.*/
				if (c instanceof CartaMagia) {
					magia = (CartaMagia) c;
					switch (magia.getMagiaTipo()) {	/*pondera a qualidade das magias no turno para a abordagem*/
					case BUFF:	/*verifica se ha lacaios de maior custo para buffar*/
						if (lacaios.isEmpty())
							return 0;
						else{
							for(CartaLacaio l : lacaios){
								if(magia.getMana() < l.getMana()){
									somatoria += magia.getMana();
									break;
								}
							}
						}
						break;
					case ALVO:	/*verifica se ha lacaios de maior custo para eliminar*/
						for(CartaLacaio l : lacaiosOponente){
							int danoDesperdicado = (magia.getMagiaDano() - l.getVidaAtual());
							if(danoDesperdicado <= 2 && danoDesperdicado >= 0){	/*checa se o dano da magia nao eh muito maior que avida do lacaio*/
								if(magia.getMana() < l.getMana()){
									somatoria += magia.getMana();
									break;
								}
							}
						}
						break;
					case AREA:	/*verifica se possui ao menos 2 lacaios em mesa*/
						if(lacaiosOponente.size() < 2)
							return 0;
						else {
							somatoria += magia.getMana();
							for(CartaLacaio l : lacaiosOponente){	/*incrementa metade do custo dos inimigos a serem eliminados como bonus.*/
								if(magia.getMagiaDano() >= l.getVidaAtual())
									somatoria += (l.getMana() / 2);
							}
						}
						break;
					}
				} else
					somatoria += ((CartaLacaio) c).getMana();
				break;
			}
		}

		return somatoria;
	}
	
	/**
	 * Um metodo que decide quais jogadas ofensivas serao realizadas pelos lacaios em mesa
	 * e pelas magias da selecao de cartas escolhida.
	 * 
	 * @param abordagem
	 * 			Enumerador que define qual abordegem estrategica deve ser tomada.
	 * @param cartasSelecionadas
	 * 			Selecao de cartas escolhida como a mais adequada para a abordagem.
	 * 			Neste caso a selecao so contem as magias, uma vez que os lacaios
	 * 			desta ja foram baixados a mesa.
	 * @param minhasJogadas
	 * 			Jogadas que serao efetuadas no turno do jogador
	 */
	private void ofensivas(Abordagens abordagem, ArrayList<Carta> cartasSelecionadas,  ArrayList<Jogada> minhasJogadas){
		CartaMagia magia = null;
		CartaLacaio alvo = null;
		int i;
		
		switch(abordagem){
		case AGRESSIVO:
			//coordena as magias a serem usadas
			for(Carta c : cartasSelecionadas){
				magia = (CartaMagia) c;
				switch(magia.getMagiaTipo()){
				case BUFF:  /*buffa lacaio de maior dano*/
					alvo = lacaios.get(0);
					for(CartaLacaio l : lacaios)
						if(l.getAtaque() > alvo.getAtaque())
							alvo = l;
					minhasJogadas.add(new Jogada(TipoJogada.MAGIA, magia, alvo));
					alvo = null;
					break;
				default:
					minhasJogadas.add(new Jogada(TipoJogada.MAGIA, magia, null));
					break;
				}
			}
			//ataca o heroi com os lacaios.
			for(CartaLacaio l : lacaios)
				minhasJogadas.add(new Jogada(TipoJogada.ATAQUE, l, null));
			break;
		case CONTROLE:
			for(Carta c : cartasSelecionadas){ /*coordena o uso das magias.*/
				magia = (CartaMagia) c;
				switch(magia.getMagiaTipo()){
				case BUFF:  /*buffa lacaio de maior vida*/
					alvo = lacaios.get(0);
					for(CartaLacaio l : lacaios)
						if(l.getVidaAtual() > alvo.getVidaAtual())
							alvo = l;
					minhasJogadas.add(new Jogada(TipoJogada.MAGIA, magia, alvo));
					alvo = null;
					break;
				case ALVO:	/*busca eliminar lacaio de de vida proxima a seu dano, se nao, ataca heroi*/
					alvo = null;
					int danoExcesso;
					for(CartaLacaio l : lacaiosOponente){
						danoExcesso = magia.getMagiaDano() - l.getVidaAtual();
						if(danoExcesso <= 1 && danoExcesso >= 0)	/*buscao aproveitar ao maximo o dano da magia*/
							alvo = l;
					}
					minhasJogadas.add(new Jogada(TipoJogada.MAGIA, magia, alvo));
					resultadoDoAtaque(alvo, magia);	/*computa dano sofrido pelo lacaio oponente*/
					alvo = null;
					break;
				case AREA:	/*caso dano em area, ataca indiferentemente*/
					minhasJogadas.add(new Jogada(TipoJogada.MAGIA, magia, null));
					for(i = 0; i < lacaiosOponente.size(); ++i){	/*computa os danos sofridos pelos oponentes.*/
						alvo = lacaiosOponente.get(i);
						if(resultadoDoAtaque(alvo, magia))
							--i;	/*corrige iteracao caso morra um lacaio oponente*/
					}
					break;
				}
			}
			
			alvo = null;
			for(CartaLacaio l : lacaios){	/*coordena ataques dos lacaios.*/
				for(i = 0; i < lacaiosOponente.size(); ++i){
					if(trocaFavoravel(lacaiosOponente.get(i), l)){	/*testa se ha troca favoravel, se nao, ataca heroi por padrao.*/
						alvo = lacaiosOponente.get(i);
						if(resultadoDoAtaque(alvo, l))
							--i;	/*corrige iteracao caso morra um lacaio oponente*/
						break;
					}
				}
				minhasJogadas.add(new Jogada(TipoJogada.ATAQUE, l, alvo));
				alvo = null;	/*padroniza o heroi como alvo*/
			}
			break;
		case CURVA_MANA:
			for(Carta c : cartasSelecionadas){ /*coordena o uso das magias.*/
				magia = (CartaMagia) c;
				switch(magia.getMagiaTipo()){
				case BUFF:  /*buffa lacaio de maior custo de mana*/
					alvo = lacaios.get(0);
					for(CartaLacaio l : lacaios)
						if(l.getMana() > alvo.getMana())
							alvo = l;
					minhasJogadas.add(new Jogada(TipoJogada.MAGIA, magia, alvo));
					alvo = null;
					break;
				case ALVO:	/*elimina lacaios de maior ou igual custo de mana, se nao, ataca heroi*/
					alvo = null;
					for(CartaLacaio l : lacaiosOponente)
						if((l.getVidaAtual() <= magia.getMagiaDano())&&(l.getMana() <= magia.getMana()))
							alvo = l;
					minhasJogadas.add(new Jogada(TipoJogada.MAGIA, magia, alvo));
					resultadoDoAtaque(alvo, magia);	/*computa dano sofrido pelo lacaio oponente*/
					alvo = null;
					break;
				case AREA:	/*caso dano em area, ataca indiferentemente*/
					minhasJogadas.add(new Jogada(TipoJogada.MAGIA, magia, null));
					for(i = 0; i < lacaiosOponente.size(); ++i){	/*computa os danos sofridos pelos oponentes.*/
						alvo = lacaiosOponente.get(i);
						if(resultadoDoAtaque(alvo, magia))
							--i;	/*corrige iteracao caso morra um lacaio oponente*/
					}
					break;
				}
			}
			
			alvo = null;
			for(CartaLacaio l : lacaios){	/*coordena ataques dos lacaios.*/
				for(i = 0; i < lacaiosOponente.size(); ++i){
					if(trocaFavoravel(lacaiosOponente.get(i), l)){	/*testa se ha troca favoravel, se nao, ataca heroi por padrao.*/
						alvo = lacaiosOponente.get(i);
						if(resultadoDoAtaque(alvo, l))
							--i;	/*corrige iteracao caso morra um lacaio oponente*/
						break;
					}
				}
				minhasJogadas.add(new Jogada(TipoJogada.ATAQUE, l, alvo));
				alvo = null;	/*padroniza o heroi como alvo*/
			}
			break;
		}
		
		return;
	}
	
	/**
	 * Um metodo que computa a vida perdida por um lacaio oponente apos um ataque, e
	 * testa se o oponente foi morto.
	 * 
	 * @param alvo
	 * 			Lacaio Oponente a ser atacado (ou heroi no caso de alvo == null).
	 * @param atacante
	 * 			Carta que sera utilizada para realizar o ataque no oponente.
	 * @return true se o lacaio oponente for morto, false se nao for.
	 */
	private boolean resultadoDoAtaque(CartaLacaio alvo, Carta atacante){
		//subtrai o dano do atacante na vida do alvo.
		if(alvo == null)	/*caso o alvo seja o heroi, nada eh feito*/
			return false;
		else if(atacante instanceof CartaLacaio)
			alvo.setVidaAtual(alvo.getVidaAtual() - ((CartaLacaio) atacante).getAtaque());
		else
			alvo.setVidaAtual(alvo.getVidaAtual() - ((CartaMagia) atacante).getMagiaDano());
		
		//caso o alvo seja morto, descarta ele dos lacaios inimigos.
		if(alvo.getVidaAtual() <= 0){
			lacaiosOponente.remove(alvo);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Um metodo que testa se um lacaio "l" realiza uma troca favoravel em relacao
	 * a um lacaio "a". 
	 * @param a
	 * 			Lacaio Oponente que deve sair em desvantagem na troca.
	 * @param l
	 * 			Lacaio atacante que deve sair com vantegeam na troca.
	 * @return	true se for uma troca favoravel, false se nao for.
	 */
	private boolean trocaFavoravel(CartaLacaio a, CartaLacaio l){
		if(l.getAtaque() >= a.getVidaAtual()){	/*testa se o lacaio elimina o alvo.*/
			if(l.getVidaAtual() > a.getAtaque())	/*true se lacaio mata e NAO morre*/
				return true;
			else if(l.getMana() > a.getMana())		/*true se lacaio mata e morre, mas tem menor custo de mana*/
				return true;
			else if(l.getVidaAtual() <= (a.getVidaAtual() - 4))	/*true se lacaio morre, mas mata oponente com, pelo menos, 4 pontos de vida a mais*/
				return true;
			else if((l.getVidaAtual() < a.getVidaAtual()) && (l.getAtaque() < a.getAtaque()))	/*true se lacaio morre, mas mata oponente com maior vida e ataque.*/
				return true;
		}
		
		return false;	/*nao ha trocas favoraveis.*/
	}

	/**
	 * Um metodo que testa se o heroi pode ser eliminado com a forca de ataque 
	 * disponivel para uso no turno atual.
	 * 
	 * @param minhaMana
	 * 			Mana do jogador.
	 * @param vidaOponente
	 * 			Vida do heroi adversario.
	 * @param minhasJogadas
	 * 			Jogadas que serao efetuadas no turno.
	 * @return	jogadas para eliminar o heroi.
	 */
	private boolean eliminarHeroi(int minhaMana, int vidaOponente, ArrayList<Jogada> minhasJogadas) {
		ArrayList<CartaMagia> magiasUteis = new ArrayList<CartaMagia>();
		CartaMagia maiorDano = null;
		int poderDeAtaque = 0;
		int manaCopia = minhaMana;

		for (Carta c : mao){	/*separa cartas magia que podem ser utilizadas da mao.*/
			if ((c instanceof CartaMagia) && (c.getMana() <= minhaMana)){
				switch(((CartaMagia) c).getMagiaTipo()){
				case BUFF:
					if(lacaios.isEmpty())	/*pula iteracao se nao hover lacaios para usar o buff, se nao, vai para o default.*/
						continue;
				default:
					magiasUteis.add((CartaMagia) c);
					break;
				}
			}
		}
				
				
		for (int i = 0; i < magiasUteis.size(); ++i) {	/*ordena magias em mao em ordem decresente de dano.*/
				maiorDano = magiasUteis.get(i);
				for(int j = 0; j < (magiasUteis.size() - i); ++j){
					if(magiasUteis.get(j).getMagiaDano() > maiorDano.getMagiaDano())
						maiorDano = magiasUteis.get(j);
				}
				
				/*remove da posicao original em lacaios e dispoe no final da lista*/
				magiasUteis.add(maiorDano);
				magiasUteis.remove(maiorDano);
		}
		
		for (CartaLacaio l : lacaios)	/*soma o poder de ataque dos lacaio em mesa.*/
			poderDeAtaque += l.getAtaque();
		
		for(CartaMagia m : magiasUteis){	/*soma o poder de ataque de magias que podem ser compradas.*/
			if(manaCopia >= m.getMana()){
				poderDeAtaque += m.getMagiaDano();
				manaCopia -= m.getMana();
			}
		}

		if (manaCopia >= 2)	/*se possivel, soma o poder Heroico*/
			++poderDeAtaque;

		if(poderDeAtaque >= vidaOponente){	/*se o poder de ataque for o suficiente para eliminar o heroi, inicia as jogadas.*/
			for(CartaMagia m : magiasUteis){	/*ataque de magias que podem ser compradas.*/
				Carta alvo = null;
				if(minhaMana >= m.getMana()){
					switch(m.getMagiaTipo()){
					case BUFF:	/*caso seja magia de buff, pega o primeiro lacaio como alvo.*/
						alvo = lacaios.get(0);
					default:
						minhasJogadas.add(new Jogada(TipoJogada.MAGIA, m, alvo));
						minhaMana -= m.getMana();
					}
				}
			}
			
			if (minhaMana >= 2)		/*ataque do heroi*/
				minhasJogadas.add(new Jogada(TipoJogada.PODER, null, null));
			
			for (CartaLacaio l : lacaios)	/*ataque dos lacaio em mesa.*/
				minhasJogadas.add(new Jogada(TipoJogada.ATAQUE, l, null));
			
			return true;
		}else
			return false;
	}
}
