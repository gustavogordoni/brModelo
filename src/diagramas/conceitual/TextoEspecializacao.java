/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diagramas.conceitual;

import controlador.Diagrama;
import controlador.Editor;
import controlador.inspector.InspectorProperty;
import desenho.preAnyDiagrama.PreTextoEspecializacao;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author ccandido
 */
public class TextoEspecializacao extends PreTextoEspecializacao {

    private static final long serialVersionUID = -9219630166211153580L;

    public TextoEspecializacao(Diagrama modelo) {
        super(modelo);
        setCanBeDeleted(false);
        nodic = false;
    }

    public TextoEspecializacao(Diagrama modelo, String texto) {
        super(modelo, texto);
        setCanBeDeleted(false);
        nodic = false;
    }

    @Override
    public ArrayList<InspectorProperty> GenerateProperty() {
        ArrayList<InspectorProperty> res = super.GenerateProperty();
        
        String[] afetados = new String[] {"setWidth", "setHeight"};
        
        InspectorProperty tmp = InspectorProperty.FindByProperty(res, "setHeight");
        int p = res.indexOf(tmp) + 1;
        
        res.add(p, InspectorProperty.PropertyFactorySN("especializacao.tamanhoautmatico", "setTamanhoAutmatico", isTamanhoAutmatico()).AddCondicaoForFalse(afetados));
        res.add(p + 1, InspectorProperty.PropertyFactorySN("especializacao.movimentacaomanual", "setMovimentacaoManual", isMovimentacaoManual()));

        return res;
    }
    
    @Override
    protected void ToXmlValores(Document doc, Element me) {
        super.ToXmlValores(doc, me);
        me.appendChild(util.XMLGenerate.ValorBoolean(doc, "TamanhoAutmatico", isTamanhoAutmatico()));
        me.appendChild(util.XMLGenerate.ValorBoolean(doc, "MovimentacaoManual", isMovimentacaoManual()));
    }

    @Override
    public boolean LoadFromXML(Element me, boolean colando) {
        if (!super.LoadFromXML(me, colando)) {
            return false;
        }
        setTamanhoAutmatico(util.XMLGenerate.getValorBooleanFrom(me, "TamanhoAutmatico"));
        setMovimentacaoManual(util.XMLGenerate.getValorBooleanFrom(me, "MovimentacaoManual"));

        return true;
    }
    
    @Override
    public boolean isAlinhavel() {
        return false;
    }
    
}