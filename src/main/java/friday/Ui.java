package friday;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Deals with all interaction with the user: reading input from the
 * console and printing output to it. No other class should call
 * {@code System.out}/{@code System.in} directly once this is in use.
 */
public class Ui {

    private static final String LINE_BREAK = "____________________________________________________________\n";

    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next line of user input, trimmed. Returns {@code null} if
     * there is no more input (e.g. end of piped input, Ctrl+D), which
     * callers should treat the same as an explicit "bye".
     */
    public String readCommand() {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine().trim();
    }

    public void showWelcome() {
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
    }

    public void showGoodbye() {
        showMessage("Bye. Hope to see you again soon!");
    }

    /**
     * Prints a single message wrapped in divider lines - the format shared
     * by every plain status/error/warning line Friday prints (e.g. command
     * errors, "invalid task number", and Storage's load/save warnings).
     */
    public void showMessage(String message) {
        System.out.println(
                LINE_BREAK +
                        message + "\n" +
                        LINE_BREAK);
    }

    public void showTaskList(ArrayList<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i) + "\n");
        }
        System.out.println(LINE_BREAK);
    }

    /**
     * Echoes the raw command text back as confirmation that a task was
     * added. (Friday doesn't yet print a friendlier "Got it..." summary
     * of the task itself - preserved as-is from the pre-refactor behavior.)
     */
    public void showTaskAdded(String rawInput) {
        System.out.println(
                LINE_BREAK +
                        rawInput + "\n" +
                        LINE_BREAK);
    }

    public void showMarkResult(Task task, boolean isMark) {
        String status = isMark ? "marked as done" : "marked as undone";

        System.out.println(
                LINE_BREAK +
                        "The task below has been " + status + "\n" +
                        "[" + task.getStatusIcon() + "] " +
                        task.description + "\n" +
                        LINE_BREAK);
    }

    public void showDeleteResult(Task removed, int remainingCount) {
        System.out.println(
                LINE_BREAK +
                        "Noted. I've removed this task:\n" +
                        removed + "\n" +
                        "Now you have " + remainingCount + " tasks in the list.\n" +
                        LINE_BREAK);
    }

    public void close() {
        scanner.close();
    }
}
