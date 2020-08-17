import javax.swing.*;
import java.awt.*;

public class Graphics extends JFrame {
    private final JLabel[][] visible = new JLabel[9][9];
    private boolean isPlaying = false;

    public Graphics() {
        super("Minesweeper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(200, 100, 300, 375);

        for(JLabel[] labels: visible){
            for(int i = 0; i < labels.length; i++){
                labels[i] = new JLabel();
                labels[i].setBackground(Color.gray);
                labels[i].setSize(30, 30);
            }
        }
        JTextField textField = new JTextField();
        JButton start = new JButton("Создать");


        add(new JPanel(){
            {
                setBounds(0, 0, 300, 50);
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                add(textField);
                add(start);
            }
        });

        add(new JPanel(){
            {
                setBounds(0, 50, 300, 300);
                setLayout(new GridLayout(9, 9, 3, 3));
                for(JLabel[] labels: visible){
                    for(JLabel obj: labels){
                        add(obj);
                    }
                }
                // здесь должен быть KeyListener
            }
        });

        setVisible(true);

        while (true){

        }
    }
}
