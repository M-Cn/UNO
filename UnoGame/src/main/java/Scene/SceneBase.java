package Scene;

import Draw.DrawSystem;
import GuiFramework.Control.GUITweakerDialog;

import java.util.TreeMap;

public class SceneBase {
    public SceneBase(int _id, SceneManager _manager) {
        this.Id = _id;
        this.SceneMgr = _manager;
        this.ChildScenes = new TreeMap<Integer, SceneBase>();
    }

    public boolean Initialize() { return true; }
    public void Finalize() {}
    public void Update(float _dt) {}
    public void Draw(DrawSystem _drawSys) {}
    public boolean OnEnter() {
        return Initialize();
    }
    public void OnExit() {
        Finalize();
    }

    /**
    Overwrite this when you want a scene to have a list of widgets display in the scene select scene, before the scene itself comes alive.
    Usage example is for scene settings that should be tweaked before the scene becomes active.
    @param _dlg The root window.
     **/
    public void CreatePreInitTweaker(GUITweakerDialog _dlg) {}

    public final int GetId() { return Id; }

    public final void RegisterChildScene(SceneBase _scene) {
        _scene.Parent = this;
        this.ChildScenes.put(_scene.Id, _scene);
    }

    public final void SetNextScene(int _id) {
        SceneBase nextScene = ChildScenes.get(_id);

        if (nextScene != null) {
            SceneMgr.SetNextScene(nextScene);
        }
    }

    public final void ReturnToParent() {
        if (Parent != null) {
            SceneMgr.SetNextScene(Parent);
        }
    }

    protected int Id;
    protected SceneManager SceneMgr;
    protected SceneBase Parent;
    protected TreeMap<Integer, SceneBase> ChildScenes;
}