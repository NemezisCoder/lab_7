import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WarehouseTransfer {

    private static final int MAX_WEIGHT = 150;
    private static Queue<Integer> products = new LinkedList<>();
    private static AtomicInteger currentWeight = new AtomicInteger(0);
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        // Инициализация товаров
        products.addAll(Arrays.asList(30, 40, 50, 60, 70, 20, 10, 30, 40));

        CompletableFuture<Void> worker1 = CompletableFuture.runAsync(() -> moveProducts("Грузчик 1"));
        CompletableFuture<Void> worker2 = CompletableFuture.runAsync(() -> moveProducts("Грузчик 2"));
        CompletableFuture<Void> worker3 = CompletableFuture.runAsync(() -> moveProducts("Грузчик 3"));

        CompletableFuture<Void> allWorkers = CompletableFuture.allOf(worker1, worker2, worker3);
        allWorkers.join();
        System.out.println("Все товары перенесены.");
    }

    private static void moveProducts(String workerName) {
        while (true) {
            int productWeight = 0;
            synchronized (lock) {
                if (products.isEmpty()) {
                    break;
                }

                while (!products.isEmpty() && (currentWeight.get() + products.peek() > MAX_WEIGHT)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (products.isEmpty()) {
                    break;
                }

                productWeight = products.poll();
                currentWeight.addAndGet(productWeight);
                System.out.println(workerName + " переносит товар весом " + productWeight + " кг.");
            }

            // Имитация времени на перенос
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            synchronized (lock) {
                currentWeight.addAndGet(-productWeight);
                // Оповещение других грузчиков после уменьшения веса
                lock.notifyAll();
            }
        }
    }
}