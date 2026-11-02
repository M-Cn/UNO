package Scene;

import GuiFramework.Control.GUITweakerDialog;
import Input.InputManager;
import Main.UnoApp;
import javafx.scene.input.KeyCode;

public class UnoGameScene extends SceneBase {
    public UnoGameScene(int _id, SceneManager _mgr) {
        super(_id, _mgr);
    }

    @Override
    public boolean Initialize() {
        return true;
    }

    @Override
    public void Finalize() {

    }

    @Override
    public void Update(float _dt) {
        InputManager inputMgr = UnoApp.GetInstance().GetInputManager();

        if (inputMgr.IsKeyDown(KeyCode.F9))
            ReturnToParent();
    }

    @Override
    public void CreatePreInitTweaker(GUITweakerDialog _wnd) {

    }
}
