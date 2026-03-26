/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diagramas.conceitual;

import controlador.Controler;
import controlador.Diagrama;
import controlador.Editor;
import controlador.inspector.InspectorProperty;
import desenho.formas.Forma;
import desenho.preAnyDiagrama.PreEntidade;
import desenho.preAnyDiagrama.PreEspecializacao;
import desenho.preAnyDiagrama.PreTextoEspecializacao;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author ccandido
 */
public class Especializacao extends PreEspecializacao {

    private static final long serialVersionUID = 3595682719755602909L;

    public Especializacao(Diagrama modelo) {
        super(modelo);
        nodic = false;
        paintParcial = true;
    }

    public Especializacao(Diagrama modelo, String texto) {
        super(modelo, texto);
        nodic = false;
        paintParcial = false;
        textoEsp = new TextoEspecializacao(modelo, getTipoEspDescricao());
        getMaster().Add(textoEsp);
    }

    // <editor-fold defaultstate="collapsed" desc="Campos">
//    private boolean Parcial = false;
//
    @Override
    public boolean isParcial() {
        return (this.getListaDeFormasLigadas().size() > 1 && LigadaAoPontoPrincipal() != null) && super.isParcial();
    }

    public boolean isTotal() {
        return (!isParcial() && LigadaAoPontoPrincipal() != null);
    }

    public void setTotal(boolean sn) {
        setParcial(!sn);
    }

    public boolean isNaoExclusiva() {
        final PreEntidade tmp = LigadaAoPontoPrincipal();
        return this.getListaDeFormasLigadas().size() > 1 && tmp != null && (
                    tmp.getListaDeFormasLigadas().stream().filter(f -> f instanceof Especializacao).map(e -> (Especializacao)e).filter(e -> e.LigadaAoPontoPrincipal() == tmp).count() > 1
                );
    }

    public boolean isExclusiva() {
        final PreEntidade tmp = LigadaAoPontoPrincipal();
        return this.getListaDeFormasLigadas().size() > 1 && tmp != null && (
                    tmp.getListaDeFormasLigadas().stream().filter(f -> f instanceof Especializacao).map(e -> (Especializacao)e).filter(e -> e.LigadaAoPontoPrincipal() == tmp).count() == 1
                );
    }
    
    public int getTipoEspForInspector() {
        return getTipoEsp().ordinal();
    }

    public void setTipoEspForInspector(int tipo) {
        setTipoEsp(tipo);
        if (textoEsp != null) {
            textoEsp.setTexto(getTipoEspDescricao());
        }
    }

    private TextoEspecializacao textoEsp = null;

    public TextoEspecializacao getTextoEspecializacao() {
        return textoEsp;
    }
    
    public void AtualizaTextoEspecializacao() {
        if (textoEsp != null) {
            textoEsp.setTexto(getTipoEspDescricao());
            PreEntidade pe = LigadaAoPontoPrincipal();
            if (pe != null && getListaDeFormasLigadas().size() > 1) {
                textoEsp.setVisible(true);
                if (!textoEsp.isMovimentacaoManual()) {
                    textoEsp.setLocation(getLeft(), getTopHeight() + 10);
                }
            } else {
                textoEsp.setVisible(true);
                textoEsp.setLocation(getLeft(), getTopHeight() + 10);
            }
        }
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        AtualizaTextoEspecializacao();
    }

    @Override
    public void DoPaint(Graphics2D g) {
        super.DoPaint(g);
        AtualizaTextoEspecializacao();
    }
    
//
//    public void setParcial(boolean Parcial) {
//        this.Parcial = Parcial;
//        if (this.Parcial) toPaintTxt = "p"; else toPaintTxt = "";
//        InvalidateArea();
//    }
    // </editor-fold>
    
    @Override
    public ArrayList<InspectorProperty> GenerateProperty() {
        ArrayList<InspectorProperty> res = super.GenerateProperty();
        res.add(InspectorProperty.PropertyFactoryMenu("direcao", "setDirecaoFromInspector", getDirecaoForInspector(), Editor.fromConfiguracao.getLstDirecao(Controler.Comandos.cmdEspecializacao)));
        
        String[] afetados = new String[] {"setWidth", "setHeight"};
        InspectorProperty tmp = InspectorProperty.FindByProperty(res, "setHeight");
        int p = res.indexOf(tmp) + 1;
        res.add(p, InspectorProperty.PropertyFactorySN("especializacao.tamanhoautmatico", "setTamanhoAutmatico", isTamanhoAutmatico()).AddCondicaoForFalse(afetados));
        res.add(p + 1, InspectorProperty.PropertyFactorySN("especializacao.movimentacaomanual", "setMovimentacaoManual", isMovimentacaoManual()));
        
        ArrayList<String> tiposEsp = new ArrayList<>();
        for (int i = 0; i < TipoEspecializacao.values().length; i++) {
            tiposEsp.add(getTipoEspDescricao(TipoEspecializacao.values()[i]));
        }
        res.add(InspectorProperty.PropertyFactoryMenu("especializacao.tipo", "setTipoEspForInspector", getTipoEspForInspector(), tiposEsp));

        if ((!isExclusiva() && !isNaoExclusiva())) {
            res.add(InspectorProperty.PropertyFactoryApenasLeituraTexto("especializacao.formacao", Editor.fromConfiguracao.getValor("Inspector.obj.especializacao.malformada")));
        } else {
            res.add(InspectorProperty.PropertyFactoryApenasLeituraSN("especializacao.exclusiva", isExclusiva()));
            res.add(InspectorProperty.PropertyFactoryApenasLeituraSN("especializacao.naoexclusiva", isNaoExclusiva()));
        }
        PreEntidade pe = LigadaAoPontoPrincipal();
        if (pe != null) {
            res.add(
                    InspectorProperty.PropertyFactoryActionSelect(Editor.fromConfiguracao.getValor("Inspector.obj.especializacao.apartirde"),
                    pe.getTexto(),
                    String.valueOf(pe.getID()))
                    );
        } else {
            res.add(InspectorProperty.PropertyFactoryApenasLeituraTexto("especializacao.apartirde", "{}"));
        }
        return res;
    }
    
//    @Override
//    public boolean LoadFromXML(Element me, boolean colando) {
//        return super.LoadFromXML(me, colando);
//    }
    
    @Override
    protected boolean FinderLinked(Forma quem, Forma origem) {
        ArrayList<Forma> outras = getListaDeFormasLigadas(origem);
        for (Forma f : outras) {
            if (f == quem) {
                return true;
            }
            List<Especializacao> esp = f.getListaDeFormasLigadas(Especializacao.class).stream().filter(o -> o != this).map(o -> (Especializacao)o).collect(Collectors.toList());
            
            if (esp.stream().anyMatch((f2) -> (f2.FinderLinked(quem, f)))) {
                return true;
            }
        }
        return false;
    }
    
}
