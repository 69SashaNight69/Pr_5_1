import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureDemo {

    public static void main(String[] args) throws Exception {

        System.out.println("===  Демонстрація thenCompose  ===");
        demonstrateThenCompose();
        System.out.println("\n=== Демонстрація thenCombine ===");
        demonstrateThenCombine();
        System.out.println("\n=== Демонстрація allOf ===");
        demonstrateAllOf();
        System.out.println("\n=== Демонстрація anyOf ===");
        demonstrateAnyOf();
    }

    static void demonstrateThenCompose() throws Exception {
        CompletableFuture<String> firstFuture = CompletableFuture.supplyAsync(() -> {
            delay(100);
            return "Result from First Task";
        });
        CompletableFuture<String> secondFuture = firstFuture.thenCompose(result -> {
            return CompletableFuture.supplyAsync(() ->{
                delay(100);
                return result + " after Processing";
            });
        });
        System.out.println(secondFuture.get());
    }

    static void demonstrateThenCombine() throws Exception {
        CompletableFuture<String> firstFuture = CompletableFuture.supplyAsync(() -> {
            delay(100);
            return "Data1";
        });
        CompletableFuture<String> secondFuture = CompletableFuture.supplyAsync(() -> {
            delay(150);
            return "Data2";
        });
        CompletableFuture<String> combinedFuture = firstFuture.thenCombine(secondFuture, (result1, result2) -> {
            return result1 + " and " + result2;
        });
        System.out.println(combinedFuture.get());

    }

    static void demonstrateAllOf() throws Exception {
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            delay(100);
            return "Task 1 result";
        });

        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            delay(120);
            return "Task 2 result";
        });
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            delay(80);
            return "Task 3 result";
        });

        CompletableFuture<Void> allTasks = CompletableFuture.allOf(task1,task2,task3);

        allTasks.thenRun(()-> {
            try{
                System.out.println("All tasks have finished");
                System.out.println("Task 1: "+ task1.get());
                System.out.println("Task 2: " + task2.get());
                System.out.println("Task 3: " + task3.get());
            }catch (Exception e) {
                System.err.println("An exception has been encountered : "+ e.getMessage());
            }
        }).get();

    }

    static void demonstrateAnyOf() throws Exception {
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            delay(150);
            return "Result from task 1";
        });

        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            delay(100);
            return "Result from task 2";
        });
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            delay(200);
            return "Result from task 3";
        });


        CompletableFuture<Object> firstCompleted = CompletableFuture.anyOf(task1,task2,task3);

        Object result = firstCompleted.get();
        System.out.println("Result from the first successfully completed task is: "+ result);


    }

    static void delay(int milliseconds){
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.err.println("An exception has been encountered : " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}