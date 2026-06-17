package pl.forestfire;

import pl.forestfire.application.*;

import java.util.Scanner;

/**
 * Główna klasa programu.
 *
 * <p>
 *     Odpowiada za uruchomienie aplikacji oraz wybór trybu działania.
 *     Użytkownik może wybrać symulację w terminalu, w interfejsie graficznym
 *     JavaFX albo symulację zapisywaną do pliku.
 * </p>
 */
public class Main {

    /**
     * Metoda startowa programu.
     *
     * <p>
     *     Wyświetla menu wyboru trybu działania, pobiera decyzję użytkownika
     *     z konsoli i uruchamia odpowiedni moduł aplikacji.
     * </p>
     *
     * @param args argumenty przekazane podczas uruchomienia programu
     * @throws InterruptedException wyjątek może wystąpic w trybie terminalowym,
     * gdy działanie programu zostanie przerwane podczas opóźnienia miedzy krokami symulacji
     */
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
                    ToFileApp.startApp("file.csv");
                    break;
                default:
                    System.out.print("Złe dane");
                    decision="-1";
                    break;
            }

        } while(decision.equals("-1"));

    }
}
