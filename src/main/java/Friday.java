import java.util.Scanner;

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
        String[] words = new String[100];
        int idx  = 0;

        while (true) {
            String word = scanner.nextLine();

            if(!"bye".equals(word)) {
                if ("list".equals(word)) {
                    for (int i = 0; i < idx; i++) {
                        System.out.println((i + 1) + ". " + words[i] + "\n");
                    }
                }
                words[idx] = word;
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
