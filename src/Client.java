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
    String haslo;
    CommunicatorServiceInterfejs service;




    public Client(String nickname, String haslo)
    {
        this.nickname=nickname;
        this.haslo=haslo;

        try
        {
            String nazwaServera = "komunikator";
            // Registry rejestr = null;
            Registry rejestr = LocateRegistry.getRegistry(4444);
            service = (CommunicatorServiceInterfejs) rejestr.lookup(nazwaServera);

            System.out.println("Klient: " + nickname + " Podłączony");

        } catch (RemoteException | NotBoundException e)
        {
            e.printStackTrace();
        }


    }

    public void start()
    {

        try
        {

            service.registerUser(nickname,haslo);
            // Watek watek = new Watek(nickname,haslo);  // Wątek w końcu nie używany bo chyba łatwiej było z wyrażeniem lambda w klasie głównej
            // watek.start();                            // jednak chciałem zostawić go tutaj.
            Thread reader = new Thread(() -> {
                try
                {
                    while(!Thread.interrupted())
                    {
                        String msg = service.getMessage(nickname, haslo);
                        if (msg != null)
                        {
                            String [] temp = msg.split(";");
                            if (temp.length == 2)
                            {
                                String doKogo = temp[0];
                                String wiadomosc = temp[1];
                                System.out.println(doKogo + ";" + wiadomosc);
                                // System.out.println("msg: " + msg);
                                // System.out.println("tmp: " + temp);
                            }
                            else
                            {
                                System.out.println("Zły format wiadomości");
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
                        System.out.println("Dawej te wiadomosc?");
                        String msgUsera = scan.nextLine();
                        String [] temp = msgUsera.split(";");
                        String doKogo = temp[0];
                        String wiadomosc = temp[1];
                        service.addMessage(nickname, haslo, doKogo, wiadomosc);
                        //System.out.println("Wiadomość nadana do - " + doKogo + " z wiadomością -  " + wiadomosc);
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
