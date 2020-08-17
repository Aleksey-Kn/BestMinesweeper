import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Minesweeper {
    private static final char[][] world = new char[9][9];
    private static final char[][] mask = new char[9][9];
    private static int opened = 0;

    public static void main(String[] args) {
        System.out.println("How many mines do you want on the field?");
        Scanner scanner = new Scanner(System.in);
        int colMine = scanner.nextInt();
        int counter = 0;
        int x, y;
        Random random = new Random();
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
        for (char[] ch : mask) {
            Arrays.fill(ch, '.');
        }
        show(mask);
        System.out.print("Set/delete mines marks (x and y coordinates): ");
        x = scanner.nextInt() - 1;
        y = scanner.nextInt() - 1;
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
        open(x, y);
        while (opened + colMine < 81) {
            show(mask);
            System.out.print("Set/delete mines marks (x and y coordinates): ");
            scanner.nextLine();
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
            if (scanner.next().equals("free")) {
                if (!open(x, y)) {
                    System.out.println("You stepped on a mine and failed!");
                    show(world);
                    return;
                }
            } else if (mask[y][x] == '*'){
                mask[y][x] = '.';
            }
            else{
                mask[y][x] = '*';
            }
            counter = 0;
            for (int i = 0; i < world.length; i++) {
                for (int j = 0; j < world.length; j++) {
                    if (mask[i][j] == '*' && world[i][j] == 'X') {
                        counter++;
                    }
                }
            }
            if (counter == colMine) {
                break;
            }
        }
        System.out.println("Congratulations! You found all mines!");
        show(world);
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

    private static boolean open(int x, int y) {
        ArrayDeque<int[]> stack = new ArrayDeque<>();
        stack.add(new int[]{x, y});
        if (world[y][x] == 'X') {
            return false;
        }
        while (!stack.isEmpty()) {
            x = stack.peek()[0];
            y = stack.pop()[1];
            try {
                if (mask[y][x] == '.' || mask[y][x] == '*') {
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
                    mask[y][x] = world[y][x];
                }
            } catch (ArrayIndexOutOfBoundsException ignore) {
            }
        }
        return true;
    }

    private static void show(char[][] mas) {
        System.out.println(" │123456789│");
        System.out.println("—│—————————│");
        for (int i = 0; i < mas.length; i++) {
            System.out.print((i + 1) + "|");
            for (char now : mas[i]) {
                System.out.print(now);
            }
            System.out.println("|");
        }
        System.out.println("—│—————————│");
    }
}

