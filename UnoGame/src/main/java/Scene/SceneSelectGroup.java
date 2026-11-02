package Scene;

import GuiFramework.Control.GUITweakerDialog;

import java.util.ArrayList;

public class SceneSelectGroup {
    private record Entry(String Name, SceneBase Scene) {}

    public SceneSelectGroup(SceneSelectScene _root, String _name) {
        Root = _root;
        Name = _name;
        SceneArray = new ArrayList<Entry>();
        _root.AddGroup(this);
    }

    public void AddScene(String _name, SceneBase _scene) {
        SceneArray.add(new Entry(_name, _scene));
        Root.RegisterChildScene(_scene);
    }

    public void CreateTweaker(GUITweakerDialog _dlg) {
        for (Entry e : SceneArray) {
            _dlg.CreateButton(e.Name, () -> Root.SetNextScene(e.Scene.GetId()));
        }
    }

    public void CreatePreInitTweaker(GUITweakerDialog _dlg) {
        for (Entry e : SceneArray)
            e.Scene.CreatePreInitTweaker(_dlg);
    }

    public final String GetName() { return Name; }

    private final SceneSelectScene Root;
    private final String Name;
    private final ArrayList<Entry> SceneArray;
}
