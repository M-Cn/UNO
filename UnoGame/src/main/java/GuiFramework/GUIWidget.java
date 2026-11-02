package GuiFramework;

import GuiFramework.System.GUISystem;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;

public abstract class GUIWidget {
    protected GUIWidget(String _label, GUIWidget _parent) {
        this(_label, _parent, _parent.GetSys());
    }

    protected GUIWidget(String _label, GUISystem _sys) {
        this(_label, null, _sys);
    }

    private GUIWidget(String _label, GUIWidget _parent, GUISystem _sys) {
        Label = _label;
        Parent = _parent;
        Sys = _sys;
    }

    public final String GetLabel() { return Label; }

    public void SetEnabled(boolean _enabled) { GetNode().setDisable(!_enabled); }
    public boolean IsEnabled()               { return !GetNode().isDisable(); }

    public void SetTooltip(String _text) {
        if (GetNode() instanceof javafx.scene.control.Control c)
            c.setTooltip(_text == null ? null : new Tooltip(_text));
    }

    public void Refresh() {}

    public final GUIWidget GetParent() { return Parent; }
    public final GUISystem GetSys()    { return Sys; }

    public abstract Node GetNode();
    public boolean UsesLabelColumn() { return true; }

    protected final GUISystem Sys;
    protected final GUIWidget Parent;
    protected final String Label;
}
