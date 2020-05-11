import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CommunicatorServiceInterfejs extends Remote {
    void registerUser(String nickname, String haslo) throws RemoteException;

    void addMessage(String odKogo, String haslo, String doKogo, String wiadomosc) throws RemoteException;

    String getMessage(String nickname, String haslo) throws RemoteException;

    List<String> getUsers() throws RemoteException;
}
