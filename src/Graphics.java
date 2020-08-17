import javax.swing.*;

public class Graphics extends JFrame {
    private final JLabel[][] visible = new JLabel[9][9];
    private final char[][] world;

    public Graphics(char[][] mas) {
        super("Minesweeper");
        world = mas;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 100, 300, 350);

        setVisible(true);
    }
}
