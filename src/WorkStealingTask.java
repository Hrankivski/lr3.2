import java.nio.file.*;
import java.util.*;
import java.util.concurrent.RecursiveTask;

/**
 * WorkStealingTask is a RecursiveTask implementation for searching files
 * containing a specific keyword within a directory and its subdirectories.
 * The task uses the work-stealing approach to balance workload dynamically.
 */
public class WorkStealingTask extends RecursiveTask<Integer> {
    private Path directory; // Path to the directory being processed
    private String keyword; // Keyword to search for in file names

    /**
     * Constructor to initialize a WorkStealingTask.
     *
     * @param directory Directory to search within.
     * @param keyword   Keyword to search for in file names.
     */
    public WorkStealingTask(Path directory, String keyword) {
        this.directory = directory;
        this.keyword = keyword.toLowerCase(); // Convert to lowercase for case-insensitive matching
    }

    /**
     * The compute method contains the logic for recursively processing directories.
     *
     * @return The count of files containing the keyword in the directory and its subdirectories.
     */
    @Override
    protected Integer compute() {
        int count = 0; // Counter for files containing the keyword
        List<WorkStealingTask> tasks = new ArrayList<>(); // List to store subtasks for subdirectories

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            // Iterate over each entry in the directory
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    // Create a subtask for each subdirectory
                    WorkStealingTask task = new WorkStealingTask(entry, keyword);
                    tasks.add(task);
                    task.fork(); // Fork the subtask for parallel execution
                } else if (entry.getFileName().toString().toLowerCase().contains(keyword)) {
                    // Increment count if file name contains the keyword
                    count++;
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading directory: " + directory.toString());
        }

        // Wait for all subtasks to complete and collect their results
        for (WorkStealingTask task : tasks) {
            count += task.join(); // Add results from each subtask
        }

        return count; // Return the total count
    }
}
