import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class NotepadApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // The default Swing look and feel is still usable if the system style is unavailable.
            }

            NotepadFrame frame = new NotepadFrame();
            frame.setVisible(true);
        });
    }
}
