package afd.estados;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Conjunto dos estados (Q) da gramática.<br>
 * No conjunto dos estados, temos dois subconjuntos:<br>
 * <ul>
 * <li>Conjunto dos <b>Estados Terminais</b>: conjunto de todos os estados
 * definidos como "Terminais".
 * <li>Conjunto dos <b>Estados Não-Terminais</b>: conjunto de todos os estados
 * definidos como "não-terminais";
 * </ul>
 * A união destes dois subconjuntos forma o conjunto dos Estados <i>Q</i>. Se
 * ao terminar o processamento da Fita de Entrada o Autômato assumir um estado
 * terminal, a palavra é aceita pela gramática, caso contrário (termina em um
 * estado não-terminal), então a palavra é regeitada pela gramática.
 * @author Leandro
 */
public class Estados {

    //Vetor de Estados.
    private final ArrayList<Estado> estados;

    /**
     * Cria uma instância da classe.
     */
    public Estados() {
        super();
        estados = new ArrayList<>();
    }

    /**
     * Obtém a referência para um estado com o nome igual ao passado no parâmetro
     * <b>nome</b>.
     * @param nome nome do estado que se deseja obter a referência.
     * @return referência para um objeto {@link Estado} contido no conjunto,
     * ou <b>null</b>, caso este não seja encontrado.
     * @see Estado
     */
    public Estado getEstado(String nome) {
        int indice = indiceDe(nome);
        if (indice >= 0) {
            return estados.get(indice);
        } else {
            return null;
        }
    }

    /**
     * Retorna o índice de um estado no vetor.
     * @param estado nome do estado a ser localizado no vetor.
     * @return índice do estado no vetor.
     */
    public int indiceDe(String estado) {
        int indice = -1;
        for (int idx = 0; idx < estados.size(); idx++) {
            Estado e = estados.get(idx);
            if (e.getNome().equals(estado)) {
                indice = idx;
            }
        }
        return indice;
    }

    /**
     * Insere um novo Estado no conjunto.<br>
     * Os <b>estados</b> são ordenados automaticamente pelo nome, sendo que, foi
     * definido que o nome de um estado deve ter a seguinte sintaxe: <br>
     * <h3 align=center>qn</h3>
     * Onde:<br>
     * <b>q</b>: letra <i>q</i> do alfabeto;<br>
     * <b>n</b>: Um índice númerico positivo qualquer, não necessáriamente sequencial.<p>
     * <b>Obs.:</b> <i>Para que o estado seja inserido, é necessário que
     * seu nome seja único, e que obedeça à sintaxe definida.</i>.
     * @param estado estado que será inserido.
     * @return <b>true</b>, caso o estado tenha sido inserido, <b>false</b>, caso
     * não tenha.
     * @see Estado
     */
    public void inserir(Estado estado) throws Exception {
        //Verifica a sintaxe do nome do Estado...
        if (estado.getNome().charAt(0) == 'q') {
            String nome = estado.getNome();
            String indice = nome.substring(1, nome.length());
            try {
                if (Integer.valueOf(indice) >= 0) {
                    //Ordena...
                    if (indiceDe(estado.getNome()) < 0) {
                        estados.add(estado);
                        Collections.sort(estados);
                    }
                } else {
                    throw new Exception("Índice de nome de estado inválido.");
                }
            } catch (Exception e) {
                throw new Exception("Índice de nome de estado inválido.");
            }
        } else {
            throw new Exception("Prefixo de nome de estado inválido.");
        }
    }

    /**
     * Remove o estado do conjunto.
     * @param estado nome do estado a ser removido.
     * @return <b>true</b>, caso o estado tenha sido removido com sucesso, <b>false</b>,
     * caso não tenha.
     */
    public boolean remover(String estado) {
        int indice = indiceDe(estado);
        if (indice >= 0) {
            estados.remove(indice);
            return true;
        }
        return false;
    }

    /**
     * Retorna uma referência para um determinado estado do conjunto.
     * @param indice índice do estado.
     * @return referência para o estado ou <b>null</b>, caso o estado não tenha sido
     * encontrado.
     */
    public Estado estadoEm(int indice) {
        return estados.get(indice);
    }

    /**
     * Retorna a quantidade de estados do conjunto.
     * @return número de estados no conjunto.
     */
    public int dimensao() {
        return estados.size();
    }

    /**
     * Remove todos os estados do conjunto.
     */
    public void limpar() {
        estados.clear();
    }

    /**
     * Conjunto dos estados definidos como terminais (F).<br>
     * Num Autômato Finito Determinísco se terminar a leitura da cadeia de
     * entrada num estado terminal então a palavra <b>é</b> aceita pela gramática.
     * @return conjunto dos estados terminais.
     */
    public Estados terminais()  {
        
        Estados terminais = new Estados() {            
            /**
             * Reescrito para retornar <b>null</b> caso se tente chamar este
             * subconjunto do conjunto dos Estados.
             */
            @Override
            public Estados terminais() {
                return null;
            }
            /**
             * Reescrito para retornar <b>null</b> caso se tente chamar este
             * subconjunto do conjunto dos Estados.
             */
            @Override
            public Estados naoTerminais() {
                return null;
            }
        };
        
        for (int i = 0; i < estados.size(); i++) {
            if (estados.get(i).isTerminal()) {
                try {
                    terminais.inserir(estados.get(i));
                } catch (Exception ex) {
                }
            }
        }
        return terminais;
    }


    /**
     * Conjunto dos estados definidos como não-terminais.<br>
     * Num Autômato Finito Determinísco se terminar a leitura da cadeia de
     * entrada num estado não-terminal então a palavra <b>não é</b> aceita pela
     * gramática.
     * @return conjunto dos estados não-terminais.
     */
    public Estados naoTerminais() {
        
        Estados nao_terminais = new Estados() {
            /**
             * Reescrito para retornar <b>null</b> caso se tente chamar este
             * subconjunto do conjunto dos Estados.
             */
            @Override
            public Estados terminais() {
                return null;
            }
            /**
             * Reescrito para retornar <b>null</b> caso se tente chamar este
             * subconjunto do conjunto dos Estados.
             */
            @Override
            public Estados naoTerminais() {
                return null;
            }            
        };

        for (int i = 0; i < estados.size(); i++) {
            if (!estados.get(i).isTerminal()) {
                try {
                    nao_terminais.inserir(estados.get(i));
                } catch (Exception ex) {
                }
            }
        }
        return nao_terminais;
    }
    
}