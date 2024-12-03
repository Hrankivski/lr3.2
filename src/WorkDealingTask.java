import java.nio.file.*;
import java.util.*;
import java.util.concurrent.RecursiveTask;

/**
 * WorkDealingTask is a RecursiveTask implementation for searching files
 * containing a specific keyword within a directory and its subdirectories.
 * The task divides the workload among multiple subtasks for directories.
 */
public class WorkDealingTask extends RecursiveTask<Integer> {
    private Path directory; // Path to the current directory being processed
    private String keyword; // Keyword to search for in file names

    /**
     * Constructor to initialize a WorkDealingTask.
     *
     * @param directory Directory to search in.
     * @param keyword   Keyword to look for in file names.
     */
    public WorkDealingTask(Path directory, String keyword) {
        this.directory = directory;
        this.keyword = keyword.toLowerCase(); // Convert keyword to lowercase for case-insensitive search
    }

    /**
     * The compute method defines the main logic for the task, recursively processing directories.
     *
     * @return The count of files containing the keyword.
     */
    @Override
    protected Integer compute() {
        int count = 0; // Count of matching files in this directory and its subdirectories
        List<WorkDealingTask> tasks = new ArrayList<>(); // List to store subtasks for subdirectories

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            // Iterate through the entries in the current directory
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    // Create a new task for each subdirectory
                    WorkDealingTask task = new WorkDealingTask(entry, keyword);
                    tasks.add(task);
                    task.fork(); // Fork the subtask to execute in parallel
                } else if (entry.getFileName().toString().toLowerCase().contains(keyword)) {
                    // Increment count if the file name contains the keyword
                    count++;
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading directory: " + directory.toString());
        }

        // Wait for all subtasks to complete and aggregate their results
        for (WorkDealingTask task : tasks) {
            count += task.join(); // Add results from each subtask
        }

        return count;
    }
}
