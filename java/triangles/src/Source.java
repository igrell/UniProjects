/* Znajdowanie trojkatow opiera sie na przeszukaniu posortowanej tablicy dwoma wskaznikami, wskazujacymi na pierwszy i drugi mozliwy
 *  element trojkata, i wykorzystaniu przeszukiwania binarnego w celu znalezienia trzeciego odcinka jak najdalej w tablicy.
 *  Po znalezieniu trzeciego elementu wypisujemy trojkaty dla wszystkich trzecich wartosci pomiedzy - wszystkie pasuja, bo
 *  drugi < trzeci_maksymalny.
 */

/* Zlozonosc: n^2 z dwoch petli, po jednej dla kazdego wskaznika, oraz log2(n) wynikajace z przeszukiwania binarnego */

import java.util.Scanner;

public class Source {

    public static Scanner scanner = new Scanner(System.in);

    /* Zamiana dwoch elementow tablicy */
    public static void swap(int[] array, int x, int y) {
        int temp = array[x];
        array[x] = array[y];
        array[y] = temp;
    }

    /* Podzial tablicy do sortowania wzgledem pivota */
    public static int Divide(int[] array, int low, int high) {
        int start = low - 1;
        int pivot = array[high];
        for (int index = low; index <= high; index++) {
            if (array[index] < pivot) {
                start++;
                swap(array, start, index);
            }
        }
        swap(array, start + 1, high);
        return (start + 1);
    }

    /* Sortowanie tablicy */
    public static void QuickSort(int[] array, int low, int high) {
        if (low < high) {
            int division = Divide(array, low, high);
            QuickSort(array, low, division - 1);
            QuickSort(array, division + 1, high);
        }
    }

    public static int BinarySearch(int[] array, int low, int high, int value) {
        int index;
        while (low <= high) {
            index = (low + high) / 2; //ustalamy indeks
            if ((index == (array.length - 1) || array[index + 1] != value) && array[index] == value) { //zakoncz jesli indeks na koncu tablicy lub znaleziono wartosc ORAZ jest to najdalsza dobra wartosc
                return index;
            } else {
                if (array[index] > value) {
                    high = index - 1;
                }
                if (array[index] < value || array[index] == value) { //jesli znaleziono wartosc, ale nie jest ona najdalsza
                    low = index + 1;
                }
            }
        }
        return high; //jesli nie znaleziono wartosci, zwracamy gorny limit
    }


    /* Znajdowanie ilosci trojkatow */
    public static int HowManyTriangles(int[] data) {
        int howMany = 0; //licznik trojek
        for (int first = 0; first < data.length - 2; first++) { //pierwszy odcinek
            for (int second = first + 1; second < data.length - 1; second++) { //drugi odcinek
                int thirdCandidate = data[first] + data[second] - 1; //najwiekszy dobry trzeci odcinek
                int third = BinarySearch(data, second + 1, data.length - 1, thirdCandidate); //szukanie kandydata
                if (third > second && data[first] + data[second] > data[third]) { //jesli znaleziono odcinek ORAZ warunek trojkata spelniony
                    int possibleThird = second + 1;
                    while (possibleThird <= third && howMany < 10) { //przejrzyj wszystkie opcje pomiedzy second a third, wykonaj maks. 10 razy
                        System.out.print("(" + first + "," + second + "," + possibleThird + ") ");
                        howMany++;
                        possibleThird++;
                    }
                    if (howMany >= 10) { //dolicz trojkaty powyzej dziesieciu
                        howMany += third - possibleThird + 1;
                    }
                }
            }
        }
        if (howMany != 0) System.out.print("\n");
        return howMany;
    }


    public static void main(String[] args) {

        /* Input */
        int datasetNo = scanner.nextInt(); //skan ilosci danych
        if (datasetNo < 1 || datasetNo >= 32768) return; //jesli niepoprawna liczba zestawow
        for (int dataset = 0; dataset < datasetNo; dataset++) { // per zestaw danych
            int datasetLength = scanner.nextInt();
            if (datasetLength < 3 || datasetLength > 100) return; //jesli niepoprawna liczba odcinkow
            int[] data = new int[datasetLength];
            for (int i = 0; i < datasetLength; i++) {
                data[i] = scanner.nextInt();
            }
            QuickSort(data, 0, datasetLength - 1); //sortowanie tablicy

            /* Output */
            System.out.printf("%d: n= %d%n", dataset + 1, datasetLength); //pierwsza linia
            int i = 0;
            while (i < datasetLength) { //wypisanie posortowanej tablicy
                for (int j = i; j < i + 25 && j < datasetLength; j++) {
                    if (j + 1 != datasetLength) System.out.print(data[j] + " ");
                    else System.out.print(data[j]);
                }
                i += 25;
                System.out.print("\n");
            }
            int howMany = HowManyTriangles(data); //wypisanie trojkatow
            if (howMany == 0) System.out.println("Triangles cannot be built "); //jesli nie znaleziono trojkatow
            else {
                System.out.println("Number of triangles: " + howMany);
            }


        }

    }


}


/* test0.in
5
51
1 2 3 4 5 6 7 8 9 5 1 2 3 4 5 6 7 8 9 5 1 2 3 4 5 6 7 8 9 5 1 2 3 4 5 6 7 8 9 5 1 2 3 4 5 6 7 8 9 5 1
3
1 1 1
6
1 1 1 1 1 1
10
1 1 1 1 1 1000 1000 1000 1000 1000
11
1 1 1 1 1 1 1 1 1 1 1

* test0.out
1: n= 51
1 1 1 1 1 1 2 2 2 2 2 3 3 3 3 3 4 4 4 4 4 5 5 5 5
5 5 5 5 5 5 6 6 6 6 6 7 7 7 7 7 8 8 8 8 8 9 9 9 9
9
(0,1,2) (0,1,3) (0,1,4) (0,1,5) (0,2,3) (0,2,4) (0,2,5) (0,3,4) (0,3,5) (0,4,5)
Number of triangles: 10625
2: n= 3
1 1 1
(0,1,2)
Number of triangles: 1
3: n= 6
1 1 1 1 1 1
(0,1,2) (0,1,3) (0,1,4) (0,1,5) (0,2,3) (0,2,4) (0,2,5) (0,3,4) (0,3,5) (0,4,5)
Number of triangles: 20
4: n= 10
1 1 1 1 1 1000 1000 1000 1000 1000
(0,1,2) (0,1,3) (0,1,4) (0,2,3) (0,2,4) (0,3,4) (0,5,6) (0,5,7) (0,5,8) (0,5,9)
Number of triangles: 70
5: n= 11
1 1 1 1 1 1 1 1 1 1 1
(0,1,2) (0,1,3) (0,1,4) (0,1,5) (0,1,6) (0,1,7) (0,1,8) (0,1,9) (0,1,10) (0,2,3)
Number of triangles: 165

 */
