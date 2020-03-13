public interface IServerRMIController extends java.rmi.Remote{

    void newGame(String clientID) throws java.rmi.RemoteException;

    IGalgeLogik getGame(String clientID) throws java.rmi.RemoteException;
}
