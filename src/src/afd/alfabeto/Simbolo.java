package afd.alfabeto;

/**
 * Um símbolo (ou caractere) é uma entidade abstrata básica, a qual não é definida
 * formalmente. Letras e dígitos são exemplos de símbolos frequêntemente usados.
 * @author Leandro
 */
public class Simbolo implements Comparable<Simbolo> {
    
    /**Caracter que representa o símbolo*/
    private final Character caracter;

    /**
     * Cria uma intância do objeto.
     * @param caracter caractere que o símbolo representa.
     */
    public Simbolo(Character caracter) {
        this.caracter = caracter;
    }

    /**
     * Caractere que o símbolo representa.
     * @return Caractere.
     */
    public Character asCharacter() {
        return caracter;
    }

    /**
     * Reescrito para definir que os símbolos são iguais somente quando ambos tem
     * o mesmo caractere.
     * @param obj símbolo a ser comparado.
     * @return <b>true</b>, caso tenham o mesmo caractere ou <b>false</b>, caso
     * tenham caracteres distintos.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Simbolo ? 
        ((Simbolo) obj).asCharacter().equals(this.caracter) : false);
    }

    /**
     * Reescrito para retornar o caractere.
     * @return Caractere que o símbolo representa.
     */
    @Override
    public String toString() {
        return caracter.toString();
    }

    /**
     * Reescrito para definir o critério de ordenação dos valores.
     * @param o
     * @return
     */
    @Override
    public int compareTo(Simbolo o) {
        return caracter.compareTo(o.asCharacter());
    }

}