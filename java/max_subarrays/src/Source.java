/*
 * Idea programu - przeniesienie algorytmu Kadane na tablice 2D.
 * Dla kazdego zestawu przechodzimy po podzbiorach tablicy kolumnowo, dla kazdego podzbioru znajdujac najlepsza podtablice algorytmem
 * Kadane. Pod koniec jednej iteracji, jesli wynik jest do tej pory najlepszy, nadpisujemy nim wynik ostateczny (s), oraz aktualizujemy
 * koordynaty prostokata, ktory jest kandydatem do wyniku. Powtarzajac ten proces dla podzbiorow tablicy 2D znajdujemy maksymalna spojna podtablice.
 */


import java.util.Scanner;

class Source {
    public static Scanner scanner = new Scanner(System.in); //tworzenie skanera
    public static int minimum = -327680001; //-32768 * 100 * 100 - 1
    public static int maximum = 10001;


    /* Algorytm Kadane 1D */
    public static int Kadane(int[] array, int[] limits) {
        int max = minimum; //zacznij od najmniejszego mozliwego wyniku
        int minSize = maximum;
        int max_temp = 0;
        int left = 0;
        for (int right = 0; right < array.length; right++) {
            max_temp += array[right]; //sumowanie wyrazow tablicy
            int size = (right - left + 1);
            if (max < max_temp) { //znaleziono kandydata na wynik
                max = max_temp; //nadpisz wynik
                limits[0] = left; //info gdzie wynik- top
                limits[1] = right; //info gdzie wynik- bottom
            }
            if (max == max_temp && minSize > size) {
                minSize = size; //nadpisanie rozmiaru
                limits[0] = left; //info gdzie wynik- top
                limits[1] = right; //info gdzie wynik- bottom
            }
            if (max_temp <= 0) { //jesli ujemny odcinek
                max_temp = 0;
                left = right + 1; //koord startu moga byc co najmniej wieksze od i
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int arrays = scanner.nextInt(); //wczytaj ilosc tablic
        for (int nz = 1; nz <= arrays; nz++) { //per ilosc tablic
            /* Input jednej tablicy 2D */
            if (scanner.nextInt() != nz) return; //czy odp. numer tablicy
            String syntax = scanner.next(); //wczytaj ':'
            if (syntax.charAt(0) != ':') return; //jesli zla skladnia inputu
            int row = scanner.nextInt(); //wczytaj rzad
            int col = scanner.nextInt(); //wczytaj kolumne
            int[][] array = new int[row][col]; //utworz tablice
            for (int j = 0; j < row; j++) {
                for (int k = 0; k < col; k++) {
                    array[j][k] = scanner.nextInt(); //wypelnij tablice z inputu
                }
            }
            /* szukanie najmniejszej podtablicy - algorytm Kadane 2D */
            int maxSum = minimum; //zacznij od najmniejszego mozliwego wyniku
            int minSize = maximum;
            int[] limits = new int[2]; //indeksy poczatku i konca, gora i dol
            int resLeft = 0, resRight = 0; //ostateczne lewo prawo
            int resTop = 0, resBottom = 0; //ostateczne gora dol

            //przejscie po podzbiorach tablicy
            for (int left = 0; left < col; left++) {
                int[] temp = new int[row]; //tablica pomocnicza
                for (int right = left; right < col; right++) {
                    for (int j = 0; j < row; j++) {
                        temp[j] += array[j][right]; //sumowanie wyrazow w kolumnie
                    }
                    int sum = Kadane(temp, limits); //Kadane dla podzbioru
                    int size = (right - left + 1) * (limits[1] - limits[0] + 1);
                    if (maxSum < sum) { //jesli znaleziono lepszego kandydata na wynik
                        maxSum = sum; //nadpisz wynik;
                        minSize = size; //nadpisanie rozmiaru (suma nadrzedna wzgl. rozmiaru)
                        //popraw koordynaty
                        resLeft = left;
                        resRight = right;
                        resTop = limits[0];
                        resBottom = limits[1];
                    }
                    if (maxSum == sum && minSize > size) {
                        minSize = size; //nadpisanie rozmiaru
                        //popraw koordynaty
                        resLeft = left;
                        resRight = right;
                        resTop = limits[0];
                        resBottom = limits[1];
                    }
                    if (maxSum == sum && minSize == size) { //porownanie 1 indeksu
                        if (left < resLeft) {
                            //popraw koordynaty
                            resLeft = left;
                            resRight = right;
                            resTop = limits[0];
                            resBottom = limits[1];
                        } else if (right < resRight) { //porownanie 2 indeksu
                            resLeft = left;
                            resRight = right;
                            resTop = limits[0];
                            resBottom = limits[1];
                        } else if (limits[0] < resTop) { //porownanie 3 indeksu
                            resLeft = left;
                            resRight = right;
                            resTop = limits[0];
                            resBottom = limits[1];
                        } else if (limits[1] < resBottom) { //porownanie 4 indeksu
                            resLeft = left;
                            resRight = right;
                            resTop = limits[0];
                            resBottom = limits[1];
                        }
                    }
                }
            }

            if (maxSum < 0) //jesli w tablicy zadnego dodatniego wyrazu (s == 0)
                System.out.println(String.format("%d: n = %d m = %d, s = 0, mst is empty", nz, row, col));
            else //jesli ms != 0
                System.out.println(String.format("%d: n = %d m = %d, s = %d, mst = a[%d..%d][%d..%d]", nz, row, col, maxSum, resTop, resBottom, resLeft, resRight));

        }
    }
}

/* test0.in
10
1 : 1 6
0 0 0 0 1 0
2 : 2 6
0 1 0 0 1 0
0 0 0 0 0 0
3 : 2 5
1 0 1 0 1
-1 0 -1 0 -1
4 : 2 5
1 -1 1 -1 1
-1 1 -1 1 -1
5 : 1 1
0
6 : 1 7
0 0 0 0 0 0 0
7 : 1 3
-1 -2 -3
8 : 4 4
2 -10 -10 -10
-10 1 -10 -10
-10 -10 3 -10
-10 -10 -10 7
9 : 4 4
0 0 0 1
0 0 1 0
0 1 0 0
1 0 0 0
10 : 5 1
0
0
0
1
0

 * test0.out
1: n = 1 m = 6, s = 1, mst = a[0..0][4..4]
2: n = 2 m = 6, s = 2, mst = a[0..0][1..4]
3: n = 2 m = 5, s = 3, mst = a[0..0][0..4]
4: n = 2 m = 5, s = 1, mst = a[0..0][0..0]
5: n = 1 m = 1, s = 0, mst = a[0..0][0..0]
6: n = 1 m = 7, s = 0, mst = a[0..0][0..0]
7: n = 1 m = 3, s = 0, mst is empty
8: n = 4 m = 4, s = 7, mst = a[3..3][3..3]
9: n = 4 m = 4, s = 4, mst = a[0..3][0..3]
10: n = 5 m = 1, s = 1, mst = a[3..3][0..0]
 */
