package GuiFramework;

import javafx.scene.layout.Pane;

/** Transparent layer that hosts every dialog. Root of the widget tree: it has no parent, so it takes GUISystem directly. */
public final class GUIRootWindow extends GUIWidget {
    GUIRootWindow(GUISystem _sys, Runnable _onResize) {
        super("", _sys);
        Layer = new Pane();
        Layer.setPickOnBounds(false);
        Layer.widthProperty().addListener((obs, oldV, newV) -> _onResize.run());
        Layer.heightProperty().addListener((obs, oldV, newV) -> _onResize.run());
    }

    @Override protected Pane GetNode() { return Layer; }     // covariant return: callers get Pane, not just Node

    private final Pane Layer;
}
