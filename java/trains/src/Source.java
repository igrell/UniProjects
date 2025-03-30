/* Idea programu
 * Operacje na pociagach opieraja sie na implementacji listy (jednokierunkowej, TrainList) list (dwukierunkowej, wiazanej, CarList), przede
 * wszystkim na 'przepinaniu' referencji w celu odpowiedniego ulozenia wagonow wzgledem siebie.
 * Wykorzystano funkcje pomocnicze opierajace sie o znajdowanie wagonow:
 * - search (szuka podanego stringiem wagonu w pociagu),
 * - remove (usuwa podany pociag z listy pociagow)
 * Ponadto uzyto funkcji swap, przepinajacej referencje next i prev dla zadanego wagonu Car toSwap.
 * */

import java.util.Scanner;

class Car { //wagon (Node)
    String name;
    Car prev = this;
    Car next = this;

    Car(String name) {
        this.name = name;
    }

}

class CarList { //wagony pociagu; lista cykliczna
    Car carHead;

    CarList(Car W) {
        carHead = W;
    }
}

class Train {
    String name;
    CarList carlist;
    Car first;
    Train next = null;


    Train() {
    }

    Train(String T1, String W) {
        name = T1;
        first = new Car(W);
        carlist = new CarList(first);
    }

}

class TrainList {
    Train trains;

    TrainList() {
        trains = new Train();
    }


    public Train search(String name) {
        Train temp = trains;
        while (temp.next != null) {
            temp = temp.next;
            if (temp.name.equals(name)) {
                return temp;
            }
        }
        return new Train();
    }

    public void remove(Train T) {
        Train temp = trains;
        if (temp.next == null) return;
        while (temp.next != T) {
            temp = temp.next;
        }
        temp.next = T.next;
        T.next = null;
    }

    public void swap(Car toSwap) {
        Car temp = toSwap.next;
        toSwap.next = toSwap.prev;
        toSwap.prev = temp;
    }

    public void InsertFirst(String t, String W) {
        Train T = search(t);
        if (T.name == null) {
            System.out.println("Train " + t + " does not exist");
            return;
        }
        Car newCar = new Car(W);
        newCar.next = T.first;
        newCar.prev = T.first.prev;

        if (T.first.prev == T.first) {
            T.first.prev = newCar;
            T.first.next = newCar;

        } else {
            if (T.first.prev.next == T.first) { // last nieodwrocony
                T.first.prev.next = newCar;
            } else {
                T.first.prev.prev = newCar;
            }
            T.first.prev = newCar;
        }

        T.first = newCar;
    }

    public void InsertLast(String t, String W) {
        Train T = search(t);
        if (T.name == null) {
            System.out.println("Train " + t + " does not exist");
            return;
        }


        Car newCar = new Car(W);
        newCar.prev = T.first.prev;
        newCar.next = T.first;

        if (T.first.prev == T.first) {
            T.first.prev = newCar;
            T.first.next = newCar;
        } else {
            if (T.first.prev.next == T.first) { // last nieodwrocony
                T.first.prev.next = newCar;
            } else {
                T.first.prev.prev = newCar;
            }
            T.first.prev = newCar;
        }


    }


    public void Display(Train T, String input) {
        if (search(T.name).name == null) {
            System.out.println("Train " + input + " does not exist");
            return;
        }
        Car temp = T.first;
        System.out.print(T.name + ": ");
        while (temp.next != T.first) {
            Car another1 = temp.next;
            if (another1.prev != temp) {
                swap(another1);
            }
            System.out.print(temp.name + " ");
            temp = temp.next;
        }
        System.out.print(temp.name + "\n");
    }

    public void New(Train newTrain) {
        if (search(newTrain.name).name != null) {
            System.out.println("Train " + newTrain.name + " already exists");
            return;
        }
        newTrain.next = trains.next;
        trains.next = newTrain;
    }

    public void Trains() {
        System.out.print("Trains: ");
        if (trains.next == null) { //jesli 0 pociagow
            System.out.print("\n");
            return;
        }
        Train temp = trains.next;
        System.out.print(temp.name);
        temp = temp.next;
        while (temp != null) {
            System.out.print(" " + temp.name);
            temp = temp.next;
        }
        System.out.print("\n");
    }

    public void Union(String t1, String t2) {
        Train T1 = search(t1);
        Train T2 = search(t2);
        if (T1.name == null){
            System.out.println("Train " + t1 + " does not exist");
            return;
        }
        if (T2.name == null){
            System.out.println("Train " + t2 + " does not exist");
            return;
        }

        Car t1Last = T1.first.prev;
        Car t2Last = T2.first.prev;

        if (t1Last.next == T1.first) { //t1Last nieodwrocony
            t1Last.next = T2.first;
        } else {
            t1Last.prev = T2.first;
        }
        if (t2Last.next == T2.first) { //t2Last nieodwrocony
            t2Last.next = T1.first;
        } else {
            t2Last.prev = T1.first;
        }
        T2.first.prev = t1Last;
        T1.first.prev = t2Last;

        remove(T2);
    }

    public void DelFirst(String t1, String t2) {
        Train T1 = search(t1);
        Train T2 = search(t2);
        if (search(T1.name).name == null) {
            System.out.println("Train " + T1.name + " does not exist");
            return;
        }
        if (search(T2.name).name != null) {
            System.out.println("Train " + T2.name + " already exists");
            return;
        }
        String temp = T1.first.name;
        if (T1.first.prev == T1.first && T1.first.next == T1.first) { //jednowagonowy pociag
            remove(T1);
            New(new Train(t2, temp));
            return;
        }


        Car last = T1.first.prev;
        Car postFirst = T1.first.next;

        if (last.next == T1.first) { //last nieodwrocony
            last.next = postFirst;
        } else { //last odwrocony
            last.prev = postFirst;
        }
        if (postFirst.prev == T1.first) { //postFirst nieodwrocony
            postFirst.prev = last;
        } else { //postFirst odwrocony
            postFirst.next = last;
            swap(postFirst);
        }

        T1.first = T1.first.next;

        New(new Train(t2, temp));

    }

    public void DelLast(String t1, String t2) {
        Train T1 = search(t1);
        Train T2 = search(t2);
        if (search(T1.name).name == null) {
            System.out.println("Train " + t1 + " does not exist");
            return;
        }
        if (search(T2.name).name != null) {
            System.out.println("Train " + t2 + " already exists");
            return;
        }
        String temp = T1.first.prev.name;
        if (T1.first.prev == T1.first && T1.first.next == T1.first) { //jednowagonowy pociag
            remove(T1);
            New(new Train(t2, temp));
            return;
        }

        Car preLast;
        if (T1.first.prev.next == T1.first) {
            preLast = T1.first.prev.prev;
        } else {
            preLast = T1.first.prev.next;
        }

        if (preLast.next == T1.first.prev) {
            preLast.next = T1.first;
        } else {
            preLast.prev = T1.first;
        }
        T1.first.prev = preLast;


        New(new Train(t2, temp));
    }

    public void Reverse(String t) {
        Train T = search(t);
        if (T.name == null) {
            System.out.println("Train " + t + " does not exist");
            return;
        }
        Car temp = T.first.prev;
        swap(T.first);
        swap(T.first.next);
        T.first = temp;
    }
}


public class Source {

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        /* Input */
        int z = scanner.nextInt();
        for (int i = 0; i < z; i++) {
            int n = scanner.nextInt();
            if (n < 1 || n > 1000000) return;
            TrainList trainlist = new TrainList();
            for (int j = 0; j < n; j++) {
                String command = scanner.next();
                if (command.equals("New")) {
                    String T1 = scanner.next();
                    String W = scanner.next();
                    Train T = new Train(T1, W);
                    trainlist.New(T);
                }
                if (command.equals("InsertFirst")) {
                    String T1 = scanner.next();
                    String W = scanner.next();
                    trainlist.InsertFirst(T1, W);
                }
                if (command.equals("InsertLast")) {
                    String T1 = scanner.next();
                    String W = scanner.next();
                    trainlist.InsertLast(T1, W);
                }
                if (command.equals("Display")) {
                    String T1 = scanner.next();
                    trainlist.Display(trainlist.search(T1), T1);
                }
                if (command.equals("Trains")) {
                    trainlist.Trains();
                }
                if (command.equals("Reverse")) {
                    String T1 = scanner.next();
                    trainlist.Reverse(T1);
                }
                if (command.equals("Union")) {
                    String T1 = scanner.next();
                    String T2 = scanner.next();
                    trainlist.Union(T1,T2);
                }
                if (command.equals("DelFirst")) {
                    String T1 = scanner.next();
                    String T2 = scanner.next();
                    trainlist.DelFirst(T1, T2);
                }
                if (command.equals("DelLast")) {
                    String T1 = scanner.next();
                    String T2 = scanner.next();
                    trainlist.DelLast(T1, T2);
                }

            }
        }
    }
}

/* test1.in
1
15
New T2 W5
InsertLast T2 W4
InsertLast T2 W3
InsertLast T2 W2
InsertLast T2 W1
Display T2
DelLast T2 T5
DelLast T2 T4
DelLast T2 T3
DelLast T2 T1
Display T1
Display T3
Display T4
Display T5
Display T2

* test1.out
T2: W5 W4 W3 W2 W1
T1: W4
T3: W3
T4: W2
T5: W1
T2: W5
* */
