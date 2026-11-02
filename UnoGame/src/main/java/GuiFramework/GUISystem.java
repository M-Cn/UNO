package GuiFramework;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUISystem {
    public boolean Initialize() {
        WindowMgr = new GUIWindowManager(this);
        if (!WindowMgr.Initialize())
            return false;

        ContentLayer = new StackPane();
        StackPane appRoot = new StackPane(ContentLayer, WindowMgr.GetRootWindow().GetNode());

        return true;
    }

    public void Finalize() {
        if (WindowMgr != null)
            WindowMgr.Finalize();
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

    public void SetContent(Parent _root) {
        ContentLayer.getChildren().setAll(_root);
    }

    public GUIWindowManager GetWindowManager() { return WindowMgr; }

    private GUIWindowManager WindowMgr;
    private StackPane ContentLayer;
}
