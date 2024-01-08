public class SumArrayMultiThread {
    // Класс, реализующий интерфейс Runnable для вычисления суммы части массива
    private static class SumTask implements Runnable {
        private int[] array;
        private int start, end;
        private long partialSum;

        public SumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            partialSum = 0;
            for (int i = start; i < end; i++) {
                partialSum += array[i];
            }
        }

        public long getPartialSum() {
            return partialSum;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int middle = array.length / 2;

        // Создаем два задания
        SumTask task1 = new SumTask(array, 0, middle);
        SumTask task2 = new SumTask(array, middle, array.length);

        // Создаем и запускаем два потока
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        thread1.start();
        thread2.start();

        // Дожидаемся завершения потоков
        thread1.join();
        thread2.join();

        // Суммируем результаты
        long totalSum = task1.getPartialSum() + task2.getPartialSum();

        // Выводим результат
        System.out.println("Total sum: " + totalSum);
    }
}