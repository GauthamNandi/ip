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

        Task[] tasks = new Task[100];
        int idx = 0;

        while (true) {

            String word = scanner.nextLine();

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

            tasks[idx] = createTask(word);
            idx++;

            System.out.println(
                    LINE_BREAK +
                            word + "\n" +
                            LINE_BREAK);
        }

        System.out.println(
                LINE_BREAK +
                        "Bye. Hope to see you again soon!\n" +
                        LINE_BREAK);

        scanner.close();
    }

    public static Task createTask(String input) {

        if (input.startsWith("todo ")) {

            String description = input.substring(5);
            return new Todo(description);

        } else if (input.startsWith("deadline ")) {

            String details = input.substring(9);
            String[] parts = details.split(" /by ", 2);

            return new Deadline(parts[0], parts[1]);

        } else if (input.startsWith("event ")) {

            String details = input.substring(6);

            String[] parts = details.split(" /from ", 2);

            String description = parts[0];

            String[] times = parts[1].split(" /to ", 2);

            String from = times[0];
            String to = times[1];

            return new Event(description, from, to);
        }

        return new Todo(input);
    }

}
