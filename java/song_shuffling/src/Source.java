/* Idea programu
Program tasuje piosenki za pomoca algorytmu QuickSort. Dodatkowo, w funkcji sortujacej wyszukiwany jest najwiekszy
wspolny prefiks wszystkich utworow podanych w tablicy typu String. Algorytm dziala na zasadzie dziel i zwyciezaj
zamieniajac 'srodkowe' partie tablicy rekurencyjnie az do uzyskania zadanej kolejnosci. Rozwazone sa przypadki gdy dlu-
gosc tablicy jest parzysta lub nieparzysta, a takze gdy tablica po podzieleniu bedzie parzysta lub nieparzysta.
*/

import java.util.Scanner;

public class Source {

    public static Scanner sc = new Scanner(System.in);

    public static String SongSort(String[] songs, int low, int high) {
        if (high - low == 0) return songs[0];
        if (high - low == 1) {
            String left = songs[low];
            String right = songs[high];
            int minLength = Math.min(left.length(), right.length());
            for (int i = 0; i < minLength; i++) {
                if (left.charAt(i) != right.charAt(i)) return left.substring(0, i);
            }
            return left.substring(0, minLength);
        }
        if (((high - low + 1) / 2) % 2 == 0) { //kiedy dlugosc tablicy = wielok. 2
            int pivot = ((low + high - 1) / 2);
            int first = ((low + pivot) / 2);
            int second = pivot + 1;
            for (int i = first + 1; i <= pivot; i++) {
                String temp = songs[i];
                songs[i] = songs[second];
                songs[second++] = temp;
            }
            String left = SongSort(songs, low, pivot);
            String right = SongSort(songs, pivot + 1, high);
            int minLength = Math.min(left.length(), right.length());
            for (int i = 0; i < minLength; i++) {
                if (left.charAt(i) != right.charAt(i)) return left.substring(0, i);
            }
            return left.substring(0, minLength);
        } else {
            int pivot = ((low + high - 1) / 2) - 1;
            int first = ((low + pivot) / 2);
            int second = pivot + 1;
            String temp = songs[second];
            if (high - second - 1 >= 0) {
                System.arraycopy(songs, second + 1, songs, second, high - second - 1);
            }
            songs[high - 1] = temp;
            for (int i = first + 1; i <= pivot; i++) {
                temp = songs[i];
                songs[i] = songs[second];
                songs[second++] = temp;
            }
            String left = SongSort(songs, low, pivot);
            String right = SongSort(songs, pivot + 1, high - 2);
            String lastLeft = songs[high - 1];
            String lastRight = songs[high];
            int minLength = Math.min(lastLeft.length(),lastRight.length());
            String last = lastLeft.substring(0,minLength); //najlepszy prefiks z dwoch ostatnich
            for(int i = 0 ; i < minLength ; i++) {
                if (lastLeft.charAt(i) != lastRight.charAt(i))  {
                    last = lastLeft.substring(0,i);
                    break;
                }
            }
            if(last.equals("")) return "";
            minLength = Math.min(minLength,Math.min(right.length(),left.length()));
            for (int i = 0; i < minLength; i++) {
                if (left.charAt(i) != right.charAt(i) || right.charAt(i) != last.charAt(i) || left.charAt(i) != last.charAt(i)) return left.substring(0, i);
            }
            return left.substring(0, minLength);
        }
    }

    public static void main(String[] args) {
        /* Input */
        int datasetNo = sc.nextInt();
        for (int i = 0; i < datasetNo; i++) {
            int songsNo = sc.nextInt();
            if (songsNo == 0) continue;
            String[] songs = new String[songsNo];
            for (int j = 0; j < songsNo; j++) {
                songs[j] = sc.next();
            }
            String common = "";
            if (songsNo % 2 == 1 && songsNo != 1) {
                int tempId = (songsNo - 1) / 2;
                String temp = songs[tempId];


                if (songsNo - 1 - tempId >= 0) {
                    System.arraycopy(songs, tempId + 1, songs, tempId, songsNo - 1 - tempId);
                    songs[songsNo - 1] = temp;
                    common = SongSort(songs, 0, songsNo - 2);
                }
            } else {
                common = SongSort(songs, 0, songsNo - 1);
            }
            for (int j = 0; j < songsNo - 1; j++) {
                System.out.print(songs[j] + " ");
            }
            System.out.print(songs[songsNo - 1] + "\n" + common + "\n");
        }
    }
}

/* test0.in
6
6
a1 a2 a3 b1 b2 b3
5
AltJFitzpleasure AltJSthGood AltJTaro ArtTatumMyIdeal ArtTatumTigerRag
1
JanacekSimfonietta
8
aa1 aa2 aa3 aa4 aa5 aa6 aa7 aa8
10
t tt ttt tttt ttttt ttttt tttt ttt tt t
4
ChetBakerButNotForMe ChetBakerBuddy ChetFakerDropTheGame ChetFakerWhatAboutUs
* test0.out
a1 b1 a2 b2 a3 b3

AltJFitzpleasure ArtTatumMyIdeal AltJSthGood ArtTatumTigerRag AltJTaro
A
JanacekSimfonietta
JanacekSimfonietta
aa1 aa5 aa2 aa6 aa3 aa7 aa4 aa8
aa
t ttttt tt tttt ttt ttt tttt tt ttttt t
t
ChetBakerButNotForMe ChetFakerDropTheGame ChetBakerBuddy ChetFakerWhatAboutUs
Chet
* */
