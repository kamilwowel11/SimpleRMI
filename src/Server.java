import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class Server {
    public static void main(String[] args) throws RemoteException {
        String name = "communicator";
        CommunicatorServiceInterface service = new CommunicatorService();
        CommunicatorServiceInterface serviceTemp = (CommunicatorServiceInterface) UnicastRemoteObject.exportObject(service, 0);
        Registry registry = LocateRegistry.createRegistry(4444);
        registry.rebind(name, serviceTemp);
        System.out.println("Service started");
    }
}
