package Draw;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public final class DrawSystem {
    private static final class ResizableCanvas extends Canvas {
        @Override public boolean isResizable()             { return true; }

        @Override public double minWidth(double _h)        { return 0; }
        @Override public double minHeight(double _w)       { return 0; }
        @Override public double prefWidth(double _h)       { return 0; }
        @Override public double prefHeight(double _w)      { return 0; }
        @Override public double maxWidth(double _h)        { return Double.MAX_VALUE; }
        @Override public double maxHeight(double _w)       { return Double.MAX_VALUE; }

        @Override public void resize(double _w, double _h) { setWidth(_w); setHeight(_h); }
    }

    public boolean Initialize() {
        FXCanvas = new ResizableCanvas();
        GC = FXCanvas.getGraphicsContext2D();

        return true;
    }

    // Empty, for now.
    public void Finalize() {

    }

    public Node GetNode() { return FXCanvas; }

    private ResizableCanvas FXCanvas;
    private GraphicsContext GC;
}
