//VINICIUS COUTO ESPINDOLA
//RA: 18815

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;

public class MotorRA188115 extends Motor {

	public MotorRA188115(Baralho deck1, Baralho deck2, ArrayList<Carta> mao1, ArrayList<Carta> mao2, Jogador jogador1,
			Jogador jogador2, int verbose, int tempoLimitado, PrintWriter saidaArquivo,
			EnumSet<Funcionalidade> funcionalidadesAtivas) {
		super(deck1, deck2, mao1, mao2, jogador1, jogador2, verbose, tempoLimitado, saidaArquivo,
				funcionalidadesAtivas);
		imprimir("========================");
		imprimir("*** Classe " + this.getClass().getName() + " inicializada.");
		imprimir("Funcionalidade ativas no Motor:");
		imprimir("INVESTIDA: " + (this.funcionalidadesAtivas.contains(Funcionalidade.INVESTIDA) ? "SIM" : "NAO"));
		imprimir("ATAQUE_DUPLO: " + (this.funcionalidadesAtivas.contains(Funcionalidade.ATAQUE_DUPLO) ? "SIM" : "NAO"));
		imprimir("PROVOCAR: " + (this.funcionalidadesAtivas.contains(Funcionalidade.PROVOCAR) ? "SIM" : "NAO"));
		imprimir("========================");
	}

	private int jogador; // 1 == turno do jogador1, 2 == turno do jogador2.
	private int turno;
	private int nCartasHeroi1;
	private int nCartasHeroi2;

	private Mesa mesa;

	// "Apontadores" - Assim podemos programar genericamente os métodos para
	// funcionar com ambos os jogadores
	private ArrayList<Carta> mao;
	private ArrayList<Carta> lacaios;
	private ArrayList<Carta> lacaiosOponente;

	// "Memória" - Para marcar ações que só podem ser realizadas uma vez por
	// turno.
	private boolean poderHeroicoUsado;
	private HashMap<Integer, Integer> lacaiosAtacaramID;

	@Override
	public int executarPartida() throws LamaException {
		vidaHeroi1 = vidaHeroi2 = 30;
		nCartasHeroi1 = cartasIniJogador1;
		nCartasHeroi2 = cartasIniJogador2;
		ArrayList<Jogada> movimentos = new ArrayList<Jogada>();
		int noCardDmgCounter1 = 1;
		int noCardDmgCounter2 = 1;
		turno = 1;

		for (int k = 0; k < 60; k++) {
			imprimir("\n=== TURNO " + turno + " ===\n");
			// Atualiza mesa (com cópia profunda)
			@SuppressWarnings("unchecked")
			ArrayList<CartaLacaio> lacaios1clone = (ArrayList<CartaLacaio>) UnoptimizedDeepCopy.copy(lacaiosMesa1);
			@SuppressWarnings("unchecked")
			ArrayList<CartaLacaio> lacaios2clone = (ArrayList<CartaLacaio>) UnoptimizedDeepCopy.copy(lacaiosMesa2);
			mesa = new Mesa(lacaios1clone, lacaios2clone, vidaHeroi1, vidaHeroi2, nCartasHeroi1 + 1, nCartasHeroi2,
					turno > 10 ? 10 : turno, turno > 10 ? 10 : (turno == 1 ? 2 : turno));

			// Apontadores para jogador1
			mao = maoJogador1;
			lacaios = lacaiosMesa1;
			lacaiosOponente = lacaiosMesa2;
			jogador = 1;
			manaJogador1 = (turno > 10 ? 10 : turno);

			// Processa o turno 1 do Jogador1
			imprimir("\n----------------------- Começo de turno para Jogador 1:");
			long startTime, endTime, totalTime;

			// Cópia profunda de jogadas realizadas.
			@SuppressWarnings("unchecked")
			ArrayList<Jogada> cloneMovimentos1 = (ArrayList<Jogada>) UnoptimizedDeepCopy.copy(movimentos);

			startTime = System.nanoTime();
			if (baralho1.getCartas().size() > 0) {
				if (nCartasHeroi1 >= maxCartasMao) {
					movimentos = jogador1.processarTurno(mesa, null, cloneMovimentos1);
					comprarCarta(); // carta descartada
				} else
					movimentos = jogador1.processarTurno(mesa, comprarCarta(), cloneMovimentos1);
			} else {
				imprimir("Fadiga: O Herói 1 recebeu " + noCardDmgCounter1
						+ " de dano por falta de cartas no baralho. (Vida restante: " + (vidaHeroi1 - noCardDmgCounter1)
						+ ").");
				vidaHeroi1 -= noCardDmgCounter1++;
				if (vidaHeroi1 <= 0) {
					// Jogador 2 venceu
					imprimir(
							"O jogador 2 venceu porque o jogador 1 recebeu um dano mortal por falta de cartas ! (Dano : "
									+ (noCardDmgCounter1 - 1) + ", Vida Herói 1: " + vidaHeroi1 + ")");
					return 2;
				}
				movimentos = jogador1.processarTurno(mesa, null, cloneMovimentos1);
			}
			endTime = System.nanoTime();
			totalTime = endTime - startTime;
			if (tempoLimitado != 0 && totalTime > 3e8) { // 300ms
				// Jogador 2 venceu, Jogador 1 excedeu limite de tempo
				return 2;
			} else
				imprimir("Tempo usado em processarTurno(): " + totalTime / 1e6 + "ms");

			// Começa a processar jogadas do Jogador 1
			this.poderHeroicoUsado = false;
			this.lacaiosAtacaramID = new HashMap<Integer, Integer>();
			for (int i = 0; i < movimentos.size(); i++) {
				processarJogada(movimentos.get(i));
			}

			if (vidaHeroi2 <= 0) {
				// Jogador 1 venceu
				return 1;
			}

			// Atualiza mesa (com cópia profunda)
			@SuppressWarnings("unchecked")
			ArrayList<CartaLacaio> lacaios1clone2 = (ArrayList<CartaLacaio>) UnoptimizedDeepCopy.copy(lacaiosMesa1);
			@SuppressWarnings("unchecked")
			ArrayList<CartaLacaio> lacaios2clone2 = (ArrayList<CartaLacaio>) UnoptimizedDeepCopy.copy(lacaiosMesa2);
			mesa = new Mesa(lacaios1clone2, lacaios2clone2, vidaHeroi1, vidaHeroi2, nCartasHeroi1, nCartasHeroi2 + 1,
					turno > 10 ? 10 : turno, turno > 10 ? 10 : (turno == 1 ? 2 : turno));

			// Apontadores para jogador2
			mao = maoJogador2;
			lacaios = lacaiosMesa2;
			lacaiosOponente = lacaiosMesa1;
			jogador = 2;
			manaJogador2 = (turno > 10 ? 10 : (turno == 1 ? 2 : turno));

			// Processa o turno 1 do Jogador2
			imprimir("\n\n----------------------- Começo de turno para Jogador 2:");

			// Cópia profunda de jogadas realizadas.
			@SuppressWarnings("unchecked")
			ArrayList<Jogada> cloneMovimentos2 = (ArrayList<Jogada>) UnoptimizedDeepCopy.copy(movimentos);

			startTime = System.nanoTime();

			if (baralho2.getCartas().size() > 0) {
				if (nCartasHeroi2 >= maxCartasMao) {
					movimentos = jogador2.processarTurno(mesa, null, cloneMovimentos2);
					comprarCarta(); // carta descartada
				} else
					movimentos = jogador2.processarTurno(mesa, comprarCarta(), cloneMovimentos2);
			} else {
				imprimir("Fadiga: O Herói 2 recebeu " + noCardDmgCounter2
						+ " de dano por falta de cartas no baralho. (Vida restante: " + (vidaHeroi2 - noCardDmgCounter2)
						+ ").");
				vidaHeroi2 -= noCardDmgCounter2++;
				if (vidaHeroi2 <= 0) {
					// Vitoria do jogador 1
					imprimir(
							"O jogador 1 venceu porque o jogador 2 recebeu um dano mortal por falta de cartas ! (Dano : "
									+ (noCardDmgCounter2 - 1) + ", Vida Herói 2: " + vidaHeroi2 + ")");
					return 1;
				}
				movimentos = jogador2.processarTurno(mesa, null, cloneMovimentos2);
			}

			endTime = System.nanoTime();
			totalTime = endTime - startTime;
			if (tempoLimitado != 0 && totalTime > 3e8) { // 300ms
				// Limite de tempo pelo jogador 2. Vitoria do jogador 1.
				return 1;
			} else
				imprimir("Tempo usado em processarTurno(): " + totalTime / 1e6 + "ms");

			this.poderHeroicoUsado = false;
			this.lacaiosAtacaramID = new HashMap<Integer, Integer>();
			for (int i = 0; i < movimentos.size(); i++) {
				processarJogada(movimentos.get(i));
			}
			if (vidaHeroi1 <= 0) {
				// Vitoria do jogador 2
				return 2;
			}
			turno++;
		}

		// Nunca vai chegar aqui dependendo do número de rodadas
		imprimir("Erro: A partida não pode ser determinada em mais de 60 rounds. Provavel BUG.");
		throw new LamaException(-1, null, "Erro desconhecido. Mais de 60 turnos sem termino do jogo.", 0);
	}

	
	protected void processarJogada(Jogada umaJogada) throws LamaException {
		Carta cartaJogada = umaJogada.getCartaJogada();// inicializao carta jogada.
		Carta cartaAlvo = umaJogada.getCartaAlvo();// inicializa a carta alvo;
		CartaLacaio lacAtacante = null;// Macro para simplificar a escrita.
		CartaLacaio lacAlvo = null; // Macro.
		CartaLacaio lacaio = null; // Macro.
		CartaMagia magia = null; // Macro.
		int ataquesRealizados = 0;
		int manaJogador = (jogador == 1 ? manaJogador1 : manaJogador2);// Macro.
		boolean investidaAtivo = funcionalidadesAtivas.contains(Funcionalidade.INVESTIDA);
		boolean ataqueDuploAtivo = funcionalidadesAtivas.contains(Funcionalidade.ATAQUE_DUPLO);
		boolean provocarAtivo = funcionalidadesAtivas.contains(Funcionalidade.PROVOCAR);
		boolean obedeceProvocacao = true;// Testa o efeito provocar.
		
		switch (umaJogada.getTipo()) {
		case ATAQUE:
			// (ERRO:3) Verifica se a carta jogada eh uma magia inves de lacaio.
			if (cartaJogada instanceof CartaMagia)
				Erros.erro3(cartaAlvo, umaJogada, jogador);
			
			// (ERRO:5) Verifica se o lacaio a ser eh usado valido.
			if (!lacaios.contains(cartaJogada))
				Erros.erro5(cartaJogada, umaJogada, jogador);
			
			// Atualiza o macro "cartaJogada" (sabe-se agora que eh um lacaio).
			lacAtacante = (CartaLacaio) lacaios.get(lacaios.indexOf(cartaJogada));

			// (ERRO:6) Verifica se o lacaio atacante fora baixado nesse turno.
			if (lacAtacante.getTurno() == turno)
				if (!investidaAtivo || lacAtacante.getEfeito() != TipoEfeito.INVESTIDA)
						Erros.erro6(lacAtacante, umaJogada, jogador);
			
			// Atualiza Macro.
			if(lacaiosAtacaramID.get(lacAtacante.getID()) != null) // Caso null, lacaio nao atacou.
				ataquesRealizados = lacaiosAtacaramID.get(lacAtacante.getID()).intValue();
			
			// (ERRO:7)Verifica se o lacaio a ser utilizado ja atacou neste turno.
			if (ataquesRealizados >= 1){
				if (ataqueDuploAtivo && lacAtacante.getEfeito() == TipoEfeito.ATAQUE_DUPLO){
					if (ataquesRealizados >= 2) // Verifica se nao ataca mais de 2 vezes.
						Erros.erro7(lacAtacante, umaJogada, ataquesRealizados, jogador);
				}else
					Erros.erro7(lacAtacante, umaJogada, ataquesRealizados, jogador);
			}
		
			// (ERRO:8) Verifica se o alvo eh valido.
			if (cartaAlvo != null && !lacaiosOponente.contains(cartaAlvo))
				Erros.erro8(lacAtacante, cartaAlvo, umaJogada, jogador);
			
			// (ERRO:13) Verifica se o ataque obedece o efeito PROVOCAR.
			if (provocarAtivo) {
				// Itera lacaiosOponente em busca do Efeito PROVOCAR.
				for (Carta c : lacaiosOponente) {
					lacaio = (CartaLacaio) c;
					// verifica se ha um lacaio com efeito provocar.
					if (lacaio.getEfeito() == TipoEfeito.PROVOCAR){ 
						obedeceProvocacao = false;
						break;
					}
				}
				// Sabendo que ao menos um lacaio na mesa tem o efeito provocar,
				// o alvo a ser atacado DEVE ser uma lacaio com o efeito Provocar.
				// logo, Testa se o alvo eh um lacaio e possui o efeito provocar.
				if(cartaAlvo != null){
					lacAlvo = (CartaLacaio) cartaAlvo;
					if(lacAlvo.getEfeito() == TipoEfeito.PROVOCAR)
						obedeceProvocacao = true;
				}

				// Se nao obedece o efeito de provocacao, lanca erro.
				if (!obedeceProvocacao)
					Erros.erro13(cartaAlvo, lacaio, umaJogada, jogador);
			}

			// Separa acoes caso alvo seja um lacaio ou um heroi.
			if (cartaAlvo == null) {
				if (jogador == 1)
					vidaHeroi2 -= lacAtacante.getAtaque();
				else
					vidaHeroi1 -= lacAtacante.getAtaque();

				imprimir("JOGADA: O Lacaio ID=" + lacAtacante.getID() + " (" + lacAtacante.getNome() + ") causou "
						+ lacAtacante.getAtaque() + " de dano no Heroi" + (jogador == 1 ? "2" : "1")
						+ " (vida restante: " + (jogador == 1 ? vidaHeroi2 : vidaHeroi1) + ")");
			} else {
				// Atualiza o macro "cartaAlvo" (sabe-se agora que eh um lacaio valido).
				lacAlvo = (CartaLacaio) lacaiosOponente.get(lacaiosOponente.indexOf(cartaAlvo));
				
				// atualiza vidas.
				lacAlvo.setVidaAtual(lacAlvo.getVidaAtual() - lacAtacante.getAtaque());
				lacAtacante.setVidaAtual(lacAtacante.getVidaAtual() - lacAlvo.getAtaque());
				
				// Descreve a jogada.
				imprimir("JOGADA: O Lacaio ID=" + lacAtacante.getID() + " (" + lacAtacante.getNome()
						+ ") ataca o Lacaio " + "ID=" + lacAlvo.getID() + " (" + lacAlvo.getNome() + ")");
				imprimir(lacAtacante.getNome() + " sofreu " + lacAlvo.getAtaque() + " de dano (vida restante "
						+ lacAtacante.getVidaAtual() + ") & " + lacAlvo.getNome() + " sofreu " + lacAtacante.getAtaque()
						+ " de dano (vida restante " + lacAlvo.getVidaAtual() + ").");

				// Atualiza lacaios em mesa (remove mortos).
				if (lacAtacante.getVidaAtual() <= 0) {
					imprimir(
							"O Lacaio atacante ID=" + lacAtacante.getID() + " (" + lacAtacante.getNome() + ") morreu!");
					lacaios.remove(lacAtacante);
				}
				if (lacAlvo.getVidaAtual() <= 0) {
					imprimir("O Lacaio alvo ID=" + lacAlvo.getID() + " (" + lacAlvo.getNome() + ") morreu!");
					lacaiosOponente.remove(lacAlvo);
				}
			}
			
			lacaiosAtacaramID.put(lacAtacante.getID(), ++ataquesRealizados); // atualiza o HashSet.
			
			break;
		case LACAIO:
			// (ERRO:1)Verifica se o jogador possui o lacaio em maos.
			if (!mao.contains(cartaJogada)) 
				Erros.erro1(cartaJogada, umaJogada, jogador, mao);
			
			// (ERRO:2)Verifica se ha mana o suficiente para a jogada.
			if (cartaJogada.getMana() > manaJogador)
				Erros.erro2(cartaJogada, umaJogada, jogador, manaJogador);
			
			// (ERRO:4) Verifica se ja possui 7 lacaios em mesa (limite).
			if (lacaios.size() >= 7)
				Erros.erro4(cartaJogada, umaJogada, jogador);
			
			// Finalizado o tratamento de erros, atualiza-se o macro "cartaJogada".
			lacaio = (CartaLacaio) mao.get(mao.indexOf(cartaJogada));

			// Encerrado o tratamento de erros, executa a jogada.
			manaJogador -= lacaio.getMana(); // Atua;iza a mana do jogador.
			lacaio.setTurno(turno); // define o turno em que fora baxado.
			lacaios.add(lacaio); // lacaio adicionado a mesa.
			mao.remove(lacaio); // lacaio retirado da mesa.

			// Descreve a jogada realizada.
			imprimir("JOGADA: Baixou-se o lacaio ID=" + lacaio.getID() + " (" + lacaio.getNome() 
					+ " | vida: " + lacaio.getVidaAtual() + " | dano: " + lacaio.getAtaque() 
					+ " | efeito: " + lacaio.getEfeito() + ").");

			break;
		case MAGIA:
			// (ERRO:1) Verifica se o jogador possui a carta a ser usada.
			if (!mao.contains(cartaJogada))
				Erros.erro1(cartaJogada, umaJogada, jogador, mao);
			
			// (ERRO:2)Verifica se ha mana suficiente para executar a jogada.
			if (cartaJogada.getMana() > manaJogador)
				Erros.erro2(cartaJogada, umaJogada, jogador, manaJogador);
			
			// (ERRO:9) Verifica se a carta eh um lacaio inves de magia.
			if (cartaJogada instanceof CartaLacaio)
				Erros.erro9(cartaJogada, umaJogada, jogador);
			
			// Atualiza o macro "cartaJogada" (trata-se de uma magia valida).
			magia = (CartaMagia) mao.get(mao.indexOf(cartaJogada));

			// Switch para teste de alvos.
			switch (magia.getMagiaTipo()) {
			case ALVO:
				// (ERRO:10)verifica se eh um alvo valido (PARA CASO ALVO).
				if (cartaAlvo != null && !lacaiosOponente.contains(cartaAlvo))
					Erros.erro10(magia, cartaAlvo, umaJogada, jogador);
				
				break;
			case BUFF:
				// (ERRO:10)verifica se eh um alvo valido (PARA CASO BUFF).
				if (!lacaios.contains(cartaAlvo))
					Erros.erro10(magia, cartaAlvo, umaJogada, jogador);
				
				break;
			case AREA: // O campo alvo nao eh utilizado.
				break;
			default:
				break;
			}

			// Separa acoes dependendo do tipo de magia.
			switch (magia.getMagiaTipo()) {
			case ALVO:
				// separa acoes para o alvo lacaio e o heroi.
				if (cartaAlvo == null) {
					if (jogador == 1)
						vidaHeroi2 -= magia.getMagiaDano();
					else
						vidaHeroi1 -= magia.getMagiaDano();
				} else {
					lacAlvo = (CartaLacaio) lacaiosOponente.get(lacaiosOponente.indexOf(cartaAlvo));

					lacAlvo.setVidaAtual(lacAlvo.getVidaAtual() - magia.getMagiaDano());

					if (lacAlvo.getVidaAtual() <= 0) {
						imprimir("Lacaio ID=" + lacAlvo.getID() + " (" + lacAlvo.getNome() + ") Morreu!");
						lacaiosOponente.remove(lacAlvo);
					}

				}
				break;
			case AREA:
				// computa dano para todos os oponentes.
				for (Iterator<Carta> i = lacaiosOponente.iterator(); i.hasNext();) {
					lacAlvo = (CartaLacaio) i.next();
					lacAlvo.setVidaAtual(lacAlvo.getVidaAtual() - magia.getMagiaDano());
					if (lacAlvo.getVidaAtual() <= 0) {
						imprimir("Lacaio ID=" + lacAlvo.getID() + " (" + lacAlvo.getNome() + ") Morreu!");
						i.remove();
					}
				}

				// Descreve jogada.
				imprimir("JOGADA: Magia de area ID=" + magia.getID() + " (" + magia.getNome() + ") causou "
						+ magia.getMagiaDano() + " de dano nos lacaios oponentes e no " + "Heroi"
						+ (jogador == 1 ? "2" : "1") + " (vida restante: " + (jogador == 1 ? vidaHeroi2 : vidaHeroi1)
						+ ")");
				break;
			case BUFF:
				// Atualiza macro "cartaAlvo".
				lacAlvo = (CartaLacaio) lacaios.get(lacaios.indexOf(cartaAlvo));

				// Computa o buff sobre o alvo.
				lacAlvo.setAtaque(lacAlvo.getAtaque() + magia.getMagiaDano());
				lacAlvo.setVidaMaxima(lacAlvo.getVidaMaxima() + magia.getMagiaDano());
				lacAlvo.setVidaAtual(lacAlvo.getVidaAtual() + magia.getMagiaDano());

				// descreve o buff.
				imprimir("JOGADA: Lacaio " + lacAlvo.getNome() + " (ID=" + lacAlvo.getID() + ") foi buffado:" + " vida("
						+ (lacAlvo.getVidaAtual() - magia.getMagiaDano()) + "->" + lacAlvo.getVidaAtual() + ") | dano("
						+ (lacAlvo.getAtaque() - magia.getMagiaDano()) + "->" + lacAlvo.getAtaque() + ")");
				break;
			default:
				break;
			}
			
			manaJogador -= magia.getMana();// Atualiza mana do Jogador.
			mao.remove(magia);// atualiza a mao (remove magia).
				
			break;
		case PODER:
			// (ERRO:2)Verifica se ha mana o suficiente para a jogada.
			if (2 > manaJogador)
				Erros.erro2(cartaJogada, umaJogada, jogador, manaJogador);

			// (ERRO:11) Verifica se o poder heroico ja foi usado no turno.
			if (poderHeroicoUsado)
				Erros.erro11(cartaAlvo, umaJogada, jogador);

			// (ERRO:12) Verifica se a cartaAlvo eh um alvo valido.
			if (cartaAlvo != null && !lacaiosOponente.contains(cartaAlvo))
				Erros.erro12(cartaAlvo, umaJogada, jogador);

			// (ERRO:13) Verifica se o ataque obedece o efeito PROVOCAR.
			if (provocarAtivo) {
				// Itera lacaiosOponente em busca do Efeito PROVOCAR.
				for (Carta c : lacaiosOponente) {
					lacaio = (CartaLacaio) c;
					// verifica se ha um lacaio com efeito provocar.
					if (lacaio.getEfeito() == TipoEfeito.PROVOCAR){ 
						obedeceProvocacao = false;
						break;
					}
				}
				// Sabendo que ao menos um lacaio na mesa tem o efeito provocar,
				// o alvo a ser atacado DEVE SER um LACAIO com o efeito PROVOCAR.
				// logo, Testa se o alvo eh um lacaio e possui o efeito provocar.
				if(cartaAlvo != null){
					lacAlvo = (CartaLacaio) cartaAlvo;
					if(lacAlvo.getEfeito() == TipoEfeito.PROVOCAR)
						obedeceProvocacao = true;
				}
				
				// Se nao obedece o efeito de provocacao, lanca erro.
				if (!obedeceProvocacao)
					Erros.erro13(cartaAlvo, lacaio, umaJogada, jogador);
			}
			
			// separa acoes caso o alvo seja um lacaio ou Heroi.
			if (cartaAlvo == null) {
				if (jogador == 1)
					vidaHeroi2 -= 1;
				else
					vidaHeroi1 -= 1;

				imprimir("JOGADA: O Heroi" + (jogador == 1 ? 1 : 2) + " usou o Poder Heroico no Heroi"
						+ (jogador == 1 ? 2 : 1) + " (vida restante: " + (jogador == 1 ? vidaHeroi2 : vidaHeroi1)
						+ ")");
			} else {
				// Atualiza macro para o lacaio alvo (sabe-se agora que eh um lacaio)
				lacAlvo = (CartaLacaio) lacaiosOponente.get(lacaiosOponente.indexOf(cartaAlvo));

				// Atualiza vida dos herois.
				if (jogador == 1)
					vidaHeroi1 -= lacAlvo.getAtaque();
				else
					vidaHeroi2 -= lacAlvo.getAtaque();

				// Descreve jogada.
				imprimir("JOGADA: O Heroi" + (jogador == 1 ? 1 : 2) + " usou o Poder Heroico no lacaio "
						+ lacAlvo.getNome() + " (ID=" + lacAlvo.getID() + " | vidaAtual: " + lacAlvo.getVidaAtual()
						+ ")");

				lacAlvo.setVidaAtual(lacAlvo.getVidaAtual() - 1);// Atualiza vida lacaio.

				// Descreve dados da jogada.
				imprimir("O Heroi" + (jogador == 1 ? 1 : 2) + " sofreu " + lacAlvo.getAtaque()
						+ " de dano (vida restante " + (jogador == 1 ? vidaHeroi1 : vidaHeroi2) + ") & " + "o lacaio "
						+ lacAlvo.getNome() + " sofreu 1 de dano (vida restante " + lacAlvo.getVidaAtual() + ")");

				// atualiza lacaio em mesa (remove mortos).
				if (lacAlvo.getVidaAtual() <= 0) {
					imprimir("O lacaio " + lacAlvo.getNome() + " (ID=" + lacAlvo.getID() + ") Morreu!");
					lacaiosOponente.remove(lacAlvo);
				}
			}

			poderHeroicoUsado = true; // Afirma que o porder heroico foi usado no turno.
			manaJogador -= 2; // Atualiza a mana.

			break;
		}
		
		// Atualiza mana dos jogadores a partir do Macro "manaJogador".
		if(jogador == 1)
			manaJogador1 = manaJogador;
		else
			manaJogador2 = manaJogador;
		
		return;
	}

	private Carta comprarCarta() {
		if (this.jogador == 1) {
			if (baralho1.getCartas().size() <= 0)
				throw new RuntimeException("Erro: Não há mais cartas no baralho para serem compradas.");
			Carta nova = baralho1.comprarCarta();
			mao.add(nova);
			nCartasHeroi1++;
			return (Carta) UnoptimizedDeepCopy.copy(nova);
		} else {
			if (baralho2.getCartas().size() <= 0)
				throw new RuntimeException("Erro: Não há mais cartas no baralho para serem compradas.");
			Carta nova = baralho2.comprarCarta();
			mao.add(nova);
			nCartasHeroi2++;
			return (Carta) UnoptimizedDeepCopy.copy(nova);
		}
	}
	
	/**
	 * Classe aninhada estatica Erros
	 * 
	 * Contem serie de metodo estaticos sem retorno que constroem
	 * a menssagem de erro e propagam o erro quando occorrido.
	 * 
	 * @method erroX (dado X um inteiro de [1,13])
	 * 		o metodo erroX cria a menssagem apropriada para o caso
	 * 		do erro tipo X e lanca um LamaException.
	 */
	private static class Erros {
		// (ERRO:1) jogador nao possui a carta a ser usada.
		private static void erro1(Carta cartaJogada, Jogada umaJogada, int jogador, ArrayList<Carta> mao)
				throws LamaException {

			String menssagemErro = new String();

			menssagemErro = "(equivoco do Jogador" + jogador + ")" + " Tentou-se usar carta que nao possui em maos:\n";
			menssagemErro += "Carta usada: ID=" + cartaJogada.getID() + " | " + "nome: " + cartaJogada.getNome() + "\n";
			menssagemErro += "Cartas em maos:\n";

			for (Carta c : mao) {
				menssagemErro += "(ID=" + c.getID() + "|" + c.getNome() + ")\n";
			}

			throw new LamaException(1, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:2) Nao ha mana suficiente para executar a jogada.
		private static void erro2(Carta cartaJogada, Jogada umaJogada, int jogador, int mana) throws LamaException {
			String menssagemErro = new String();

			menssagemErro = "(equivoco do Jogador" + jogador + ") " + "O jogador nao possui mana para a jogada:\n";
			menssagemErro += "TipoJogada: " + TipoJogada.MAGIA + " | custoMana: " + cartaJogada.getMana()
					+ " | mana disponivel: " + mana;

			throw new LamaException(2, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:3) Carta a ser usada eh uma magia inves de lacaio.
		private static void erro3(Carta cartaJogada, Jogada umaJogada, int jogador) throws LamaException {

			String menssagemErro = new String();

			menssagemErro = "(equivoco do Jogador" + jogador + ")" + "Tentou-se utilizar uma Magia como Lacaio:\n";
			menssagemErro += "Carta usada: ID=" + cartaJogada.getID() + " | " + "nome: " + cartaJogada.getNome();

			throw new LamaException(3, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:4) Baixou um lacaio possuindo 7 lacaios em mesa (limite).
		private static void erro4(Carta cartaJogada, Jogada umaJogada, int jogador) throws LamaException {
			String menssagemErro = new String();

			menssagemErro = "(equivoco do Jogador" + jogador + ")"
					+ " Tentou-se exceder o limite de lacaios em mesa:\n";
			menssagemErro += "Jogador tentou usar o lacaio ID=" + cartaJogada.getID() 
		        	+ " (" + cartaJogada.getNome() + "), mas ja possui 7 lacaios em mesa.";

			throw new LamaException(4, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:5) Lacaio a ser eh usado nao eh valido.
		private static void erro5(Carta cartaJogada, Jogada umaJogada, int jogador) throws LamaException {
			String menssagemErro = new String();

			menssagemErro = "(equivoco do jogador" + jogador + "Jogador nao possui o lacaio em mesa para atacar:\n";
			menssagemErro += "Atacante ID: " + cartaJogada.getID() + " (" + cartaJogada.getNome() + ")";

			throw new LamaException(5, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:6) lacaio atacante nao pode atacar no turno que foi baixado.
		private static void erro6(CartaLacaio lacAtacante, Jogada umaJogada, int jogador) throws LamaException {
			String menssagemErro = new String();

			menssagemErro = "(equivoco do jogador" + jogador + ")"
					+ "Lacaio usado nao pode atacar no turno em que foi baixado:\n";
			menssagemErro += "Lacaio usado: ID=" + lacAtacante.getID() + " | Nome: " + lacAtacante.getNome()
					+ " | Efeito: " + lacAtacante.getEfeito();

			throw new LamaException(6, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:7)Verifica se o lacaio a ser utilizado ja atacou neste turno.
		private static void erro7(CartaLacaio lacAtacante, Jogada umaJogada, int vezesAtaque,
								  int jogador) throws LamaException {
			String menssagemErro = new String();

			menssagemErro = "(equivoco do jogador" + jogador + ") "
					+ "Lacaio usado nao pode atacar outra vez no mesmo turno:\n";
			menssagemErro += "Lacaio usado: ID=" + lacAtacante.getID() + " | Nome: " + lacAtacante.getNome()
					+ " | Efeito: " + lacAtacante.getEfeito() + " | Atacou " + vezesAtaque + " vezes";

			throw new LamaException(7, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:8)Verifica se o alvo eh valido.
		private static void erro8(CartaLacaio lacAtacante, Carta cartaAlvo, Jogada umaJogada, int jogador)
				throws LamaException {

			String menssagemErro = new String();

			menssagemErro = "(Equivoco do jogador" + jogador + "Tentativa de ataque sobre alvo invalido:\n";
			menssagemErro += "Lacaio atacante ID=" + lacAtacante.getID() + " | nome: " + lacAtacante.getNome() + "\n";
			menssagemErro += "Lacaio alvo ID=" + cartaAlvo.getID() + " | nome: " + cartaAlvo.getNome();

			throw new LamaException(8, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:9) Verifica se a carta eh um lacaio inves de magia.
		private static void erro9(Carta cartaJogada, Jogada umaJogada, int jogador) throws LamaException {
			String menssagemErro = new String();

			menssagemErro = "(equivoco do Jogador" + jogador + ")" + "Tentou-se utilizar um Lacaio como Magia:\n";
			menssagemErro += "Carta usada: ID=" + cartaJogada.getID() + " | " + "nome: " + cartaJogada.getNome();

			throw new LamaException(9, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:10) Nao eh um alvo valido para magia de BUFF.
		private static void erro10(CartaMagia magia, Carta cartaAlvo, Jogada umaJogada, int jogador)
				throws LamaException {

			String menssagemErro = new String();

			menssagemErro = "(Equivoco do jogador" + jogador + "Uso de Magia sobre alvo invalido:\n";
			menssagemErro += "Magia usada ID=" + magia.getID() + " | nome: " + magia.getNome() + "\n";
			menssagemErro += "Lacaio alvo ID=" + cartaAlvo.getID() + " | nome: " + cartaAlvo.getNome();

			throw new LamaException(10, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:11) O poder heroico ja foi usado no turno.
		private static void erro11(Carta cartaAlvo, Jogada umaJogada, int jogador) throws LamaException {
			String menssagemErro = new String();

			menssagemErro = "(equivoco do Jogador" + jogador + ")" + " Poder heroico ja foi usado no turno atual:\n";
			if (cartaAlvo == null)
				menssagemErro += "Alvo: Heroi" + (jogador == 1 ? "2" : "1");
			else
				menssagemErro += "Alvo: ID=" + cartaAlvo.getID() + " | " + "Nome: " + cartaAlvo.getNome();

			throw new LamaException(11, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:12) cartaAlvo nao eh um alvo valido para o PODER heroico.
		private static void erro12(Carta cartaAlvo, Jogada umaJogada, int jogador) throws LamaException {
			String menssagemErro = new String();

			menssagemErro = "(Equivoco do jogador" + jogador + ") Uso do poder heroico sobre alvo invalido:\n";
			menssagemErro += "Alvo: ID=" + cartaAlvo.getID() + " | nome: " + cartaAlvo.getNome();

			throw new LamaException(12, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

		// (ERRO:13) ataque/poder nao obedece o efeito PROVOCAR.
		private static void erro13(Carta cartaAlvo, Carta alvoProvocar, Jogada umaJogada, int jogador)
				throws LamaException {
			
			String menssagemErro = new String();

			menssagemErro = "(Equivoco do jogador" + jogador + ") Nao obedeceu o efeito provocar:\n";
			menssagemErro += "Alvo Invalido: ";
			
			if(cartaAlvo == null)
				menssagemErro += "Heroi" + (jogador == 1 ? "2" : "1") + "\n"; 
			else
				menssagemErro += "ID=" + cartaAlvo.getID() + " | nome: " + cartaAlvo.getNome() + "\n";
			
			menssagemErro += "Alvo PROVOCAR: ID=" + alvoProvocar.getID() + " | nome: " + alvoProvocar.getNome() + "\n";

			throw new LamaException(13, umaJogada, menssagemErro, (jogador == 1 ? 2 : 1));
		}

	}

}
