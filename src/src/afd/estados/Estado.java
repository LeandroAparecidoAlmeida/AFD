package afd.estados;

/**
 * Define um estado. Cada estado têm dois atributos: <br>
 * <ol>
 * <li><b>nome</b>: nome do estado, que deve ter a sintaxe <b>qn</b>, onde <i>q</i>
 * é o caractere "q" do teclado, e <i>n</i> é um índice numérico positivo
 * qualquer, não necessáriamente sequêncial;
 * <li><b>terminal</b>: atributo que indica se um estado é terminal ou não.
 * </ol>
 * @author Leandro
 */
public class Estado implements Comparable<Estado> {

    /**Nome do Estado.*/
    private String nome;
    /**Definição do Estado.*/
    private boolean terminal;

    /**
     * Espera o nome do estado e seu tipo, se terminal ou não.
     * @param nome nome do estado.
     * @param terminal definição se o estado é terminal ou não.
     */
    public Estado(String nome, boolean terminal) {
        this.nome = nome;
        this.terminal = terminal;
    }

    /**
     * Espera o nome do estado. O tipo será definido como não-terminal.
     * @param nome nome do estado.
     */
    public Estado(String nome) {
        this(nome, false);
    }

    /**
     * Retorna o nome do estado.
     * @return Nome do estado.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o tipo do estado.
     * @return <b>true</b>, caso o estado seja terminal ou <b>false</b>, caso não
     * seja.
     */
    public boolean isTerminal() {
        return terminal;
    }

    /**
     * Define o tipo do estado, se terminal ou não.
     * @param terminal tipo do estado. Se <b>true</b>, indica que ele é terminal,
     * se <b>false</b>, indica que ele não é.
     */
    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    /**
     * Reescrito para indicar que os Estados só são iguais quando têm o mesmo
     * nome.
     * @param obj estado a ser comparado.
     * @return <b>true</b>, caso os estados tenham o mesmo nome, <b>false</b>, caso
     * não tenham.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Estado ? 
        ((Estado)obj).getNome().equals(this.nome) : false);
    }

    /**
     * Reescrito para retornar as informações do estado formatada adequadamente.
     * @return informações sobre o Estado.
     */
    @Override
    public String toString() {
        return "Nome: " + nome + ", Terminal: " + terminal;
    }

    /**
     * Captura o índice numérico presente no nome do estado.
     * @param nome
     * @return
     */
    private Integer getIndice(String nome) {
        return new Integer(nome.substring(1, nome.length()));
    }

    @Override
    public int compareTo(Estado estado) {
        return getIndice(nome).compareTo(getIndice(estado.getNome()));
    }
    
}