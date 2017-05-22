package app;
/**
 * Created by hugo on 17-05-21.
 */
public class Calc {

    public int add(String a, String b) {
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x + y;
    }

    public int multiply(String a, String b) {
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x * y;
    }

    public int substraction(String a, String b) {
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x - y;
    }

    public int divide(String a, String b) {
        int x = Integer.parseInt(a);
        int y = Integer.parseInt(b);
        return x / y;
    }

}
