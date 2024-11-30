import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.Vector;

class IceCream {
    String type;
    int price;
    int stock;

    IceCream(String type, int price, int stock) {
        this.type = type;
        this.price = price;
        this.stock = stock;
    }

    public void updateStock(int quantity) {
        stock -= quantity;
    }

    public void updatePrice(int newPrice) {
        this.price = newPrice;
    }
}

class Customer {
    String name;
    String order;
    int quantity;
    String address;
    LocalDateTime orderTime;

    Customer(String name, String order, int quantity, String address, LocalDateTime orderTime) {
        this.name = name;
        this.order = order;
        this.quantity = quantity;
        this.address = address;
        this.orderTime = orderTime;
    }
}

class IceCreamService {
    Vector<IceCream> iceCreams = new Vector<>();
    Vector<Customer> customers = new Vector<>();
    int totalAmount = 0;
    final String ADMIN_PASSWORD = "siws";

    IceCreamService() {
        initializeIceCreams();
    }

    private void initializeIceCreams() {
        iceCreams.add(new IceCream("Vanilla", 50, 100));
        iceCreams.add(new IceCream("Chocolate", 60, 80));
        iceCreams.add(new IceCream("Strawberry", 70, 60));
    }

    public void placeOrder(String name, int choice, int quantity, String address) {
        IceCream selectedIceCream = iceCreams.get(choice - 1);
        selectedIceCream.updateStock(quantity);
        totalAmount += selectedIceCream.price * quantity;
        customers.add(new Customer(name, selectedIceCream.type, quantity, address, LocalDateTime.now()));
    }

    public void updatePrice(int choice, int newPrice) {
        iceCreams.get(choice - 1).updatePrice(newPrice);
    }

    public void updateStock(int choice, int newStock) {
        iceCreams.get(choice - 1).stock = newStock;
    }

    public void viewCustomersAndOrders() {
        for (Customer customer : customers) {
            System.out.println("Customer: " + customer.name + " | Ordered: " + customer.order + 
                               " | Quantity: " + customer.quantity + " | Address: " + customer.address + 
                               " | Order Time: " + customer.orderTime);
        }
    }

    public void viewStockAndPrices() {
        for (IceCream iceCream : iceCreams) {
            System.out.println(iceCream.type + " - Price: Rs " + iceCream.price + " | Stock: " + iceCream.stock);
        }
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public boolean validateAdminPassword(String password) {
        return password.equals(ADMIN_PASSWORD);
    }

    public Vector<IceCream> getIceCreams() {
        return iceCreams;
    }
}

class IceCreamManagementUI {
    IceCreamService service;
    Scanner sc = new Scanner(System.in);

    IceCreamManagementUI(IceCreamService service) {
        this.service = service;
    }

    public void displayMenu() {
        System.out.println("********** Welcome to Ice Cream Palace **********");
        for (int i = 0; i < service.getIceCreams().size(); i++) {
            IceCream iceCream = service.getIceCreams().get(i);
            System.out.println((i + 1) + ". " + iceCream.type + " - Rs " + iceCream.price + " (Stock: " + iceCream.stock + ")");
        }
        System.out.println("4. Exit");
        System.out.println("==================================================");
    }

    public void takeCustomerOrder() {
        sc.nextLine();
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter your address: ");
        String address = sc.nextLine();

        while (true) {
            displayMenu();
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            if (choice == 4) {
                System.out.println("Your total bill is: Rs " + service.getTotalAmount());
                break;
            }

            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please select a valid ice cream.");
                continue;
            }

            IceCream selectedIceCream = service.getIceCreams().get(choice - 1);
            System.out.println("You have selected " + selectedIceCream.type);
            System.out.print("Enter the quantity: ");
            int quantity = sc.nextInt();

            if (selectedIceCream.stock < quantity) {
                System.out.println("Sorry, we only have " + selectedIceCream.stock + " left in stock.");
                continue;
            }

            service.placeOrder(name, choice, quantity, address);
            System.out.print("Do you want to order more ice creams? (Y/N): ");
            String moreOrder = sc.next();
            if (moreOrder.equalsIgnoreCase("N")) {
                System.out.println("Your total bill is: Rs " + service.getTotalAmount());
                break;
            }
        }
    }

    public void adminMode() {
        System.out.print("Enter admin password: ");
        String enteredPassword = sc.next();

        if (!service.validateAdminPassword(enteredPassword)) {
            System.out.println("Incorrect password. Access denied.");
            return;
        }

        System.out.println("Access granted to Admin Mode.");
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. View Customers and Orders");
            System.out.println("2. Update Prices");
            System.out.println("3. Update Stock");
            System.out.println("4. View Stock and Prices");
            System.out.println("5. Exit Admin Mode");
            System.out.print("Enter your choice: ");
            int adminChoice = sc.nextInt();

            switch (adminChoice) {
                case 1 -> service.viewCustomersAndOrders();
                case 2 -> updatePrices();
                case 3 -> updateStock();
                case 4 -> service.viewStockAndPrices();
                case 5 -> {
                    System.out.println("Exiting Admin Mode...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void updatePrices() {
        System.out.println("Select the ice cream to update price:");
        for (int i = 0; i < service.getIceCreams().size(); i++) {
            IceCream iceCream = service.getIceCreams().get(i);
            System.out.println((i + 1) + ". " + iceCream.type + " - Rs " + iceCream.price);
        }
        int choice = sc.nextInt();
        if (choice >= 1 && choice <= service.getIceCreams().size()) {
            System.out.print("Enter new price for " + service.getIceCreams().get(choice - 1).type + ": ");
            int newPrice = sc.nextInt();
            service.updatePrice(choice, newPrice);
            System.out.println("Price updated successfully!");
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private void updateStock() {
        System.out.println("Select the ice cream to update stock:");
        for (int i = 0; i < service.getIceCreams().size(); i++) {
            IceCream iceCream = service.getIceCreams().get(i);
            System.out.println((i + 1) + ". " + iceCream.type + " - Stock: " + iceCream.stock);
        }
        int choice = sc.nextInt();
        if (choice >= 1 && choice <= service.getIceCreams().size()) {
            System.out.print("Enter new stock for " + service.getIceCreams().get(choice - 1).type + ": ");
            int newStock = sc.nextInt();
            service.updateStock(choice, newStock);
            System.out.println("Stock updated successfully!");
        } else {
            System.out.println("Invalid selection.");
        }
    }
}

public class V2_18041 {
    public static void main(String[] args) {
        IceCreamService service = new IceCreamService();
        IceCreamManagementUI ui = new IceCreamManagementUI(service);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Select Mode: ");
            System.out.println("1. Customer Mode");
            System.out.println("2. Admin Mode");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int mode = sc.nextInt();

            switch (mode) {
                case 1 -> ui.takeCustomerOrder();
                case 2 -> ui.adminMode();
                case 3 -> {
                    System.out.println("Exiting the system...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid selection. Please choose a valid mode.");
            }
        }
    }
}
