public class MaxElementInMatrixMultiThread {
    // Класс для поиска максимального элемента в строке матрицы
    private static class MaxTask implements Runnable {
        private int[] row;
        private int maxElement;

        public MaxTask(int[] row) {
            this.row = row;
        }

        @Override
        public void run() {
            maxElement = Integer.MIN_VALUE;
            for (int value : row) {
                maxElement = Math.max(maxElement, value);
            }
        }

        public int getMaxElement() {
            return maxElement;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int[][] matrix = {{1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}};
        int numberOfRows = matrix.length;

        MaxTask[] tasks = new MaxTask[numberOfRows];
        Thread[] threads = new Thread[numberOfRows];

        // Создаем и запускаем поток для каждой строки
        for (int i = 0; i < numberOfRows; i++) {
            tasks[i] = new MaxTask(matrix[i]);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }

        // Дожидаемся завершения всех потоков
        for (Thread thread : threads) {
            thread.join();
        }

        // Находим максимальный элемент среди всех строк
        int maxElement = Integer.MIN_VALUE;
        for (MaxTask task : tasks) {
            maxElement = Math.max(maxElement, task.getMaxElement());
        }

        // Выводим наибольший элемент
        System.out.println("Maximum element in matrix: " + maxElement);
    }
}