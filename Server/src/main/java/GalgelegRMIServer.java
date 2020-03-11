import brugerautorisation.transport.rmi.Brugeradmin;

import java.rmi.Naming;

public class GalgelegRMIServer {

    public static void main(String[] args) throws Exception{

        java.rmi.registry.LocateRegistry.createRegistry(1099);

        IGalgeLogik galgeLogik = new GalgelogikImpl();
        galgeLogik.hentOrdFraDr();
        Naming.rebind("rmi://localhost/Hangman", galgeLogik);
        System.out.println("Galelogik registreret");
        galgeLogik.logStatus();

//        Brugeradmin brugeradmin = new BrugeradminImpl();
//        Naming.rebind("rmi://localhost/brugeradmin", brugeradmin);
//        System.out.println("BrugerAdmin registreret");
//
        Brugeradmin brugeradmin = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Naming.rebind("rmi://localhost/brugeradmin", brugeradmin);
        System.out.println("BrugerAdmin registreret");
    }
}
