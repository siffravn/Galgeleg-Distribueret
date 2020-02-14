import brugerautorisation.transport.rmi.Brugeradmin;

import java.rmi.Naming;

public class GalgelegServer {

    public static void main(String[] args) throws Exception{

        java.rmi.registry.LocateRegistry.createRegistry(1099);

        IGalgeLogik galgeLogik = new GalgelogikImpl();
        Naming.rebind("rmi://localhost/Hangman", galgeLogik);
        System.out.println("Galelogik registreret");

//        Brugeradmin brugeradmin = new BrugeradminImpl();
//        Naming.rebind("rmi://localhost/brugeradmin", brugeradmin);
//        System.out.println("BrugerAdmin registreret");
//
        Brugeradmin brugeradmin = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Naming.rebind("rmi://localhost/brugeradmin", brugeradmin);
        System.out.println("BrugerAdmin registreret");
    }
}
