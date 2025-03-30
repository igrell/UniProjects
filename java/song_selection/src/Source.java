    /* Idea programu
    Program wyszukuje k-ty najmniejszy element w nieposortowanej tablicy poprzez modyfikacje algorytmu "magiczne piatki".
    Zbior dzielony jest na 5-elementowe podzbiory oraz zbior z reszta mniejsza od 5. Z kazdego z tych jest wyznaczana mediana,
    ktore to zamieniamy z elementami na poczatku tablicy. Wyciagamy mediane z tych median i wyniku uzywamy jako pivota do
    partition (tu wersja Lomuto'a). Jesli trafilismy na odpowiedni indeks (bo pivot po partition bedzie mial dobry indeks rel.
    do swojej wielkosci w tablicy), konczymy. Jesli nie, przeszukujemy rekurencyjnie lewa lub prawa czesc tablicy.

    Czas: O(n) - partition w O(n), petla w magicFives O(n/5) = O(n); selectionSort O(n^2), ale tutaj zawsze n <= 5, a wiec
    tak naprawde zlozonosc stala.

    Miejsce: O(1) - cala informacja przechowywana jest na tej samej tablicy lub jest definiowana na pojedynczych zmiennych.
    * */


    import java.util.Scanner;

    public class Source {
        public static Scanner sc = new Scanner(System.in);

        public static void swap(int[] array, int a, int b) {
            int temp = array[a];
            array[a] = array[b];
            array[b] = temp;
        }

        public static int findMedian(int[] arr, int low, int length) {
            selectionSort(arr, low, length); //sort. (czas T(n) <= 25 bo zawsze sortujemy maks. 5 elementow)
            return (length / 2);
        }

        public static void selectionSort(int[] arr, int low, int high) {
            for (int i = low; i < high - 1; i++) {
                int min = i;
                for (int j = i + 1; j < high; j++)
                    if (arr[j] < arr[min]) {
                        min = j;
                    }
                swap(arr, i, min);
            }
        }

        static int partition(int[] arr, int low, int high, int pivot) { //wersja Lomuto; czas O(n)
            int i = low;
            while (i < high && arr[i] != pivot) i++;
            swap(arr, i, high);
            i = low;
            int j = low;
            while (j < high) {
                if (arr[j] <= pivot) {
                    swap(arr, i, j);
                    i++;
                }
                j++;
            }
            swap(arr, high, i);
            return i;
        }

        public static int magicFives(int[] arr, int low, int high, int key) {
            int length = high - low + 1; //dlugosc badanego odcinka
            if (key < 1 || key > length) return -1; //niewlasciwy klucz

            int i; //iterator przejscia po podzbiorach
            int medianNo = 0; //ilosc median na poczatku

            for (i = 0; i < length / 5; i++) { //dla kazdego 5-elementowego podzbioru
                int median = findMedian(arr, (5 * i) + low, 5) + low; //wyznaczanie mediany w podzbiorze
                swap(arr, low + medianNo, median); //swap mediany z ostatnim nie-medianowym el. z poczatku; zwieksz iterator
                medianNo++;
            }

            if ((5 * i) < length) { //obsluzeni e koncowki
                int median = findMedian(arr, 5 * i + low, length % 5) + low; //wyzn. mediany w koncowce
                swap(arr, low + medianNo, median); //swap, analogicznie
                i++;
                medianNo++;
            }

            int medianOfMedians = (i == 1) ? arr[low] : magicFives(arr, high - medianNo, high, i / 2); //rekurencyjnie wyzn. mediane od poczatku do iteratora

            int supposedResult = partition(arr, low, high, medianOfMedians); //partition wzgl. mediany median

            if ((supposedResult + 1) == low + key) return arr[supposedResult]; //znaleziono rozwiazanie
            if ((supposedResult + 1) > low + key) {
                return magicFives(arr, low, supposedResult - 1, key); //odpowiedz za duza; sprawdz lewa czesc tablicy
            }
            return magicFives(arr, supposedResult + 1, high, key + low - supposedResult - 1); //odp. za mala; sprawdz prawa czesc tablicy
        }

        public static void main(String[] args) {
            int datasetNo = sc.nextInt();
            for (int dataset = 0; dataset < datasetNo; dataset++) {
                int arrayLength = sc.nextInt();
                int[] array = new int[arrayLength];
                for (int i = 0; i < arrayLength; i++) {
                    array[i] = sc.nextInt();
                }
                int questionNo = sc.nextInt();
                for (int i = 0; i < questionNo; i++) {
                    int key = sc.nextInt();
                    int res = magicFives(array, 0, arrayLength - 1, key);
                    if (res == -1) System.out.println(key + " brak");
                    else System.out.println(key + " " + res);
                }
            }
        }
    }

    /* test0.in
    3
    5
    1 2 3 4 5
    3
    1 2 3
    5
    5 3 4 4 3
    5
    2 5 1 3 4
    10
    1 1 1 1 1 1 1 1 1 1
    5
    1 10 0 -1 11

    * test0.out
    1 1
    2 2
    3 3
    2 3
    5 5
    1 3
    3 4
    4 4
    1 1
    10 1
    0 brak
    -1 brak
    11 brak
    * */
