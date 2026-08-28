import java.util.Scanner;
import java.util.Arrays;


public class Friday {
    public static void main(String[] args) {
        String banner = " _____     _     _             \n" +
                "|  ___| __(_) __| | __ _ _   _ \n" +
                "| |_ | '__| |/ _` |/ _` | | | |\n" +
                "|  _|| |  | | (_| | (_| | |_| |\n" +
                "|_|  |_|  |_|\\__,_|\\__,_|\\__, |\n" +
                "                         |___/ ";

        System.out.println("____________________________________________________________\n" +
                banner + "\n" +
                "Hello! I'm Friday!\n" +
                "What can I do for you?\n" +
                "____________________________________________________________\n");

        Scanner scanner = new Scanner(System.in);

        Task[] tasks = new Task[100];
        int idx  = 0;

        while (true) {
            String word = scanner.nextLine();

            if(!"bye".equals(word)) {
                if ("list".equals(word)) {
                    for (int i = 0; i < idx; i++) {
                        System.out.println((i + 1) + ".[" + tasks[i].getStatusIcon() + "] " + tasks[i].description + "\n");
                    }
                    continue;
                }

                if(word.contains(" ")){
                    String[] change = word.split(" ");

                    if ("mark".equals(change[0])) {
                        int num = Integer.parseInt(change[1]);
                        tasks[num - 1].changeStatus(change[0]);
                        System.out.println("____________________________________________________________\n" +
                                            "the task below has been marked as done\n" +
                                            "[" + tasks[num - 1].getStatusIcon() + "] " + tasks[num - 1].description + "\n" +
                                            "____________________________________________________________\n");

                    }else if ("unmark".equals(change[0])) {
                        int num = Integer.parseInt(change[1]);
                        tasks[num - 1].changeStatus(change[0]);
                        System.out.println("____________________________________________________________\n" +
                                    "the task below has been marked as undone\n" +
                                    "[" + tasks[num - 1].getStatusIcon() + "] " + tasks[num - 1].description + "\n" +
                                    "____________________________________________________________\n");
                        }
                    continue;
                }

                tasks[idx] = new Task(word);
                idx++;
                System.out.println("____________________________________________________________\n" +
                                    word + "\n" +
                                    "____________________________________________________________\n");
            }else{
                break;
            }
        }

        System.out.println("____________________________________________________________\n" +
                            "Bye. Hope to see you again soon!\n" +
                            "____________________________________________________________\n");


    }
}
