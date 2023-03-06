package afd.ftransicao;

import afd.estados.Estados;
import afd.estados.Estado;
import afd.alfabeto.Alfabeto;
import afd.alfabeto.Simbolo;
import java.util.ArrayList;

/**
 * Função que determina o conjunto das transições para cada par <b>estado/símbolo</b>.<br>
 * No contexto de um computador, este seria o programa.<br>
 * Formalmente, uma função de transição δ é definida como:<br>
 * <h3 align=center>δ: Q X ∑ -> Q</h3>
 * a qual é uma função parcial;<br>
 * @see AutomatoFinitoDeterministico
 * @see Estados
 * @see Alfabeto
 * @see Estado
 * @author Leandro
 */
public class FuncaoTransicao {

    /**
     * Define uma transição, que informalmente consiste na leitura de um símbolo
     * da Fita de Entrada, e estando o autômato em um estado de origem passa para
     * um novo estado (estado de destino).
     */
    private class Transicao {

        /**Estado do autômato antes da transição.*/
        private final Estado origem;
        /**Símbolo lido da Fita de Entrada.*/
        private final Simbolo simbolo;
        /**Estado do autômato após a leitura do símbolo e estando no estado de
         * origem.
         */
        private final Estado destino;

        /**
         * Cria uma instância da classe.
         * @param origem nome do estado de orígem.
         * @param simbolo caractere do símbolo lid.
         * @param destino nome do estado de destino.
         */
        public Transicao(Estado origem, Simbolo simbolo, Estado destino) {
            this.origem = origem;
            this.simbolo = simbolo;
            this.destino = destino;
        }

        /**
         * Estado para o qual o autômato converge após a leitura do símbolo da
         * Fita de Entrada e estando no Estado de Origem.
         * @return
         */
        public Estado getEstadoDestino() {
            return destino;
        }

        /**
         * Retorna o estado do autômato antes da transição.
         * @return Estado do autômato antes da transição.
         */
        public Estado getEstadoOrigem() {
            return origem;
        }

        /**
         * Símbolo da Fita de Entrada.
         * @return
         */
        public Simbolo getSimbolo() {
            return simbolo;
        }

        /**
         * Reescrito para indicar que as transições só são consideradas iguais
         * se tiverem o mesmo estado de origem e símbolo da Fita de Entrada.
         * @param obj transição a ser comparada.
         * @return <b>true</b>, caso sejam iguais, <b>false</b>, caso não sejam.
         */
        @Override
        public boolean equals(Object obj) {
            return (obj instanceof Transicao ? 
            ((Transicao)obj).getEstadoOrigem().equals(this.origem) &&
            ((Transicao)obj).getSimbolo().equals(this.simbolo) : false);
        }

        /**
         * Reescrito para retornar as informações do objeto no formato:<p>
         * <h3 align=center>qn -> s = qm</h3><p>
         * Onde:<p>
         * <b>qn:</b> estado de origem da transicao.<br>
         * <b>s:</b> símbolo lido da Fita de Entrada.<br>
         * <b>qm:</b> estado de destino da transição, ou seja, estado para o qual o
         * autômato converge após a leitura do símbolo e estando no estado de
         * origem.
         * @return Informações no formato: <i>qn -> s = qm</i>
         */
        @Override
        public String toString() {
            return origem + " -> " + simbolo + " = " + destino;
        }

    }

    /**Conjunto dos estados.*/
    private final Estados estados;
    /**Alfabeto dos símbolos da gramática.*/
    private final Alfabeto alfabeto;
    /**Conjunto das transições do autômato.*/
    private final ArrayList<Transicao> transicoes;

    /**
     * Cria uma instância da classe.
     * @param estados referêcia para o conjunto dos estados da gramática.
     * @param alfabeto referência para o alfabeto da gramática.
     * @see Estados
     * @see Alfabeto
     */
    public FuncaoTransicao(Estados estados, Alfabeto alfabeto) {
        this.estados = estados;
        this.alfabeto = alfabeto;
        transicoes = new ArrayList<>();
    }

    /**
     * Insere uma transição à Função de Transição.
     * @param origem nome do estado de origem.
     * @param simbolo caractere do símbolo.
     * @param destino nome do estado de destino (converge).
     * @return <b>true</b>, caso todas as referencias tenham sido realizadas com
     * sucesso, e caso a transição não tenha sido definida anteriormente,
     * <b>false</b>, caso qualquer destas condições não estejam satisfeitas.
     */
    public boolean adicionarTransicao(String origem, Character simbolo,
    String destino) {
        /*
         * Obtém as referências com base nos nomes dos estados e no caractere
         * do simbolo da Fita de Entrada.
         */
        //Estado antes da transição.
        Estado _origem = estados.getEstado(origem);
        //Símbolo da Fita de Entrada.
        Simbolo _simbolo = alfabeto.getSimbolo(simbolo);
        //Estado após a transição.
        Estado _destino = estados.getEstado(destino);
        if (_origem != null && _simbolo != null && _destino != null) {
            Transicao transicao = new Transicao(_origem, _simbolo, _destino);
            int indice = transicoes.indexOf(transicao);
            if (indice < 0) {
                return transicoes.add(transicao);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Otém o estado para o qual o autômato converge com a leitura de um símbolo
     * da Fita de Entrada e estando no Estado de Origem.
     * @param origem estado de origem.
     * @param simbolo símbolo lido da Fita de Entrada.
     * @return Estado para o qual o autômato converge com a leitura do símbolo
     * na Fita de Entrada e estando no estado de origem, ou null, caso a transição
     * não esteja definida.
     */
    public Estado getTransicao(String origem, Character simbolo) {
        int indice = -1;
        for (int i = 0; i < transicoes.size(); i++) {
            Transicao transicao = transicoes.get(i);
            if (transicao.getEstadoOrigem().getNome().equals(origem) &&
                transicao.getSimbolo().asCharacter().equals(simbolo)) {
                indice = i;
                break;
            }
        }
        return (indice >= 0 ? transicoes.get(indice).getEstadoDestino() : null);
    }

    /**
     * Remove todas as transições da Função de Transição.
     */
    public void limpar() {
        transicoes.clear();
    }

    /**
     * É necessário que a Função de Transição esteja definida para todos os pares
     * <i>Simbolo/Estado</i>, para que se realize o processamento da Fita de
     * Entrada.<br>
     * Este método assegura que esta condiçao esteja satisfeita.
     * @return <b>true</b>, caso todos os pares Simbolo/Estado estejam definidos,
     * <b>false</b>, caso não estejam.
     */
    public boolean totalmenteDefinida() {
        return (transicoes.size() == (estados.dimensao()) * (alfabeto.dimensao()))&&
        (estados.dimensao() > 0 && alfabeto.dimensao() > 0);
    }

}