import javax.swing.*;
import java.awt.*;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    private static Random random;
    private static char[][] world = new char[9][9];
    private static int opened;

    public static void main(String[] args) {
        random = new Random();
        Graphics graphics = new Graphics();
    }

    public static char firstOpen(int x, int y) {
        if (world[y][x] == 'X') {
            int tx, ty;
            do {
                tx = random.nextInt(9);
                ty = random.nextInt(9);
            } while (world[ty][tx] == 'X');
            world[ty][tx] = 'X';
            world[y][x] = '.';
        }
        for (int i = 0, temp; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (world[i][j] != 'X') {
                    temp = countMine(j, i);
                    world[i][j] = (char) (temp == 0 ? '/' : '0' + temp);
                }
            }
        }
        return open(x, y);
    }

    private static int countMine(int x, int y) {
        int max = 8;
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

    private static char open(int x, int y) {
        ArrayDeque<int[]> stack = new ArrayDeque<>();
        stack.add(new int[]{x, y});
        if (world[y][x] == 'X') {
            return 'X';
        }
        while (!stack.isEmpty()) {
            x = stack.peek()[0];
            y = stack.pop()[1];
            try {
                if (world[y][x] == '/') {
                    stack.add(new int[]{x - 1, y - 1});
                    stack.add(new int[]{x, y - 1});
                    stack.add(new int[]{x + 1, y - 1});
                    stack.add(new int[]{x + 1, y});
                    stack.add(new int[]{x + 1, y + 1});
                    stack.add(new int[]{x, y + 1});
                    stack.add(new int[]{x - 1, y + 1});
                    stack.add(new int[]{x - 1, y});
                }
                opened++;
            } catch (ArrayIndexOutOfBoundsException ignore) {
            }
        }
        return world[y][x];
    }

    public static char[][] createWorld(int colMine) {
        int counter = 0, x, y;
        opened = 0;
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
        return world;
    }

    public static boolean isWin(int colMine, JLabel[][] mask) {
        int counter = 0;
        if (opened + colMine == 81) {
            return true;
        }
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world.length; j++) {
                if (mask[i][j].getBackground().equals(Color.orange) && world[i][j] == 'X') {
                    counter++;
                }
            }
        }
        if (counter == colMine) {
            return true;
        }
        return false;
    }
}

