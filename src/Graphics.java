import javax.swing.*;
import java.awt.*;
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
        setBounds(200, 100, 300, 375);

        for (JLabel[] labels : visible) {
            for (int i = 0; i < labels.length; i++) {
                labels[i] = new JLabel();
            }
        }
        JTextField textField = new JTextField();
        JButton start = new JButton("Создать");
        JPanel firstPanel = new JPanel() {
            {
                setBounds(0, 0, 300, 50);
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                add(textField);
                add(start);
            }
        };

        add(firstPanel);

        add(new JPanel() {
            char temp;
            int x, y;

            {
                setBounds(0, 50, 300, 300);
                setLayout(new GridLayout(9, 9, 3, 3));
                for (JLabel[] labels : visible) {
                    for (JLabel obj : labels) {
                        add(obj);
                    }
                }
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (isPlaying) {
                            x = e.getX() / 33;
                            y = e.getY() / 33;
                            if (e.getButton() == MouseEvent.BUTTON1 && !visible[y][x].getBackground().equals(Color.orange)) {
                                if (firstOpen) {
                                    temp = Minesweeper.firstOpen(x, y);
                                    firstOpen = false;
                                } else {
                                    temp = Minesweeper.open(x, y);
                                }
                                if (temp == 'X') { // поражение
                                    for (JLabel[] labels : visible) {
                                        for (JLabel obj : labels) {
                                            if (Character.isDigit(temp)) {
                                                obj.setText(Character.toString(temp));
                                                obj.setBackground(Color.white);
                                                obj.setForeground(temp - '0' < 5 ? (temp - '0' < 3 ? Color.BLUE : Color.ORANGE) : Color.red);
                                            } else if (temp == 'X') {
                                                obj.setBackground(Color.red);
                                            } else {
                                                obj.setBackground(Color.white);
                                            }
                                        }
                                    }
                                    JOptionPane.showMessageDialog(null, "Поражение");
                                    isPlaying = false;
                                    firstPanel.setVisible(true);
                                } else {
                                    if(Character.isDigit(temp)) {
                                        visible[y][x].setText(Character.toString(temp));
                                        visible[y][x].setBackground(Color.white);
                                        visible[y][x].setForeground(temp - '0' < 5 ? (temp - '0' < 3 ? Color.BLUE : Color.ORANGE) : Color.red);
                                    }else{
                                        visible[y][x].setBackground(Color.white);
                                    }
                                    if (Minesweeper.isWin(colMine, visible)) {
                                        JOptionPane.showMessageDialog(null, "Все мины обнаружены. Победа!");
                                        isPlaying = false;
                                        firstPanel.setVisible(true);
                                    }
                                }
                            } else{
                                if(visible[y][x].getBackground().equals(Color.gray)){
                                    visible[y][x].setBackground(Color.orange);
                                } else if(visible[y][x].getBackground().equals(Color.orange)){
                                    visible[y][x].setBackground(Color.gray);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Игровое поле не создано!");
                        }
                    }
                });
            }
        });

        start.addActionListener(l -> {
            if (!isPlaying) {
                isPlaying = true;
                firstOpen = true;
                firstPanel.setVisible(false);
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
        });

        setVisible(true);
    }
}
