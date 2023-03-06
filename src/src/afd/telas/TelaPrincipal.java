package afd.telas;

import afd.automato.Automato;
import afd.estados.Estado;
import afd.alfabeto.Simbolo;
import afd.automato.FitaEntrada;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * Tela do sistema. Toda a interface de manipulação do AFD está definida nesta
 * tela.
 * @author Leandro
 */

public class TelaPrincipal extends javax.swing.JFrame {

    /**Objeto do Autômato Finito Determinístico.*/
    private final Automato automato;
    /**Separador de elementos nas caixas de edição.*/
    private final char SEPARADOR = ',';

    /**
     * Cria uma instância da Tela Principal.
     */
    public TelaPrincipal() {
        initComponents();
        automato = new Automato();
        traduzirDialogo();
        configurarMenus();
        jtfAlfabeto.requestFocus();
        jtFuncaoTransicao.setRowSelectionAllowed(false);
        jtFuncaoTransicao.setColumnSelectionAllowed(false);
        jtFuncaoTransicao.getTableHeader().setReorderingAllowed(false);
    }

    /**
     * Por padrão, os textos dos botões das caixas de diálogo, da classe
     * JOptionPane são em inglês.<br>
     * Aqui é feita a tradução dos botões para o idioma português.
     * Para ter o efeito desejado, basta executar o método somente uma única
     * vez, neste caso, no constructor.
     */
    private void traduzirDialogo() {
        UIManager.put("OptionPane.yesButtonText", "Sim");
        UIManager.put("OptionPane.noButtonText", "Não");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "OK");
    }

    /**
     * Configura o itens dos menus popup de cada componente para que estejam
     * habilitados ou desabilitados no contexto adequado.
     */
    private void configurarMenus() {
        jmiRemoverSimbolos.setEnabled(jlAlfabeto.getSelectedValues().length > 0);
        jmiLimparAlfabeto.setEnabled(automato.getAlfabeto().dimensao() > 0);        
        jmiRemoverEstados.setEnabled(jlEstadosNaoTerminais.getSelectedValues().length > 0);
        jmiDefinirTerminal.setEnabled(jlEstadosNaoTerminais.getSelectedValues().length > 0);
        jbDefinirTerminais.setEnabled(jlEstadosNaoTerminais.getSelectedValues().length > 0);
        jmiLimparEstados.setEnabled(automato.getEstados().dimensao() > 0);        
        jmiRemoverTerminais.setEnabled(jlEstadosTerminais.getSelectedValues().length > 0);
        jmiDefinirNaoTerminal.setEnabled(jlEstadosTerminais.getSelectedValues().length > 0);
        jbDefinirNaoTerminais.setEnabled(jlEstadosTerminais.getSelectedValues().length > 0);
        jmiLimparEstados2.setEnabled(automato.getEstados().dimensao() > 0);
    }

    /**
     * Lista todos os Símbolos do Alfabeto na <code>jlAlfabeto</code>.
     */
    private void listarAlfabeto() {
        jlAlfabeto.removeAll();
        final String[] alfabeto = new String[automato.getAlfabeto().dimensao()];
        for (int i = 0; i < automato.getAlfabeto().dimensao(); i++) {
           alfabeto[i] = automato.getAlfabeto().simboloEm(i).toString();
        }
        jlAlfabeto.setModel(new javax.swing.AbstractListModel() {
            String[] strings = alfabeto;
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
    }

    /**
     * Lista todos os estado definidos como não-terminais na
     * <code>jlEstadosNaoTerminais</code>.
     */
    private void listarEstadosNaoTerminais(){
        final String[] estados = new String[automato.getEstadosNaoTerminais().dimensao()];
        jlEstadosNaoTerminais.removeAll();
        for (int i = 0; i < automato.getEstadosNaoTerminais().dimensao(); i++) {
            estados[i] = automato.getEstadosNaoTerminais().estadoEm(i).getNome();
        }
        jlEstadosNaoTerminais.setModel(new javax.swing.AbstractListModel() {
            String[] strings = estados;
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
    }

    /**
     * Lista todos os Estados definidos como terminais na
     * <code>jlEstadosTerminais</code>.
     */
    private void listarEstadosTerminais(){
        final String[] estados = new String[automato.getEstadosTerminais().dimensao()];
        jlEstadosTerminais.removeAll();
        for (int i = 0; i < automato.getEstados().terminais().dimensao(); i++) {
            estados[i] = automato.getEstadosTerminais().estadoEm(i).getNome();
        }
        jlEstadosTerminais.setModel(new javax.swing.AbstractListModel() {
            String[] strings = estados;
            @Override
            public int getSize() { return strings.length; }
            @Override
            public Object getElementAt(int i) { return strings[i]; }
        });
    }

    /**
     * Lista todos os Estados na <code>jcbEstadoInicial</code> para se selecionar
     * o estado inicial do autômato.
     */
    private void listarEstadoInicial() {
        final String[] estados = new String[automato.getEstados().dimensao()];
        jcbEstadoInicial.removeAllItems();
        for (int i = 0; i < automato.getEstados().dimensao(); i++) {
            estados[i] = automato.getEstados().estadoEm(i).getNome();
        }
        jcbEstadoInicial.setModel(
            new javax.swing.DefaultComboBoxModel(estados)
        );
        if (automato.getEstadoInicial() != null) {
            jcbEstadoInicial.setSelectedItem(automato.getEstadoInicial().getNome());
        }
    }

    /**
     * Lista todos os Estados terminais e não-terminais.
     */
    private void listarEstados() {
        listarEstadosNaoTerminais();
        listarEstadosTerminais();
    }

    /**
     * Configura a tabela de transição para que tenha os símbolos como títulos
     * das colunas, os Estados como título das linhas, e cada célula formada
     * pelo encontro de um linha com uma coluna constitua uma transição daquele
     * estado com aquele símbolo. Outra alteração é transformar cada célula
     * em um componente se seleção contendo todos os Estados do autômato, de forma
     * que para selecionar o estado de destino de uma transição, basta selecioná-lo.
     */
    private void gerarTabelaTransicao() {
        /*Configura a jtFuncaoTransicao para que possa criar a interface para
         definição da Função de Transição.

         Como caraterísticas desta JTable, têm-se:

            1. Têm células fixas acima, onde serão listados os símbolos;
            2. Têm células fixas à esquerda, onde serão listados os Estados;
            2. A função de transição será montada de forma tabular, sendo a
               transição definida como a combinação do estado à esquerda com o
               simbolo no topo, formando uma célula que receberá o estado
               para o qual a função comuta, logo,
            3. Cada célula editável terá como componente de entrada uma lista
               suspensa (JComboBox), com todos os Estados da gramática.

         Do exposto acima, algumas alterações serão necessárias para que a
         jtFuncaoTransicao possa obter o comportamento esperado. Essas alterações
         serão as seguintes:

            1. Deve-se mudar o componente de exibição das células de títulos
               das colunas, as que receberão os símbolos, para que tenham
               aparência das células fixas à esquerda, as que recerão os Estados,
               padronizando a exibição das células fixas;
            2. Por padrão, apenas as células do título são fixas(não editáveis).
               Devemos definir que as células da primeira coluna (à esquerda)
               também não sejam editáveis;
            3. As células editáveis deverão receber um estado qualquer. Logo,
               para facilitar a construção da Função de Transição, tais células
               deverão ter uma lista suspensa com todos os Estados listados nela.
               Por padrão, o componente de edição das células de uma JTable é
               o JTextField, porém, devemos mudar o componente para o JComboBox.

         Tais configurações só são aplicáveis à nível de código-fonte, sendo que
         elas não serão manipuladas na IDE do Netbeans.*/
        /*Aqui, a jtFuncaoTransicao têm todas as colunas e linhas removidas,
         ficando vísivel somente a moldura.
         Se não houver ao menos um símbolo ou um estado definido, o método
         é abortado, ficando a jtFuncaoTransicao apenas com a moldura
         desenhada.*/
        jtFuncaoTransicao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        if (automato.getAlfabeto().dimensao() <= 0 && automato.getEstados().dimensao() <= 0) {
            return;
        }
        {//Cria e atribui a estrutura da jtFuncaoTransicao (colunas, células, ...).
            /*Cria o vetor de String que irá receber os símbolos, que serão listados
             no topo da JTable (título das colunas). Este vetor têm a o dimensão
             do getAlfabeto mais um, porque a primeira coluna não irá receber
             símbolo algum, pois ela será a coluna fixa de Estados, à esquerda.
             Este vetor recebe os símbolos, obtidos do getAlfabeto.*/
            String [] alfabeto = new String[automato.getAlfabeto().dimensao() + 1];
            alfabeto[0] = "";
            for (int i = 0; i < automato.getAlfabeto().dimensao(); i++) {
                alfabeto[i + 1] = automato.getAlfabeto().simboloEm(i).toString();
            }
            /*Cria a matriz que será a estrutura da JTable. Após criar, preenche a
             primeira coluna com os Estados.*/
            Object[][] estrutura = new Object[automato.getEstados().dimensao()][automato.getAlfabeto().dimensao() + 1];
            for (int i = 0; i < automato.getEstados().dimensao(); i++) {
                estrutura[i][0] = automato.getEstados().estadoEm(i).getNome();
            }
            /*Cria o vetor que determina quais colunas são editáveis ou não.
             Como definido, apenas a primeira coluna, a que irá receber os Estados
             não é editável, logo, colunas_editaveis[0] = false, as demais receberão
             true.*/
            final boolean[] colunas_editaveis = new boolean[alfabeto.length];
            colunas_editaveis[0] = false;
            for (int i = 1; i < colunas_editaveis.length; i++) {
                colunas_editaveis[i] = true;
            }
            /*Neste ponto, a estrutura da jtFuncaoTransicao já está definida. Temos
             os Estados que serão listados à esquerda, no topo os simbolos do
             getAlfabeto, também já foram definidas quais colunas serão ou não editáveis.
             Esta estrutura já pode ser passada para a construção da jtFuncaoTransicao,
             porém, ela ainda terá o comportamento padrão.
             As próximas instruções é que irão mudar a comportamento dela.*/
            jtFuncaoTransicao.setModel(new javax.swing.table.DefaultTableModel(
            estrutura,alfabeto){
                boolean[] canEdit = colunas_editaveis;
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });            
        }
        /*Com até 8 caracteres as colunas são redimensionadas automáticamente,
         após essa quantidade elas não são mais, sendo criada uma barra de
         rolagem horizontal.*/
        if (automato.getAlfabeto().dimensao() < 9) {
            jtFuncaoTransicao.setAutoResizeMode(
                javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
            );
        } else {
            jtFuncaoTransicao.setAutoResizeMode(
                javax.swing.JTable.AUTO_RESIZE_OFF
            );
        }
        /*Faz a configuração de cada uma das células que compõem os títulos
         das colunas, as que exibem os símbolos. Para isso, será necessário fazer
         a alteração do componente de exibição desta coluna. Isso é possível,
         através do método "jtFuncaoTransicao.getTableHeader().getColumnModel().
         getColumn(i).setHeaderRenderer(...)".
         Através deste método, é possível acessar o modelo de cada coluna da
         TableHeader, e redefinir o componente de exibição dele.*/
        //O componente será um JTextField...
        final JTextField jtfCelulasFixas = new JTextField(); {
            //Define a borda do componente
            jtfCelulasFixas.setBorder(
                javax.swing.BorderFactory.
                createBevelBorder(javax.swing.border.BevelBorder.RAISED)
            );
            //Aplica a cor de fundo do componente
            jtfCelulasFixas.setBackground(new Color(249, 250, 253));
            //Define a fonte do texto como "Tahoma", "Negrito", tamanho "12".
            jtfCelulasFixas.setFont(new java.awt.Font("Tahoma", 1, 12));
            //O texto no componente ficará centralizado.
            jtfCelulasFixas.setHorizontalAlignment(SwingConstants.CENTER);
        }
        for (int i = 0; i < jtFuncaoTransicao.getTableHeader().getColumnModel().getColumnCount(); i++) {
            //Obtém o modelo da coluna na posição i.
            jtFuncaoTransicao.getTableHeader().getColumnModel().getColumn(i).
            setHeaderRenderer(new TableCellRenderer() {
                //Redefine o componente de exibição...
                @Override
                public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
                    //O componente será um JTextField.
                    jtfCelulasFixas.setText(value.toString());
                    return jtfCelulasFixas;
                }                
            });
        }
        /*Faz a configuração da coluna à esquerda, a que exibe os Estados da
         gramática. O processo é basicamente o mesmo utilizado nos títulos das
         colunas.
         O componente de exibição é exatamente o mesmo, com os mesmos atributos,
         pois essa coluna e os títulos devem ter a mesma aparência.*/
        jtFuncaoTransicao.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
                //O componente será um JTextField.
                jtfCelulasFixas.setText(value.toString());
                return jtfCelulasFixas;

            }
        });
        /*A última etapa da configuração da jtFuncaoTransicao consiste em atribuir
         a cada célula editável, um JCombobox com todos os Estados listados nele
         para a edição dos dados.
         Para isso, cria-se um componente JCombobox e lista todos o Estados no
         mesmo.
         Logo após, para cada célula editável, altera seu componente de edição
         para o JComboBox criado, através do método "jtFuncaoTransicao.
         getColumnModel().getColumn(i).setCellEditor".
         Pronto, agora cada célula editável, ao invés de ter uma caixa de edição
         onde o usuário deveria entrar com as Estados digitando-os, ele terá uma
         ComboBox, a qual o usuário irá expandir e escolher o estado desejado,
         sem nenhuma digitação.
         Também aqui é alterado componente de exibição de cada célula editável.*/
        //Cria a JCombox com os Estados.
        final JComboBox jcbEstadosTransicao = new JComboBox(); {
            final String[] estados = new String[automato.getEstados().dimensao()];
            for (int i = 0; i < automato.getEstados().dimensao(); i++) {
                estados[i] = automato.getEstados().estadoEm(i).getNome();
            }
            jcbEstadosTransicao.setModel(
                new javax.swing.DefaultComboBoxModel(estados)
            );
            //jcbEstadosTransicao.setBackground(Color.GRAY);
        }
        for (int i = 1; i < jtFuncaoTransicao.getColumnModel().getColumnCount(); i++) {
            //Atribui a JComboBox como componente de edição da célula.
            jtFuncaoTransicao.getColumnModel().getColumn(i).setCellEditor(
                new DefaultCellEditor(jcbEstadosTransicao)
            );
            //Redefine o componente de exibição da célula.
            jtFuncaoTransicao.getColumnModel().getColumn(i).setCellRenderer(
            new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
                    JTextField jtfCelulasEditaveis;
                    if (value != null) {
                        jtfCelulasEditaveis = new JTextField(value.toString());
                    } else {
                        jtfCelulasEditaveis = new JTextField();
                    }
                    //Sem borda.
                    jtfCelulasEditaveis.setBorder(null);
                    //Define a cor do texto.
                    jtfCelulasEditaveis.setForeground(Color.BLUE);
                    //Fonte 12 em negrito.
                    jtfCelulasEditaveis.setFont(new java.awt.Font("Tahoma", 1, 12));
                    //Texto Centralizado.
                    jtfCelulasEditaveis.setHorizontalAlignment(SwingConstants.CENTER);
                    jtfCelulasEditaveis.setFocusable(true);
                    return jtfCelulasEditaveis;
                }
            });
        }
    }

    /**
     * Recupera cada transição da Tabela de Transição e passa para constituir as
     * transições na Função de Transição.
     * @throws Exception Caso alguma transição faça referência a um estado ou
     * símbolo inexistente.
     */
    private void gerarFuncaoTransicao() throws Exception {
        automato.getFuncaoTransicao().limpar();
        for (int i = 0; i < jtFuncaoTransicao.getModel().getRowCount(); i++) {
            for (int j = 1; j < jtFuncaoTransicao.getModel().getColumnCount(); j++) {
                automato.getFuncaoTransicao().adicionarTransicao(
                    (String)jtFuncaoTransicao.getModel().getValueAt(i, 0),
                    jtFuncaoTransicao.getModel().getColumnName(j).charAt(0),
                    (String)jtFuncaoTransicao.getModel().getValueAt(i, j)
                );                
            }
        }
    }

    /**
     * Insere cada símbolo contido no campo jtfAlfabeto. Os símbolos podem estar
     * separados por vírgula, ou podem ser inseridos isoladamente.
     */
    private void inserirSimbolos() {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        //Recupera o texto contendo os simbolos a serem inseridos.
        String cadeia = jtfAlfabeto.getText();        
        boolean inseriu = false;
        boolean atualizarTabelaTransicao = false;
        //Notifica os erros ocorridos.
        ArrayList<String> invalidos = new ArrayList<>();
        jtfAlfabeto.setText("");
        if (!cadeia.equals("")) {
            String[] simbolos = cadeia.split(new String(new char[]{SEPARADOR}));
            for (int i = 0; i < simbolos.length; i++) {
                inseriu = false;
                String simbolo = simbolos[i];
                if (simbolo.length() == 1) {
                    //Insere o simbolo no getAlfabeto.
                    automato.getAlfabeto().inserir(
                        new Simbolo(simbolo.charAt(0))
                    );
                    inseriu = true;
                    atualizarTabelaTransicao = true;
                }
                if (!inseriu) {
                    //Ocorre quando há ocorrências do tipo aa,sd,ab, etc.
                    if (!invalidos.contains(simbolo)) {
                        invalidos.add(simbolo);
                    }
                }
            }            
        }
        if (atualizarTabelaTransicao) {
            //Altera o layout da jtFuncaoTransicao.
            gerarTabelaTransicao();
            //Atualiza a lista de exibição do getAlfabeto.
            listarAlfabeto();
            //Configura os menus.
            configurarMenus();
        }
        /*Caso ocorra erros, emite mensagem indicando qual(is) contrução(es)
         foi(ram) considerada(s) inválida(s).*/
        if (!invalidos.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "As seguintes construções foram consideradas inválidas:\n\n" +
                invalidos +
                "\n\n" +
                "Isso ocorreu por ter tentado inserir uma sequencia de caracteres, \n" +
                "como aa, ab, ca, por exemplo, em vez de um caracter simples.",
                "Atenção!",
                JOptionPane.WARNING_MESSAGE
            );
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Insere cada estado do campo jtfEstados. Os Estados podem estar
     * separados por vírgula ou podem ser inseridos isoladamente.
     */
    private void inserirEstados(){
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        String cadeia = jtfEstados.getText();
        boolean atualizarTabelaTransicao = false;
        //Notifica os erros ocorridos.
        ArrayList<String> invalidos = new ArrayList<>();
        jtfEstados.setText("");
        if (!cadeia.equals("")) {            
            String[] estados = cadeia.split(new String(new char[]{SEPARADOR}));
            for (int i = 0; i < estados.length; i++) {
                String estado = estados[i];
                try {
                    //Insere o Estado.
                    automato.getEstados().inserir(new Estado(estado));
                    atualizarTabelaTransicao = true;
                } catch (Exception ex) {
                    //Ocorre quando a sintaxe do nome do estado é incorreta.
                    if (!invalidos.contains(estado)) {
                        invalidos.add(estado);
                    }
                }
            }
        }       
        /*Só refaz a jtFuncaoTransicao se, pelo um estado tenha sido inserido.*/
        if (atualizarTabelaTransicao) {
            gerarTabelaTransicao();
            //Atualiza a lista de Estados Não-Terminais.
            listarEstadosNaoTerminais();
            //Atualiza a lista para definição do Estado Inicial.
            listarEstadoInicial();
            //Configura os menus.
            configurarMenus();
        }
        /*Caso ocorra erros, emite mensagem indicando qual(is) contrução(es)
          foi(ram) considerada(s) inválida(s).*/
        if (!invalidos.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "As seguintes construções foram consideradas inválidas:\n\n" +
                invalidos +
                "\n\n" +
                "Isso ocorreu por ter tentado inserir um estado com a sintaxe \n" +
                "incorreta.",
                "Atenção!",
                JOptionPane.WARNING_MESSAGE
            );
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Remove todos os símbolos selecionados na jlAlfabeto.
     */
    private void removerSimbolos() {
        /*Este e os demais métodos prefixados por Remover têm a mesma funciona-
         lidade, mudando apenas o conjunto ao qual trabalham, e o componente de
         exibição.
         O funcionamento do método é o seguinte:

            1. É solicitada a confirmação do usuário para a remoção dos simbolos
               que estão selecionados na lista (no caso jlAlfabeto);
            2. Tendo a confirmação do usuário, recupera os valores do itens
               selecionados, e procede à exclusão de cada elemento de seus
               respectivos conjunto;
            3. É atualizado o layout da jtFuncaoTransicao para refletir a nova
               situação, bem como outros componentes de exibição, conforme o
               contexto da exclusão.
         */
        if (jlAlfabeto.getSelectedIndices().length > 0) {
            Object[] selecionados = jlAlfabeto.getSelectedValues();
            if (JOptionPane.showConfirmDialog(this,
            "Confirma a remoção dos caracteres selecionados?",
            "Atenção!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE) ==
            JOptionPane.YES_OPTION) {
                this.setCursor(new Cursor(Cursor.WAIT_CURSOR));                
                String caracter;
                for (int idx = 0; idx < selecionados.length; idx++) {
                    caracter = (String) selecionados[idx];
                    automato.getAlfabeto().remover(caracter.charAt(0));
                }
                listarAlfabeto();
                gerarTabelaTransicao();
                configurarMenus();
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    /**
     * Remove todos os Estados selecionados na jlEstadosNaoTerminais.
     */
    private void removerEstadosNaoTerminais(){
        if (jlEstadosNaoTerminais.getSelectedIndices().length > 0) {
            Object[] selecionados = jlEstadosNaoTerminais.getSelectedValues();
            if (JOptionPane.showConfirmDialog(this,
            "Confirma a remoção dos estados selecionados?",
            "Atenção!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE) ==
            JOptionPane.YES_OPTION) {
                this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                for (int idx = 0; idx < selecionados.length; idx++) {
                    automato.getEstados().remover((String) selecionados[idx]);
                }
                listarEstadosNaoTerminais();
                listarEstadoInicial();
                gerarTabelaTransicao();
                configurarMenus();
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    /**
     * Remove todos os Estados selecionados na jlEstadosTerminais.
     */
    private void removerEstadosTerminais() {
        if (jlEstadosTerminais.getSelectedIndices().length > 0) {
            Object[] selecionados = jlEstadosTerminais.getSelectedValues();
            if (JOptionPane.showConfirmDialog(this,
            "Confirma a remoção dos estados terminais selecionados?",
            "Atenção!",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE) ==
            JOptionPane.YES_OPTION) {
                this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                for (int idx = 0; idx < selecionados.length; idx++) {
                    automato.getEstados().remover((String) selecionados[idx]);
                }
                listarEstadosTerminais();
                listarEstadoInicial();
                gerarTabelaTransicao();
                configurarMenus();
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    /**
     * Remove todos os símbolos do Alfabeto.
     */
    private void limparAlfabeto() {
        if (JOptionPane.showConfirmDialog(this,
        "Limpar o Alfabeto?",
        "Atenção!",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE) ==
        JOptionPane.YES_OPTION) {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            automato.getAlfabeto().limpar();
            listarAlfabeto();
            gerarTabelaTransicao();
            configurarMenus();
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Remove todos os Estados da gramática.
     */
    private void limparEstados() {
        if (JOptionPane.showConfirmDialog(this,
        "Limpar o Conjunto dos Estados?",
        "Atenção!",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE) ==
        JOptionPane.YES_OPTION) {
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            automato.getEstados().limpar();
            listarEstados();
            listarEstadoInicial();
            gerarTabelaTransicao();
            configurarMenus();
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Define todos os Estados selecionados na jlEstadosNaoTerminais como
     * terminais. Com isso eles passarão para a jlEstadosTerminais.
     */
    private void definirTerminais() {
        /*Obtém o nome de cada estado selecionado na lista de Estados não-terminais
         à esquerda, obtém a referência para o estado com o referido nome, e
         muda sua sua propriedade terminal para true.
         Feito isso, executa o método ListarEstados(), que tem a finalidade de
         atualizar a lista de Estados não-terminais e terminais.
         Os Estados em questão passam para a lista de Estados não-terminais à esquerda,
         sendo removidos da lista à direita.*/
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));  
        Object[] selecionados = jlEstadosNaoTerminais.getSelectedValues();
        for (int idx = 0; idx < selecionados.length; idx++) {
            try {
                automato.getEstados().getEstado((String) selecionados[idx]).setTerminal(true);
            } catch (Exception ex) {                
            }
        }
        listarEstados();
        configurarMenus();
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Define todos os Estados selecionados na jlEstadosTerminais como
     * não-terminais. Com isso eles passarão para a jlEstadosNaoTerminais.
     */
    private void definirNaoTerminais() {
        /*Obtém o nome de cada estado selecionado na lista de Estados terminais
         à direita, obtém a referência para o estado com o referido nome, e
         muda sua sua propriedade terminal para false.
         Feito isso, executa o método ListarEstados(), que tem a finalidade de
         atualizar a lista de Estados não-terminais e terminais.
         Os Estados em questão passam para a lista de Estados terminais à direita,
         sendo removidos da lista à esquerda.*/
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Object[] selecionados = jlEstadosTerminais.getSelectedValues();
        for (int idx = 0; idx < selecionados.length; idx++) {
            try {
                automato.getEstados().getEstado((String) selecionados[idx]).setTerminal(false);
            } catch (Exception ex) {
            }
        }
        listarEstados();
        configurarMenus();
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));        
    }

    /**
     * Define o estado selecionado na jcbEstadoInicial como o estado inicial do
     * autômato.
     */
    private void definirEstadoInicial() {
        if (jcbEstadoInicial.getSelectedIndex() >= 0) {
            String estado = (String) jcbEstadoInicial.getSelectedItem();
            automato.setEstadoInicial(estado);
        }
    }


    /**
     * Verifica se a palavra no campo jtfPalavra é aceita pelo autômato definido.<br>
     * Após terminar o processamento da Fita de Entrada com a palavra em questão,
     * exibe a tela com as informações sobre o processamento da palavra em forma
     * de Função Programa Extendida.
     */
    private void verificarPalavra() {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            gerarFuncaoTransicao();            
            //Força obter o Estado Inicial, para o caso do usuário não ter selecionado.
            definirEstadoInicial();
            boolean aceita = automato.aceita(
                new FitaEntrada(jtfPalavra.getText().toCharArray())
            );
            new TelaVerificacao(
                this,
                aceita,
                automato.processamentoFitaEntrada()
            ).setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Erro ao gerar transições!",
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));            
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jppmAlfabeto = new javax.swing.JPopupMenu();
        jmiRemoverSimbolos = new javax.swing.JMenuItem();
        js1 = new javax.swing.JPopupMenu.Separator();
        jmiLimparAlfabeto = new javax.swing.JMenuItem();
        jppmEstados = new javax.swing.JPopupMenu();
        jmiRemoverEstados = new javax.swing.JMenuItem();
        js2 = new javax.swing.JPopupMenu.Separator();
        jmiDefinirTerminal = new javax.swing.JMenuItem();
        js3 = new javax.swing.JPopupMenu.Separator();
        jmiLimparEstados = new javax.swing.JMenuItem();
        jppmEstadosTerminais = new javax.swing.JPopupMenu();
        jmiRemoverTerminais = new javax.swing.JMenuItem();
        js4 = new javax.swing.JPopupMenu.Separator();
        jmiDefinirNaoTerminal = new javax.swing.JMenuItem();
        js5 = new javax.swing.JPopupMenu.Separator();
        jmiLimparEstados2 = new javax.swing.JMenuItem();
        jppmCadeia = new javax.swing.JPopupMenu();
        jmiLimparCadeia = new javax.swing.JMenuItem();
        js6 = new javax.swing.JPopupMenu.Separator();
        jmiVerificarCadeia = new javax.swing.JMenuItem();
        jpAlfabeto = new javax.swing.JPanel();
        jtfAlfabeto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jspAlfabeto = new javax.swing.JScrollPane();
        jlAlfabeto = new javax.swing.JList();
        jpEstados = new javax.swing.JPanel();
        jtfEstados = new javax.swing.JTextField();
        jspEstados = new javax.swing.JScrollPane();
        jlEstadosNaoTerminais = new javax.swing.JList();
        jbDefinirTerminais = new javax.swing.JButton();
        jbDefinirNaoTerminais = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jlEstadosTerminais = new javax.swing.JList();
        jcbEstadoInicial = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtFuncaoTransicao = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jtfPalavra = new javax.swing.JTextField();
        jbVerificarPalavra = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();

        jmiRemoverSimbolos.setText("Remover");
        jmiRemoverSimbolos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiRemoverSimbolosActionPerformed(evt);
            }
        });
        jppmAlfabeto.add(jmiRemoverSimbolos);
        jppmAlfabeto.add(js1);

        jmiLimparAlfabeto.setText("Limpar Alfabeto");
        jmiLimparAlfabeto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiLimparAlfabetoActionPerformed(evt);
            }
        });
        jppmAlfabeto.add(jmiLimparAlfabeto);

        jmiRemoverEstados.setText("Remover");
        jmiRemoverEstados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiRemoverEstadosActionPerformed(evt);
            }
        });
        jppmEstados.add(jmiRemoverEstados);
        jppmEstados.add(js2);

        jmiDefinirTerminal.setText("Definir como Terminal");
        jmiDefinirTerminal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiDefinirTerminalActionPerformed(evt);
            }
        });
        jppmEstados.add(jmiDefinirTerminal);
        jppmEstados.add(js3);

        jmiLimparEstados.setText("Limpar Conjunto dos Estados");
        jmiLimparEstados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiLimparEstadosActionPerformed(evt);
            }
        });
        jppmEstados.add(jmiLimparEstados);

        jmiRemoverTerminais.setText("Remover");
        jmiRemoverTerminais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiRemoverTerminaisActionPerformed(evt);
            }
        });
        jppmEstadosTerminais.add(jmiRemoverTerminais);
        jppmEstadosTerminais.add(js4);

        jmiDefinirNaoTerminal.setText("Definir como Não-Terminal");
        jmiDefinirNaoTerminal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiDefinirNaoTerminalActionPerformed(evt);
            }
        });
        jppmEstadosTerminais.add(jmiDefinirNaoTerminal);
        jppmEstadosTerminais.add(js5);

        jmiLimparEstados2.setText("Limpar Conjunto dos Estados");
        jmiLimparEstados2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiLimparEstados2ActionPerformed(evt);
            }
        });
        jppmEstadosTerminais.add(jmiLimparEstados2);

        jmiLimparCadeia.setText("Limpar");
        jmiLimparCadeia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiLimparCadeiaActionPerformed(evt);
            }
        });
        jppmCadeia.add(jmiLimparCadeia);
        jppmCadeia.add(js6);

        jmiVerificarCadeia.setText("Verificar");
        jmiVerificarCadeia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiVerificarCadeiaActionPerformed(evt);
            }
        });
        jppmCadeia.add(jmiVerificarCadeia);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulador de Autômato Finito Determinístico");
        setName("TelaPrincipal"); // NOI18N
        setResizable(false);

        jpAlfabeto.setBorder(javax.swing.BorderFactory.createTitledBorder(" Alfabeto (∑) "));
        jpAlfabeto.setPreferredSize(new java.awt.Dimension(200, 200));

        jtfAlfabeto.setNextFocusableComponent(jlAlfabeto);
        jtfAlfabeto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfAlfabetoFocusLost(evt);
            }
        });
        jtfAlfabeto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfAlfabetoKeyPressed(evt);
            }
        });

        jLabel1.setText("Símbolos. (Ex.: a, b, c)");

        jlAlfabeto.setToolTipText("");
        jlAlfabeto.setComponentPopupMenu(jppmAlfabeto);
        jlAlfabeto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jlAlfabeto.setInheritsPopupMenu(true);
        jlAlfabeto.setNextFocusableComponent(jtfEstados);
        jlAlfabeto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jlAlfabetoKeyPressed(evt);
            }
        });
        jlAlfabeto.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jlAlfabetoValueChanged(evt);
            }
        });
        jspAlfabeto.setViewportView(jlAlfabeto);

        javax.swing.GroupLayout jpAlfabetoLayout = new javax.swing.GroupLayout(jpAlfabeto);
        jpAlfabeto.setLayout(jpAlfabetoLayout);
        jpAlfabetoLayout.setHorizontalGroup(
            jpAlfabetoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAlfabetoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpAlfabetoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jtfAlfabeto)
                    .addComponent(jspAlfabeto, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpAlfabetoLayout.setVerticalGroup(
            jpAlfabetoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpAlfabetoLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(9, 9, 9)
                .addComponent(jtfAlfabeto, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jspAlfabeto, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jpEstados.setBorder(javax.swing.BorderFactory.createTitledBorder(" Estados (Q) "));

        jtfEstados.setNextFocusableComponent(jlEstadosNaoTerminais);
        jtfEstados.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfEstadosFocusLost(evt);
            }
        });
        jtfEstados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfEstadosKeyPressed(evt);
            }
        });

        jlEstadosNaoTerminais.setComponentPopupMenu(jppmEstados);
        jlEstadosNaoTerminais.setNextFocusableComponent(jbDefinirTerminais);
        jlEstadosNaoTerminais.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jlEstadosNaoTerminaisKeyPressed(evt);
            }
        });
        jlEstadosNaoTerminais.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jlEstadosNaoTerminaisValueChanged(evt);
            }
        });
        jspEstados.setViewportView(jlEstadosNaoTerminais);

        jbDefinirTerminais.setText(">");
        jbDefinirTerminais.setNextFocusableComponent(jcbEstadoInicial);
        jbDefinirTerminais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDefinirTerminaisActionPerformed(evt);
            }
        });

        jbDefinirNaoTerminais.setText("<");
        jbDefinirNaoTerminais.setNextFocusableComponent(jtFuncaoTransicao);
        jbDefinirNaoTerminais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDefinirNaoTerminaisActionPerformed(evt);
            }
        });

        jlEstadosTerminais.setComponentPopupMenu(jppmEstadosTerminais);
        jlEstadosTerminais.setNextFocusableComponent(jbDefinirNaoTerminais);
        jlEstadosTerminais.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jlEstadosTerminaisKeyPressed(evt);
            }
        });
        jlEstadosTerminais.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jlEstadosTerminaisValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(jlEstadosTerminais);

        jcbEstadoInicial.setNextFocusableComponent(jlEstadosTerminais);
        jcbEstadoInicial.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jcbEstadoInicialFocusLost(evt);
            }
        });
        jcbEstadoInicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbEstadoInicialActionPerformed(evt);
            }
        });

        jLabel5.setText("Estados Não-Terminais (Q - F)");

        jLabel6.setText("Estados Terminais (F)");

        jLabel3.setText("Estados. (Ex.: q1, q2, q3)");

        jLabel7.setText("Estado Inicial (q0)");

        javax.swing.GroupLayout jpEstadosLayout = new javax.swing.GroupLayout(jpEstados);
        jpEstados.setLayout(jpEstadosLayout);
        jpEstadosLayout.setHorizontalGroup(
            jpEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEstadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jtfEstados)
                    .addComponent(jspEstados, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbDefinirTerminais)
                    .addComponent(jbDefinirNaoTerminais))
                .addGap(8, 8, 8)
                .addGroup(jpEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpEstadosLayout.createSequentialGroup()
                        .addGroup(jpEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jcbEstadoInicial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpEstadosLayout.setVerticalGroup(
            jpEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEstadosLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(8, 8, 8)
                .addComponent(jtfEstados, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGroup(jpEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEstadosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(jbDefinirTerminais)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDefinirNaoTerminais)
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEstadosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jpEstadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jspEstados, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEstadosLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbEstadoInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText("Função de Transição (δ)");

        jScrollPane2.setAutoscrolls(true);

        jtFuncaoTransicao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtFuncaoTransicao.setCellSelectionEnabled(true);
        jtFuncaoTransicao.setFocusable(false);
        jtFuncaoTransicao.setNextFocusableComponent(jtfPalavra);
        jtFuncaoTransicao.setRowHeight(20);
        jtFuncaoTransicao.setSelectionBackground(new java.awt.Color(255, 255, 51));
        jScrollPane2.setViewportView(jtFuncaoTransicao);

        jLabel2.setText("Palavra");

        jtfPalavra.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jtfPalavra.setForeground(new java.awt.Color(0, 0, 102));
        jtfPalavra.setComponentPopupMenu(jppmCadeia);
        jtfPalavra.setNextFocusableComponent(jbVerificarPalavra);
        jtfPalavra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfPalavraActionPerformed(evt);
            }
        });
        jtfPalavra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtfPalavraKeyPressed(evt);
            }
        });

        jbVerificarPalavra.setText("...");
        jbVerificarPalavra.setNextFocusableComponent(jtfAlfabeto);
        jbVerificarPalavra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbVerificarPalavraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 852, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jtfPalavra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbVerificarPalavra, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jpAlfabeto, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jpEstados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator3))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator4)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpEstados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpAlfabeto, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbVerificarPalavra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtfPalavra))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jtfAlfabetoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfAlfabetoKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER){
            inserirSimbolos();
        }
    }//GEN-LAST:event_jtfAlfabetoKeyPressed

    private void jmiRemoverSimbolosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiRemoverSimbolosActionPerformed
        removerSimbolos();
    }//GEN-LAST:event_jmiRemoverSimbolosActionPerformed

    private void jtfEstadosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfEstadosKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER){
            inserirEstados();
        }
    }//GEN-LAST:event_jtfEstadosKeyPressed

    private void jmiRemoverEstadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiRemoverEstadosActionPerformed
        removerEstadosNaoTerminais();
    }//GEN-LAST:event_jmiRemoverEstadosActionPerformed

    private void jmiDefinirTerminalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiDefinirTerminalActionPerformed
        definirTerminais();
    }//GEN-LAST:event_jmiDefinirTerminalActionPerformed

    private void jbDefinirTerminaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDefinirTerminaisActionPerformed
        definirTerminais();
    }//GEN-LAST:event_jbDefinirTerminaisActionPerformed

    private void jmiRemoverTerminaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiRemoverTerminaisActionPerformed
        removerEstadosTerminais();
    }//GEN-LAST:event_jmiRemoverTerminaisActionPerformed

    private void jmiDefinirNaoTerminalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiDefinirNaoTerminalActionPerformed
        definirNaoTerminais();
    }//GEN-LAST:event_jmiDefinirNaoTerminalActionPerformed

    private void jbDefinirNaoTerminaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDefinirNaoTerminaisActionPerformed
        definirNaoTerminais();
    }//GEN-LAST:event_jbDefinirNaoTerminaisActionPerformed

    private void jbVerificarPalavraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbVerificarPalavraActionPerformed
        verificarPalavra();
    }//GEN-LAST:event_jbVerificarPalavraActionPerformed

    private void jtfAlfabetoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfAlfabetoFocusLost
        inserirSimbolos();
        if (jlAlfabeto.getModel().getSize() > 0) {
            jlAlfabeto.setSelectedIndex(0);
        }
    }//GEN-LAST:event_jtfAlfabetoFocusLost

    private void jtfEstadosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfEstadosFocusLost
        inserirEstados();
        if (jlEstadosNaoTerminais.getModel().getSize() > 0) {
            jlEstadosNaoTerminais.setSelectedIndex(0);
        }
    }//GEN-LAST:event_jtfEstadosFocusLost

    private void jtfPalavraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfPalavraActionPerformed
        verificarPalavra();
    }//GEN-LAST:event_jtfPalavraActionPerformed

    private void jlAlfabetoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jlAlfabetoKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE){
            removerSimbolos();
        }
    }//GEN-LAST:event_jlAlfabetoKeyPressed

    private void jlEstadosNaoTerminaisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jlEstadosNaoTerminaisKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE){
            removerEstadosNaoTerminais();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            definirTerminais();
        }
    }//GEN-LAST:event_jlEstadosNaoTerminaisKeyPressed

    private void jlEstadosTerminaisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jlEstadosTerminaisKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE){
            removerEstadosTerminais();
        } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            definirNaoTerminais();
        }
    }//GEN-LAST:event_jlEstadosTerminaisKeyPressed

    private void jmiLimparAlfabetoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiLimparAlfabetoActionPerformed
        limparAlfabeto();
    }//GEN-LAST:event_jmiLimparAlfabetoActionPerformed

    private void jmiLimparEstadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiLimparEstadosActionPerformed
        limparEstados();
    }//GEN-LAST:event_jmiLimparEstadosActionPerformed

    private void jmiLimparEstados2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiLimparEstados2ActionPerformed
        limparEstados();
    }//GEN-LAST:event_jmiLimparEstados2ActionPerformed

    private void jmiLimparCadeiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiLimparCadeiaActionPerformed
        jtfPalavra.setText("");
    }//GEN-LAST:event_jmiLimparCadeiaActionPerformed

    private void jmiVerificarCadeiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiVerificarCadeiaActionPerformed
        verificarPalavra();
    }//GEN-LAST:event_jmiVerificarCadeiaActionPerformed

    private void jtfPalavraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfPalavraKeyPressed
        //Usando a tecla ESC, limpa o campo de texto da cadeia.
        if (evt.getKeyCode() == 0x1B) {
            jtfPalavra.setText("");
        }
    }//GEN-LAST:event_jtfPalavraKeyPressed

    private void jlAlfabetoValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jlAlfabetoValueChanged
        configurarMenus();
    }//GEN-LAST:event_jlAlfabetoValueChanged

    private void jlEstadosNaoTerminaisValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jlEstadosNaoTerminaisValueChanged
        configurarMenus();
    }//GEN-LAST:event_jlEstadosNaoTerminaisValueChanged

    private void jlEstadosTerminaisValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jlEstadosTerminaisValueChanged
        configurarMenus();
    }//GEN-LAST:event_jlEstadosTerminaisValueChanged

    private void jcbEstadoInicialFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jcbEstadoInicialFocusLost
        if (jlEstadosTerminais.getModel().getSize() > 0) {
            jlEstadosTerminais.setSelectedIndex(0);
        }
    }//GEN-LAST:event_jcbEstadoInicialFocusLost

    private void jcbEstadoInicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbEstadoInicialActionPerformed
        definirEstadoInicial();
    }//GEN-LAST:event_jcbEstadoInicialActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JButton jbDefinirNaoTerminais;
    private javax.swing.JButton jbDefinirTerminais;
    private javax.swing.JButton jbVerificarPalavra;
    private javax.swing.JComboBox jcbEstadoInicial;
    private javax.swing.JList jlAlfabeto;
    private javax.swing.JList jlEstadosNaoTerminais;
    private javax.swing.JList jlEstadosTerminais;
    private javax.swing.JMenuItem jmiDefinirNaoTerminal;
    private javax.swing.JMenuItem jmiDefinirTerminal;
    private javax.swing.JMenuItem jmiLimparAlfabeto;
    private javax.swing.JMenuItem jmiLimparCadeia;
    private javax.swing.JMenuItem jmiLimparEstados;
    private javax.swing.JMenuItem jmiLimparEstados2;
    private javax.swing.JMenuItem jmiRemoverEstados;
    private javax.swing.JMenuItem jmiRemoverSimbolos;
    private javax.swing.JMenuItem jmiRemoverTerminais;
    private javax.swing.JMenuItem jmiVerificarCadeia;
    private javax.swing.JPanel jpAlfabeto;
    private javax.swing.JPanel jpEstados;
    private javax.swing.JPopupMenu jppmAlfabeto;
    private javax.swing.JPopupMenu jppmCadeia;
    private javax.swing.JPopupMenu jppmEstados;
    private javax.swing.JPopupMenu jppmEstadosTerminais;
    private javax.swing.JPopupMenu.Separator js1;
    private javax.swing.JPopupMenu.Separator js2;
    private javax.swing.JPopupMenu.Separator js3;
    private javax.swing.JPopupMenu.Separator js4;
    private javax.swing.JPopupMenu.Separator js5;
    private javax.swing.JPopupMenu.Separator js6;
    private javax.swing.JScrollPane jspAlfabeto;
    private javax.swing.JScrollPane jspEstados;
    private javax.swing.JTable jtFuncaoTransicao;
    private javax.swing.JTextField jtfAlfabeto;
    private javax.swing.JTextField jtfEstados;
    private javax.swing.JTextField jtfPalavra;
    // End of variables declaration//GEN-END:variables

}
