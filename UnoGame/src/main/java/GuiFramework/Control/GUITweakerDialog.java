package GuiFramework.Control;

import GuiFramework.GUIWidget;
import GuiFramework.Widgets.GUIButton;
import GuiFramework.Widgets.GUICheckBox;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.*;

public final class GUITweakerDialog extends GUIWidget {
    public enum DialogFlags { NO_CLOSE, NO_COLLAPSE, NO_RESIZE, NO_MOVE }

    private static final double DEFAULT_WIDTH = 300;
    private static final double DEFAULT_HEIGHT = 400;
    private static final double MIN_WIDTH = 150;
    private static final double MIN_HEIGHT = 80;
    private static final double EDGE = 5;

    public GUITweakerDialog(String _name, GUIWidget _parent) {
        this(_name, _parent, EnumSet.noneOf(DialogFlags.class), DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public GUITweakerDialog(String _name, GUIWidget _parent, EnumSet<DialogFlags> _flags) {
        this(_name, _parent, _flags, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public GUITweakerDialog(String _name, GUIWidget _parent, EnumSet<DialogFlags> _flags,
                            double _width, double _height) {
        super(_name, _parent);
        Flags = _flags;
        TitleBar = _BuildTitleBar(_name);
        Body = new GUIAutoVLayoutScroll();
        VBox.setVgrow(Body, Priority.ALWAYS);

        Root = new VBox(TitleBar, Body);
        Root.setPrefSize(_width, _height);
        Root.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        if (!Flags.contains(DialogFlags.NO_MOVE))   _SetupDrag();
        if (!Flags.contains(DialogFlags.NO_RESIZE)) _SetupEdgeResize();

        Root.setStyle("""
        -fx-base: #e8e8e8;
        -fx-background: #e8e8e8;
        -fx-background-color: -fx-background;
        -fx-border-color: #989ba4;
        -fx-border-width: 1;
        -fx-border-radius: 3;
        -fx-background-radius: 3;
        """);
        Root.setPadding(new Insets(1));

        Sys.RegisterWindow(this);
    }

    private StackPane _BuildTitleBar(String _name) {
        Label title = new Label(_name);
        title.setMouseTransparent(true);              // clicks fall through to the bar, so drag still works
        title.setStyle("-fx-font-weight: bold;");

        HBox buttons = new HBox(4);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setPickOnBounds(false);               // its empty area doesn't swallow drag clicks

        if (!Flags.contains(DialogFlags.NO_COLLAPSE)) {
            CollapseButton = new Button("_");
            CollapseButton.setOnAction(e -> { if (bCollapsed) Restore(); else Collapse(); });
            buttons.getChildren().add(CollapseButton);
        }
        if (!Flags.contains(DialogFlags.NO_CLOSE)) {
            Button close = new Button("X");
            close.setOnAction(e -> Close());
            buttons.getChildren().add(close);
        }

        StackPane bar = new StackPane(title, buttons);
        StackPane.setAlignment(title, Pos.CENTER);
        bar.setPadding(new Insets(2, 4, 2, 4));
        bar.setStyle("""
        -fx-background-color:
            linear-gradient(to bottom, derive(-fx-base, 25%), derive(-fx-base, -15%)),
            derive(-fx-base, 40%);
        -fx-background-insets: 0, 0 0 1 0;
        -fx-background-radius: 3 3 0 0;
        """);

        return bar;
    }

    /** Adds a widget to the open group, or to the dialog body when no group is open. */
    public <T extends GUIWidget> T Add(T _widget) {
        GUITweakerGroup group = GroupStack.peek();
        if (group != null) {
            group.Add(_widget);
        } else {
            Body.Add(_WrapTopLevel(_widget));
            Children.add(_widget);
        }
        return _widget;
    }

    public GUIButton CreateButton(String _name, Runnable _cb) {
        return Add(new GUIButton(_CurrentParent(), _name, _cb));
    }

    public GUICheckBox CreateCheckBox(String _name, BooleanProperty _value) {
        return Add(new GUICheckBox(_CurrentParent(), _name, _value));
    }

    public boolean BeginGroup(String _name, boolean _expanded) {
        GroupStack.push(Add(new GUITweakerGroup(_CurrentParent(), _name, _expanded)));   // Add routes it to the parent group or the body
        return true;
    }

    public void EndGroup() {
        if (GroupStack.isEmpty()) throw new IllegalStateException("EndGroup without BeginGroup");
        GroupStack.pop();
    }

    public void Finish() {
        if (!GroupStack.isEmpty())
            throw new IllegalStateException("Unclosed group: " + GroupStack.peek().GetLabel());
    }

    /** Re-reads every bound value. Call once per frame (e.g. from GUISystem's update). */
    @Override public void Refresh() {
        if (bCollapsed) return;                       // nothing visible, skip the work
        for (GUIWidget w : Children) w.Refresh();
    }

    public GUITweakerGroup GetCurrentGroup() { return GroupStack.peek(); }

    /** Parent for the next widget: the open group, or the dialog itself. */
    private GUIWidget _CurrentParent() {
        GUITweakerGroup group = GroupStack.peek();
        return group != null ? group : this;
    }

    @Override public Node GetNode() { return Root; }

    /** Outside a group there is no label column, so labelled widgets get a [label | control] row of their own. */
    private Node _WrapTopLevel(GUIWidget _widget) {
        Node node = _widget.GetNode();
        if (!_widget.UsesLabelColumn()) return node;

        HBox row = new HBox(8, new Label(_widget.GetLabel()), node);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(node, Priority.ALWAYS);
        return row;
    }

    public void SetOnClose(Runnable _cb)    { OnClose = _cb; }
    public void SetOnCollapse(Runnable _cb) { OnCollapse = _cb; }
    public void SetOnRestore(Runnable _cb)  { OnRestore = _cb; }

    /** Window manager bookkeeping only; kept apart from SetOnClose so user callbacks can't replace it. */
    public void SetOnManagerClose(Runnable _cb)  { OnManagerClose = _cb; }

    public double GetPosX() { return Root.getLayoutX(); }
    public double GetPosY() { return Root.getLayoutY(); }

    public void Close() {
        if (!(Root.getParent() instanceof Pane parent)) return;   // already closed: do nothing
        parent.getChildren().remove(Root);
        if (OnManagerClose != null) OnManagerClose.run();
        if (OnClose != null) OnClose.run();
    }

    public void Collapse() {
        if (bCollapsed) return;
        bCollapsed = true;
        ExpandedHeight = Root.getPrefHeight();
        _SetBodyShown(false);
        Root.setPrefHeight(Region.USE_COMPUTED_SIZE);
        if (OnCollapse != null) OnCollapse.run();
    }

    public void Restore() {
        if (!bCollapsed) return;
        bCollapsed = false;
        _SetBodyShown(true);
        Root.setPrefHeight(ExpandedHeight);
        if (OnRestore != null) OnRestore.run();
    }

    private void _SetBodyShown(boolean _shown) {
        Body.setVisible(_shown);
        Body.setManaged(_shown);
    }

    private void _SetupDrag() {
        TitleBar.setOnMousePressed(e -> {
            if (e.getButton() != MouseButton.PRIMARY || Root.getParent() == null) return;
            Point2D p = Root.getParent().sceneToLocal(e.getSceneX(), e.getSceneY());
            DragOffsetX = p.getX() - Root.getLayoutX();
            DragOffsetY = p.getY() - Root.getLayoutY();
        });

        TitleBar.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY || Root.getParent() == null) return;
            Point2D p = Root.getParent().sceneToLocal(e.getSceneX(), e.getSceneY());
            // stop at left/top here; GUIWindowManager._ClampAll handles right/bottom
            Root.relocate(Math.max(0, p.getX() - DragOffsetX), Math.max(0, p.getY() - DragOffsetY));
        });
    }

    private void _SetupEdgeResize() {
        Root.addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
            if (bResizing) return;
            _DetectEdges(e.getX(), e.getY());
            Root.setCursor(bCollapsed ? Cursor.DEFAULT : _CursorForEdges());
        });

        Root.addEventFilter(MouseEvent.MOUSE_EXITED, e -> { if (!bResizing) Root.setCursor(Cursor.DEFAULT); });

        Root.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            _DetectEdges(e.getX(), e.getY());
            if (bCollapsed || !_OnAnyEdge()) return;
            bResizing = true;
            StartMouseX = e.getSceneX(); StartMouseY = e.getSceneY();
            StartX = Root.getLayoutX();       StartY = Root.getLayoutY();
            StartW = Root.getWidth();         StartH = Root.getHeight();
            e.consume();
        });

        Root.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if (!bResizing) return;
            double dx = e.getSceneX() - StartMouseX;
            double dy = e.getSceneY() - StartMouseY;
            double x = StartX, y = StartY, w = StartW, h = StartH;

            if (ResizeR) w = Math.max(MIN_WIDTH,  StartW + dx);
            if (ResizeB) h = Math.max(MIN_HEIGHT, StartH + dy);
            if (ResizeL) { w = Math.max(MIN_WIDTH,  StartW - dx); x = StartX + StartW - w; }
            if (ResizeT) { h = Math.max(MIN_HEIGHT, StartH - dy); y = StartY + StartH - h; }

            Root.relocate(x, y);
            Root.setPrefSize(w, h);
            e.consume();
        });

        Root.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (!bResizing) return;
            bResizing = false;
            e.consume();
        });
    }

    private void _DetectEdges(double _mx, double _my) {
        ResizeL = _mx < EDGE;
        ResizeR = _mx > Root.getWidth() - EDGE;
        ResizeT = _my < EDGE;
        ResizeB = _my > Root.getHeight() - EDGE;
    }

    private boolean _OnAnyEdge() { return ResizeL || ResizeR || ResizeT || ResizeB; }

    private Cursor _CursorForEdges() {
        if ((ResizeL && ResizeT) || (ResizeR && ResizeB)) return Cursor.NW_RESIZE;
        if ((ResizeR && ResizeT) || (ResizeL && ResizeB)) return Cursor.NE_RESIZE;
        if (ResizeL || ResizeR) return Cursor.H_RESIZE;
        if (ResizeT || ResizeB) return Cursor.V_RESIZE;
        return Cursor.DEFAULT;
    }

    private final EnumSet<DialogFlags> Flags;
    private final StackPane TitleBar;
    private final GUIAutoVLayoutScroll Body;
    private Button CollapseButton;

    private boolean bCollapsed;
    private double ExpandedHeight;

    private double DragOffsetX, DragOffsetY;

    private boolean ResizeL, ResizeR, ResizeT, ResizeB, bResizing;
    private double StartMouseX, StartMouseY, StartX, StartY, StartW, StartH;

    private Runnable OnClose, OnCollapse, OnRestore, OnManagerClose;

    private final VBox Root;
    private final List<GUIWidget> Children = new ArrayList<>();
    private final ArrayDeque<GUITweakerGroup> GroupStack = new ArrayDeque<>();
}
