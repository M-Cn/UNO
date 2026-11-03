package Main;

import Draw.DrawSystem;
import Draw.FontDrawContext;
import Font.FontManager;
import GuiFramework.System.GUISystem;
import Input.InputManager;
import Scene.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class UnoApp extends Application {
    private static UnoApp s_instance;

    private static final String APP_NAME = "UNO";
    private static final int APP_VER_MAJOR = 1;
    private static final int APP_VER_MINOR = 0;
    private static final int APP_VER_REVISION = 0;

    public static String GetAppVersionString() {
        return String.format("%d.%d.%d",  APP_VER_MAJOR, APP_VER_MINOR, APP_VER_REVISION);
    }

    public UnoApp() {
        s_instance = this;
    }

    public static UnoApp GetInstance()  { return s_instance; }

    @Override
    public void init() { _PreInitialize(); }

    @Override
    public void start(Stage _stage) throws IOException {
        _Initialize(_stage);
        _StartUpdate(_stage);
    }

    @Override
    public void stop() {
        _Finalize();
    }

    private void _PreInitialize() {
        UpdateTickControl = new UpdateTickCtrl();
        FontMgr = new FontManager();
        FontMgr.Register(FontManager.FontId.DEBUG, "Consolas", FontWeight.NORMAL, 14);
        FontMgr.Register(FontManager.FontId.GAME, "Uno", FontWeight.NORMAL, 14);
    }

    private void _Initialize(Stage _stage) {
        Window = new AppWindow(_stage, 1280, 720);

        DrawSys = new DrawSystem();
        DrawSys.Initialize();

        Window.AddLayer(DrawSys.GetNode());

        InputMgr = new InputManager();
        InputMgr.Initialize(_stage);

        GUISys = new GUISystem();
        GUISys.Initialize();
        GUISys.SetFont(FontMgr.Get(FontManager.FontId.DEBUG));

        Window.AddLayer(GUISys.GetNode());

        SceneMgr = new SceneManager();
        SceneMgr.Initialize();

        if (!SceneMgr.Initialize())
            throw new RuntimeException("Failed to create the scene manager.");

        SceneSelectScene rootScene = new SceneSelectScene(0, SceneMgr);
        SceneSelectGroup mainGroup = new SceneSelectGroup(rootScene, "Main");
        SceneSelectGroup testGroup = new SceneSelectGroup(rootScene, "Test");

        UnoGameScene gameScene = new UnoGameScene(1, SceneMgr);
        mainGroup.AddScene("Game", gameScene);

        GUITestScene guiTestScene = new GUITestScene(10, SceneMgr);
        testGroup.AddScene("GUI Test", guiTestScene);

        SceneMgr.SetRootScene(rootScene);

        _stage.setTitle(APP_NAME);
        _stage.show();
    }

    private void _MainLoop(long _now) {
        if (UpdateTickControl == null)
            throw new IllegalStateException("UpdateTickControl is null");

        UpdateTickControl.UpdateDeltaTime(_now);
        float dt = UpdateTickControl.GetDeltaTime();

        _Update(dt);
        _Draw();
    }

    private void _Update(float _dt) {
        InputMgr.Update();
        SceneMgr.Update(_dt);
    }

    private void _Draw() {
        DrawSys.BeginFrame();
        SceneMgr.Draw(DrawSys);
        _ShowFps();
        DrawSys.EndFrame();
    }

    private void _ShowFps() {
        FontDrawContext dc = new FontDrawContext(DrawSys.GetContext(), FontManager.FontId.DEBUG);
        dc.DrawFormatText(40, 40, "FPS: %.1f", UpdateTickControl.GetFps());
    }

    private void _StartUpdate(Stage _stage) {
        MainLoop = new AnimationTimer() {
            @Override
            public void handle(long _now) {
                _MainLoop(_now);
            }
        };

        MainLoop.start();
    }

    private void _Finalize() {
        if (SceneMgr != null) SceneMgr.Finalize();
        if (GUISys != null) GUISys.Finalize();
        if (InputMgr != null) InputMgr.Finalize();
        if (DrawSys != null) DrawSys.Finalize();
        if (MainLoop != null) MainLoop.stop();
    }

    public AppWindow GetWindow() { return Window; }
    public UpdateTickCtrl GetUpdateTickCtrl() { return UpdateTickControl; }
    public FontManager GetFontManager() { return FontMgr; }
    public DrawSystem GetDrawSystem() { return DrawSys; }
    public GUISystem GetGUISystem() { return GUISys; }
    public InputManager GetInputManager() { return InputMgr; }
    public SceneManager GetSceneManager() { return SceneMgr; }

    private AppWindow Window;
    private AnimationTimer MainLoop;
    private UpdateTickCtrl UpdateTickControl;
    private FontManager FontMgr;
    private DrawSystem DrawSys;
    private GUISystem GUISys;
    private Properties UserConfigProps;
    private InputManager InputMgr;
    private SceneManager SceneMgr;
}