package com.example.inventorymanagement.server.model;

import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.objects.OrderDetail;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.objects.ItemOrder;
import com.example.inventorymanagement.util.objects.Stock;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.LinkedList;

public class GSONProcessing {

    public static User authenticate(User key) {
        String filePath = "InventoryManagement/src/main/resources/com/example/inventorymanagement/data/users.json";

        Gson gson = new GsonBuilder().create();

        try {
            JsonElement rootElement = JsonParser.parseReader(new FileReader(filePath));
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray userJsonArray = rootObject.getAsJsonArray("users");
            for (JsonElement jsonElement : userJsonArray) {
                User user = gson.fromJson(jsonElement, User.class);
                if (user.username.equals(key.username) && user.password.equals(key.password)) {
                    return user;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Adds a new item to a JSON file.
     *
     * @param newItem item to be added
     * @return true if item is added successfully, false if otherwise.
     */
    public synchronized static boolean addItem(Item newItem) {
        try {
            String filePath = "InventoryManagement/src/main/resources/com/example/inventorymanagement/data/items.json";
            JsonParser jsonParser = new JsonParser();
            JsonElement rootElement = jsonParser.parse(new FileReader(filePath));
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray itemJsonArray = rootObject.getAsJsonArray("items");

            newItem.setItemId(itemJsonArray.size()+1);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement newItemJson = gson.toJsonTree(newItem);
            itemJsonArray.add(newItemJson);

            FileWriter writer = new FileWriter(filePath);
            gson.toJson(rootElement, writer);
            writer.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }//end of method

    /**
     * Removes an item from a JSON file based on its name.
     *
     * @param itemName name of the item to be removed.
     * @return true if the item was successfully removed, false if otherwise.
     */
    public synchronized static boolean removeItem(String itemName) {
        try {
            String filePath = "src/main/resources/com/example/inventorymanagement/data/items.json";
            JsonParser jsonParser = new JsonParser();
            JsonElement rootElement = jsonParser.parse(new FileReader(filePath));
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray itemJsonArray = rootObject.getAsJsonArray("items");

            for (JsonElement jsonElement : itemJsonArray) {
                JsonObject itemObject = jsonElement.getAsJsonObject();
                String name = itemObject.get("itemName").getAsString();
                if (name.equals(itemName)) {
                    itemJsonArray.remove(jsonElement);
                    FileWriter writer = new FileWriter(filePath);
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(rootElement, writer);
                    writer.close();
                    return true;
                }
            }
            return false; // Item not found
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }//end of method

    /**
     * Adds a new purchase order or sales order to the respective JSON file.
     *
     * @param orderType type of order to be added (purchase/sales).
     * @param newOrder  new object of ItemOrder to be added.
     * @return true if order is successfully added, false otherwise.
     */
    public synchronized static boolean addItemOrder(String orderType, ItemOrder newOrder) {
        try {
            String filePath;
            String orderArrayName;

            switch (orderType.toLowerCase()) {
                case "purchase":
                    filePath = "InventoryManagement/src/main/resources/com/example/inventorymanagement/data/purchaseorders.json";
                    orderArrayName = "purchaseOrders";
                    break;
                case "sales":
                    filePath = "InventoryManagement/src/main/resources/com/example/inventorymanagement/data/salesorders.json";
                    orderArrayName = "salesOrders";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid order type: " + orderType);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            StringBuilder fileContent = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    fileContent.append(line).append('\n');
                }
            }

            JsonElement rootElement = JsonParser.parseString(fileContent.toString());
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray orderJsonArray = rootObject.getAsJsonArray(orderArrayName);

            int currentID = orderJsonArray.size()+1;

            //Somehow setter method for order id does not set correctly i dont know why, this is the work around
            ItemOrder updatedOrder = new ItemOrder(currentID, newOrder.getByUser(), newOrder.getDate(), newOrder.getOrderDetails());

            updateItem(updatedOrder, orderType);


            String ioString = gson.toJson(updatedOrder);
            JsonElement ioElement = JsonParser.parseString(ioString);
            orderJsonArray.add(ioElement);

            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(rootElement, writer);
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private synchronized static void updateItem(ItemOrder order, String type) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File itemFile = new File("InventoryManagement/src/main/resources/com/example/inventorymanagement/data/items.json");

        try {
            JsonElement rootElement;
            if (itemFile.exists()) {
                try (FileReader reader = new FileReader(itemFile);
                     JsonReader jsonReader = new JsonReader(reader)) {
                    rootElement = gson.fromJson(jsonReader, JsonElement.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            } else {
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("items", new JsonArray());
                rootElement = jsonObject;
            }

            if (!rootElement.isJsonObject()) {
                System.err.println("Error: items.json is not valid JSON");
                return;
            }

            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray itemArray = rootObject.getAsJsonArray("items");

            for (OrderDetail detail : order.getOrderDetails()) {
                updateItemQuantity(itemArray, detail, type);
            }

            try (FileWriter writer = new FileWriter(itemFile)) {
                gson.toJson(rootElement, writer);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void updateItemQuantity(JsonArray itemArray, OrderDetail detail, String type) {
        String batch = detail.getBatchNo();
        for (int i = 0; i < itemArray.size(); i++) {
            JsonObject item = itemArray.get(i).getAsJsonObject();
            if (Integer.parseInt(item.get("itemId").getAsString()) == detail.getItemId()) {
                JsonArray stocks = item.getAsJsonArray("stocks");
                if (stocks == null) {
                    stocks = new JsonArray();
                    item.add("stocks", stocks);
                }

                Stock updatedStock = updateStockQuantity(stocks, batch, detail, type);

                int initialTQty = Integer.parseInt(item.get("totalQty").getAsString());
                item.addProperty("totalQty", String.valueOf(initialTQty + (type.equals("sales") ? -detail.getQty() : detail.getQty())));
                break;
            }
        }
    }

    private static Stock updateStockQuantity(JsonArray stocks, String batch, OrderDetail detail, String type) {
        for (int j = 0; j < stocks.size(); j++) {
            JsonObject stockObject = stocks.get(j).getAsJsonObject();
            if (stockObject != null && stockObject.has("batchNo") && stockObject.get("batchNo").getAsString().equals(batch)) {
                int initialQty = Integer.parseInt(stockObject.get("qty").getAsString());
                int updatedQty = type.equals("sales") ? initialQty - detail.getQty() : initialQty + detail.getQty();
                stockObject.addProperty("qty", String.valueOf(updatedQty));
                return new Stock(
                        stockObject.get("batchNo").getAsString(),
                        updatedQty,
                        stockObject.get("price").getAsFloat(),
                        stockObject.get("cost").getAsFloat(),
                        stockObject.get("supplier").getAsString(),
                        stockObject.get("date").getAsString()
                );
            }
        }

        if (type.equals("purchase")) {
            String[] disseminatedBatch = detail.getBatchNo().split("_");
            float price = (float) (detail.getUnitPrice() + (detail.getUnitPrice() * 0.20));
            Stock newStock = new Stock(detail.getBatchNo(), detail.getQty(), price, Float.parseFloat(disseminatedBatch[2]) , disseminatedBatch[0], disseminatedBatch[1]);
            Gson gson = new Gson();
            stocks.add(JsonParser.parseString(gson.toJson(newStock)));
            return newStock;
        }

        return null;
    }

    // TODO: Update object of item as well inside the items.json
    /**
     * Removes an ItemOrder from the respective JSON file.
     *
     * @param orderType type of order to remove (purchase/sales).
     * @param orderID ID of order to be removed.
     * @return true if ItemOrder is successfully removed, false otherwise.
     */
    public synchronized static boolean removeItemOrder(String orderType, String orderID) {
        try {
            String filePath;
            if (orderType.equalsIgnoreCase("purchase")) {
                filePath = "com/example/inventorymanagement/data/purchaseorders.json";
            } else if (orderType.equalsIgnoreCase("sales")) {
                filePath = "com/example/inventorymanagement/data/salesorder.json";
            } else {
                throw new IllegalArgumentException("Invalid order type: " + orderType);
            }

            JsonParser jsonParser = new JsonParser();
            JsonElement rootElement = jsonParser.parse(new FileReader(filePath));
            JsonObject rootObject = rootElement.getAsJsonObject();

            JsonArray orderJsonArray;
            if (orderType.equalsIgnoreCase("purchase")) {
                orderJsonArray = rootObject.getAsJsonArray("purchaseOrders");
            } else {
                orderJsonArray = rootObject.getAsJsonArray("salesOrders");
            }

            for (JsonElement jsonElement : orderJsonArray) {
                JsonObject orderObject = jsonElement.getAsJsonObject();
                String id = orderObject.get("orderID").getAsString();
                if (id.equals(orderID)) {
                    orderJsonArray.remove(jsonElement);

                    FileWriter writer = new FileWriter(filePath);
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(rootElement, writer);
                    writer.close();

                    return true;
                }
            }

            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static synchronized boolean changePassword(User toChange, String newPassword) {
        File file = new File("InventoryManagement/src/main/resources/com/example/inventorymanagement/data/users.json");
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        ) {
            JsonElement rootElement = JsonParser.parseReader(bufferedReader);
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray userList = rootObject.getAsJsonArray("users");

            for (JsonElement userElement : userList) {
                JsonObject userObject = userElement.getAsJsonObject();
                String name = userObject.get("username").getAsString();
                if (name.equals(toChange.getUsername())) {
                    String currentPassword = userObject.get("password").getAsString();
                    userObject.remove("password");
                    if (currentPassword.equals(newPassword)) {
                        return false;
                    }
                    userObject.addProperty("password", newPassword);


                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    try (Writer writer = new FileWriter(file)) {
                        gson.toJson(rootElement, writer);
                    }
                    return true;
                }
            }
            return false; // User not found
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static synchronized boolean changeUserRole(User toChange, String newRole) {
        File file =new File("InventoryManagement/src/main/resources/com/example/inventorymanagement/data/users.json");
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
        ) {
            JsonElement rootElement = JsonParser.parseReader(bufferedReader);
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray userList = rootObject.getAsJsonArray("users");

            for (JsonElement userElement : userList) {
                JsonObject userObject = userElement.getAsJsonObject();
                String name = userObject.get("username").getAsString();
                if (name.equals(toChange.getUsername())) {
                    String role = userObject.get("role").getAsString();
                    if (!role.equals(newRole)) {
                        userObject.remove("role");
                        userObject.addProperty("role", newRole);

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        try (Writer writer = new FileWriter(file)) {
                            gson.toJson(rootElement, writer);
                        }
                        return true;
                    }
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static synchronized ArrayList<String> fetchListOfSuppliers() {
        ArrayList<String> suppliers = new ArrayList<>();
        File file = new File("InventoryManagement/src/main/resources/com/example/inventorymanagement/data/suppliers.json");
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
        ) {
            JsonElement rootElement = JsonParser.parseReader(bufferedReader);
            JsonObject jsonObject = rootElement.getAsJsonObject();
            JsonArray suppliersArray = jsonObject.getAsJsonArray("suppliers");

            for (JsonElement supplierElement : suppliersArray) {
                JsonObject supplierObject = supplierElement.getAsJsonObject();
                String name = supplierObject.get("name").getAsString();
                suppliers.add(name);
            }
            return suppliers;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }



    /**
     * Method for fetching object of user
     * @param username  String of username will be used as a search key to find the object of user inside the json
     * @return  object of User or null if not found
     */
    public static synchronized User fetchUser(String username){
        File file = new File("InventoryManagement/src/main/resources/com/example/inventorymanagement/data/users.json");
        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
                ){

            JsonElement rootElement = JsonParser.parseReader(bufferedReader);
            JsonObject jsonObject = rootElement.getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray("users");
            for(JsonElement jsonElement: jsonArray){
                Gson gson = new Gson();

                JsonObject userObject = jsonElement.getAsJsonObject();
                if(userObject.get("username").getAsString().equals(username))
                    return gson.fromJson(jsonElement, User.class);

            }
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    /**
     *   Method for fetching list of item orders (purchase or sales)
     * @param type  String value, should be either purchase or sales
     * @return  Returns LinkedList of object ItemOrder
     */
    public static synchronized LinkedList<ItemOrder> fetchListOfItemOrder(String type){
        LinkedList<ItemOrder> listOfItemOrder = new LinkedList<>();
        File file = new File("InventoryManagement/src/main/resources/com/example/inventorymanagement/data/"+type+"orders.json");
        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
        ){
            JsonElement rootElement = JsonParser.parseReader(bufferedReader);
            if (rootElement != null && rootElement.isJsonObject()) {
                JsonObject rootObject = rootElement.getAsJsonObject();
                JsonArray jsonArray = rootObject.getAsJsonArray(type+"Orders");
                if (jsonArray != null) {
                    for(JsonElement jsonElement: jsonArray){
                        Gson gson = new Gson();
                        ItemOrder itemOrder = gson.fromJson(jsonElement, ItemOrder.class);
                        listOfItemOrder.addLast(itemOrder);
                    }
                } else {
                    System.out.println("JSON array is null");
                }
            } else {
                System.out.println("Root element is null or not a JSON object");
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return listOfItemOrder;
    }



    public static synchronized LinkedList<Item> fetchListOfItems(){
        LinkedList<Item> itemList = new LinkedList<>();
        File file = new File("InventoryManagement/src/main/resources/com/example/inventorymanagement/data/items.json");
        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
                ){

            JsonElement rootElement = JsonParser.parseReader(bufferedReader);
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray itemJsonArray = rootObject.getAsJsonArray("items");
            for(JsonElement jsonElement : itemJsonArray){
                Gson gson = new Gson();
                Item item = gson.fromJson(jsonElement, Item.class);
                itemList.addLast(item);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return itemList;
    }

    public static synchronized boolean addUser(User newUser){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File("InventoryManagement/src/main/resources/com/example/inventorymanagement/data/users.json");
        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                ){
            JsonElement rootElement = JsonParser.parseReader(bufferedReader);
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray userJsonArray = rootObject.getAsJsonArray("users");

            if(!(userExists(userJsonArray, newUser.getUsername()))) {
                String jsonString = gson.toJson(newUser);
                JsonElement userElement = JsonParser.parseString(jsonString);
                userJsonArray.add(userElement);
                try (FileWriter writer = new FileWriter(file)) {
                    gson.toJson(rootElement, writer);
                }
                return true;
            }
            return false;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean userExists(JsonArray usersArray, String username){
        Gson gson = new Gson();

        for(JsonElement jsonElement : usersArray){
            User cUser = gson.fromJson(jsonElement, User.class);
            if(cUser.getUsername().equals(username)) return true;
        }
        return false;
    }

    private static boolean itemExists(JsonArray itemArray, String itemName){
        Gson gson = new Gson();

        for(JsonElement jsonElement : itemArray){
            Item cItem = gson.fromJson(jsonElement, Item.class);
            if(cItem.getItemName().equals(itemName)){
                return false;
            }
        }

        return false;
    }

    public static synchronized boolean removeUser(User toRemove){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File("InventoryManagement/src/main/resources/com/example/inventorymanagement/data/users.json");
        try{
            JsonElement rootElement = JsonParser.parseReader(new FileReader(file));
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray userArray = rootObject.getAsJsonArray("users");
            for(JsonElement jsonElement: userArray){
                User user = gson.fromJson(jsonElement, User.class);
                String cUsername = user.getUsername();
                String toRemoveUsername = toRemove.getUsername();
                if(cUsername.equals(toRemoveUsername)){
                    userArray.remove(jsonElement);
                    try(FileWriter fileWriter = new FileWriter(file)){
                        gson.toJson(rootElement, fileWriter);
                        return true;
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public synchronized static LinkedList<User> fetchListOfUsers(){
        LinkedList<User> userList = new LinkedList<>();
        Gson gson = new Gson();
        File file = new File("InventoryManagement/src/main/resources/com/example/inventorymanagement/data/users.json");
        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                ){
            JsonElement rootElement = JsonParser.parseReader(bufferedReader);
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray jsonArray = rootObject.getAsJsonArray("users");
            for(JsonElement jsonElement: jsonArray){
                userList.add(gson.fromJson(jsonElement, User.class));
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return userList;
    }

    public static Item fetchItem(int id){
        Gson gson = new Gson();
        File file = new File("InventoryManagement/src/main/resources/com/example/inventorymanagement/data/items.json");
        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
        ){

            JsonElement rootElement = JsonParser.parseReader(bufferedReader);
            JsonObject rootObject = rootElement.getAsJsonObject();
            JsonArray jsonArray = rootObject.getAsJsonArray("items");
            for(JsonElement jsonElement : jsonArray){
                Item item = gson.fromJson(jsonElement, Item.class);
                if(item.getItemId()==id){
                    return item;
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
