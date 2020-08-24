import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;

public class Minesweeper {
    private static Random random;
    private static char[][] world;
    private static int opened;

    public static void main(String[] args) {
        random = new Random();
        new Graphics();
    }

    public static boolean firstOpen(int x, int y, JLabel[][] labels) {
        if (world[y][x] == 'X') {
            int tx, ty;
            do {
                tx = random.nextInt(9);
                ty = random.nextInt(9);
            } while (world[ty][tx] == 'X');
            world[ty][tx] = 'X';
            world[y][x] = '.';
        }
        for (int i = 0, temp; i < world.length; i++) {
            for (int j = 0; j < world.length; j++) {
                if (world[i][j] != 'X') {
                    temp = countMine(j, i);
                    world[i][j] = (char) (temp == 0 ? '/' : '0' + temp);
                }
            }
        }
        return open(x, y, labels);
    }

    private static int countMine(int x, int y) {
        int max = world.length - 1;
        int counter = (x > 0 && y > 0 && world[y - 1][x - 1] == 'X' ? 1 : 0);
        counter += (y > 0 && world[y - 1][x] == 'X' ? 1 : 0);
        counter += (y > 0 && x < max && world[y - 1][x + 1] == 'X' ? 1 : 0);
        counter += (x < max && world[y][x + 1] == 'X' ? 1 : 0);
        counter += (y < max && x < max && world[y + 1][x + 1] == 'X' ? 1 : 0);
        counter += (y < max && world[y + 1][x] == 'X' ? 1 : 0);
        counter += (y < max && x > 0 && world[y + 1][x - 1] == 'X' ? 1 : 0);
        counter += (x > 0 && world[y][x - 1] == 'X' ? 1 : 0);
        return counter;
    }

    public static boolean open(int x, int y, JLabel[][] labels) {
        ArrayDeque<int[]> stack = new ArrayDeque<>();
        stack.add(new int[]{x, y});
        if (world[y][x] == 'X') {
            return false;
        }
        while (!stack.isEmpty()) {
            x = stack.peek()[0];
            y = stack.pop()[1];
            if (x >= 0 && x < world.length && y >= 0 && y < world.length
                    && (labels[y][x].getBackground().equals(Color.gray)
                    || labels[y][x].getBackground().equals(Color.orange))) {
                if (world[y][x] == '/') {
                    stack.add(new int[]{x - 1, y - 1});
                    stack.add(new int[]{x, y - 1});
                    stack.add(new int[]{x + 1, y - 1});
                    stack.add(new int[]{x + 1, y});
                    stack.add(new int[]{x + 1, y + 1});
                    stack.add(new int[]{x, y + 1});
                    stack.add(new int[]{x - 1, y + 1});
                    stack.add(new int[]{x - 1, y});
                } else {
                    labels[y][x].setText(Integer.toString(world[y][x] - '0'));
                    labels[y][x].setForeground(world[y][x] - '0' < 5 ? (world[y][x] - '0' < 3 ? Color.BLUE : Color.ORANGE) : Color.RED);
                }
                labels[y][x].setBackground(Color.white);
                opened++;
            }
        }
        return true;
    }

    public static int createWorld(int colMine) {
        int counter = 0, x, y, size;
        opened = 0;
        size = colMine < 18? 9: (colMine < 45? 15: 30);
        world = new char[size][size];
        for (char[] chars : world) {
            Arrays.fill(chars, '.');
        }
        while (colMine > counter) { // создание поля
            x = random.nextInt(world.length);
            y = random.nextInt(world.length);
            if (world[y][x] == '.') {
                counter++;
                world[y][x] = 'X';
            }
        }
        return size;
    }

    public static boolean isWin(int colMine, JLabel[][] mask) {
        int counter = 0;
        if (opened + colMine == world.length * world.length) {
            return true;
        }
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world.length; j++) {
                if (mask[i][j].getBackground().equals(Color.orange) && world[i][j] == 'X') {
                    counter++;
                }
            }
        }
        return counter == colMine;
    }

    public static char[][] getWorld() {
        return world;
    }
}

