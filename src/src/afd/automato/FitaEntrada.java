package afd.automato;

/**
 * Dispositivo de entrada que contém a informação a ser processada. A fita é finita
 * (à esquerda e à direita), sendo dividida em células, cada uma armazenando um
 * símbolo (caractere).
 * @author Leandro
 */
public class FitaEntrada {

    /**Conteúdo da Fita de Entrada.*/
    private final char[] cadeia;

    /**
     * Cria uma instância da Fita de Entrada.
     * @param cadeia conjunto de símbolos (caracteres) da Fita de Entrada.
     */
    public FitaEntrada(char[] cadeia) {
        this.cadeia = cadeia;
    }

    /**
     * Retorna todos os símbolos da Fita de Entrada.
     * @return Todos os símbolos da Fita de Entrada.
     */
    public char[] getCadeia() {
        return cadeia.clone();
    }

    /**
     * Retorna o símbolo em uma célula específica da Fita de Entrada.
     * @param indice posição da célula na Fita de Entrada.
     * @return Símbolo em uma célula específica da Fita de Entrada.
     */
    public char getCelula(int indice) {
        return cadeia[indice];
    }

    /**
     * Retorna o número de símbolos na Fita de Entrada.
     * @return Número de símbolos na Fita de Entrada.
     */
    public int dimensao() {
        return cadeia.length;
    }

}