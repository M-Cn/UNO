package GuiFramework.Widgets;

import GuiFramework.GUIWidget;
import javafx.scene.Node;
import javafx.scene.control.Button;

public final class GUIButton extends GUIWidget {
    public GUIButton(GUIWidget _parent, String _label, Runnable _onClick) {
        super(_label, _parent);
        Btn = new Button(_label);
        Btn.setMaxWidth(Double.MAX_VALUE);
        Btn.setOnAction(ev -> _onClick.run());
    }

    @Override public Node GetNode() { return Btn; }
    @Override public boolean UsesLabelColumn() { return false; }

    private final Button Btn;
}
