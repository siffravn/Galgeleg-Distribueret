import brugerautorisation.transport.rmi.Brugeradmin;

import java.rmi.Naming;

public class GalgelegRMIServer {

    public static void main(String[] args) throws Exception{

        java.rmi.registry.LocateRegistry.createRegistry(1099);

        IServerRMIController serverRMIController = new ServerControllerRMI();
        Naming.rebind("rmi://localhost/Controller", serverRMIController);
        System.out.println("Controller registreret");

        Brugeradmin brugeradmin = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");
        Naming.rebind("rmi://localhost/brugeradmin", brugeradmin);
        System.out.println("BrugerAdmin registreret");
    }
}
