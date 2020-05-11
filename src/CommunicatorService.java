//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class CommunicatorService implements CommunicatorServiceInterfejs, Serializable {

    Map<String, BlockingQueue<String>> mapUzytkownicy = new HashMap();
    Map<String, String> mapHasla = new HashMap();
   

    public CommunicatorService() {
        this.mapUzytkownicy = Collections.synchronizedMap(this.mapUzytkownicy); // by zapewnić bezpiecznowątkowość
        this.mapHasla = Collections.synchronizedMap(this.mapHasla);
    }

    protected boolean sprawdzanieHasla(String userName, String pass) {
        boolean sprawdz = false;
        if (pass != null) {
            String hash = (String)this.mapHasla.get(userName);
            if (hash.equals(this.hasloDoHash(pass))) {          // hash.equals a nie '==' bo porównujemy wartości
                sprawdz = true;
            }
        }
        return sprawdz;
    }

    public List<String> getUsers() throws RemoteException {
		List<String> uzytkownicy = new ArrayList(this.mapUzytkownicy.keySet());
        return uzytkownicy;
    }


    protected String hasloDoHash(String haslo) {        //protected potrzebujemy by nikt nie miał dostępu i nie mógł nadpisać hashCode
        return "" + haslo.hashCode();
    }

    public void registerUser(String nickname, String haslo) throws RemoteException {
        this.mapHasla.computeIfAbsent(nickname, (k) -> {
            this.mapUzytkownicy.put(nickname, new LinkedBlockingQueue());
            return this.hasloDoHash(haslo);
        });
    }

    public void addMessage(String odKogo, String haslo, String doKogo, String wiadomosc) throws RemoteException {
        if (this.sprawdzanieHasla(odKogo, haslo)) {
            Queue<String> q = this.mapUzytkownicy.get(doKogo);
            if (q != null) {
                q.add(odKogo + ";" + wiadomosc);
            }
        }

    }

    public String getMessage(String nickname, String haslo) throws RemoteException {
        try {
            if (this.sprawdzanieHasla(nickname, haslo)) {
                BlockingQueue<String> q = (BlockingQueue)this.mapUzytkownicy.get(nickname);
                if (q != null) {
                    return (String)q.poll(2L, TimeUnit.SECONDS);            // decydowanie o sprawdzaniu czy jest nowa wiadomosć w ciągu 2 sekund
                }                                                                   // Jak nic nie ma to if się nie wykona i zwracany jest null
            }
        } catch (InterruptedException e) {
        }

        return null;
    }


}
