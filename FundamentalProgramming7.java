package day3;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class FundamentalProgramming7 {

    // Products Array
    static String[] listItems = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
    static int[] listStockItems = { 10, 9, 5, 4, 7 };
    static float[] listPriceItems = { 10000, 20000, 30000, 25000, 55000 };

    // User Array
    static String[] listUsers = { "Fahri", "Rizal", "Adi", "Rani", "Ridwan" };

    // PaymentMethods Array
    static String[] listPaymentMethods = { "cash", "bank transfer", "e-wallet" };
    static float[] listFeePaymentMethods = { 2, 1.5f, 3 };

    // PaymentStatus Array
    static String[] listPaymentStatus = { "pending", "in progress", "completed" };

    // MENU ARRAY
    static String[] listMenus = { "SEE PRODUCTS", "DO TRANSACTIONS", "HISTORY TRANSACTION", "EXIT OR LOGOUT" };

    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        displayLogin();
        in.close();
    }

    // VOID DISPLAY LOGIN
    public static void displayLogin() {
        boolean isValid = false;

        while (!isValid) {
            System.out.println("===LOGIN===");
            System.out.print("Input username : ");
            String user = in.nextLine();
            System.out.print("Input password : ");
            String password = in.nextLine();
            if (isValidLogin(user, password)) {
                displayMenu();
                isValid = true;
            } else {
                System.out.println("Username or Password is Invalid. Please, try again!!!");
            }
        }
    }

    // CHECK USER LOGIN VALIDATION
    public static boolean isValidLogin(String user, String password) {
        int i = 0;
        String[] usersValid = { "Randy" };
        String[] passwordValid = { "admin" };

        for (String userValid : usersValid) {
            if (user.equalsIgnoreCase(userValid) && password.equalsIgnoreCase(passwordValid[i])) {
                return true;
            }
            i++;
        }
        return false;
    }

    // VOID DISPLAY MENU
    public static void displayMenu() {
        String[][] listTransaction = new String[50][7];
        String[][] listDetailTransaction = new String[50][5];

        int n = 1;
        int menu;
        boolean hasMenu = true;

        while (hasMenu) {
            System.out.println("===MENU===");
            for (String i : listMenus) {
                if (n == listMenus.length) {
                    n = 0;
                }
                System.out.println(n + ". " + i);
                n++;
            }

            System.out.print("Input Menu : ");
            menu = Integer.valueOf(in.nextLine());

            switch (menu) {
                case 1:
                    displayProduct();
                    hasMenu = backToMenu();
                    break;
                case 2:
                    displayProccessTransaction(listTransaction, listDetailTransaction);
                    hasMenu = backToMenu();
                    break;
                case 3:
                    displayTransaction(listTransaction);
                    hasMenu = backToMenu();
                    break;
                case 0:
                    // System.exit(0);
                    hasMenu = false;
                    break;
                default:
                    System.out.println("Invalid Menu!!!\nTry again!");
                    break;
            }

        }
    }

    // VOID TO DO TRANSACTION
    public static void displayProccessTransaction(String[][] listTransaction, String[][] listDetailTransaction) {
        System.out.print("Input Username : ");
        String user = in.nextLine();

        if (isValidUser(listUsers, user)) {
            displayProduct();

            System.out.print("Input total product want to buy : ");
            int totalProduct = Integer.valueOf(in.nextLine());

            // FROM USER INPUT
            String[] itemBuy = new String[totalProduct];
            int[] quantity = new int[totalProduct];
            float[] totalPriceItem = new float[itemBuy.length];
            inputItem(itemBuy, quantity, totalProduct);
            for (int i = 0; i < itemBuy.length; i++) {
                if (!isAvailable(itemBuy[i], quantity[i])) {
                    itemBuy[i] = "";
                    quantity[i] = 0;
                }
                quantity[i] = getReplace(itemBuy[i], quantity[i]);
            }

            listStockItems = updateStockTransaction(itemBuy, quantity);
            totalPriceItem = countTotalPriceItem(itemBuy, quantity);

            displayPaymentMethod();
            System.out.print("Input Payment Method : ");
            int paymentMethod = Integer.valueOf(in.nextLine());

            float totalTransaction = sumTotalTransaction(totalPriceItem);
            float fee = listFeePaymentMethods[(paymentMethod - 1)];
            float totalFee = sumTotalFee(totalTransaction, fee);
            float totalTransactionWithFee = sumTotalTransactionWithFee(totalTransaction,
                    totalFee);

            listTransaction = saveTransaction(paymentMethod - 1,
                    2,
                    listTransaction, totalTransaction, fee, totalFee,
                    totalTransactionWithFee);
            listDetailTransaction = saveTransactionDetail(getIndex(listUsers, user),
                    itemBuy, quantity,
                    totalPriceItem,
                    listDetailTransaction, getIndexNotNull(listTransaction));

            displayDetailTransaction(user, listPaymentMethods[paymentMethod - 1],
                    itemBuy, quantity,
                    totalPriceItem,
                    totalTransaction, fee, totalFee, totalTransactionWithFee,
                    listPaymentStatus[2]);

            displayProduct();
        } else

        {
            System.out.println("User Not Found");
        }

        displayTransaction(listTransaction);
        displayTransactionDetail(listDetailTransaction, 0);
        System.out.println("INDEX LIST TRANSACTION NOW : " +
                getIndexNotNull(listTransaction));
        System.out.println("INDEX LIST TRANSACTION DETAIL NOW : " +
                getIndexNotNull(listDetailTransaction));
    }

    // VOID DISPLAY FOR INPUT ITEM
    public static void inputItem(String[] itemBuy, int[] quantity, int totalProduct) {
        Scanner in = new Scanner(System.in);
        boolean isFinish = false;
        int n = 0;
        while (!isFinish && n < totalProduct) {
            if (n > 0) {
                System.out.print(
                        "Max Item " + n + "/" + itemBuy.length
                                + ". Do you want to stop add item (input : 'yes') or ");
            }
            System.out.print("Input Item " + (n + 1) + " name : ");
            itemBuy[n] = in.nextLine();
            if (itemBuy[n].equalsIgnoreCase("Yes")) {
                isFinish = true;
            } else {
                if (itemBuy[n].equalsIgnoreCase("")) {
                    quantity[n] = 0;
                } else {
                    System.out.print("Input quantity item " + (n + 1) + " : ");
                    quantity[n] = Integer.valueOf(in.nextLine());
                }
                n++;
            }
        }
    }

    // VALIDATION TO GET BACK TO MENU
    public static boolean backToMenu() {
        System.out.print("Input '0' to back to menu : ");
        int input = Integer.valueOf(in.nextLine());
        if (input == 0) {
            return true;
        }
        return false;
    }

    // DISPLAY DETAIL TRANSACTION
    public static void displayDetailTransaction(String user, String paymentMethod, String[] itemBuy, int[] quantity,
            float[] totalPriceItem,
            float totalTransaction, float fee, float totalFee, float totalTransactionWithFee, String paymentStatus) {
        int j = 1;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        System.out.println("=====Detail Transaction=====");
        System.out.println("Username\t: " + user);
        System.out.println("Date\t\t: " + date);
        System.out.println("Payment Method\t: " + paymentMethod);
        System.out.println("Payment Status\t: " + paymentStatus);
        System.out.println("Name\t\tQuantity\tTotal Price Item");
        for (int i = 0; i < itemBuy.length; i++) {
            if (quantity[i] != 0) {
                System.out.println(
                        j + ". " + itemBuy[i] + "\t" + quantity[i] + "\t\t" + setFormatCurrency(totalPriceItem[i]));
                j++;
            }
        }
        System.out.println("Total Transaction\t\t: " + setFormatCurrency(totalTransaction));
        System.out.println("Transaction Fee (Rp)\t\t: " + setFormatCurrency(totalFee));
        System.out.println("Grand Total\t: " + setFormatCurrency(totalTransactionWithFee));
    }

    // FIND USER
    public static boolean isValidUser(String[] listUsers, String userF) {
        for (String user : listUsers) {
            if (user.equalsIgnoreCase(userF)) {
                return true;
            }
        }
        return false;
    }

    // FIND INDEX
    public static int getIndex(String[] list, String find) {
        int n = 0;
        for (String i : list) {
            if (i.equalsIgnoreCase(find)) {
                return n;
            }
            n++;
        }
        return 0;
    }

    // CHECK IF STOCK AVAILABLE (STOCK > 0)
    public static boolean isAvailable(String itemBuy, int quantity) {
        for (int i = 0; i < listItems.length; i++) {
            if (listItems[i].equalsIgnoreCase(itemBuy)) {
                int stock = listStockItems[i];
                return stock > 0;
            }
        }
        return false;
    }

    // REPLACE QUANTITY TO STOCK IF INPUT IS MORE THAN STOCK
    public static int getReplace(String itemBuy, int quantity) {
        for (int i = 0; i < listItems.length; i++) {
            if (listItems[i].equalsIgnoreCase(itemBuy)) {
                int stock = listStockItems[i];
                if (!(stock - quantity >= 0)) {
                    return stock;
                }
                ;
            }
        }
        return quantity;
    }

    // COUNT TOTAL PRICE PER ITEM
    public static float[] countTotalPriceItem(String[] itemBuy, int[] quantity) {
        float[] total = new float[itemBuy.length];
        for (int i = 0; i < itemBuy.length; i++) {
            for (int j = 0; j < listItems.length; j++) {
                if (itemBuy[i].equalsIgnoreCase(listItems[j])) {
                    total[i] = quantity[i] * listPriceItems[j];
                }
            }
        }
        return total;
    }

    // UPDATE STOCK
    public static int[] updateStockTransaction(String[] itemBuy, int[] quantity) {
        for (int i = 0; i < itemBuy.length; i++) {
            for (int j = 0; j < listItems.length; j++) {
                if (itemBuy[i].equalsIgnoreCase(listItems[j])) {
                    listStockItems[j] = listStockItems[j] - quantity[i];
                }
            }
        }
        return listStockItems;
    }

    // SUM ALL TRANSACTION BY TOTAL PRICE PER ITEM
    public static float sumTotalTransaction(float[] totalPriceItem) {
        float total = 0;
        for (float i : totalPriceItem) {
            total += i;
        }
        return total;
    }

    // SUM TOTAL FEE
    public static float sumTotalFee(float totalTransaction, Float fee) {
        return totalTransaction * (fee / 100);
    }

    // SUM TOTAL TRANSACTION WITH FEE
    public static float sumTotalTransactionWithFee(float totalTransaction, float totalFee) {
        return totalTransaction + totalFee;
    }

    // COUNT TOTAL CHARGE
    public static float countTotalCharge(float totalTransaction, float money) {
        return money - totalTransaction;
    }

    // FORMAT CURRENCY RUPIAH
    public static String setFormatCurrency(float format) {
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        decimalFormat.setDecimalFormatSymbols(formatRp);
        return decimalFormat.format(format);
    }

    // FIND INDEX WHERE IS NOT NULL
    public static int getIndexNotNull(String[][] listTransaction) {
        int n = 0;
        for (String[] list : listTransaction) {
            for (String i : list) {
                if (i == null) {
                    return n;
                }
            }
            n++;
        }
        return 1;
    }

    // SAVING TRANSACTION
    public static String[][] saveTransaction(int paymentMethodId, int paymentStatusId, String[][] listTransaction,
            float totalTransaction, float fee, float totalFee, float totalTransactionWithFee) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        int lastTransaction = getIndexNotNull(listTransaction);

        listTransaction[lastTransaction][0] = date;
        listTransaction[lastTransaction][1] = String.valueOf(totalTransaction);
        listTransaction[lastTransaction][2] = String.valueOf(fee);
        listTransaction[lastTransaction][3] = String.valueOf(totalFee);
        listTransaction[lastTransaction][4] = String.valueOf(totalTransactionWithFee);
        listTransaction[lastTransaction][5] = String.valueOf(paymentStatusId);
        listTransaction[lastTransaction][6] = String.valueOf(paymentMethodId);
        return listTransaction;
    }

    // SAVING DETAIL TRANSACTION
    public static String[][] saveTransactionDetail(int userId, String[] itemBuy,
            int[] quantity,
            float[] totalPriceItem, String[][] listTransactionDetails, int transactionId) {

        for (int i = 0; i < itemBuy.length; i++) {
            for (int j = 0; j < itemBuy.length; j++) {
                listTransactionDetails[i][0] = String.valueOf(userId);
                listTransactionDetails[i][1] = itemBuy[j];
                listTransactionDetails[i][2] = String.valueOf(quantity[j]);
                listTransactionDetails[i][3] = String.valueOf(totalPriceItem[j]);
                listTransactionDetails[i][4] = String.valueOf(transactionId);
            }
        }
        return listTransactionDetails;
    }

    // OPTIONAL
    // DISPLAY PRODUCT
    public static void displayProduct() {
        System.out.println("=== List Item ===");
        System.out.println("Name\t\tStock\tPrice");
        for (int i = 0; i < listItems.length; i++) {
            if (listStockItems[i] > 0) {
                System.out.println(
                        i + 1 + ". " + listItems[i] + "\t" + listStockItems[i] + "\t" + listPriceItems[i]);
            }
        }
    }

    // DISPLAY PAYMENT METHOD
    public static void displayPaymentMethod() {
        System.out.println("=== List Payment Method ===");
        System.out.println("No\tTransation Fee\t\tName");
        for (int i = 0; i < listPaymentMethods.length; i++) {
            System.out.println(
                    (i + 1) + ". \t" + listFeePaymentMethods[i] + "\t\t\t"
                            + listPaymentMethods[i]);
        }
    }

    // DISPLAY TRANSACTION
    public static void displayTransaction(String[][] listTransactions) {
        System.out.println("=====List Transaction=====");

        if (getIndexNotNull(listTransactions) == 0) {
            System.out.println("NO TRANSACTION HISTORY");
        } else {
            for (int i = 0; i < getIndexNotNull(listTransactions); i++) {
                System.out.println("Transaction Number : " + (i + 1));
                System.out.println("Date\t\t: " + listTransactions[i][0]);
                System.out.println("Payment Method\t: " + listPaymentMethods[Integer.valueOf(listTransactions[i][6])]);
                System.out.println("Payment Status\t: " + listPaymentStatus[Integer.valueOf(listTransactions[i][5])]);
                System.out.println(
                        "Total Transaction\t\t: " + setFormatCurrency(Float.parseFloat(listTransactions[i][1])));
                System.out.println(
                        "Transaction Fee (Rp)\t\t: " + setFormatCurrency(Float.parseFloat(listTransactions[i][3])));
                System.out.println(
                        "Grand Total\t: " + setFormatCurrency(Float.parseFloat(listTransactions[i][4])));
            }
        }
    }

    // DISPLAY TRANSACTION
    public static void displayTransactionDetail(String[][] listTransactionDetails, int transactionId) {
        int j = 1;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        System.out.println("=====List Transaction Detail=====");

        for (int i = 0; i < getIndexNotNull(listTransactionDetails); i++) {
            // if (Integer.valueOf(listTransactionDetails[i][4]) == transactionId) {
            System.out.println("Username\tName\t\tQuantity\tTotal Price Item");
            // if (Integer.valueOf(listTransactionDetails[transactionId][i]) != 0) {
            System.out.println(
                    listUsers[Integer.valueOf(listTransactionDetails[transactionId][0])] + "\t\t"
                            + listTransactionDetails[i][1]
                            + "\t\t"
                            + listTransactionDetails[i][2] + "\t\t" +
                            listTransactionDetails[i][3]);
            j++;
            // }
            // }

        }
    }
}

// Yang dimasukan kedalam transaction adalah id (selesai)
// Parameter yang terdapat dalam method adalah parameter yang akan diolah
// (selesai)
// Variable yang akan dijadikan sebagai pembanding dalam sebuah method dijadikan
// publik (selesai)
// Menambahkan fitur login pada sebuah aplikasi (selesai)
// Stok pada sebuah produk. Produk tidak akan ditampilkan apabila stok 0
// (selesai)
// Jika admin input pembelian dengan item melebihi jumlah stok maka quantity
// akan menyesuaikan jumlah stok (selesai)
