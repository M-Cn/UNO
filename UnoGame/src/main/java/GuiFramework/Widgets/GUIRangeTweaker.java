package GuiFramework.Widgets;

import GuiFramework.GUIWidget;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

public class GUIRangeTweaker extends GUIWidget {
    public GUIRangeTweaker(GUIWidget _parent, String _name) {
        super(_name, _parent);
    }

    @Override
    public Node GetNode() { return null; }
}
