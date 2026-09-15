package friday;

import java.util.ArrayList;
import java.util.Scanner;

public class Friday {

    public static final String LINE_BREAK = "____________________________________________________________\n";

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

        ArrayList<Task> tasks = new ArrayList<>();

        while (true) {

            String word = scanner.nextLine();

            // Exit
            if ("bye".equals(word)) {
                break;
            }

            // List all tasks
            if ("list".equals(word)) {
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + "." + tasks.get(i) + "\n");
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
                    if (num < 1 || num > tasks.size()) {
                        System.out.println(
                                LINE_BREAK +
                                        "Invalid task number.\n" +
                                        LINE_BREAK);
                        continue;
                    }

                    // Change task status
                    Task task = tasks.get(num - 1);
                    task.changeStatus(command[0]);

                    String status;

                    if ("mark".equals(command[0])) {
                        status = "marked as done";
                    } else {
                        status = "marked as undone";
                    }

                    System.out.println(
                            LINE_BREAK +
                                    "The task below has been " + status + "\n" +
                                    "[" + task.getStatusIcon() + "] " +
                                    task.description + "\n" +
                                    LINE_BREAK);

                } catch (NumberFormatException e) {
                    System.out.println(
                            LINE_BREAK +
                                    "Please enter a valid task number.\n" +
                                    LINE_BREAK);
                }

                continue;
            }

            // Delete command
            if ("delete".equals(command[0])) {

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
                    if (num < 1 || num > tasks.size()) {
                        System.out.println(
                                LINE_BREAK +
                                        "Invalid task number.\n" +
                                        LINE_BREAK);
                        continue;
                    }

                    Task removed = tasks.remove(num - 1);

                    System.out.println(
                            LINE_BREAK +
                                    "Noted. I've removed this task:\n" +
                                    removed + "\n" +
                                    "Now you have " + tasks.size() + " tasks in the list.\n" +
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
            try {
                tasks.add(createTask(word));

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

    public static Task createTask(String input) throws FridayException {

        if (input.equals("todo") || input.startsWith("todo ")) {

            String description = input.length() > 4 ? input.substring(5).trim() : "";

            if (description.isEmpty()) {
                throw new FridayException("OOPS!!! The description of a todo cannot be empty.");
            }

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

            return new Event(description, from, to);
        }

        throw new FridayException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

}
