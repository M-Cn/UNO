package Main;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class AppWindow {
    AppWindow(Stage _stage, int _width, int _height) {
        FXStage = _stage;
        AppRoot = new StackPane();
        FXScene = new Scene(AppRoot, _width, _height);
        FXStage.setScene(FXScene);
    }

    public void AddLayer(Node _layer) {
        AppRoot.getChildren().add(_layer);
    }

    public void SetStyle(String _value) {
        if (AppRoot == null) throw new IllegalStateException("AppRoot is null");
        if (_value != null) AppRoot.setStyle(_value);
    }

    public void SetBackColor(Color _color) {
        if (AppRoot == null) throw new IllegalStateException("AppWindow not initialized");
        AppRoot.setBackground(_color != null ? Background.fill(_color) : null);
    }

    public Stage GetStage() { return FXStage; }
    public Scene GetScene() { return FXScene; }
    public StackPane GetAppRoot()  { return AppRoot; }

    private final Stage FXStage;
    private final Scene FXScene;
    private final StackPane AppRoot;
}