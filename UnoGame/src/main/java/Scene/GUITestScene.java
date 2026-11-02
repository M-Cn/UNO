package Scene;

import GuiFramework.GUITweakerDialog;
import Input.InputManager;
import Main.UnoApp;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.KeyCode;

import java.util.EnumSet;

public class GUITestScene extends AppScene {
    public GUITestScene(int _id, SceneManager _mgr) {
        super(_id, _mgr);
    }

    @Override
    public boolean Initialize() {
        Dialog = new GUITweakerDialog("GUI Test", UnoApp.GetInstance().GetGUISystem().GetRootWindow(), EnumSet.of(GUITweakerDialog.DialogFlags.NO_CLOSE));
        Dialog.CreateCheckBox("Checkbox", TestBool);

        return true;
    }

    @Override
    public void Finalize() {
        Dialog.Close();
    }

    @Override
    public void Update(float _dt) {
        InputManager inputMgr = UnoApp.GetInstance().GetInputManager();

        if (inputMgr.IsKeyDown(KeyCode.F9))
            ReturnToParent();
    }

    @Override
    public void CreatePreInitTweaker(GUITweakerDialog _dlg) {
    }

    GUITweakerDialog Dialog;
    private BooleanProperty TestBool = new SimpleBooleanProperty(false);
}
