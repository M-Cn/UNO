package GuiFramework.System;

import GuiFramework.Control.GUITweakerDialog;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class GUIWindowManager {
    private static final double TOLERANCE = 50.f;

    public GUIWindowManager(GUISystem _sys) {
        Sys = _sys;
        RootWindow = new GUIRootWindow(_sys, this::_ClampAll);
    }

    public boolean Initialize() {
        return true;
    }

    public void Finalize() {
        for (GUITweakerDialog dlg : new ArrayList<>(Dialogs))   // copy: Close() removes from Dialogs
            dlg.Close();

        CurrentDialog = null;
    }

    public GUIRootWindow GetRootWindow() { return RootWindow; }

    /** Called once per frame: lets every open dialog re-read its bound values. */
    public void Update() {
        for (GUITweakerDialog dlg : Dialogs)
            dlg.Refresh();
    }

    /**
     * Create a new window at the specified position.
     * @param _dlg The window.
     * @param _x The x position.
     * @param _y The y position.
     */
    public void Open(GUITweakerDialog _dlg, int _x, int _y) {
        if (Dialogs.contains(_dlg)) {
            SetCurrentWindow(_dlg);
            return;
        }

        Node node = _dlg.GetNode();
        Dialogs.add(_dlg);
        RootWindow.GetNode().getChildren().add(node);
        node.relocate(_x, _y);
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, ev -> SetCurrentWindow(_dlg));
        _dlg.SetOnManagerClose(() -> _OnDialogClosed(_dlg));

        SetCurrentWindow(_dlg);
    }

    /**
     * Creates a new window at the first free position.
     * @param _dlg The window.
     */
    public void Open(GUITweakerDialog _dlg) {
        Open(_dlg, NextX, NextY);
        _CalcNextWindowPos();
    }

    public void SetCurrentWindow(GUITweakerDialog _wnd) {
        CurrentDialog = _wnd;

        if (_wnd != null)
            _wnd.GetNode().toFront();
    }

    public GUITweakerDialog GetCurrentWindow() { return CurrentDialog; }

    private void _OnDialogClosed(GUITweakerDialog _dlg) {
        Dialogs.remove(_dlg);
        if (CurrentDialog == _dlg) CurrentDialog = null;
    }

    private void _CalcNextWindowPos() {
        NextX += 20;
        NextY += 20;

        double w = RootWindow.GetNode().getWidth();
        double h = RootWindow.GetNode().getHeight();

        if (w > 0.f && NextX > (w - TOLERANCE))
            NextX = 0;

        if (h > 0.f && NextY > (h - TOLERANCE))
            NextY = 0;
    }

    private void _ClampAll() {
        double w = RootWindow.GetNode().getWidth();
        double h = RootWindow.GetNode().getHeight();
        if (w <= 0 || h <= 0) return;

        for (GUITweakerDialog dlg : Dialogs) {
            Node n = dlg.GetNode();
            Bounds b = n.getBoundsInParent();
            double dx = 0, dy = 0;

            if (b.getMaxX() > w)      dx = w - b.getMaxX();
            if (b.getMinX() + dx < 0) dx = -b.getMinX();

            if (b.getMaxY() > h)      dy = h - b.getMaxY();
            if (b.getMinY() + dy < 0) dy = -b.getMinY();

            if (dx != 0 || dy != 0)
                n.relocate(n.getLayoutX() + dx, n.getLayoutY() + dy);
        }
    }

    private final GUISystem Sys;
    private final GUIRootWindow RootWindow;
    private final List<GUITweakerDialog> Dialogs = new ArrayList<>();
    private GUITweakerDialog CurrentDialog;
    private int NextX = 0;
    private int NextY = 0;
}
