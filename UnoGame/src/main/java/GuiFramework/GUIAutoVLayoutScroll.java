package GuiFramework;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class GUIAutoVLayoutScroll extends ScrollPane {
    public GUIAutoVLayoutScroll() {
        Content = new VBox(4);
        Content.setPadding(new Insets(4));

        setContent(Content);
        setFitToWidth(true);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        getStyleClass().add("gui-auto-v-layout-scroll");
    }

    public void Add(Node _node) {
        Content.getChildren().add(_node);
    }

    public void Remove(Node _node) {
        Content.getChildren().remove(_node);
    }

    public void Clear() {
        Content.getChildren().clear();
    }

    private final VBox Content;
}
