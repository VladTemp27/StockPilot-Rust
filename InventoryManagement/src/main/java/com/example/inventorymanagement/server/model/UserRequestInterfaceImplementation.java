package com.example.inventorymanagement.server.model;

import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import com.example.inventorymanagement.util.exceptions.AlreadyLoggedInException;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.exceptions.UserExistenceException;
import com.example.inventorymanagement.util.objects.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

// #TODO: Implement GSONProcessing methods, add real logic
public class UserRequestInterfaceImplementation extends UnicastRemoteObject implements UserRequestInterface {
    LinkedList<ClientCallback> clientCallbacks = new LinkedList<>();

    public UserRequestInterfaceImplementation() throws RemoteException{

    }

    //This method changes the object of user inside the callback for the user to be able to access their role
    @Override
    public void login(ClientCallback clientCallback) throws RemoteException, AlreadyLoggedInException, UserExistenceException {

        if(isLoggedIn(clientCallback)) throw new AlreadyLoggedInException(clientCallback.getUser().getUsername() + " is already logged in");

        User toLogIn = clientCallback.getUser();
        User localUserData = GSONProcessing.fetchUser(toLogIn.getUsername());
        if (localUserData != null && toLogIn.getPassword().equals(localUserData.getPassword())) {
            clientCallbacks.addFirst(clientCallback);
            clientCallback.setUser(localUserData);
            callUpdate("user");
        } else {
            throw new UserExistenceException("Invalid credentials");
        }


    }

    @Override
    public void logout(ClientCallback clientCallback) throws RemoteException, NotLoggedInException {

        if(!(isLoggedIn(clientCallback))) throw new NotLoggedInException(clientCallback.getUser().getUsername()+ "is not logged in");

        clientCallbacks.remove(clientCallback);
        callUpdate("user");

    }

    // This returns an object of LinkedList of object User
    @Override
    public LinkedList<User> getActiveUser(ClientCallback clientCallback) throws OutOfRoleException, NotLoggedInException, RemoteException {
        if(clientCallback.getUser().getRole().equals("admin")){

            if(!clientCallbacks.contains(clientCallback)){
                throw new NotLoggedInException("Not logged in");
            }else{
                LinkedList<User> listOfUsers = new LinkedList<>();
                for(ClientCallback clients : clientCallbacks){
                    listOfUsers.addLast(clients.getUser());
                }
                clientCallback.objectCall(listOfUsers);
                return listOfUsers;
            }
        }else{
            throw new OutOfRoleException("Your are not allowed to perform this request");
        }
    }

    @Override
    public boolean addUser(ClientCallback clientCallback, User toAdd) throws OutOfRoleException, NotLoggedInException, RemoteException, UserExistenceException {

        if(clientCallback.getUser().getRole().equals("admin")) {

            boolean success = GSONProcessing.addUser(toAdd);
            clientCallback.objectCall(success);
            callUpdate("user");
            return success;
        } else{
            throw new OutOfRoleException("Insufficient Permission");
        }
    }

    @Override
    public boolean removeUser(ClientCallback clientCallback, User toRemove) throws OutOfRoleException, NotLoggedInException, RemoteException, UserExistenceException {
        if(clientCallback.getUser().getRole().equals("admin")){
            boolean success = GSONProcessing.removeUser(toRemove);
            clientCallback.objectCall(success);
            callUpdate("user");
            return success;
        }else{
            throw new OutOfRoleException("Insufficient Permission");
        }
    }

    @Override
    public boolean changeUserRole(ClientCallback clientCallback, User toChange, String newRole) throws OutOfRoleException, NotLoggedInException, RemoteException, UserExistenceException {

        if(clientCallback.getUser().getRole().equals("admin")) {

            boolean success = GSONProcessing.changeUserRole(toChange,newRole);
            clientCallback.objectCall(success);
            callUpdate("user");
            return success;

        }else{
            throw new OutOfRoleException("Insufficient permission");
        }
    }

    // This method returns boolean object
    @Override
    public boolean changePassword(ClientCallback clientCallback, User toChange, String newPassword) throws OutOfRoleException, NotLoggedInException, RemoteException, UserExistenceException {
        if(clientCallback.getUser().getRole().equals("admin") || clientCallback.getUser().getRole().equals(toChange.getRole())){

            boolean status = GSONProcessing.changePassword(toChange, newPassword);
            clientCallback.objectCall(status);

            return status;
        }else{
            throw new OutOfRoleException("You don't have permission to perform this operation");
        }
    }

    @Override
    public LinkedList<User> fetchUsers(ClientCallback clientCallback) throws RemoteException, OutOfRoleException, NotLoggedInException {
        isLoggedIn(clientCallback);
        if(clientCallback.getUser().getRole().equals("admin")){
            return GSONProcessing.fetchListOfUsers();
        }else{
            throw new OutOfRoleException("Insufficient Permission");
        }
    }

    @Override
    public boolean isLoggedIn(ClientCallback clientCallback) throws RemoteException {
        return clientCallbacks.stream().anyMatch(callbacks ->{
            try {
                return (callbacks.getUser().equals(clientCallback.getUser()));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //PLease use this whenever you change panels
    public boolean updateCallback(User user, ClientCallback clientCallback) throws RemoteException, NotLoggedInException {

        for(int x = 0; x < clientCallbacks.size();x++){
            User owner = clientCallbacks.get(x).getUser();
            if(owner.getUsername().equals(user.getUsername())){
                clientCallbacks.set(x, clientCallback);
                return true;
            }
        }
        return false;
    }

    //panel = csv of objects used in panel ex user,item
    public void callUpdate(String object) throws RemoteException {
        for(int x=0; x<clientCallbacks.size();x++){
            ClientCallback clientCallback = clientCallbacks.get(x);
            ArrayList<String> objects = new ArrayList<>(Arrays.asList((clientCallback.getObjectsUsedByPanel().split(","))));
            if(objects.contains(object)){
                System.out.println("Updating "+clientCallback.getUser());
                clientCallback.updateUICall();
            }
        }
    }
}
