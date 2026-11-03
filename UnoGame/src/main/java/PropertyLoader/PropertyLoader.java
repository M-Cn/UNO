package PropertyLoader;

import Main.UnoApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public final class PropertyLoader {
    static Properties LoadProperties(String _path) {
        Properties prop = new Properties();

        try (InputStream in = UnoApp.class.getResourceAsStream(_path)) {
            if (in == null) throw new IllegalStateException("Resource not found: " + _path);
            prop.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read " + _path, e);
        }

        return prop;
    }
}
