import brugerautorisation.transport.rmi.Brugeradmin;

import java.rmi.Naming;
import java.util.Scanner;

public class GalgelegRMIClient {

    public static void main(String[] args) throws Exception{

        Brugeradmin brugeradmin = (Brugeradmin) Naming.lookup("rmi://localhost/brugeradmin");

        logIn(brugeradmin);

        IGalgeLogik galgelogik = (IGalgeLogik) Naming.lookup("rmi://localhost/Hangman");

        runHangman(galgelogik);

    }

    public static void runHangman(IGalgeLogik galgeLogik) throws Exception{

        Scanner scanner = new Scanner(System.in);

        galgeLogik.nulstil();
        System.out.println("Velkommen til Galgeleg!");
        String message = "";

        ui(galgeLogik);

        while (!galgeLogik.erSpilletSlut()){
            System.out.println("Indtast et bogstav");
            String letter = scanner.next();
            galgeLogik.gætBogstav(letter);

            ui(galgeLogik);
        }
        if (galgeLogik.erSpilletVundet()){
            message += "\nTillykke du vandt spillet!";
        }else{
            message += "\nDu tabte spillet...";
        }

        System.out.println(message + "\nOrdet var: " + galgeLogik.getOrdet());
    }

    public static void logIn(Brugeradmin brugeradmin) throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Login:\nBrugernavn:");
        String userID = scanner.next();
        System.out.println("Adgangskode:");
        String password = scanner.next();

        try {
            brugeradmin.hentBruger(userID, password);

        }catch (Exception e){
            System.out.println("Forkert login! Prøv igen:");
            logIn(brugeradmin);
        }
        System.out.println("Du er nu logget ind\n");
    }

    public static void ui(IGalgeLogik galgeLogik) throws Exception{

        int life = 7-galgeLogik.getAntalForkerteBogstaver();
        String string = "";
        string += "\n\nGæt ordet: " + galgeLogik.getSynligtOrd();
        string += "\t\tLiv tilbage: " + life;
        string += "\t\tBrugte bogstaver" + galgeLogik.getBrugteBogstaver();

        System.out.println(string);

//        System.out.println("\nGæt ordet: " + galgeLogik.getSynligtOrd() );
//        System.out.println(7-galgeLogik.getAntalForkerteBogstaver() + " Liv tilbage!");
//        System.out.println("Brugte bogstaver" + galgeLogik.getBrugteBogstaver());

    }
}
