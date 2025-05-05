import com.example.inventorymanagement.client.microservices.*;
import com.example.inventorymanagement.client.model.ClientCallbackImpl;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.objects.ItemOrder;
import com.example.inventorymanagement.util.objects.OrderDetail;
import com.example.inventorymanagement.util.objects.User;

import com.example.inventorymanagement.util.requests.UserRequestInterface;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;

import com.example.inventorymanagement.util.exceptions.UserExistenceException;
import org.junit.jupiter.api.Test;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ServicesTest implements ControllerInterface{

   private static Registry registry;
   private static ClientCallbackImpl clientCallback;
   @BeforeAll
   public static void setUp() {
      try {
         ServicesTest tester = new ServicesTest();
         registry = LocateRegistry.getRegistry("serverMachine", 2018);
         UserRequestInterface userStub = (UserRequestInterface) registry.lookup("userRequest");

         User user = new User("testadmin", "admintest", "admin");
         clientCallback = new ClientCallbackImpl(user);
         clientCallback.setCurrentPanel(tester);
         userStub.login(clientCallback);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public void fetchAndUpdate() throws RemoteException {
      System.out.println("updated");
   }

   @Override
   public String getObjectsUsed() throws RemoteException {
      return "user,item,itemorder";
   }


   @Test
   public void testAddUserService() {

      User newUser = new User("testUser", "password123", "admin");

       boolean result = false;
       try {
          UserRequestInterface stub = (UserRequestInterface) registry.lookup("userRequest");
          result = stub.addUser(clientCallback, newUser);
       } catch (UserExistenceException e) {
           throw new RuntimeException(e);
       } catch (OutOfRoleException e) {
           throw new RuntimeException(e);
       } catch (NotLoggedInException e) {
          System.out.println("Not logged in");
       } catch (AccessException e) {
           throw new RuntimeException(e);
       } catch (NotBoundException e) {
           throw new RuntimeException(e);
       } catch (RemoteException e) {
           throw new RuntimeException(e);
       }

       Assertions.assertTrue(result);
   }

   @Test
   public void testChangePasswordService() throws RemoteException, NotBoundException, UserExistenceException, OutOfRoleException, NotLoggedInException {
      User userToChange = new User("testUser", "password123", "admin");
      String newPassword = "newPassword";

      boolean result = ChangePasswordService.process(registry, clientCallback, userToChange, newPassword);

      Assertions.assertFalse(result);
   }


   @Test

   public void testChangeUserRole() throws  RemoteException, UserExistenceException, OutOfRoleException, NotLoggedInException{

      User userToChange = new User("testUser", "password123", "sales");
      String neRole = "sales";

      boolean result = ChangeUserRoleService.process(registry,clientCallback,userToChange,neRole);

      Assertions.assertTrue(result);
   }

   @Test
   public void testCreateItemListingService() throws RemoteException, NotBoundException, NotLoggedInException, OutOfRoleException {
      Item item = new Item("TestItem", 2234, 100, new LinkedList<>());

      boolean result = CreateItemListingService.process(registry, clientCallback, item);

      Assertions.assertTrue(result);
   }


   @Test
   public void testCreatePurchaseOrderService() {
      OrderDetail orderDetail = new OrderDetail(1, 2, 200, "FeatherFarms_2024-04-13_200.00");

      LinkedList<OrderDetail> orderDetails = new LinkedList<>();
      orderDetails.add(orderDetail);
      ItemOrder purchaseOrder = new ItemOrder(223, "Marven", "2/20/23", orderDetails);

      boolean result = false;
      try {
         result = CreatePurchaseOrderService.process(registry, clientCallback, purchaseOrder);
      } catch (NotLoggedInException | OutOfRoleException e) {
         e.printStackTrace();
      }

      Assertions.assertTrue(result);
   }

   @Test
   public void testCreateSalesInvoiceService() {
      OrderDetail orderDetail = new OrderDetail(1, 2, 200, "FeatherFarms_2024-01-03_200.00");

      LinkedList<OrderDetail> orderDetails = new LinkedList<>();
      orderDetails.add(orderDetail);
      ItemOrder salesInvoice = new ItemOrder(123,"Marven","2/20/23",orderDetails);

      boolean result = false;
      try {
         result = CreateSalesInvoiceService.process(registry, clientCallback, salesInvoice);
      } catch (NotLoggedInException | OutOfRoleException e) {
         e.printStackTrace();
      }

      Assertions.assertTrue(result);
   }

   @Test
   public void testFetchActiveUsersService() {
      LinkedList<User> activeUsers = null;
      try {
         activeUsers = FetchActiveUsersService.process(registry, clientCallback);
      } catch (NotLoggedInException | OutOfRoleException e) {
         e.printStackTrace();
      }

      Assertions.assertNotNull(activeUsers);
   }

   @Test
   public void testFetchCostTodayService() {

      float costToday = 0.0f;
      try {
         costToday = FetchCostTodayService.process(registry, clientCallback);
      } catch (NotLoggedInException | OutOfRoleException e) {
         e.printStackTrace();
      }

      Assertions.assertTrue(costToday >= 0.0f);
   }

   @Test
   public void testFetchListOfItemsService() {
      LinkedList<Item> listOfItems = null;
      try {
         listOfItems = FetchListOfItemsService.process(registry, clientCallback);
      } catch (NotLoggedInException e) {
         e.printStackTrace();
      }

      Assertions.assertNotNull(listOfItems);
   }

   @Test
   public void testFetchMonthlyCostService() {
      LinkedHashMap<Integer, Float> monthlyCost = null;
      try {
         monthlyCost = FetchMonthlyCostService.process(registry, clientCallback);
      } catch (NotLoggedInException | OutOfRoleException e) {
         e.printStackTrace();
      }

      Assertions.assertNotNull(monthlyCost);
   }

   @Test
   public void testFetchMonthlyRevenueService() {

      LinkedHashMap<Integer, Float> monthlyRevenue = null;
      try {
         monthlyRevenue = FetchMonthlyRevenueService.process(registry, clientCallback);
      } catch (NotLoggedInException | OutOfRoleException e) {
         e.printStackTrace();
      }

      Assertions.assertNotNull(monthlyRevenue);
   }

   @Test
   public void testFetchRevenueTodayService() {

      float revenueToday = 0.0f;
      try {
         revenueToday = FetchRevenueTodayService.process(registry, clientCallback);
      } catch (NotLoggedInException | OutOfRoleException e) {
         e.printStackTrace();
      }

      Assertions.assertTrue(revenueToday >= 0.0f);
   }

   @Test
   public void testFetchSalesInvoicesService() {
      LinkedList<ItemOrder> salesInvoices = null;
      try {
         salesInvoices = FetchSalesInvoicesService.process(registry, clientCallback);
      } catch (NotLoggedInException | OutOfRoleException e) {
         e.printStackTrace();
      }


      Assertions.assertNotNull(salesInvoices);
   }

   @Test
   public void testRemoveItemListingService() {
      Item itemToRemove = new Item("TestItem", 2234, 100, new LinkedList<>());

      boolean result = false;
      try {
         result = RemoveItemListingService.process(registry, clientCallback, itemToRemove);
      } catch (NotLoggedInException | OutOfRoleException e) {
         e.printStackTrace();
      }

      Assertions.assertTrue(result);
   }

   @Test
   public void testFetchSuppliersService() {

      ArrayList<String> suppliers = null;
      try {
         suppliers = FetchSuppliersService.process(registry, clientCallback);
      } catch (NotLoggedInException e) {
         e.printStackTrace();
      }

      Assertions.assertNotNull(suppliers);
   }

   @Test
   public void testRemoveUserService() {
      User userToRemove = new User("testUser", "password123", "admin");

      boolean result = false;
      try {
         result = RemoveUserService.process(registry, clientCallback, userToRemove);
      } catch (NotLoggedInException | OutOfRoleException | UserExistenceException e) {
         e.printStackTrace();
      }

      Assertions.assertTrue(result);
   }
   @AfterAll
   public static void logout(){
      try {
         UserRequestInterface stub = (UserRequestInterface) registry.lookup("userRequest");
         stub.logout(clientCallback);
      } catch (AccessException e) {
          throw new RuntimeException(e);
      } catch (NotBoundException e) {
          throw new RuntimeException(e);
      } catch (RemoteException e) {
          throw new RuntimeException(e);
      } catch (NotLoggedInException e) {
          throw new RuntimeException(e);
      }
   }
}
