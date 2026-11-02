package GuiFramework.Widgets;

import GuiFramework.GUIWidget;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

public class GUICheckBox extends GUIWidget {
    public GUICheckBox(GUIWidget _parent, String _name, BooleanProperty _value) {
        super(_name, _parent);
        Value = _value;
        Box = new CheckBox();
        Box.selectedProperty().bindBidirectional(_value);
    }

    @Override public Node GetNode() { return Box; }

    private final BooleanProperty Value;
    private final CheckBox Box;
}