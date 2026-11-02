package GuiFramework;

import javafx.scene.Node;

public class GUITweakerItem {
    public GUITweakerItem(String _name, Node _control) {
        Name = _name;
        Control = _control;
    }

    private String Name;
    private Node Control;
}
