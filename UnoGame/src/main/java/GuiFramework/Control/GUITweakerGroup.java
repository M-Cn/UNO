package GuiFramework.Control;

import GuiFramework.GUIWidget;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public final class GUITweakerGroup extends GUIWidget {
    public GUITweakerGroup(GUIWidget _parent, String _name, boolean _bExpanded) {
        super(_name, _parent);

        Content = new GridPane();
        Content.setHgap(8);
        Content.setVgap(4);

        ColumnConstraints labelCol = new ColumnConstraints();
        labelCol.setMinWidth(Region.USE_PREF_SIZE);

        ColumnConstraints controlCol = new ColumnConstraints();
        controlCol.setHgrow(Priority.ALWAYS);
        controlCol.setFillWidth(true);

        Content.getColumnConstraints().addAll(labelCol, controlCol);

        Pane = new TitledPane(_name, Content);
        Pane.setExpanded(_bExpanded);
        Pane.setAnimated(false);
    }

    public void SetExpanded(boolean _expanded) { Pane.setExpanded(_expanded); }
    public boolean IsExpanded()                { return Pane.isExpanded(); }

    /** Widgets and nested groups go through the same path: a group is just another widget. */
    void Add(GUIWidget _widget) {
        if (_widget.UsesLabelColumn())
            Content.addRow(NextRow++, new Label(_widget.GetLabel()), _widget.GetNode());
        else
            Content.add(_widget.GetNode(), 0, NextRow++, 2, 1);   // span both columns
        Children.add(_widget);
    }

    @Override public void Refresh() {
        if (!Pane.isExpanded()) return;           // collapsed: nothing visible to update
        for (GUIWidget w : Children) w.Refresh();
    }

    @Override public Node GetNode()            { return Pane; }
    @Override public boolean UsesLabelColumn() { return false; }   // the title bar already shows the name

    private final TitledPane Pane;
    private final GridPane Content;
    private final List<GUIWidget> Children = new ArrayList<>();
    private int NextRow;
}
