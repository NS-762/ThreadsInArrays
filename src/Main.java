import java.util.Arrays;

public class Main {


    public static void main(String[] args) {

        simpleMethod();
        hardMethod();
    }

    public static void simpleMethod() { // Без потоков
        final int size = 1_000_000_0;
        float[] arr = new float[size];

        for (int i = 0; i < size; i++) {
            arr[i] = 1;
        }

        long a = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println(System.currentTimeMillis()-a);
    }

    public static void hardMethod() { // С потоками
        final int size = 1_000_000_0;
        final int h = size / 2;
        float[] arr = new float[size];
        float[] a1 = new float[h];
        float[] a2 = new float[h];

        for (int i = 0; i < size; i++) {
            arr[i] = 1;
        }

        long a = System.currentTimeMillis();

        System.arraycopy(arr, 0, a1, 0, h);
        System.arraycopy(arr, h, a2, 0, h);

        MyThread thread1 = new MyThread(a1);
        MyThread thread2 = new MyThread(a2);

        thread1.start();
        thread2.start();

        // Не знаю, правильно ли дальше
        try {
            thread1.join(); // Ждать окончания расчетов в потоке
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        a1 = thread1.returnArray(); // Записать вычисленные значения из потока в массив. Так же можно делать?

        try {
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        a2 = thread1.returnArray();

        System.arraycopy(a1, 0, arr, 0, h); //склеить массивы
        System.arraycopy(a2, 0, arr, h, h);

        System.out.println(System.currentTimeMillis()-a);
    }
}
