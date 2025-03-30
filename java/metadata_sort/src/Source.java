/* Idea Programu
 Program sortuje metadane wczytane do tablicy 2D.
 - funkcja isNumeric rozstrzyga czy dana do posortowania kolumna sklada sie z liczb czy stringow (czas O(1))
 - funkcja compare na podstawie wartosci isNumeric porownuje (sparsowane) int'y badz stringi za pomoca compareTo (czas O(1))
 - funkcja partition dzieli tablice na podstawie schematu Lomuto (czas O(n))
 - funkcja rowSwap dziala jak zwykly Swap, ale zamienia cale wiersze tablicy 2D (czas O(1))
 - funkcja pushColFront wywolywana przed posortowaniem przenosi kolumne wskazana jako ta do posortowania na przod
 tablicy, jednoczesnie zachowujac porzadek pozostalych kolumn (czas O(n))
 - SelectionSort klasyczny (czas O(n^2))
 - QuickSort iteracyjny bez stosu (czas O(nlog(n),O(n^2) gdy uzywamy SelectionSort dla malych podzadan)
 - funkcja printArray drukuje tablice 2D na ekran (czas O(n^2))
*/

import java.util.Scanner;

public class Source {

    public static Scanner sc = new Scanner(System.in);

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean compare(String a, String b, boolean isNumeric, boolean isInverse) {
        if (isNumeric) {
            return isInverse ? Integer.parseInt(a) <= Integer.parseInt(b) : Integer.parseInt(a) >= Integer.parseInt(b);
        } else {
            return isInverse ? a.compareTo(b) <= 0 : a.compareTo(b) >= 0;
        }
    }

    public static int Partition(String[][] values, int low, int high, boolean isNumeric, boolean isInverse) {  //wersja Lomuto
        int i = low - 1;
        for (int j = low; j <= high - 1; j++) {
            if (compare(values[high][0], values[j][0], isNumeric, isInverse)) {
                i++;
                rowSwap(values, i, j);
            }
        }
        rowSwap(values, i + 1, high);
        return i + 1;
    }

    public static void rowSwap(String[][] values, int i, int j) {
        String[] temp = values[i];
        values[i] = values[j];
        values[j] = temp;
    }

    public static void SelectionSort(String[][] values, boolean isNumeric, boolean isInverse, int low, int high) {
        for (int i = low; i < high; i++) {
            int edge = i;
            for (int j = i + 1; j <= high; j++) {
                if (compare(values[edge][0], values[j][0], isNumeric, isInverse)) {
                    edge = j;
                }
            }
            rowSwap(values, i, edge);
        }
    }

    public static void QuickSort(String[][] values, boolean isInverse) {
        int i = 0; //'i' symuluje glebokosc stosu
        int low = 1; //od 1, poniewaz nie interesuje nas naglowek
        int high = values.length - 1;
        boolean isNumeric = isNumeric(values[1][0]);
        while (true) {
            if (low >= high && i <= 0) break; //jesli 'stos' pusty
            if (high > low) { //jest obszar do sprawdzenia
                if (high - low <= 5) { //podzadanie mniejsze niz 5
                    SelectionSort(values, isNumeric, isInverse, low, high);
                    low = high + 1; //edge-case
                    while (high < values.length) { // szukamy oznaczonej wartosci do konca tablicy
                        if (values[high][0].charAt(values[high][0].length() - 1) == '!') { //znaleziono wartosc ozn. '!' high jest teraz na tej pozycji
                            values[high][0] = values[high][0].substring(0, values[high][0].length() - 1); //usuwamy '!'
                            break;
                        } else { //nie znaleziono - szukamy dalej
                            high++;
                        }
                    }
                    continue;
                }
                int pivot = Partition(values, low, high, isNumeric, isInverse);
                values[high][0] += "!"; //ozn. poczatek fragmentu do sprawdzenia pozniej przez '!'
                high = pivot - 1; //ozn. gorna granice podzadania po lewej przez high
                i++; //symuluje stack.push
            } else {
                low = high + 2; //przesun low na pierwsza mozliwa dobra pozycje
                while (high < values.length) { // szukamy oznaczonej wartosci do konca tablicy
                    if (values[high][0].charAt(values[high][0].length() - 1) == '!') { //znaleziono wartosc ozn. '!' high jest teraz na tej pozycji
                        values[high][0] = values[high][0].substring(0, values[high][0].length() - 1); //usuwamy '!'
                        break;
                    } else { //nie znaleziono - szukamy dalej
                        high++;
                    }
                }
                i--; //symuluje stack.pop
            }
        }
    }

    public static void pushColFront(String[][] array, int priority) {
        for (int row = 0; row < array.length; row++) {
            String temp = array[row][priority];
            System.arraycopy(array[row], 0, array[row], 1, priority);
            array[row][0] = temp;
        }
    }

    public static void printArray(String[][] values) {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length - 1; j++) {
                System.out.print(values[i][j] + ",");
            }
            System.out.print(values[i][values[0].length - 1] + "\n");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int datasetNo = Integer.parseInt(sc.nextLine());
        for (int dataset = 0; dataset < datasetNo; dataset++) {
            String[] data = sc.nextLine().split(",");
            int rowNo = Integer.parseInt(data[0]);
            int sortPriority = Integer.parseInt(data[1]) - 1;
            int order = Integer.parseInt(data[2]);
            String[][] values = new String[rowNo + 1][];
            String[] header = sc.nextLine().split(",");
            values[0] = header;
            for (int row = 0; row < rowNo; row++) {
                values[row + 1] = sc.nextLine().split(",");
            }
            pushColFront(values, sortPriority);
            QuickSort(values, order == -1);
//            SelectionSort(values,isNumeric(values[1][0]),order == -1 , 1, values.length - 1);
            printArray(values);
        }
    }
}

/* test0.in
5
1,1,1
Album,Artist,Year,Songs,Length
An Awesome Wave,Alt J,2012,14,43
2,1,-1
Album,Artist,Year,Songs,Length
An Awesome Wave,Alt J,2012,14,43
Relaxer,Band Alt J,2017,8,39
2,2,1
Album,Artist,Year,Songs,Length
An Awesome Wave,Alt J,2012,14,43
Relaxer,Band Alt J,2017,8,39
3,5,1
Album,Artist,Year,Songs,Length
This Is All Yours,Super Band Alt J,2014,14,55
An Awesome Wave,Alt J,2012,14,43
Relaxer,Band Alt J,2017,8,39
10,2,1
A,B
a1,1
a2,3
a3,2
a4,5
a5,4
a6,7
a7,6
a8,9
a9,8
a10,10

* test0.out
Album,Artist,Year,Songs,Length
An Awesome Wave,Alt J,2012,14,43

Album,Artist,Year,Songs,Length
Relaxer,Band Alt J,2017,8,39
An Awesome Wave,Alt J,2012,14,43

Artist,Album,Year,Songs,Length
Alt J,An Awesome Wave,2012,14,43
Band Alt J,Relaxer,2017,8,39

Length,Album,Artist,Year,Songs
39,Relaxer,Band Alt J,2017,8
43,An Awesome Wave,Alt J,2012,14
55,This Is All Yours,Super Band Alt J,2014,14

B,A
1,a1
2,a3
3,a2
4,a5
5,a4
6,a7
7,a6
8,a9
9,a8
10,a10

* */
