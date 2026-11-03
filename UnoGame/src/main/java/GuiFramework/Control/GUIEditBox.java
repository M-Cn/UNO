package GuiFramework.Control;

import GuiFramework.GUIWidget;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class GUIEditBox extends GUIWidget {
    GUIEditBox(GUIWidget _parent, String _name) {
        super(_name, _parent);
        Node = new TextField();
        Value = new String();
    }

    public void SetValue(String _value) { Value = _value; }
    public String GetValue() { return Value; }

    private final TextField Node;
    private String Value;
    private boolean bCommitted;

    @Override
    public Node GetNode() { return Node; }
}
