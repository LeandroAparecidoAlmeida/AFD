package afd.alfabeto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Define o <b>Alfabeto</b> (∑) da gramática. O Alfabeto é um conjunto finito 
 * de símbolos.<br>
 * Um símbolo, é um caracter qualquer do teclado, como a,b,c,1,@, por exemplo.
 * @see Simbolo
 * @author Leandro
 */
public class Alfabeto {

    /**Conjunto de símbolos.*/
    private final ArrayList<Simbolo> simbolos;

    /**
     * Cria uma instância do Alfabeto
     */
    public Alfabeto() {
        simbolos = new ArrayList<>();
    }

    /**
     * Recupera o índice de um símbolo no alfabeto.
     * @param simbolo símbolo a ser obtido o índice.
     * @return Índice do símbolo no alfabeto.
     */
    public int indiceDe(Character simbolo) {
        int indice = -1;
        for (int idx = 0; idx < simbolos.size(); idx++) {
            Simbolo s = simbolos.get(idx);
            if (s.asCharacter().equals(simbolo)) {
                indice = idx;
            }
        }
        return indice;
    }

    /**
     * Insere um novo símbolo no alfabeto.<br>
     * O alfabeto, é automaticamente ordenado a cada inserção, sendo que o usuário
     * não precisa preocupar-se com a ordem com que irá entrar com os símbolos.<br>
     * Para ser aceito, o símbolo não deve ter sido inserido anteriormente.
     * @param simbolo símbolo a ser inserido.
     */
    public void inserir(Simbolo simbolo) {
        if (indiceDe(simbolo.asCharacter()) < 0) {
            simbolos.add(simbolo);
            Collections.sort((List)simbolos);
        }
    }

    /**
     * Remove um símbolo do alfabeto.
     * @param simbolo símbolo a ser removido.
     * @return <b>true</b>, caso o símbolo tenha sido removido, <b>false</b>, caso
     * o símbolo não tenha sido encontrado no alfabeto.
     */
    public boolean remover(Character simbolo) {
        int indice = indiceDe(simbolo);
        if (indice >= 0) {
            simbolos.remove(indice);
            return true;
        }
        return false;       
    }

    /**
     * Retorna o número de símbolos do alfabeto.
     * @return Número de símbolos do alfabeto.
     */
    public int dimensao() {
        return simbolos.size();
    }

    /**
     * Retorna o símbolo numa posição específica do alfabeto.
     * @param indice índice do símbolo no alfabeto.
     * @return Símbolo na posição referida ou <b>null</b>, caso não seja
     * localizado.
     */
    public Simbolo simboloEm(int indice) {
        return simbolos.get(indice);
    }

    /**
     * Retorna o símbolo no alfabeto que corresponde ao caractere especificado.
     * @param simbolo caractere.
     * @return Símbolo no alfabeto.
     */
    public Simbolo getSimbolo(Character simbolo) {
        int indice = indiceDe(simbolo);
        return (indice >= 0 ? simboloEm(indice) : null);
    }

    /**
     * Remove todos os símbolos do alfabeto.
     */
    public void limpar(){
        simbolos.clear();
    }

}