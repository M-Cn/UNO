package Draw;

import Font.FontManager;
import Main.UnoApp;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.Locale;

public class FontDrawContext {
    public FontDrawContext(GraphicsContext _gc, FontManager.FontId _id) {
        GC = _gc;
        FontId = _id;
        Attr = new FontAttribute();
    }

    public enum TextAlign { LEFT, CENTER, RIGHT }

    public static class FontAttribute {
        private Color     Fill         = Color.WHITE;
        private TextAlign Align        = TextAlign.LEFT;
        private Color     Outline      = null;      // no outline
        private float     OutlineWidth = 0;
        private boolean   bBold        = false;
    }

    public void SetColor(Color _col) {
        Attr.Fill = _col;
    }

    public void SetAlign(TextAlign _align) {
        Attr.Align = _align;
    }

    public void SetOutline(Color _outlineCol) {
        Attr.Outline = _outlineCol;
    }

    public void SetOutlineWidth(float _width) {
        Attr.OutlineWidth = _width;
    }

    public void SetBold(boolean _bBold) {
        Attr.bBold = _bBold;
    }

    public void DrawText(float _x, float _y, String _text) {
        if (Attr == null) throw new IllegalStateException("No FontAttribute set");

        FontManager fontMgr = UnoApp.GetInstance().GetFontManager();
        Font font = fontMgr.Get(FontId);
        if (Attr.bBold)
            font = Font.font(font.getFamily(), FontWeight.BOLD, font.getSize());

        GC.setFont(font);
        GC.setTextBaseline(VPos.TOP);
        GC.setTextAlign(GetFXTextAlignment(Attr.Align));

        if (Attr.Outline != null && Attr.OutlineWidth > 0) {
            GC.setStroke(Attr.Outline);
            GC.setLineWidth(Attr.OutlineWidth);
            GC.strokeText(_text, _x, _y);
        }

        GC.setFill(Attr.Fill);
        GC.fillText(_text, _x, _y);
    }

    public void DrawFormatText(float _x, float _y, String _fmt, Object... _args) {
        DrawText(_x, _y, String.format(Locale.ROOT, _fmt, _args));
    }

    private static TextAlignment GetFXTextAlignment(TextAlign _align) {
        return switch (_align) {
            case LEFT -> TextAlignment.LEFT;
            case CENTER -> TextAlignment.CENTER;
            case RIGHT -> TextAlignment.RIGHT;
        };
    }

    private final GraphicsContext GC;
    private FontManager.FontId FontId;
    private FontAttribute Attr;
}
