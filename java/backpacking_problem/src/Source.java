/* Idea programu */
/* Program implementuje pakowanie plecaka. W wersji iteracyjnej uzywa przejscia po tablicy za pomoca petli w poszukiwaniu
 * dobrych elementow; jesli pojemnosc plecaka dostanie przekrocozna, pierwszy element jest odrzucany i kolejny brany. Wersja
 * rekurencyjna dziala analogicznie, ale uzywa funkcji rec_pakuj do rownoleglego sprawdzenia spakowania plecaka z i bez
 * kazdego przedmiotu; dwa wyniki sa porownywane i brany jest lepszy.
 */

import java.util.Scanner;

class Stack {

    private final Dane[] store;
    private int size = 0;

    Stack(int capacity) {
        this.store = new Dane[capacity];

    }

    public void push(Dane value) {
        store[size++] = value;
    }

    public Dane pop() {
        return store[size--];
    }

    public Dane peek() {
        return store[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}


class Dane {
    public int currSize;
    public int id;
    public boolean result = false;

    public Dane with = null;
    public Dane without = null;

    public Dane(int currSize, int id) {
        this.currSize = currSize;
        this.id = id;
    }
}

public class Source {
    public static Scanner sc = new Scanner(System.in);

    public static boolean rec_pakuj(int[] objects, int size, int id, boolean[] isTaken) {
        if (size == 0) { //spakowane
            return true;
        }
        if (size < 0 || id >= objects.length) { //zadna kombinacja nie pasuje
            return false;
        }
        if (objects[id] > size) { //jesli rozmiar plecaka przekroczony
            return rec_pakuj(objects, size, id + 1, isTaken);
        } else { //jesli dobry kandydat do pakowania- sprawdz pozostale podzbiory
            isTaken[id] = true; //odznacz ze da sie spakowac ten przedmiot
            if (rec_pakuj(objects, size - objects[id], id + 1, isTaken))
                return true; //jesli da sie spakowac reszte plecaka zakoncz
            isTaken[id] = false; //przedmiot nie do spakowania w najlepszym przypadku
            return rec_pakuj(objects, size, id + 1, isTaken);
        }
    }

    public static String iter_pakuj(int[] objects, int size) {
        boolean[] isTaken = new boolean[objects.length];
        for (int j = 0; j < objects.length; j++) {
            isTaken[j] = false;
        }
        Stack stack = new Stack(objects.length * 5);
        Dane s = new Dane(size, 0);
        stack.push(s);
        Dane currConfig;
        boolean result_is_found = false;
        while (!stack.isEmpty()) {
            currConfig = stack.peek();
            if (currConfig.currSize == 0) { //spakowane
                result_is_found = true;
                currConfig.result = true;
                stack.pop();
                continue;
            }

            if (currConfig.id >= objects.length || currConfig.currSize < 0) { //zadna kombinacja nie pasuje
                stack.pop();
                continue;
            }
            if (currConfig.with != null) {
                if (currConfig.with.result) {
                    isTaken[currConfig.id] = true;
                    currConfig.result = true;
                }
                if (currConfig.without.result) {
                    currConfig.result = true;
                }
                stack.pop();
                continue;
            }

            if (currConfig.without != null) {
                currConfig.result = currConfig.without.result;
                stack.pop();
                continue;
            }

            if (result_is_found) {
                stack.pop();
                continue;
            }

            // forward path
            if (objects[currConfig.id] > currConfig.currSize) { //jesli rozmiar plecaka przekroczony
                currConfig.without = new Dane(currConfig.currSize, currConfig.id + 1);
                stack.push(currConfig.without);
            } else { //jesli dobry kandydat do pakowania- sprawdz pozostale podzbiory
                currConfig.without = new Dane(currConfig.currSize, currConfig.id + 1);
                stack.push(currConfig.without);
                currConfig.with = new Dane(currConfig.currSize - objects[currConfig.id], currConfig.id + 1);
                stack.push(currConfig.with);
            }
        }
        StringBuilder res = new StringBuilder();
        for (int j = 0; j < objects.length; j++) {
            if (isTaken[j]) res.append(" ").append(objects[j]);
        }
        return res.toString();
    }

    public static void main(String[] args) {
        /* Input */
        int z = sc.nextInt();
        for (int i = 0; i < z; i++) {
            int n = sc.nextInt();
            int k = sc.nextInt();
            int[] a = new int[k];
            boolean[] isTaken = new boolean[a.length];
            for (int j = 0; j < k; j++) {
                a[j] = sc.nextInt();
                isTaken[j] = false;
            }

            boolean rec = rec_pakuj(a, n, 0, isTaken);
            if (!rec) {
                System.out.println("BRAK ");
                continue;
            }

            System.out.print("REC:  " + n + " =");
            for (int j = 0; j < k; j++) {
                if (isTaken[j]) System.out.print(" " + a[j]);
            }
            System.out.print("\nITER: " + n + " =" + iter_pakuj(a, n) + "\n");
        }
    }
}

/* test0.in
 * 5
 * 1
 * 3
 * 2 3 5
 * 10
 * 3
 * 5 5 4
 * 10
 * 4
 * 4 1 4 2
 * 20
 * 3
 * 15 6 1
 * 40
 * 6
 * 10 15 5 8 5 2
 *
 * test0.out
 * BRAK
 * REC:  5 5
 * ITER: 5 5
 * REC:  4 4 2
 * ITER: 4 4 2
 * BRAK
 * REC:  10 15 5 8 2
 * ITER: 10 15 5 8 2
 *
 *  */
