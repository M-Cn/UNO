package Font;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.EnumMap;
import java.util.HashMap;

public class FontManager {
    public record FontKey(String Family, FontWeight Weight, double Size) {}

    public enum FontId { DEBUG, GAME }

    private final EnumMap<FontId, Font> Fonts = new EnumMap<>(FontId.class);

    public void Register(FontId _id, String _family, FontWeight _weight, double _size) {
        Fonts.put(_id, Font.font(_family, _weight, _size));
    }

    public Font Get(FontId _id) {
        Font f = Fonts.get(_id);
        if (f == null) throw new IllegalStateException("Font not registered: " + _id);
        return f;
    }
}
