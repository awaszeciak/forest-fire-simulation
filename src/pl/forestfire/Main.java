package pl.forestfire;

import pl.forestfire.application.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.out.print("\033[H\033[2J");
        System.out.println("Wybierz tryb działania:");
        System.out.println("1 - terminal (upewnij się, że terminal jest wystarczająco duży!)");
        System.out.println("2 - GUI");
        System.out.println("3 - symulacja do plików");
        System.out.println();

        String decision;
        Scanner scanner=new Scanner(System.in);
        do{
            System.out.print("\033[A\r\033[K");
            decision = scanner.next();
            switch (decision) {
                case "1":
                    TerminalApplication.startApp();
                    break;
                case "2":
                    JavaFxApplication.startApp(args);
                    break;
                case "3":
                    ToFileApp.startApp();
                    break;
                default:
                    System.out.print("Złe dane");
                    decision="-1";
                    break;
            }

        } while(decision=="-1");

    }
}
