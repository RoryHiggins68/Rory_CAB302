package Asset_Trading;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

//db pass should be: root
// server name: MariaDB
//TCP port:3306
public class DBConnect{

    public static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS Users ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "FirstName VARCHAR(15),"
                    + "LastName VARCHAR(15),"
                    + "UserName VARCHAR(15),"
                    + "Password VARCHAR(30),"
                    + "Team VARCHAR(30)" + ");";

    public static final String CREATE_ASSET_TABLE =
            "CREATE TABLE IF NOT EXISTS Assets ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "AssetName VARCHAR(15),"
                    + "HighPrice VARCHAR(10),"
                    + "LowPrice VARCHAR(10),"
                    + "Description VARCHAR(150)" + ");";

    public static final String CREATE_SHOP_TABLE =
            "CREATE TABLE IF NOT EXISTS Shop ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "Asset VARCHAR(15),"
                    + "TeamRequest VARCHAR(15),"
                    + "BuyOrSell VARCHAR(15),"
                    + "Price VARIANT(15),"
                    + "Details VARCHAR(30)" + ");";


    public static final String CREATE_TEAM_TABLE =
            "CREATE TABLE IF NOT EXISTS Team ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "TeamName VARCHAR(15),"
                    + "TeamLeader VARCHAR(15),"
                    + "Credits VARCHAR(15)" + ");";

    public static final String CREATE_SHOP_HISTORY_TABLE =
            "CREATE TABLE IF NOT EXISTS ShopHistory ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "Asset VARCHAR(15),"
                    + "BuyTeam VARCHAR(15),"
                    + "SellTeam VARCHAR(15),"
                    + "Price VARIANT(15)" + ");";


    private static Connection connection = null;

    //public static void main(String[] args) throws SQLException, IOException {
    public DBConnect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:asset_trading.db", "", "");

        createDatabase(connection);
        int num = numRows(connection);
        if (num == 0){
            System.out.println("the number of rows is " + num + " database needs populating");
        }else{
            System.out.println("the number of rows is " + num + " database has been populated");
        }

        if(num < 7){
            PopulateDatabase(connection);
        }

        num = numRows(connection);
        if (num == 0){
            System.out.println("the number of rows is " + num + " database needs populating");
        }else{
            System.out.println("the number of rows is " + num + " database has been populated");
        }

    }
    public static void getAll(Connection connection) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        resultSet.next();
        while (resultSet.next()){

            String Asset = resultSet.getString(2);
            String Price = resultSet.getString(5);

            System.out.println(Asset + ": $" + Price);
        }

    }
    public static void printNth(Connection connection, int Nth ) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        for (int i = 0; i < Nth; i++) {resultSet.next();}

        String Asset = resultSet.getString(2);
        String Team =  resultSet.getString(3);
        String BuyOrSell =  resultSet.getString(4);
        String Price = resultSet.getString(5);

        System.out.println("The "+Team+" team is looking to "+BuyOrSell+" "+Asset+" for C" + Price);

    }
    public static String getNthAsset(Connection connection, int Nth ) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        //resultSet.next();
        if(resultSet.isClosed())
            System.out.println("ThE RESULT WAS CLOSED, might be empty");




        for (int i = 0; i < Nth; i++) {
            resultSet.next();
//            String indx = resultSet.getString(1);
//            String N = String.valueOf(Nth);
//
//            if(indx.equals(N)){
//                i = Nth;
//            }
        }


        String Asset = resultSet.getString(2);
        return Asset;
    }
    public static String getNthTeam(Connection connection, int Nth ) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        //resultSet.next();

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String Team = resultSet.getString(3);
        return Team;
    }
    public static String getNthBuyOrSell(Connection connection, int Nth ) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        //resultSet.next();

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String BuyOrSell = resultSet.getString(4);
        return BuyOrSell;
    }
    public static String getNthPrice(Connection connection, int Nth ) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        //resultSet.next();

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String Price = resultSet.getString(5);
        return Price;
    }
    public static String getNth(Connection connection, int Nth ) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        resultSet.next();
        for (int i = 0; i < Nth; i++) { resultSet.next();}

        String Asset = resultSet.getString(2);
        String Team =  resultSet.getString(3);
        String BuyOrSell =  resultSet.getString(4);
        String Price = resultSet.getString(5);

        System.out.println("The "+Team+" team is looking to "+BuyOrSell+" "+Asset+" for C" + Price);

        return Asset;
    }
    public static String getTeam(Connection connection, String  UserName) throws SQLException {

        String team = " ";
        String uName;
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Users;");

        for (int i = 0; i < 5; i++) {
            resultSet.next();
            uName = resultSet.getString(4);
            if (uName.equals(UserName)) {
                team = resultSet.getString(6);
                return team;
            }
        }
        return team;

    }


    public static String getShopHisAsset(Connection connection, int Nth ) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from ShopHistory;");

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String Asset = resultSet.getString(2);
        return Asset;
    }
    public static String getShopHisBuyTeam(Connection connection, int Nth ) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from ShopHistory;");

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String BuyTeam = resultSet.getString(3);
        return BuyTeam;

    }
    public static String getShopHisSellTeam(Connection connection, int Nth ) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from ShopHistory;");

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String SellTeam = resultSet.getString(4);
        return SellTeam;
    }
    public static String getShopHisPrice(Connection connection, int Nth )throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from ShopHistory;");

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String Price = resultSet.getString(5);
        return Price;
    }




    public static int getCredits(Connection connection, String  UserName){
/*
        String uName;
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Users;");


        for (int i = 0; i < 5; i++) {
            resultSet.next();
            uName = resultSet.getString(4);
            if (uName.equals(UserName)) {
                team = resultSet.getString(6);
                return team;

            }
        }
*/
        return 2500;


    }
    public static int numRows(Connection connection) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        resultSet.next();
        int rowNum =  resultSet.getRow();

        if (rowNum == 0){
            System.out.println("The num is  " + rowNum);
        }else{
            int i = 1;
            while(i==1){
                resultSet.next();
                if (resultSet.getRow() > rowNum){
                    rowNum =resultSet.getRow();
                }else{
                    i = 2;
                }
            }
        }

        return rowNum;

    }
    public static Connection getInstance() throws SQLException {
        if (connection == null) {
            new DBConnect();
        }
        return connection;
    }
    public static void PopulateDatabase(Connection connection) throws SQLException{


        String firstName = "bob";
        String lastName = "smith";
        String Username = "bob_smith";
        String Password = "smithbob";
        String Team = "A";

        addUser(connection,firstName,lastName,Username,Password,Team);

        firstName = "jane";
        lastName = "smith";
        Username = "jane_smith";
        Password = "password";
        Team = "A";

        addUser(connection,firstName,lastName,Username,Password,Team);

        firstName = "Jack";
        lastName = "John";
        Username = "John_j";
        Password = "password";
        Team = "A";

        addUser(connection, firstName,lastName,Username,Password,Team);

        firstName = "rob";
        lastName = "Jo";
        Username = "rob_jo";
        Password = "password";
        Team = "A";

        addUser(connection, firstName,lastName,Username,Password,Team);

        firstName = "mat";
        lastName = "J";
        Username = "mat_j";
        Password = "password";
        Team = "A";

        addUser(connection, firstName,lastName,Username,Password,Team);


        String AssetName = "CPU time";
        String HighPrice = "40";
        String LowPrice = "20";
        String Description = "CPU time on one of the servers to used for what ever the user needs";

        addAsset(connection, AssetName,HighPrice,LowPrice,Description);


        AssetName = "Printing x 10";
        HighPrice = "5";
        LowPrice = "1";
        Description = "Printing services";

        addAsset(connection, AssetName,HighPrice,LowPrice,Description);

        String Asset = "Printing";
        String TeamRequest = "A";
        String BuyOrSell = "Buy";
        String Price = "2";
        String Details = "Printing services required";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);

        Asset = "Printing";
        TeamRequest = "B";
        BuyOrSell = "Sell";
        Price = "2";
        Details = "Printing services required";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);

        Asset = "CPU Time";
        TeamRequest = "A";
        BuyOrSell = "Buy";
        Price = "100";
        Details = "Printing services required";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);

        Asset = "CPU Time";
        TeamRequest = "C";
        BuyOrSell = "Sell";
        Price = "125";
        Details = "CPU time on one of the servers to used for what ever the user needs";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);
        Asset = "CPU Time";
        TeamRequest = "C";
        BuyOrSell = "Buy";
        Price = "120";
        Details = "CPU time on one of the servers to used for what ever the user needs";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);
        Asset = "Computer";
        TeamRequest = "C";
        BuyOrSell = "Buy";
        Price = "1000";
        Details = "CPU time on one of the servers to used for what ever the user needs";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);
        Asset = "Computer";
        TeamRequest = "F";
        BuyOrSell = "Sell";
        Price = "1000";
        Details = "CPU time on one of the servers to used for what ever the user needs";

        //addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);
        Asset = "Lazering";
        TeamRequest = "F";
        BuyOrSell = "Sell";
        Price = "100000";
        Details = "CPU time on one of the servers to used for what ever the user needs";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);

        Asset = "Lazering";
        TeamRequest = "G";
        BuyOrSell = "Buy";
        Price = "10";
        Details = "CPU time on one of the servers to used for what ever the user needs";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);

        Asset = "Proof reading";
        TeamRequest = "C";
        BuyOrSell = "Buy";
        Price = "150";
        Details = "CPU time on one of the servers to used for what ever the user needs";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);

        Asset = "Proof reading";
        TeamRequest = "A";
        BuyOrSell = "Sell";
        Price = "150";
        Details = "CPU time on one of the servers to used for what ever the user needs";

        Asset = "IT services";
        TeamRequest = "A";
        BuyOrSell = "Sell";
        Price = "150";
        Details = "CPU time on one of the servers to used for what ever the user needs";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);

        Asset = "IT services";
        TeamRequest = "B";
        BuyOrSell = "Buy";
        Price = "140";
        Details = "CPU time on one of the servers to used for what ever the user needs";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);

        Team = "great";
        String TeamLeader = "bob_sm";
        String Credits = "1200";

        addTeam(connection, Team, TeamLeader,Credits );

        Team = "A";
        TeamLeader = "bob_smith";
        Credits = "1200";

        addTeam(connection, Team, TeamLeader,Credits );

        Team = "B";
        TeamLeader = "John_j";
        Credits = "1500";

        addTeam(connection, Team, TeamLeader,Credits );

        Team = "C";
        TeamLeader = "jane_smith";
        Credits = "1500";

        addTeam(connection, Team, TeamLeader,Credits );

        Team = "C";
        TeamLeader = "j";
        Credits = "1500";

        addTeam(connection, Team, TeamLeader,Credits );



    }
    public static void createDatabase(Connection connection) throws SQLException {

        Statement st = connection.createStatement();
        st.execute(CREATE_USER_TABLE);
        st.execute(CREATE_ASSET_TABLE);
        st.execute(CREATE_SHOP_TABLE);
        st.execute(CREATE_TEAM_TABLE);
        st.execute(CREATE_SHOP_HISTORY_TABLE);

    }
    public static void addAsset(Connection connection,String AssetName, String HighPrice, String LowPrice, String Description) throws SQLException {
        String INSERT_ASSET = "INSERT INTO Assets (AssetName, HighPrice, LowPrice, Description) VALUES (?, ?, ?, ?);";

        PreparedStatement addAsset = connection.prepareStatement(INSERT_ASSET);

        addAsset.setString(1, AssetName);
        addAsset.setString(2, HighPrice);
        addAsset.setString(3, LowPrice);
        addAsset.setString(4, Description);


        addAsset.execute();

    }
    public static void addItemToShop(Connection connection,String Asset, String TeamRequest, String BuyOrSell, String Price) throws SQLException{
        String INSERT_SHOPITEM = "INSERT INTO Shop (Asset, TeamRequest, BuyOrSell, Price, Details) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement addShopItem = connection.prepareStatement(INSERT_SHOPITEM);

        String Details = "Test Details string ";
        addShopItem.setString(1, Asset);
        addShopItem.setString(2, TeamRequest);
        addShopItem.setString(3, BuyOrSell);
        addShopItem.setString(4, Price);
        addShopItem.setString(5, Details);

        addShopItem.execute();

    }
    public static void addUser(Connection connection, User U) throws SQLException{

        String INSERT_USER = "INSERT INTO Users (FirstName, LastName, UserName, Password, Team) VALUES (?, ?, ?, ?, ?);";

        PreparedStatement addUser = connection.prepareStatement(INSERT_USER);

        addUser.setString(1, U.getFirstName());
        addUser.setString(2, U.getLastName());
        addUser.setString(3, U.getUserName());
        addUser.setString(4, U.getPassword());
        addUser.setString(5, U.getTeam());

        addUser.execute();


    }
    public static void addUser(Connection connection, String firstName, String lastName, String Username, String Password, String Team) throws SQLException {



        String INSERT_USER = "INSERT INTO Users (FirstName, LastName, UserName, Password, Team) VALUES (?, ?, ?, ?, ?);";

        PreparedStatement addUser = connection.prepareStatement(INSERT_USER);

        addUser.setString(1, firstName);
        addUser.setString(2, lastName);
        addUser.setString(3, Username);
        addUser.setString(4, Password);
        addUser.setString(5, Team);

        addUser.execute();

    }
    public static void addTeam(Connection connection, String TeamName, String TeamLeader, String Credits) throws SQLException {

        String INSERT_TEAM = "INSERT INTO Team (TeamName, TeamLeader, Credits) VALUES (?, ?, ?);";

        PreparedStatement addTeam = connection.prepareStatement(INSERT_TEAM);

        addTeam.setString(1, TeamName);
        addTeam.setString(2, TeamLeader);
        addTeam.setString(3, Credits);

        if(userExists(connection, TeamLeader)){
            addTeam.execute();
        }else{
            System.out.println("No such user ");
        }

    }
    public static void addShopHistoryItem(Connection connection, String Asset, String BuyTeam, String SellTeam, String Price) throws SQLException {
        String INSERT_SHOPHISTORY = "INSERT INTO ShopHistory (Asset, BuyTeam, SellTeam, Price) VALUES (?, ?, ?, ?);";

        PreparedStatement addShopHistoryItem = connection.prepareStatement(INSERT_SHOPHISTORY);

        addShopHistoryItem.setString(1, Asset);
        addShopHistoryItem.setString(2, BuyTeam);
        addShopHistoryItem.setString(3, SellTeam);
        addShopHistoryItem.setString(4, Price);
        addShopHistoryItem.execute();
    }
    public static void removeItemFromShop(Connection connectionOrSell, int index) throws SQLException{

       PreparedStatement statement = connection.prepareStatement("DELETE FROM Shop WHERE idx = ? ;");
       statement.setInt(1,index);
       statement.executeUpdate();
    }
    public static int UserLogin(Connection connection, String UserName, String Password ) throws SQLException {

        int login = 0;
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Users;");

        String Name;
        String pass;
        System.out.println("Check login credentuals");

        for (int i = 0; i < 5; i++) {
            resultSet.next();
            Name = resultSet.getString(4);
            System.out.println("Check login Name " + Name);
            System.out.println("recived Name " + UserName);

            if (Name.equals(UserName)){
                pass = resultSet.getString(5);
                System.out.println("Check login password " + pass);
                if (pass.equals(Password)){
                    login = 1;
                    return login;
                }
            }else{
                login = 0;
            }
        }

        return login;

    }
    public static boolean userExists(Connection connection, String UserName) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Users;");
        String Name;
        boolean isIn = false;
        for (int i = 0; i < 4; i++) {
            resultSet.next();
            Name = resultSet.getString(4);
            if (Name.equals(UserName)){
                isIn = true;
                return isIn;
            }
        }

        return isIn;
    }

}



