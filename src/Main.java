import java.nio.file.*;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

/**
 * Main class for executing file search tasks using Work Stealing and Work Dealing
 * strategies in a directory. Compares their performance and outputs the results.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String directoryPath = ""; // Path to the directory for searching
        Path path;

        // Prompt user for directory input
        while (true) {
            System.out.print("Enter 1 to use default directory or 2 to enter your directory: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    directoryPath = "test"; // Default directory
                    break;
                case "2":
                    System.out.print("Enter the path to the directory: ");
                    directoryPath = scanner.nextLine();
                    break;
                default:
                    System.out.println("Invalid option! Please choose 1 or 2.");
            }

            // Validate the provided path
            path = Paths.get(directoryPath);
            if (!Files.isDirectory(path)) {
                System.out.println("Provided path is not a directory. Please try again.");
            } else {
                break; // Exit loop if the path is valid
            }
        }

        // Prompt user for the keyword to search
        System.out.print("Enter the keyword to search for in file names: ");
        String keyword = scanner.nextLine();

        // Create ForkJoinPool for parallel processing
        ForkJoinPool pool = new ForkJoinPool();

        // Execute Work Stealing strategy
        long startTime = System.nanoTime();
        WorkStealingTask workStealingTask = new WorkStealingTask(path, keyword);
        int countStealing = pool.invoke(workStealingTask);
        long endTime = System.nanoTime();

        // Output results of Work Stealing
        System.out.println("Number of files containing '" + keyword + "' using Work Stealing: " + countStealing);
        System.out.printf("Time taken (Work Stealing): %.3f ms\n", (endTime - startTime) / 1_000_000.0);

        // Execute Work Dealing strategy
        startTime = System.nanoTime();
        WorkDealingTask workDealingTask = new WorkDealingTask(path, keyword);
        int countDealing = pool.invoke(workDealingTask);
        endTime = System.nanoTime();

        // Output results of Work Dealing
        System.out.println("Number of files containing '" + keyword + "' using Work Dealing: " + countDealing);
        System.out.printf("Time taken (Work Dealing): %.3f ms", (endTime - startTime) / 1_000_000.0);

        // Close the scanner
        scanner.close();
    }
}
