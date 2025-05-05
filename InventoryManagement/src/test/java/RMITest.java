import com.example.inventorymanagement.client.model.ClientCallbackImpl;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class RMITest implements ControllerInterface {
    @Test
    public void logIn(){
        try{
            Registry registry = LocateRegistry.getRegistry("server", 1099);
            Registry registry1 = LocateRegistry.getRegistry("server", 1099);

            UserRequestInterface userStub = (UserRequestInterface) registry.lookup("userRequest");
            System.out.println("userStub Retrieved");
            ItemOrderRequestInterface iOStub = (ItemOrderRequestInterface) registry.lookup("itemOrder");
            System.out.println("itemOrderStub Retrieved");
            ItemRequestInterface itemStub = (ItemRequestInterface) registry1.lookup("item");
            System.out.println("itemStub Retrieved");
            User user = new User("testadmin","admintest","admin");
            ClientCallback clientCallback = new ClientCallbackImpl(user);
            clientCallback.setCurrentPanel(this);
            userStub.login(clientCallback);
            LinkedList<User> users = userStub.fetchUsers(clientCallback);
            users.forEach(user1 -> System.out.println(user1.username));
            System.out.println(itemStub.fetchItem(clientCallback, 1));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void fetchAndUpdate() throws RemoteException {

    }

    @Override
    public String getObjectsUsed() throws RemoteException {
        return "user";
    }
}
