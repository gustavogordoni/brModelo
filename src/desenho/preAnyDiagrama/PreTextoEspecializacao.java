/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package desenho.preAnyDiagrama;

import controlador.Diagrama;
import controlador.apoios.TreeItem;
import desenho.formas.FormaTextoBase;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import util.DesenhadorDeTexto;

/**
 *
 * @author ccandido
 */
public class PreTextoEspecializacao extends FormaTextoBase {

    private static final long serialVersionUID = 7310412510571815120L;

    public PreTextoEspecializacao(Diagrama modelo) {
        super(modelo);
        AceitaAjusteAutmatico = false;
    }

    public PreTextoEspecializacao(Diagrama modelo, String texto) {
        super(modelo, texto);
        AceitaAjusteAutmatico = false;
    }

    private transient double z = 0.0;

    @Override
    public void PinteTexto(Graphics2D g) {
        if (getMaster().getZoom() != z) {
            setTextoFormatado(null);
            z = getMaster().getZoom();
        }
        DesenhadorDeTexto txtf = getTextoFormatado();
        txtf.PinteTexto(g, getForeColor(), getArea(), getTexto());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        MovimentacaoManual = true;
    }

    private boolean MovimentacaoManual = false;

    public boolean isMovimentacaoManual() {
        return MovimentacaoManual;
    }

    public void setMovimentacaoManual(boolean MovimentacaoManual) {
        if (this.MovimentacaoManual != MovimentacaoManual) {
            this.MovimentacaoManual = MovimentacaoManual;
        }
    }

    private boolean TamanhoAutmatico = true;

    public boolean isTamanhoAutmatico() {
        return TamanhoAutmatico;
    }

    public void setTamanhoAutmatico(boolean TamanhoAutmatico) {
        if (this.TamanhoAutmatico != TamanhoAutmatico) {
            this.TamanhoAutmatico = TamanhoAutmatico;
            InvalidateArea();
        }
    }

    @Override
    public void DoPaint(Graphics2D g) {
        if (!isVisible()) {
            return;
        }
        if (TamanhoAutmatico) {
            int tamLetra = g.getFontMetrics(getFont()).stringWidth("M");
            int largura = g.getFontMetrics(getFont()).stringWidth(getTexto()) + tamLetra;
            int altura = g.getFontMetrics(getFont()).getHeight();
            if (getWidth() != largura || getHeight() != altura) {
                setStopRaize(true);
                setWidth(largura);
                setHeight(altura);
                setStopRaize(false);
            }
        }
        super.DoPaint(g);
    }

    @Override
    public boolean getIsLoadedFromXML() {
        return false;
    }

    @Override
    public boolean MostreSeParaExibicao(controlador.apoios.TreeItem root) {
        return false;
    }
}