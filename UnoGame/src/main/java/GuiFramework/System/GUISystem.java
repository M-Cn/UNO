package GuiFramework.System;

import GuiFramework.Control.GUITweakerDialog;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.util.Locale;

public class GUISystem {
    public boolean Initialize() {
        WindowMgr = new GUIWindowManager(this);
        if (!WindowMgr.Initialize())
            return false;

        return true;
    }

    public void Finalize() {
        if (WindowMgr != null)
            WindowMgr.Finalize();
    }

    public void SetFont(Font _font) {
        String weight = _font.getStyle().toLowerCase().contains("bold") ? "bold" : "normal";
        WindowMgr.GetRootWindow().GetNode().setStyle(String.format(Locale.ROOT,
                "-fx-font-family: '%s'; -fx-font-size: %.1fpx; -fx-font-weight: %s;",
                _font.getFamily(), _font.getSize(), weight));
    }

    public void RegisterWindow(GUITweakerDialog _dlg, int _x, int _y) {
        if (WindowMgr == null)
            throw new IllegalStateException("WindowManager is null");

        WindowMgr.Open(_dlg, _x, _y);
    }

    public void RegisterWindow(GUITweakerDialog _dlg) {
        if (WindowMgr == null)
            throw new IllegalStateException("WindowManager is null");

        WindowMgr.Open(_dlg);
    }

    public GUIRootWindow GetRootWindow() {
        if (WindowMgr == null)
            return null;

        return WindowMgr.GetRootWindow();
    }

    public Node GetNode() {
        if (WindowMgr == null)
            return null;

        GUIRootWindow root = WindowMgr.GetRootWindow();

        if (root == null)
            return null;

        return root.GetNode();
    }

    public GUIWindowManager GetWindowManager() { return WindowMgr; }

    private GUIWindowManager WindowMgr;
}
