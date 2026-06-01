import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class StatusBar extends JPanel {
    private final JLabel caretLabel = new JLabel("第 1 行，第 1 列");
    private final JLabel zoomLabel = new JLabel("100%");

    public StatusBar() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        caretLabel.setPreferredSize(new Dimension(170, 22));
        zoomLabel.setHorizontalAlignment(JLabel.RIGHT);
        zoomLabel.setPreferredSize(new Dimension(80, 22));
        add(caretLabel, BorderLayout.WEST);
        add(zoomLabel, BorderLayout.EAST);
    }

    public void updateCaretPosition(int line, int column) {
        caretLabel.setText("第 " + line + " 行，第 " + column + " 列");
    }

    public void updateZoom(int percent) {
        zoomLabel.setText(percent + "%");
    }
}
