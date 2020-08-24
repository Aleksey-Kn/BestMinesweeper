import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Graphics extends JFrame {
    private JLabel[][] visible;
    private boolean isPlaying = false;
    private int colMine;
    private boolean firstOpen = true;
    private int sizeOfWorld;

    public Graphics() {
        super("Minesweeper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 100, 500, 500);

        JTextField textField = new JTextField();
        JButton start = new JButton("Создать");
        JPanel firstPanel = new JPanel() {
            {
                setBounds(0, 0, 400, 50);
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                add(textField);
                add(start);
            }
        };

        add(firstPanel);

        JPanel secondPanel = new JPanel() {
            boolean temp;
            char now;
            int x, y;

            {
                setBounds(0, 50, 500, 500);
                setBackground(Color.black);
                setVisible(false);
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (isPlaying) {
                            x = e.getX() / (getWidth() / sizeOfWorld);
                            y = e.getY() / (getHeight() / sizeOfWorld);
                            if(x >= 0 && x < sizeOfWorld && y >= 0 && y < sizeOfWorld) {
                                if (e.getButton() == MouseEvent.BUTTON1 && !visible[y][x].getBackground().equals(Color.orange)) {
                                    if (firstOpen) {
                                        temp = Minesweeper.firstOpen(x, y, visible);
                                        firstOpen = false;
                                    } else {
                                        temp = Minesweeper.open(x, y, visible);
                                    }
                                    if (!temp) { // поражение
                                        for (int i = 0; i < sizeOfWorld; i++) {
                                            for (int j = 0; j < sizeOfWorld; j++) {
                                                now = Minesweeper.getWorld()[i][j];
                                                if (Character.isDigit(now)) {
                                                    visible[i][j].setText(Character.toString(now));
                                                    visible[i][j].setBackground(Color.white);
                                                    visible[i][j].setForeground(now < 5 ? (now < 3 ? Color.BLUE : Color.ORANGE) : Color.red);
                                                } else if (now == 'X') {
                                                    visible[i][j].setBackground(Color.red);
                                                } else {
                                                    visible[i][j].setBackground(Color.white);
                                                }
                                            }
                                        }
                                        JOptionPane.showMessageDialog(null, "Поражение");
                                        isPlaying = false;
                                        firstPanel.setVisible(true);
                                        setVisible(false);
                                    }
                                } else {
                                    if (visible[y][x].getBackground().equals(Color.gray)) {
                                        visible[y][x].setBackground(Color.orange);
                                    } else if (visible[y][x].getBackground().equals(Color.orange)) {
                                        visible[y][x].setBackground(Color.gray);
                                    }
                                }
                                if (Minesweeper.isWin(colMine, visible)) {
                                    JOptionPane.showMessageDialog(null, "Все мины обнаружены. Победа!");
                                    isPlaying = false;
                                    firstPanel.setVisible(true);
                                    setVisible(false);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Игровое поле не создано!");
                        }
                    }
                });
            }
        };

        add(secondPanel);

        ActionListener actionListener = e -> {
            if (!isPlaying) {
                isPlaying = true;
                firstOpen = true;
                firstPanel.setVisible(false);
                secondPanel.setVisible(true);
                colMine = Integer.parseInt(textField.getText());
                sizeOfWorld = Minesweeper.createWorld(colMine);
                if(visible != null){
                    secondPanel.removeAll();
                }
                secondPanel.setLayout(new GridLayout(sizeOfWorld, sizeOfWorld, 3, 3));
                visible = new JLabel[sizeOfWorld][sizeOfWorld];
                for (JLabel[] labels : visible) {
                    for (int i = 0; i < labels.length; i++) {
                        labels[i] = new JLabel();
                        labels[i].setOpaque(true);
                        labels[i].setHorizontalAlignment(SwingConstants.CENTER);
                        labels[i].setVerticalAlignment(SwingConstants.CENTER);
                        labels[i].setBackground(Color.gray);
                        secondPanel.add(labels[i]);
                    }
                }
            }
        };

        start.addActionListener(actionListener);
        textField.addActionListener(actionListener);

        setVisible(true);
    }
}
