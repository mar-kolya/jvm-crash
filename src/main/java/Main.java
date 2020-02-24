public class Main {

    private static final int SIZE = 1023;

    public static void main(final String... args) {
        while (true) {
            recursion(2048);
        }
    }

    private static void recursion(final int n) {
        final var a = new byte[SIZE];
        a[SIZE - 1] = 5;
        if (n > 0) {
            recursion(n - 1);
        }
    }

}
