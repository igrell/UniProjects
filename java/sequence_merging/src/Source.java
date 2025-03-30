/* Idea programu
Program ma scalac posortowane ciagi. W tym celu definiuje sie minimalny kopiec. Dane zbiera sie w tablicy 2D.
Bierzemy po pierwszym (najmniejszym) elemencie z kazdej tablicy. Tworzymy z nich kopiec- a wiec porownujemy ze soba i jako
korzen dostajemy najmniejszy z nich. Ten wstawiamy i sprawdzamy, czy w tablicy z ktorej pochodzil element sa kolejne, a
jesli tak, to czy nadal sa na tyle male, zeby byc korzeniem kopca. Jesli tak to je wstawiamy, jesli nie- ustawiamy jako
wartosc korzenia bardzo duza liczbe (Integer.MAX_VALUE), ktora w wyniku funkcji applyHeapCondition zostanie przerzucona
na dol kopca. Proces powtarzamy dla wszystkich elementow wszystkich tablic, na poczatku kazdej iteracji dodajac wartosc
korzenia do outputu.

Zlozonosc czasowa opisana w linii 107; operacje inne niz petla for w funkcji merge albo w niej wystepuja, albo maja
zlozonosc < O(n*m*log(n))
 */

import java.util.Scanner;

//wezel w kopcu tablicowym
class Node {
    int val; //wartosc
    int index; //indeks wartosci w tablicy
    int next; //nastepny indeks do sprawdzenia

    Node(int val, int index, int next) {
        this.val = val;
        this.index = index;
        this.next = next;
    }
}

//kopiec tablicowy w wersji min
class Heap {
    int currSize;
    Node[] array;

    //konstruktor tworzy kopiec z tablicy wezlow
    public Heap(int currSize, Node[] array) {
        this.currSize = currSize;
        this.array = array;
        for (int i = (currSize - 1) / 2; i >= 0; i--) //petla porzadkuje strukture do warunku kopca
            applyHeapCondition(i);
    }

    //swap wezlow
    public static void swap(Node[] array, int a, int b) {
        Node temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    //modyfikuje strukture, tak, aby miala spelniony warunek kopca minimalnego
    //zlozonosc czasowa:
    public void applyHeapCondition(int i) {
        int minimalIndex = i; //pierwotny najmniejszy (jesli takim pozostanie to nie trzeba nic zmieniac)
        int leftChild = leftChild(i);
        int rightChild = rightChild(i);
        if (leftChild < currSize && array[leftChild].val < array[i].val) { //lewe dziecko w zlym miejscu, trzeba zamienic
            minimalIndex = leftChild;
        }
        if (rightChild < currSize && array[rightChild].val < array[minimalIndex].val) { //prawe dziecko w zly miejscu; zamien z obecnym najmniejszym
            minimalIndex = rightChild;
        }
        if (minimalIndex != i) { //jesli minimalIndex zostal zmodyfikowany (a wiec jesli ktorykolwiek z dzieci do zamiany)
            swap(array, minimalIndex, i);
            applyHeapCondition(minimalIndex); //sprawdz rekurencyjnie warunek kopca dla podkopca
        }
    }

    //zwraca korzen
    public Node getRoot() {
        return (currSize > 0) ? array[0] : null;
    }

    //zamienia korzen na nowy i wykonuje warunek kopca
    public void setRoot(Node node) {
        array[0] = node;
        applyHeapCondition(0); //porzadkuje caly kopiec
    }

    //zwraca lewe dziecko danego indeksu (przesuniecie o jeden poziom w dol)
    public int leftChild(int i) {
        return ((2 * i) + 1);
    }

    //zwraca prawe dziecko danego indeksu (przesuniecie o jeden poziom w dol + 1 w prawo)
    public int rightChild(int i) {
        return ((2 * i) + 2);
    }

    //scala posortowane tablice
    public static int[] merge(int[][] arrays) {
        Node[] tempArr = new Node[arrays.length]; //tablica wezlow do kopca
        int heapSize = 0; //zmienna do sumowania komorek wszystkich tablic

        for (int i = 0; i < arrays.length; i++) { //petla pobiera po pierwszym el. z kazdej tablicy i zlicza sume el.
            heapSize += arrays[i].length;
            tempArr[i] = new Node(arrays[i][0], i, 1); //next = 1, poniewaz mamy juz element zerowy- kolejny bedzie pierwszy
        }

        Heap heap = new Heap(arrays.length, tempArr); //utworzenie kopca wielkosci takiej jak ilosc tablic
        int[] res = new int[heapSize]; //inicjalizacja tablicy z rozwiazaniem

        //heapSize = n*m, a dla kazdej iteracji petli wykonuje sie applyHeapCond o czasie O(log n), a wiec
        //razem zlozonosc czasowa = O(n*m*log(n))

        for (int i = 0; i < heapSize; i++) { //przejscie po wszystkich elementach w celu znalezienia rozwiazania
            Node root = heap.getRoot(); //root to najmniejszy element jeszcze niewrzucony do res, a wiec kolejny do wrzucenia
            res[i] = root.val; //wrzucamy najmniejszy el. do res

            if (root.next < arrays[root.index].length) { //kolejny element w tablicy bylego roota nadaje sie na korzen kopca
                root.val = arrays[root.index][root.next]; //wstawiamy element jako wartosc korzenia
                root.next++; //nastepnym razem sprawdzimy kolejny element tego samego ciagu
            } else { //element w sprawdzanej tablicy nie pasuje na korzen kopca
                root.val = Integer.MAX_VALUE; //to spowoduje wywalenie roota na dol kopca- bo jest minimalny
            }
            heap.setRoot(root); //cokolwiek wyszlo- ustaw jako korzen; tutaj tez nastepuje applyHeapCond
        }
        return res;
    }
}


public class Source {

    public static Scanner sc = new Scanner(System.in);


    public static void printArray(int[] array) {
        for (int el : array) System.out.print(el + " ");
        System.out.println();
    }

    public static void main(String[] args) {
        /* Input */
        int datasetsNo = sc.nextInt();
        for (int dataset = 0; dataset < datasetsNo; dataset++) {
            int seqencesNo = sc.nextInt();
            int[][] sequences = new int[seqencesNo][];
            int[] sequencesLengths = new int[seqencesNo];
            for (int sequence = 0; sequence < seqencesNo; sequence++) {
                sequencesLengths[sequence] = sc.nextInt();
            }
            for (int i = 0; i < seqencesNo; i++) {
                sequences[i] = new int[sequencesLengths[i]];
                for (int j = 0; j < sequencesLengths[i]; j++) {
                    sequences[i][j] = sc.nextInt();
                }
            } //koniec inputu

            int[] res = Heap.merge(sequences); //scal, zapisz do zmiennej
            printArray(res);
        }

    }
}

/* test0.in
3
1
5
1 1 1 1 1
5
5 4 3 2 1
6 7 8 9 10
34 35 36 37
69 420 2137
1 2
11
5
5 5 5 5 5
7 7 7 7 7
5 5 5 5 5
3 3 3 3 3
2 2 2 2 2
1 1 1 1 1

test0.out
1 1 1 1 1
1 2 6 7 8 9 10 11 34 35 36 37 69 420 2137
1 1 1 1 1 2 2 2 2 2 3 3 3 3 3 5 5 5 5 5 7 7 7 7 7
 */
