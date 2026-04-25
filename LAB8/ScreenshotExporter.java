import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class ScreenshotExporter {
    public static void main(String[] args) throws Exception {
        Path outDir = Path.of("LAB8", "screenshots");
        Files.createDirectories(outDir);

        SwingUtilities.invokeAndWait(() -> {
            try {
                export(LoginFrameApp.createContentPanel(), outDir.resolve("lab8-1-login.png"));
                export(GuessGamePanel.createContentPanel(), outDir.resolve("lab8-2-guessgame.png"));
                export(EventDemoApp.createContentPanel(), outDir.resolve("lab8-3-event.png"));
                export(StudentManagerPreview.createContentPanel(), outDir.resolve("lab8-5-student-mvc.png"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void export(JComponent component, Path path) throws Exception {
        Dimension size = component.getPreferredSize();
        component.setSize(size);
        layoutRecursively(component);
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, size.width, size.height);
        component.printAll(graphics);
        graphics.dispose();
        ImageIO.write(image, "png", path.toFile());
    }

    private static void layoutRecursively(Component component) {
        component.doLayout();
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                layoutRecursively(child);
            }
        }
    }
}
