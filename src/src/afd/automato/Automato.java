package afd.automato;

import afd.alfabeto.Alfabeto;
import afd.estados.Estado;
import afd.estados.Estados;
import afd.ftransicao.FuncaoTransicao;
import java.util.ArrayList;

/**
 * <h3 align=center>Definição de Autômato Finito Determinístico</h3><p>
 * Um Autômato Finito Determinístico é uma 5-upla: <br>
 *
 * <h3 align=center><font size="-1">M=(∑, Q, δ, q<sub>0</sub>, F) </font></h3>
 * Onde:<p>
 *
 * <b>∑</b>: Alfabeto de símbolos de entrada;<br>
 * <b>Q</b>: conjunto de Estados do autômato, o qual é finito;<br>
 * <b>δ</b>: função programa ou função de transição:<br>
 * <h3 align=center><font size="-1">δ: Q X ∑ -> Q</font></h3>
 * a qual é uma função parcial;<br>
 * <b>q<sub>0</sub></b>: estado inicial, tal que q<sub>0</sub> é elemento de Q;<br>
 * <b>F</b>: conjunto dos Estados finais, ou terminais, tal que F está contido
 * em Q.<p>
 * O processamento de um Autômato Finito <b>M</b>, para uma palavra de entrada
 * <i>w</i>, consiste na sucessiva aplicação da função programa para cada símbolo
 * de <i>w</i> (da esquerda para a direita) até ocorrer uma condição de parada.<p>
 * As condições de parada são as seguintes:<br>
 *
 * <ul>
 *
 *  <li>Após processar o último símbolo da fita, o Autômato Finito assume um estado
 *  terminal: o autômato para e a entrada <i>w</i> é aceita;
 *
 *  <li>Após processar o último símbolo da fita, o Autômato Finito assume um estado
 *  não-terminal: o autômato para e a entrada <i>w</i> é rejeitada;
 *
 *  <li>A função programa é indefinida para o argumento (estado corrente e símbolo
 *  lido): a máquina para e a palavra de entrada <i>w</i> é rejeitada.<p>
 *
 * </ul>
 *
 * @see Alfabeto
 * @see Estado
 * @see Estados
 * @see FuncaoTransicao
 * @author Leandro
 */
public class Automato {

    /**
     * Recupera cada etapa do processamento da Fita de Entrada.<br>
     * O conjunto destas, forma um histórico do processamento da Fita. <br>
     * A classe registra as seguintes informações sobre uma transição:
     * <ul>
     * <li><b>estado origem</b>: o estado em que o autômato estava no momento da
     * transição;
     * <li><b>símbolo lido</b>: o símbolo lido da fita de entrada, que irá
     * determinar a transição, juntamente com o estado origem do autômato;
     * <li><b>estado destino</b>: estado para o qual o autômato converge, com
     * a leitura do símbolo da fita de entrada, e o estado atual, este obtido
     * da função de transição;
     * <li><b>parcela a ser lida</b>: parcela restante da cadeia a ser lida;
     * <li><b>processamento normal</b>: se <b>true</b>, indica que havia transição
     * definida para o estado origem e o símbolo lido, se <b>false</b>, indica que
     * não havia transição definida. Isso ocorre se o usuário entra com um símbolo
     * na cadeia de entrada que não pertence ao Alfabeto da gramática.
     * </ul>
     */
    public class Processamento {

        /**Estado em que o autômato estava no momento da transição.*/
        private final Estado estadoOrigem;
        /**Estado para o qual o autômato convergiu após a transição.*/
        private final Estado estadoDestino;
        /**Símbolo lido da Fita de Entrada.*/
        private final char simboloLido;
        /**Parcela da fita de entrada ainda a ser lida.*/
        private final String parcela;
        /**Indica se tudo ocorreu normalmente com a leitura da fita de entrada.*/
        private final boolean normal;

        /**
         * Cria uma instância da classe.
         * @param estadoOrigem estado atual do autômato, no momento da transição.
         * @param simboloLido símbolo lido da fita de entrada.
         * @param estadoDestino estado para o qual o autômato converge, com a
         * leitura do símbolo da fita de entrada, e o estado atual. Este obtido
         * da função de transição;
         * @param parcela parcela restante da cadeia ainda a ser lida.
         * @param normal indica a leitura de um símbolo inválido.
         */
        public Processamento(Estado estadoOrigem, char simboloLido,
        Estado estadoDestino, String parcela, boolean normal) {
            this.estadoOrigem = estadoOrigem;
            this.estadoDestino = estadoDestino;
            this.simboloLido = simboloLido;
            this.parcela = parcela;
            this.normal = normal;
        }

        /**
         * Estado para o qual o autômato converge, após a leitura do símbolo da
         * Fita de Entrada.
         * @return Estado de destino.
         */
        public Estado getEstadoDestino() {
            return estadoDestino;
        }

        /**
         * Estado atual do autômato, no momento da transição.
         * @return Estado de origem.
         */
        public Estado getEstadoOrigem() {
            return estadoOrigem;
        }

        /**
         * Símbolo lido da fita de entrada.
         * @return Símbolo lido da Fita de Entrada.
         */
        public char getSimboloLido() {
            return simboloLido;
        }

        /**
         * Parcela restante da fita de entrada a ser lida.
         * @return Uma String, contendo o restante da cadeia a ser lida.
         */
        public String getParcela() {
            return parcela;
        }

        /**
         * Indicador de validade da transição.
         * @return <b>true</b>, caso haja uma transição e <b>false</b>, caso a
         * transição não seja possível, devido à leitura de um símbolo inválido.
         */
        public boolean isNormal() {
            return normal;
        }

    }

    /**Alfabeto da gramática.*/
    private final Alfabeto alfabeto;
    /**Conjunto dos Estados da gramática.*/
    private final Estados estados;
    /**Estado inicial o qual define o início das transições.*/
    private Estado estadoInicial;
    /**Função de Transição que define as regras da transição.*/
    private final FuncaoTransicao funcaoTransicao;
    /**Histórico do processamento da fita de entrada.*/
    private final ArrayList<Processamento> processamentoFita;
    /**Fita de Entrada.*/
    private FitaEntrada fitaEntrada;

    /**
     * Cria uma instância do Autômato.
     */
    public Automato() {
        alfabeto = new Alfabeto();
        estados = new Estados();
        estadoInicial = new Estado("");
        funcaoTransicao = new FuncaoTransicao(estados, alfabeto);
        processamentoFita = new ArrayList<>();
    }

    /**
     * Alfabeto da gramática.
     * @return Alfabeto da gramática.
     */
    public Alfabeto getAlfabeto () {
        return alfabeto;
    }

    /**
     * Conjunto dos estados da gramática.
     * @return Conjunto dos estados da gramática.
     */
    public Estados getEstados () {
        return estados;
    }

    /**
     * Retorna o conjunto de todos os estados definidos como <b>terminais</b>.
     * @return Conjunto dos estados definidos como terminais.
     */
    public Estados getEstadosTerminais() {
        return estados.terminais();
    }

    /**
     * Retorna o conjunto de todos os estados definidos como <b>não-terminais</b>.
     * @return Conjunto dos estados definidos como não-terminais.
     */
    public Estados getEstadosNaoTerminais() {
        return estados.naoTerminais();
    }

    /**
     * Estado inicial, a partir do qual começam as transições.
     * A leitura da cadeia começa inicialmente com a leitura do primeiro símbolo
     * à esquerda, estando o autômato posicionado no estado inicial. A partir daí
     * será levado a outro estado, que com a leitura do segundo símbolo será
     * levado a outro, e, assim, sucessivamente.
     * @return Estado inicial da gramática.
     */
    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    /**
     * Funcao de transicao, que define, para cada para cada par <b>estado atual/símbolo
     * lido</b>, o próximo estado para o qual o autômato irá convergir.<br>
     * É o equivalente a um programa de computador.
     * @return Funcao de transição.
     */
    public FuncaoTransicao getFuncaoTransicao() {
        return funcaoTransicao;
    }

    /**
     * Define qual estado da gramática será o estado inicial.
     * @param nome nome do estado a ser obtida a referência
     */
    public void setEstadoInicial(String nome) {
        estadoInicial = estados.getEstado(nome);
    }

    /**
     * Histórico do processamento da fita de entrada.
     * @return Histórico do processamento da Fita de Entrada.
     */
    public ArrayList<Processamento> processamentoFitaEntrada() {
        return processamentoFita;
    }

    /**
     * Processa a Fita de Entrada, um símbolo de cada vez, a partir da esquerda.<br>
     * Inicialmente o autômato se encontra no estado inicial e posicionado na
     * primeira célula à esquerda da Fita de Entrada.<br>
     * Com a leitura do símbolo da fita, o autômato passa para outro estado, com
     * base no definido na Função de Transição da gramática.<br>
     * O novo estado e a próxima célula da Fita de Entrada são passados
     * recursivamente para o método para iniciar novamente o processamento, assim
     * até processar o último símbolo da Fita de Entrada.<br>
     * Ao processar o último símbolo, retorna o Estado em que o Autômato convergiu.
     * @param estadoOrigem estado em que o autômato se encontra antes da transição.
     * @param celula Célula da Fita de Entrada a ser lida.
     * @return Estado em que o Autômato convergiu após processar o último símbolo
     * da Fita de Entrada.
     * @throws Exception Caso uma transição não esteja definida (símbolo indefinido).
     */
    private Estado processarFitaEntrada(Estado estadoOrigem, int celula) throws Exception {
        //Lê uma célula da Fita de Entrada.
        Character simbolo = fitaEntrada.getCelula(celula - 1);
        /* Obtém o estado para o qual o Autômato converge, com base no estado atual
         * e o símbolo lido.*/
        Estado estadoDestino = funcaoTransicao.getTransicao(
            estadoOrigem.getNome(),
            simbolo
        );
        /*Registra o processamento do símbolo, para posterior recuperação para
         a construção da Função Programa Extendida.*/
        processamentoFita.add(new Processamento(
            estadoOrigem, //Estado antes da transicao.
            simbolo, //Símbolo lido.
            estadoDestino, //Estado após a transição.
            new String(fitaEntrada.getCadeia()).substring(
                celula,
                fitaEntrada.dimensao()
            ), //Parcela da Fita de Entrada ainda a ser lida.
            estadoDestino != null //Algum erro ocorrido (Símbolo não definido).
        ));
        //Há uma transição definida...
        if (estadoDestino != null) {
            //Verifica se leu a última célula da Fita de Entrada.
            if (celula < fitaEntrada.dimensao()) {
                /* 
                 * Se não terminou a leitura da fita de entrada,chama o método,
                 * recursivamente, com base nos novos parâmetros de Estado de
                 * Origem, que agora é o Estado para  o qual o autômato convergiu
                 * e a célula na sequência.
                 */
                return processarFitaEntrada(estadoDestino, ++celula);
            } else {
                /*
                 * Retorna o estado para o qual convergiu o autômato após a
                 * leitura do último símbolo da fita de entrada.
                 */
                return estadoDestino;
            }
        } else {
            //Há um símbolo indefinido...
            throw new Exception("Transição não definida.");
        }
    }

    /**
     * Verifica se uma determinada palavra de entrada é ou não aceita pela
     * gramática.<br>
     * Uma palavra aceita pela gramática, é toda aquela que leva o autômato a
     * um estado final, ou terminal, a partir de um estado inicial, após a leitura
     * do último símbolo da Fita de Entrada (cadeia).
     * @param fita Fita de Entrada a ser analizada pelo autômato.
     * @return <b>true</b> se o estado pertencer ao conjunto dos terminais, ou
     * <b>false</b>, se não.
     * @throws Exception Caso alguma transição ainda não tenha sido definida.
     */
    public boolean aceita(FitaEntrada palavra) throws Exception {
        if (funcaoTransicao.totalmenteDefinida()) {
            try {
                fitaEntrada = palavra;
                processamentoFita.clear();
                Estado referencia = estadoInicial;
                /*
                 * Uma Fita sem nenhum símbolo é uma palavra vazia, e também é
                 * passível de verificação.
                 * Nesse caso, a verificação é feita pelo status de terminal do
                 * estado inicial.
                 */
                if (fitaEntrada.dimensao() > 0){
                    referencia = processarFitaEntrada(estadoInicial, 1);
                }
                return referencia.isTerminal();
            } catch (Exception ex) {
                return false;
            }
        } else {
            throw new Exception("Função de Transição ainda não totalmente definida.");
        }
    }
    
}