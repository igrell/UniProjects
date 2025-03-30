public class Compare {
    public static void main(String[] args) {
        int int1 = 1100;
        int int2 = 0033;
        int res = String.valueOf(int1).compareTo(String.valueOf(int2));
        if(res > 0){
            System.out.println(String.valueOf(int1)+" > "+String.valueOf(int2));

        }
        if(res < 0){
            System.out.println(String.valueOf(int1)+ " < "+String.valueOf(int2));
        }
        if(res == 0){
            System.out.println(String.valueOf(int1)+" = "+String.valueOf(int2));
        }
    }


}
