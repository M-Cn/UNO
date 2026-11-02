package Scene;

import GuiFramework.Control.GUITweakerDialog;
import Main.UnoApp;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.EnumSet;

public class SceneSelectScene extends SceneBase {
    public SceneSelectScene(int _id, SceneManager _mgr) {
        super(_id, _mgr);
        SceneGroupArray = new ArrayList<SceneSelectGroup>();
    }

    @Override
    public boolean Initialize() {
        UnoApp.GetInstance().GetWindow().SetBackColor(Color.rgb(128, 128, 128));

        Dialog = new GUITweakerDialog("SceneSelect", UnoApp.GetInstance().GetGUISystem().GetRootWindow(), EnumSet.of(GUITweakerDialog.DialogFlags.NO_CLOSE), 400, 900);

        for (SceneSelectGroup group : SceneGroupArray) {
            if (Dialog.BeginGroup(group.GetName(), true)) {
                group.CreateTweaker(Dialog);
                Dialog.EndGroup();
            }
        }

        for (SceneSelectGroup group : SceneGroupArray) {
            group.CreatePreInitTweaker(Dialog);
        }

        return true;
    }

    @Override
    public void Finalize() {
        Dialog.Close();
    }

    @Override
    public void Update(float _dt) {
        super.Update(_dt);
    }

    public final void AddGroup(SceneSelectGroup _group) {
        SceneGroupArray.add(_group);
    }

    private final ArrayList<SceneSelectGroup> SceneGroupArray;
    private GUITweakerDialog Dialog;
}