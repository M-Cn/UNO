package Input;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.EnumSet;

public final class InputManager {
    public boolean Initialize(Stage _stage) {
        _stage.addEventFilter(KeyEvent.KEY_PRESSED, e -> Live.add(e.getCode()));
        _stage.addEventFilter(KeyEvent.KEY_RELEASED, e -> Live.remove(e.getCode()));

        return true;
    }

    public void Finalize() {}

    public void Update() {
        DownPrev.clear();
        DownPrev.addAll(Down);

        Down.clear();
        Down.addAll(Live);
    }

    public boolean IsKeyDown(KeyCode keyCode) {
        return Down.contains(keyCode) && !DownPrev.contains(keyCode);
    }

    public boolean IsKeyHeld(KeyCode keyCode) {
        return Down.contains(keyCode) && DownPrev.contains(keyCode);
    }

    public boolean IsKeyReleased(KeyCode keyCode) {
        return  !Down.contains(keyCode) && DownPrev.contains(keyCode);
    }

    public void Clear() {
        Live.clear();
    }

    private final EnumSet<KeyCode> Down     = EnumSet.noneOf(KeyCode.class);
    private final EnumSet<KeyCode> DownPrev = EnumSet.noneOf(KeyCode.class);
    private final EnumSet<KeyCode> Live     = EnumSet.noneOf(KeyCode.class);
}
