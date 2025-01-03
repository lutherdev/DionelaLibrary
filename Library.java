import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Library {
    private int itemId;
    private String title; // mga general attributes
    private String creator;
    private int quantity;

    Scanner input = new Scanner(System.in);
    accounts account;
    private ArrayList<Library> availableItems = new ArrayList<>(); // arrayList para sa items

    private int accounts = 0; // pang loop
    private int adminCount = 0; // pang limit ng size ng array tsaka pang loop
    private int studentCount = 0; // pang limit ng size ng array tsaka pang loop
    public static int UserID = 100; // automatic magbibigay ng id pra sa User --tatanggalin to

    private int Custcount = 0; // pang loop

    public Library(int itemId, String title, String creator, int quantity) {
        this.itemId = itemId;
        this.title = title;
        this.creator = creator;
        this.quantity = quantity;
    }

    public Library() {
        System.out.println("Normal Constructor");
        fileTOlist();
    }

    // METHODS

    private int generateItemId() { // pang generate ng item id
        return ++itemId;
    }

    public void removeItem() {
        Scanner input = new Scanner(System.in);

        System.out.println("--------Remove Item--------");
        System.out.println("Items available: ");
        displayInventory();
        System.out.print("Enter the ID of the item to be removed: ");
        int removeId = input.nextInt();
        input.nextLine();

        boolean itemFound = false;

        // while(more){
        for (int i = 0; i < availableItems.size(); i++) {
            if (availableItems.get(i).getItemId() == removeId) { // hahanapin kung match yung ID na ininput ng user sa
                                                                 // arraylist
                itemFound = true;
                availableItems.remove(i);
                saveItemsToFile();
                System.out.println("Item " + removeId + " has been removed");
                System.out.println("New list of items: ");
                displayInventory();
                break;
            }
        }

        if (!itemFound) {
            System.out.println("Item " + removeId + " not found");
        }
        displayInventory();
        // saveItemsToFile();
    }

    // add items
    public void addItem() { // mag aadd ng either book,dvd,magazine sa array
        System.out.println("--------Add Items--------");
        System.out.print("What do you want to add?\n1. Book\n2. DVD\n3. Magazine\nYour choice: ");
        int choice = input.nextInt();
        input.nextLine();

        System.out.print("\nEnter the title: ");
        String title = input.nextLine();
        System.out.print("Enter the creator: ");
        String creator = input.nextLine();
        System.out.print("Enter the quantity: ");
        int quantity = input.nextInt();
        input.nextLine();

        Library newItem = null;
        int itemId = generateItemId();
        switch (choice) {

            case 1: // pang add ng book
                System.out.print("Enter number of pages in the book: ");
                int pages = input.nextInt();
                input.nextLine();
                newItem = new Book(itemId, title, creator, quantity, pages);// gagawa ng object na book

                break;

            case 2: // pag add ng dvd
                System.out.print("Enter the duration of the DVD (Hours): ");
                int duration = input.nextInt();
                input.nextLine();
                newItem = new DVD(itemId, title, creator, quantity, duration);
                break;

            case 3:
                // pag add ng magazine
                System.out.print("Enter the issue number of the magazine: ");
                int issueNumber = input.nextInt();
                input.nextLine();
                newItem = new Magazine(itemId, title, creator, quantity, issueNumber);
                break;
        }

        if (newItem != null) {
            boolean ExistAlready = false;
            for (Library item : availableItems) {
                if (item.getTitle().equals(newItem.getTitle()) && item.getCreator().equals(newItem.getCreator())
                        && item.getClass().equals(newItem.getClass())) {
                    item.increaseQuantity(quantity);
                    ExistAlready = true;
                    break;
                }

            }
            if (ExistAlready == false) {
                availableItems.add(newItem);
            }
            saveItemsToFile(); // FOR SAVING THE CURRENT CHANGES
        }

    }

    public void saveItemsToFile() { // test
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("libraryItems.txt"))) {
            for (Library item : availableItems) {
                String itemType = item.getClass().getSimpleName().toLowerCase();
                if (item instanceof Book) {
                    Book book = (Book) item;
                    writer.write(book.getItemId() + ", " + book.getTitle() + ", " + book.getCreator() + ", "
                            + book.getQuantity() + ", " + itemType + ", " + book.getPages() + "\n");
                }
                else if (item instanceof Magazine) {
                    Magazine magazine = (Magazine) item;
                    writer.write(magazine.getItemId() + ", " + magazine.getTitle() + ", " + magazine.getCreator() + ", "
                            + magazine.getQuantity() + ", " + itemType + ", " + magazine.getIssueNumber() + "\n");
                }
                else if (item instanceof DVD) {
                    DVD dvd = (DVD) item;
                    writer.write(dvd.getItemId() + ", " + dvd.getTitle() + ", " + dvd.getCreator() + ", "
                            + dvd.getQuantity() + ", " + itemType + ", " + dvd.getHours() + "\n");
                }
            }
            writer.close();
            System.out.println("ITEMS SUCCESS UPDATED TO FILE..\n");
        } catch (IOException e) {
            System.out.println("An error occurred while saving items to the file.");
            e.printStackTrace();
        }
    }

    public void fileTOlist() { // test
        try {
            BufferedReader reader = new BufferedReader(new FileReader("libraryItems.txt"));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                this.itemId = Integer.parseInt(parts[0].trim());
                this.title = parts[1].trim();
                this.creator = parts[2].trim();
                this.quantity = Integer.parseInt(parts[3].trim());

                String itemType = parts[4].trim();
                Library newItem = null;

                switch (itemType.toLowerCase()) {
                    case "book":
                        int pages = Integer.parseInt(parts[5].trim());
                        newItem = new Book(itemId, title, creator, quantity, pages);
                        break;

                    case "dvd":
                        int hours = Integer.parseInt(parts[5].trim());
                        newItem = new DVD(itemId, title, creator, quantity, hours);
                        break;

                    case "magazine":
                        int issueNumber = Integer.parseInt(parts[5].trim());
                        newItem = new Magazine(itemId, title, creator, quantity, issueNumber);
                        break;

                    default:
                        break;

                }
                if (newItem != null) {
                    availableItems.add(newItem);
                }
            }
            System.out.println("Successfully loaded the text file.");
        } catch (IOException e) {
            System.out.println("An error occurred while reading items to the file." + e.getMessage());
            // e.printStackTrace();
        }
    }

    public void displayInventory() { // display inventory
        System.out.println("\t---------------Available Items----------------");
        System.out.printf("%-10s %-30s %-20s %-10s%n", "Item ID", "Title", "Creator", "Quantity"); // Added Creator
                                                                                                   // column
        System.out.println("----------------------------------------------------");

        if (availableItems.size() != 0) { // checks if the availableItems list is not empty
            for (Library item : availableItems) {
                // Display each item's details, including the creator
                System.out.printf("%-10d %-30s %-20s %-10d%n", item.getItemId(), item.getTitle(), item.getCreator(),
                        item.getQuantity());
            }
        } else {
            System.out.println("No items in inventory");
        }
    }

    public void displayInfo() { // iooverride ng mga children
        System.out.println("Item ID: " + getItemId());
        System.out.println("Title: " + getTitle());
        System.out.println("Creator: " + getCreator());
        System.out.println("Quantity: " + getQuantity());
    }

    public int borrowItem(int quantity, int itemId) {
        Scanner scan = new Scanner(System.in);
            for (Library items : availableItems) {
                if (items.getItemId() == itemId) { // PUT A CONDITION NA KAPAG MERON PANG QUANTITY OR WALA
                    if (items.getQuantity() >= quantity && quantity > 0) {
                        items.quantity = (items.quantity - quantity);
                        System.out.println("\nUpdated Quantity of "+ items.getTitle() + ": " + items.getQuantity());
                        saveItemsToFile();
                        return 1;
                    } else {
                        System.out.println("Not enough quantity available.");
                        return 2;
                    }
                }
            }
            return 2;
    }

    // RETURNING METHODSSS
    public void returnItem(int itemId, int quantity) {
        for (Library item : availableItems) {
            if (item.getItemId() == itemId) {
            item.quantity = (item.quantity + quantity);
            System.out.println("Item " + item.getTitle() + " with ID " + itemId + " has been returned");
            System.out.println("New quantity: " + item.getQuantity()); // pang check lang
            saveItemsToFile();
            System.out.println("UPDATE QUANTITY SUCCESS");
            break;
            }
        }
    }
    public int getItemId() {
        return itemId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCreator() {
        return creator;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increaseQuantity(int returned) {
        this.quantity += returned; // pagupdate directaa sa quantity
    }

    public void setQuantity(int quantity) { // pra sa Quantityyy
        this.quantity = quantity;
    }

}
