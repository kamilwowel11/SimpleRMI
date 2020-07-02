
import java.io.Serializable;

/**
 *
 * @author Loniek
 */
public class Client1 implements Serializable{

    public static void main(String[] args) {
        Client client = new Client("Jacek", "ZAQwsx");
        client.start();
    }

}
