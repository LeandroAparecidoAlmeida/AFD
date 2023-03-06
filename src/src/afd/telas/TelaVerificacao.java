package afd.telas;

import afd.automato.Automato;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Exibe o processamento da palavra de entrada.<br>
 * Na linha, indica se a palavra foi ou não aceita pelo autômato. No texto,exibe
 * a Função Extendida para a palavra processada.
 * @author Leandro
 */
public class TelaVerificacao extends javax.swing.JDialog {

    private boolean aceita;
    private ArrayList <Automato.Processamento> historicoProcessamento;

    /**
     * O constructor recebe as informações necessárias para a reconstrução
     * completa do processamento da palavra de entrada.
     * @param parent componente associado à tela.
     * @param AFD referência para o objeto da classe {@link AutomatoFinitoDeterministico}.
     */
    public TelaVerificacao(java.awt.Frame parent, boolean aceita, 
            ArrayList <Automato.Processamento> historicoProcessamento) {
        super(parent, true);
        initComponents();
        this.historicoProcessamento = historicoProcessamento;
        this.aceita = aceita;
        jtpFuncaoExtendida.setText("");
        criarFuncaoExtendida();
    }


    private void criarFuncaoExtendida() {

        /*Faz a transcrição do processamento da palavra de entrada para a forma
         de Função Programa Extendida. Exibe também o texto indicando se a palavra
         foi ou não aceita pela gramática.

         Para entender melhor o procedimento, considere o Autômato Finito

                   M1 = ({a,b}, {q0, q1, q2, q3}, δ1, q0, {q3})

         com:
                       .
                    δ1 .  a    b
                   ...............
                    q0 .  q1   q2
                    q1 .  q3   q2
                    q2 .  q1   q3
                    q3 .  q3   q3

         e a palavra de entrada abaa.

         O resultado do processamento desta palavra pelo método recursivo
         LerCadeia(...) da classe AutomatoFinitoDeterministico é o seguinte:

                     .
            interação.   estado_atual   simbolo lido   converge   parcela
            ..............................................................
               1ª    .        q0             a            q1      baa
               2ª    .        q1             b            q2      aa
               3ª    .        q2             a            q1      a
               4ª    .        q1             a            q1

         Temos o resultado do processamento, no formato acima, agora devemos
         transformar em Função Programa Extendida.
         Mas antes, devemos definir uma Função Programa Extendida.

         Seja M=(∑, Q, δ, q0, F) um Autômato Finito Determinístico. A Função
         Programa extendida, denotado por

                               _δ:Q x ∑* -> Q

         é a Função Programa, ou Função de Transição _δ:Q x ∑ -> Q extendida para
         palavras, e é indutivamente definida como segue:

                               _δ(q, ε) = q
                               _δ(q, aw) = _δ(δ(q, a), w)

         Logo, considerando o Autômatop M1, acima, então a Função Programa Extendida
         aplicada à palavra abaa a partir do estado inicial q0 é como segue:

         _δ(q0, abaa) =                  função extendida sobre abaa
         _δ(δ(q0, a), baa) =             processa abaa
         _δ(q1, baa) =                   função extendida sobre baa
         _δ(δ(q1, b), aa) =              processa baa
         _δ(q2, aa) =                    função extendida sobre aa
         _δ(δ(q2, a), a) =               processa aa
         _δ(q1, a) =                     função extendida sobre a
         _δ(δ(q1, a), ε) =               processa a
         _δ(q3, ε) =  q3                 função extendida sobre ε: fim da indução

         Como a função terminou o processamento da palavra no estado q3, e sendo
         q3 um estado terminal, então a palavra abaa é aceita por M1.

         Temos que reproduzir isto no editor de texto da tela.

         Já temos o resultado do processamento da palavra, obtido através do
         método AFD.HistoricoProcessamentoCadeia().

         Uma transição, resulta numa construção do tipo:

            1. _δ(q0, abaa) =                  função extendida sobre abaa
            2. _δ(δ(q0, a), baa) =             processa abaa

         que só é obtida com dois acessos à linha de transição, pois na linha
         de transição temos os dados nesta ordem:

               estado atual > simbolo lido > estado converge > parcela

         Para a construção de 1., q0 seria o estado atual da linha de transição,
         e abaa seria obtido com a concatenação do símbolo lido com a parcela
         restante da palavra a ser lida.

         Para a construção de 2., q0 seria o estado atual da linha de transição,
         a seria o símbolo lido, e baa a parcela a ser lida da palavra.

         Todas as linhas da transição, com exceção da última terão estas duas
         construções, utilizando-se os dados mencionados.

         A última linha de transição foi quando terminou a leitura da fita de
         entrada, com a leitura do último símbolo. No caso do Autômato M1 acima,
         ela ocorreu nas três últimas linhas.

              .
              .
              .
         _δ(q1, a) =                     função extendida sobre a
         _δ(δ(q1, a), ε) =               processa a
         _δ(q3, ε) =  q3                 função extendida sobre ε: fim da indução

         Logo, devemos incluir o símbolo de vazio (ε), indicando que após o
         processamento do último símbolo a, não houve mais símbolos a serem lidos,
         e, como visto, _δ(q, ε) = q, então devemos mostrar que a função extendida
         _δ(q3, ε) = q3

         Todo esse processo é feito por cancatenação de String.

         Para exibir a frase indicando se a palavra foi ou não aceita pelo Autômato,
         na leitura da última linha, deve-se obter o status isTerminal de
         getEstadoConverge(), da linha de transicão. Caso seja true, a palavra foi
         aceita, caso seja false, não foi.

         A leitura da última linha deve considerar uma parâmetro extra da transição,
         o status dela, se normal ou não.
         Caso não seja normal, indica que no processamento da palavra foi encontrado
         um símbolo não definido no alfabeto.

         Isso ocorrendo, deve-se realizar uma formatação especial, indicando
         este fato.*/

        if (historicoProcessamento.size() > 0) {

            String funcao = "";

            for (int i = 0; i < historicoProcessamento.size(); i++) {

                Automato.Processamento transicao;
                transicao = historicoProcessamento.get(i);

                //_δ(q0, abaa) =
                funcao +=
                "_δ(" +
                transicao.getEstadoOrigem().getNome() +
                ", " +
                transicao.getSimboloLido() + transicao.getParcela() +
                ") =\n";

                if (i < historicoProcessamento.size() - 1) {

                    //_δ(δ(q0, a), baa) =
                    funcao +=
                    "_δ(δ(" +
                    transicao.getEstadoOrigem().getNome() +
                    ", " +
                    transicao.getSimboloLido() +
                    "), " + transicao.getParcela() +
                    ") =\n";

                } else {

                    if (transicao.isNormal()) {

                        //_δ(δ(q0, a), ε) =
                        funcao +=
                        "_δ(δ(" +
                        transicao.getEstadoOrigem().getNome() +
                        ", " +
                        transicao.getSimboloLido() +
                        "), ε) =\n";

                        //_δ(q0, ε) = q0
                        funcao +=
                        "_δ(" +
                        transicao.getEstadoDestino().getNome() +
                        ", ε) = " +
                        transicao.getEstadoDestino().getNome();

                    } else {

                        if (!transicao.getParcela().isEmpty()) {

                            //_δ(δ(q0, x), abaa) = (símbolo indefinido)
                            funcao +=
                            "_δ(δ(" +
                            transicao.getEstadoOrigem().getNome() +
                            ", " +
                            transicao.getSimboloLido() +
                            "), " + transicao.getParcela() +
                            ") = (símbolo indefinido)";

                        } else {

                            //_δ(δ(q0, x), ε) = (símbolo indefinido)
                            funcao +=
                            "_δ(δ(" +
                            transicao.getEstadoOrigem().getNome() +
                            ", " +
                            transicao.getSimboloLido() +
                            "), ε) = (símbolo indefinido)";

                        }

                    }

                }

            }

            jtpFuncaoExtendida.setText(funcao);

        } else {

            jtpFuncaoExtendida.setText(
                "Não há Função Programa Extendida quando a palavra a ser " +
                "verificada é uma palavra vazia."
            );
            
        }

        if (aceita) {
            jlAceita.setText("Palavra aceita para a gramática");
        } else {
            jlAceita.setText("Palavra \"não\" aceita para a gramática");
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtpFuncaoExtendida = new javax.swing.JTextPane();
        jlAceita = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Função Programa Extendida");
        setResizable(false);

        jtpFuncaoExtendida.setEditable(false);
        jScrollPane1.setViewportView(jtpFuncaoExtendida);

        jlAceita.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlAceita.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jlAceita))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlAceita)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlAceita;
    private javax.swing.JTextPane jtpFuncaoExtendida;
    // End of variables declaration//GEN-END:variables

}
