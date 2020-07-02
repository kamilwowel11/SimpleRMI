import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CommunicatorServiceInterface extends Remote {
    void registerUser(String nickname, String password) throws RemoteException;

    void addMessage(String messageFrom, String password, String messageTo, String message) throws RemoteException;

    String getMessage(String nickname, String password) throws RemoteException;

    List<String> getUsers() throws RemoteException;
}
