package friday;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Friday {

    public static final String LINE_BREAK = "____________________________________________________________\n";
    public static final String SAVE_FILE_PATH = "./data/friday.txt";

    public static void main(String[] args) {

        String banner = " _____     _     _             \n" +
                "|  ___| __(_) __| | __ _ _   _ \n" +
                "| |_ | '__| |/ _` |/ _` | | | |\n" +
                "|  _|| |  | | (_| | (_| | |_| |\n" +
                "|_|  |_|  |_|\\__,_|\\__,_|\\__, |\n" +
                "                         |___/ ";

        System.out.println(LINE_BREAK +
                banner + "\n" +
                "Hello! I'm Friday!\n" +
                "What can I do for you?\n" +
                LINE_BREAK);

        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int idx = loadTasks(tasks);

        while (true) {

            // Handle end-of-input (e.g. piped input, Ctrl+D) gracefully
            // instead of crashing with NoSuchElementException.
            if (!scanner.hasNextLine()) {
                break;
            }

            String word = scanner.nextLine().trim();

            // Exit
            if ("bye".equals(word)) {
                break;
            }

            // List all tasks
            if ("list".equals(word)) {
                for (int i = 0; i < idx; i++) {
                    System.out.println((i + 1) + "." + tasks[i] + "\n");
                }

                System.out.println(LINE_BREAK);
                continue;
            }

            // Split input into words
            String[] command = word.split("\\s+");

            // Mark / unmark commands
            if ("mark".equals(command[0]) ||
                    "unmark".equals(command[0])) {

                // Check that a task number was provided
                if (command.length < 2) {
                    System.out.println(
                            LINE_BREAK +
                                    "Please specify a task number.\n" +
                                    LINE_BREAK);
                    continue;
                }

                try {
                    int num = Integer.parseInt(command[1]);

                    // Check that task number is valid
                    if (num < 1 || num > idx) {
                        System.out.println(
                                LINE_BREAK +
                                        "Invalid task number.\n" +
                                        LINE_BREAK);
                        continue;
                    }

                    // Change task status
                    tasks[num - 1].changeStatus(command[0]);
                    saveTasks(tasks, idx);

                    String status;

                    if ("mark".equals(command[0])) {
                        status = "marked as done";
                    } else {
                        status = "marked as undone";
                    }

                    System.out.println(
                            LINE_BREAK +
                                    "The task below has been " + status + "\n" +
                                    "[" + tasks[num - 1].getStatusIcon() + "] " +
                                    tasks[num - 1].description + "\n" +
                                    LINE_BREAK);

                } catch (NumberFormatException e) {
                    System.out.println(
                            LINE_BREAK +
                                    "Please enter a valid task number.\n" +
                                    LINE_BREAK);
                }

                continue;
            }

            // Add a new task
            if (idx >= tasks.length) {
                System.out.println(
                        LINE_BREAK +
                                "You have reached the maximum number of tasks.\n" +
                                LINE_BREAK);
                continue;
            }

            try {
                tasks[idx] = createTask(word);
                idx++;
                saveTasks(tasks, idx);

                System.out.println(
                        LINE_BREAK +
                                word + "\n" +
                                LINE_BREAK);
            } catch (FridayException e) {
                System.out.println(
                        LINE_BREAK +
                                e.getMessage() + "\n" +
                                LINE_BREAK);
            }
        }

        System.out.println(
                LINE_BREAK +
                        "Bye. Hope to see you again soon!\n" +
                        LINE_BREAK);

        scanner.close();
    }

    /**
     * Writes the current task list to disk at {@link #SAVE_FILE_PATH},
     * one task per line, overwriting whatever was there before. Creates
     * the parent "data" folder if it doesn't already exist.
     */
    public static void saveTasks(Task[] tasks, int idx) {
        File file = new File(SAVE_FILE_PATH);
        File parentDir = file.getParentFile();

        if (parentDir != null) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i < idx; i++) {
                writer.write(tasks[i].toFileFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println(
                    LINE_BREAK +
                            "Warning: could not save tasks to disk (" + e.getMessage() + ").\n" +
                            LINE_BREAK);
        }
    }

    /**
     * Loads tasks from disk at {@link #SAVE_FILE_PATH} into {@code tasks},
     * returning how many were loaded. If the file doesn't exist yet (e.g.
     * first run), returns 0 without error. Lines that can't be parsed are
     * skipped.
     */
    public static int loadTasks(Task[] tasks) {
        File file = new File(SAVE_FILE_PATH);
        int idx = 0;

        if (!file.exists()) {
            return idx;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine() && idx < tasks.length) {
                String line = fileScanner.nextLine();

                try {
                    tasks[idx] = parseTaskFromFile(line);
                    idx++;
                } catch (RuntimeException e) {
                    System.out.println(
                            LINE_BREAK +
                                    "Warning: skipping corrupted line in save file: " + line + "\n" +
                                    LINE_BREAK);
                }
            }

            if (fileScanner.hasNextLine()) {
                System.out.println(
                        LINE_BREAK +
                                "Warning: the save file has more tasks than can be loaded "
                                + "(max " + tasks.length + "); the rest were skipped.\n" +
                                LINE_BREAK);
            }
        } catch (IOException e) {
            System.out.println(
                    LINE_BREAK +
                            "Warning: could not load tasks from disk (" + e.getMessage() + ").\n" +
                            LINE_BREAK);
        }

        return idx;
    }

    /**
     * Parses one line of the save file (e.g. "D | 0 | return book | Sunday")
     * back into a Task. Throws an unchecked exception if the line is
     * malformed, which the caller treats as a corrupted line to skip.
     */
    private static Task parseTaskFromFile(String line) {
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = "1".equals(parts[1]);
        String description = parts[2];

        Task task;

        if ("T".equals(type)) {
            task = new Todo(description);
        } else if ("D".equals(type)) {
            task = new Deadline(description, parts[3]);
        } else if ("E".equals(type)) {
            task = new Event(description, parts[3], parts[4]);
        } else {
            throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (isDone) {
            task.changeStatus("mark");
        }

        return task;
    }

    public static Task createTask(String input) throws FridayException {

        if (input.equals("todo") || input.startsWith("todo ")) {

            String description = input.length() > 4 ? input.substring(5).trim() : "";

            if (description.isEmpty()) {
                throw new FridayException("OOPS!!! The description of a todo cannot be empty.");
            }

            validateNoPipeCharacter(description);

            return new Todo(description);

        } else if (input.equals("deadline") || input.startsWith("deadline ")) {

            String details = input.length() > 8 ? input.substring(9).trim() : "";

            if (!details.contains(" /by ")) {
                throw new FridayException(
                        "OOPS!!! A deadline needs a description and a '/by' date/time, "
                                + "e.g. deadline return book /by Sunday");
            }

            String[] parts = details.split(" /by ", 2);
            String description = parts[0].trim();
            String by = parts[1].trim();

            if (description.isEmpty()) {
                throw new FridayException("OOPS!!! The description of a deadline cannot be empty.");
            }

            if (by.isEmpty()) {
                throw new FridayException("OOPS!!! Please state a date/time after '/by'.");
            }

            validateNoPipeCharacter(description);
            validateNoPipeCharacter(by);

            return new Deadline(description, by);

        } else if (input.equals("event") || input.startsWith("event ")) {

            String details = input.length() > 5 ? input.substring(6).trim() : "";

            if (!details.contains(" /from ")) {
                throw new FridayException(
                        "OOPS!!! An event needs a description, a '/from' and a '/to' timing, "
                                + "e.g. event meeting /from Mon 2pm /to Mon 4pm");
            }

            String[] parts = details.split(" /from ", 2);
            String description = parts[0].trim();

            if (description.isEmpty()) {
                throw new FridayException("OOPS!!! The description of an event cannot be empty.");
            }

            if (!parts[1].contains(" /to ")) {
                throw new FridayException("OOPS!!! Please state both a '/from' and a '/to' timing.");
            }

            String[] times = parts[1].split(" /to ", 2);
            String from = times[0].trim();
            String to = times[1].trim();

            if (from.isEmpty() || to.isEmpty()) {
                throw new FridayException("OOPS!!! Please state both a '/from' and a '/to' timing.");
            }

            validateNoPipeCharacter(description);
            validateNoPipeCharacter(from);
            validateNoPipeCharacter(to);

            return new Event(description, from, to);
        }

        throw new FridayException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    /**
     * Rejects task text containing '|', since that character is the field
     * delimiter used by {@link Task#toFileFormat}/{@link #parseTaskFromFile} -
     * allowing it would silently corrupt the save file on the next reload.
     */
    private static void validateNoPipeCharacter(String value) throws FridayException {
        if (value.contains("|")) {
            throw new FridayException("OOPS!!! Task details cannot contain the '|' character.");
        }
    }

}
