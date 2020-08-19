import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Graphics extends JFrame {
    private final JLabel[][] visible = new JLabel[9][9];
    private boolean isPlaying = false;
    private int colMine;
    private boolean firstOpen = true;

    public Graphics() {
        super("Minesweeper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 100, 500, 500);

        for (JLabel[] labels : visible) {
            for (int i = 0; i < labels.length; i++) {
                labels[i] = new JLabel();
                labels[i].setOpaque(true);
            }
        }
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
                setLayout(new GridLayout(9, 9, 3, 3));
                for (JLabel[] labels : visible) {
                    for (JLabel obj : labels) {
                        add(obj);
                    }
                }
                setBackground(Color.black);
                setVisible(false);
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (isPlaying) {
                            x = e.getX() / (getWidth() / 9);
                            y = e.getY() / (getHeight() / 9);
                            if(x >= 0 && x < 9 && y >= 0 && y < 9) {
                                if (e.getButton() == MouseEvent.BUTTON1 && !visible[y][x].getBackground().equals(Color.orange)) {
                                    if (firstOpen) {
                                        temp = Minesweeper.firstOpen(x, y, visible);
                                        firstOpen = false;
                                    } else {
                                        temp = Minesweeper.open(x, y, visible);
                                    }
                                    if (!temp) { // поражение
                                        for (int i = 0; i < 9; i++) {
                                            for (int j = 0; j < 9; j++) {
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
                for (JLabel[] labels : visible) {
                    for (JLabel label : labels) {
                        label.setBackground(Color.gray);
                        label.setSize(30, 30);
                        label.setText("");
                    }
                }
                colMine = Integer.parseInt(textField.getText());
                textField.setText("");
                Minesweeper.createWorld(colMine);
            }
        };

        start.addActionListener(actionListener);
        textField.addActionListener(actionListener);

        setVisible(true);
    }
}
