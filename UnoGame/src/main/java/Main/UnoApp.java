package Main;

import GuiFramework.GUISystem;
import Input.InputManager;
import Scene.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class UnoApp extends Application {
    private static UnoApp s_instance;

    private static final String APP_NAME = "UNO";
    private static final int APP_VER_MAJOR = 1;
    private static final int APP_VER_MINOR = 0;
    private static final int APP_VER_REVISION = 0;

    private static String GetAppVersionString() {
        return String.format("%d.%d.%d",  APP_VER_MAJOR, APP_VER_MINOR, APP_VER_REVISION);
    }

    public UnoApp() {
        s_instance = this;
    }

    public static UnoApp GetInstance()  { return s_instance; }

    @Override
    public void init() { _Initialize(); }

    @Override
    public void start(Stage stage) throws IOException {
        _Start(stage);
    }

    @Override
    public void stop() {
        _Finalize();
    }

    private void _Initialize() {}

    private void _Update(float _dt) {
        InputMgr.Update();
        SceneMgr.Update(_dt);
    }

    private void _Start(Stage _stage) {
        Window = new AppWindow(_stage, 1280, 720);

        InputMgr = new InputManager();
        InputMgr.Initialize(_stage);

        GUISys = new GUISystem();
        GUISys.Initialize();

        SceneMgr = new SceneManager();
        SceneMgr.Initialize();

        if (!SceneMgr.Initialize())
            throw new RuntimeException("Failed to create the scene manager.");

        SceneSelectScene rootScene = new SceneSelectScene(0, SceneMgr);
        SceneSelectGroup mainGroup = new SceneSelectGroup(rootScene, "Main");
        SceneSelectGroup testGroup = new SceneSelectGroup(rootScene, "Test");

        GameScene gameScene = new GameScene(1, SceneMgr);
        mainGroup.AddScene("Game", gameScene);

        GUITestScene guiTestScene = new GUITestScene(10, SceneMgr);
        testGroup.AddScene("GUI Test", guiTestScene);

        SceneMgr.SetRootScene(rootScene);

        String title = String.format("%s v%s", APP_NAME, GetAppVersionString());
        _stage.setTitle(title);
        _stage.show();

        MainLoop = new AnimationTimer() {
            @Override
            public void handle(long _now) {
                float dt = (LastFrameTime < 0) ? 0.0f : (_now - LastFrameTime) * 1E-9f;
                LastFrameTime = _now;
                _Update(dt);
            }
        };

        MainLoop.start();
    }

    private void _Finalize() {
        if (SceneMgr != null) SceneMgr.Finalize();
        if (GUISys != null) GUISys.Finalize();
        if (MainLoop != null) MainLoop.stop();
    }

    public AppWindow GetWindow() { return Window; }
    public GUISystem GetGUISystem() { return GUISys; }
    public InputManager GetInputManager() { return InputMgr; }
    public SceneManager GetSceneManager() { return SceneMgr; }

    private AppWindow Window;
    private AnimationTimer MainLoop;
    private long LastFrameTime = -1;
    private GUISystem GUISys;
    private InputManager InputMgr;
    private SceneManager SceneMgr;
}