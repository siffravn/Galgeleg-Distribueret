import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ServerControllerRMI extends UnicastRemoteObject implements IServerRMIController{

    public static HashMap<String, IGalgeLogik> games;

    public ServerControllerRMI() throws java.rmi.RemoteException {
        games = new HashMap<>();
    }

    @Override
    public void newGame(String clientID) throws java.rmi.RemoteException{

        try {
            IGalgeLogik galgeLogik = new GalgelogikImpl();
            galgeLogik.hentOrdFraDr();
            galgeLogik.logStatus();
            games.put(clientID, galgeLogik);
            Naming.rebind("rmi://localhost/Hangman/" + clientID, games.get(clientID));
            System.out.println("Galelogik registreret");

        } catch (Exception e) {
            System.out.println("Could not create new Game " + clientID);
        }
    }

    @Override
    public IGalgeLogik getGame(String clientID) throws java.rmi.RemoteException{
        return games.get(clientID);
    }

}
