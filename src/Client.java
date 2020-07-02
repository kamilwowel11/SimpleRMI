import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;





/**
 *
 * @author Loniek
 */
public class Client implements Serializable{

    String nickname;
    String password;
    CommunicatorServiceInterface service;




    public Client(String nickname, String password)
    {
        this.nickname=nickname;
        this.password=password;

        try
        {
            String serverName = "communicator";
            // Registry rejestr = null;
            Registry registry = LocateRegistry.getRegistry(4444);
            service = (CommunicatorServiceInterface) registry.lookup(serverName);

            System.out.println("Client: " + nickname + " connected");

        } catch (RemoteException | NotBoundException e)
        {
            e.printStackTrace();
        }


    }

    public void start()
    {

        try
        {

            service.registerUser(nickname, password);
            Thread reader = new Thread(() -> {
                try
                {
                    while(!Thread.interrupted())
                    {
                        String msg = service.getMessage(nickname, password);
                        if (msg != null)
                        {
                            String [] temp = msg.split(";");
                            if (temp.length == 2)
                            {
                                String messageTo = temp[0];
                                String message = temp[1];
                                System.out.println(messageTo + ";" + message);
                            }
                            else
                            {
                                System.out.println("Wrong input");
                            }
                        }
                    }

                } catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            });

            Thread writer = new Thread(() -> {
                try
                {
                    while(!Thread.interrupted())
                    {
                        Scanner scan = new Scanner(System.in);
                        System.out.println("Type your message !");
                        String messageUser = scan.nextLine();
                        String [] temp = messageUser.split(";");
                        String messageTo = temp[0];
                        String message = temp[1];
                        service.addMessage(nickname, password, messageTo, message);
                    }
                } catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            });

            writer.start();
            reader.start();


        } catch (RemoteException e)
        {
            e.printStackTrace();
        }

    }


}
