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


public class CommunicatorService implements CommunicatorServiceInterface, Serializable {

    Map<String, BlockingQueue<String>> mapUsers = new HashMap();
    Map<String, String> mapPassword = new HashMap();


    public CommunicatorService() {
        this.mapUsers = Collections.synchronizedMap(this.mapUsers);
        this.mapPassword = Collections.synchronizedMap(this.mapPassword);
    }

    protected boolean checkPassword(String userName, String pass) {
        boolean check = false;
        if (pass != null) {
            String hash = (String)this.mapPassword.get(userName);
            if (hash.equals(this.passwordToHash(pass))) {
                check = true;
            }
        }
        return check;
    }

    public List<String> getUsers() throws RemoteException {
        List<String> users = new ArrayList(this.mapUsers.keySet());
        return users;
    }


    protected String passwordToHash(String password) {        //protected potrzebujemy by nikt nie miał dostępu i nie mógł nadpisać hashCode
        return "" + password.hashCode();
    }

    public void registerUser(String nickname, String password) throws RemoteException {
        this.mapPassword.computeIfAbsent(nickname, (k) -> {
            this.mapUsers.put(nickname, new LinkedBlockingQueue());
            return this.passwordToHash(password);
        });
    }

    public void addMessage(String messageFrom, String password, String messageTo, String message) throws RemoteException {
        if (this.checkPassword(messageFrom, password)) {
            Queue<String> q = this.mapUsers.get(messageTo);
            if (q != null) {
                q.add(messageFrom + ";" + message);
            }
        }

    }

    public String getMessage(String nickname, String password) throws RemoteException {
        try {
            if (this.checkPassword(nickname, password)) {
                BlockingQueue<String> q = (BlockingQueue)this.mapUsers.get(nickname);
                if (q != null) {
                    return (String)q.poll(2L, TimeUnit.SECONDS);            // decydowanie o sprawdzaniu czy jest nowa wiadomosć w ciągu 2 sekund
                }                                                                   // Jak nic nie ma to if się nie wykona i zwracany jest null
            }
        } catch (InterruptedException e) {
        }

        return null;
    }


}
