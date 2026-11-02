package Scene;

import Draw.DrawSystem;

public class SceneManager {
    public SceneManager()  {}

    public boolean Initialize() {
        return true;
    }

    public void Finalize() {
        if (CurrentScene != null)
            CurrentScene.OnExit();

        RootScene = null;
    }

    public void Update(float _dt) {
        if (NextScene != null) {
            if (CurrentScene != null)
                CurrentScene.OnExit();

            CurrentScene = NextScene;
            NextScene = null;

            if (!CurrentScene.OnEnter())
                throw new RuntimeException("Failed to enter scene.");
        }

        if (CurrentScene != null)
            CurrentScene.Update(_dt);
    }

    public void Draw(DrawSystem _drawSys) {
        if (CurrentScene != null)
            CurrentScene.Draw(_drawSys);
    }

    public final void SetRootScene(SceneBase _scene) {
        NextScene = _scene;
        RootScene = _scene;
    }

    public final void SetNextScene(SceneBase _scene) {
        NextScene = _scene;
    }

    private SceneBase CurrentScene;
    private SceneBase RootScene;
    private SceneBase NextScene;
}