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
                    + "Amount VARCHAR(10)" +
                    ");";


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
        int num = numShopRows(connection);
        if (num == 0){
            System.out.println("the number of rows is " + num + " database needs populating");
        }else{
            System.out.println("the number of rows is " + num + " database has been populated");
        }

        if(num < 7){
            AddUserandTeams(connection);
            PopulateDatabase(connection);
        }

        num = numShopRows(connection);
        if (num == 0){
            System.out.println("the number of rows is " + num + " database needs populating");
        }else{
            System.out.println("the number of rows is " + num + " database has been populated");
        }



    }
    public static Connection getInstance() throws SQLException {
        if (connection != null) {

            if(connection.isClosed()){
                connection= null;
            }

        }


        if (connection == null) {
            new DBConnect();
        }else{System.out.println("Connection not null");}
        return connection;
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
    public static void AddUserandTeams(Connection connection) throws SQLException{


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


        Team = "great";
        String TeamLeader = "bob_sm";
        String Credits = "2000";

        addTeam(connection, Team, TeamLeader,Credits );

        Team = "A";
        TeamLeader = "bob_smith";
        Credits = "2000";

        addTeam(connection, Team, TeamLeader,Credits );

        Team = "B";
        TeamLeader = "John_j";
        Credits = "2000";

        addTeam(connection, Team, TeamLeader,Credits );

        Team = "C";
        TeamLeader = "jane_smith";
        Credits = "2000";

        addTeam(connection, Team, TeamLeader,Credits );

        Team = "C";
        TeamLeader = "j";
        Credits = "2000";

        addTeam(connection, Team, TeamLeader,Credits );

        Team = "D";
        TeamLeader = "rob_jo";
        Credits = "2000";

        addTeam(connection, Team, TeamLeader,Credits );

        createTeamsDatabase(connection);


    }
    public static void PopulateDatabase(Connection connection) throws SQLException{

        String AssetName = "CPU Time";
        String HighPrice = "0";
        String LowPrice = "0";
        String Description = "CPU time on one of the servers to used for what ever the user needs";

        addAsset(connection, AssetName,HighPrice,LowPrice,Description);

        AssetName = "Printing";
        HighPrice = "0";
        LowPrice = "0";
        Description = " Printing";

        addAsset(connection, AssetName,HighPrice,LowPrice,Description);


        AssetName = "Computer";
        HighPrice = "0";
        LowPrice = "0";
        Description = "Computer";

        addAsset(connection, AssetName,HighPrice,LowPrice,Description);

        AssetName = "IT services";
        HighPrice = "0";
        LowPrice = "0";
        Description = "It services";

        addAsset(connection, AssetName,HighPrice,LowPrice,Description);

        AssetName = "A New car";
        HighPrice = "0";
        LowPrice = "0";
        Description = "Car ";

        addAsset(connection, AssetName,HighPrice,LowPrice,Description);

        AssetName = "Somthing";
        HighPrice = "0";
        LowPrice = "0";
        Description = "Somthing, Somthing ,Somthing";

        addAsset(connection, AssetName,HighPrice,LowPrice,Description);

        AssetName = "Test Asset";
        HighPrice = "0";
        LowPrice = "0";
        Description = "Testing";

        addAsset(connection, AssetName,HighPrice,LowPrice,Description);



        String Asset = "Printing";
        String TeamRequest = "A";
        String BuyOrSell = "Buy";
        String Price = "2";
        String Amount = "25";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "Printing";
        TeamRequest = "B";
        BuyOrSell = "Sell";
        Price = "2";
        Amount = "2000";
        addAssetToTeam(connection,"B", "Printing", 2000);
        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "CPU Time";
        TeamRequest = "A";
        BuyOrSell = "Buy";
        Price = "10";
        Amount = "75";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price,Amount);

        Asset = "CPU Time";
        TeamRequest = "C";
        BuyOrSell = "Sell";
        Price = "12";
        Amount = "100";
        addAssetToTeam(connection,"C", "CPU Time", 110);
        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "CPU Time";
        TeamRequest = "C";
        BuyOrSell = "Buy";
        Price = "120";
        Amount = "12";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "Computer";
        TeamRequest = "C";
        BuyOrSell = "Buy";
        Price = "1000";
        Amount = "1";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "Computer";
        TeamRequest = "D";
        BuyOrSell = "Sell";
        Price = "1000";
        Amount = "1";

        //addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price);
        addAssetToTeam(connection,"D", "Computer", 1);
        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "Lazering";
        TeamRequest = "A";
        BuyOrSell = "Sell";
        Price = "100000";
        Amount = "2";
        addAssetToTeam(connection,"A", "Lazering", 2);
        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "Lazering";
        TeamRequest = "C";
        BuyOrSell = "Buy";
        Price = "10";
        Amount = "2";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "Proof reading";
        TeamRequest = "C";
        BuyOrSell = "Buy";
        Price = "3";
        Amount = "10";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "Proof reading";
        TeamRequest = "A";
        BuyOrSell = "Sell";
        Price = "3";
        Amount = "20";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);
        addAssetToTeam(connection,"A", "Proof reading", 30);

        Asset = "IT services";
        TeamRequest = "A";
        BuyOrSell = "Sell";
        Price = "150";
        Amount = "60";

        addAssetToTeam(connection,"A", "IT services", 100);
        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "IT services";
        TeamRequest = "B";
        BuyOrSell = "Buy";
        Price = "140";
        Amount = "60";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "A New car";
        TeamRequest = "B";
        BuyOrSell = "Buy";
        Price = "100000";
        Amount = "1";

        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "A New car";
        TeamRequest = "A";
        BuyOrSell = "Sell";
        Price = "100000";
        Amount = "1";

        addAssetToTeam(connection,"A", "A New car", 1);
        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "Somthing";
        TeamRequest = "A";
        BuyOrSell = "Sell";
        Price = "105";
        Amount = "4";
        addAssetToTeam(connection,"A", "Somthing", 4);
        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price, Amount);

        Asset = "Somthing";
        TeamRequest = "A";
        BuyOrSell = "Buy";
        Price = "105";
        Amount = "5";


        addItemToShop(connection, Asset, TeamRequest, BuyOrSell, Price,Amount);


/*
        addAssetToTeam(connection,"A", "Computer", 50);
        addAssetToTeam(connection,"C", "Computer", 4);
        addAssetToTeam(connection,"B", "Printing", 10);
        addAssetToTeam(connection,"A", "CPU Time", 5);*/



    }
    public static void createDatabase(Connection connection) throws SQLException {

        Statement st = connection.createStatement();
        st.execute(CREATE_USER_TABLE);
        st.execute(CREATE_ASSET_TABLE);
        st.execute(CREATE_SHOP_TABLE);
        st.execute(CREATE_TEAM_TABLE);
        st.execute(CREATE_SHOP_HISTORY_TABLE);
        createTeamsDatabase(connection);



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
    public static void addItemToShop(Connection connection,String Asset, String TeamRequest, String BuyOrSell, String Price, String Amount) throws SQLException{
        String INSERT_SHOPITEM = "INSERT INTO Shop (Asset, TeamRequest, BuyOrSell, Price, Amount) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement addShopItem = connection.prepareStatement(INSERT_SHOPITEM);

        String Details = "Test Details string ";
        addShopItem.setString(1, Asset);
        addShopItem.setString(2, TeamRequest);
        addShopItem.setString(3, BuyOrSell);
        addShopItem.setString(4, Price);
        addShopItem.setString(5, Amount);

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
    public static void removeItemFromShop(Connection connection, int index) throws SQLException{

        PreparedStatement statement = connection.prepareStatement("DELETE FROM Shop WHERE idx = ? ;");
        statement.setInt(1,index);
        statement.executeUpdate();
    }
    public static void deductItemsFromShop(Connection connection, int indexbuy, int indexsell, int amountToBeSold) throws SQLException{

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        int shopitems = numShopRows(connection);
        int amountInShop = 0;
        int amountToBuy = 0;

        for (int i = 0; i < shopitems; i++) {
            resultSet.next();

            String sellint = String.valueOf(indexsell);
            if (resultSet.getString(1).equals(sellint)) {

                amountInShop = Integer.parseInt(resultSet.getString(6));
            }
        }

        Statement statement2 = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet2 = statement2.executeQuery("select * from Shop;");

        String buyint = String.valueOf(indexbuy);

        for (int i = 0; i < shopitems; i++) {
            resultSet2.next();
            if (resultSet2.getString(1).equals(buyint)) {
                amountToBuy = Integer.parseInt(resultSet.getString(6));
            }
        }

        if (amountToBeSold < amountToBuy){

            int newbuyAmount = amountToBuy - amountToBeSold;
            PreparedStatement statement3 = connection.prepareStatement("UPDATE Shop SET Amount = ? WHERE idx = ? ;");
            statement3.setInt(1,newbuyAmount);
            statement3.setInt(2,indexbuy);
            statement3.executeUpdate();
        }else{
            removeItemFromShop(connection, indexbuy);
        }

        if(amountToBeSold < amountInShop){

            int newsellAmount = amountInShop - amountToBeSold;
            PreparedStatement statement4 = connection.prepareStatement("UPDATE Shop SET Amount = ? WHERE idx = ? ;");
            statement4.setInt(1, newsellAmount);
            statement4.setInt(2, indexsell);
            int rows = statement4.executeUpdate();
        }else{

            removeItemFromShop(connection, indexsell);
        }

    }
    public static void addCredits(Connection connection, String  Team, int amount) throws SQLException {
        int index;
        int cred;
        String credits;
        String currentTeam;
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Team;");

        for (int i = 0; i < 4; i++) {
            resultSet.next();
            currentTeam = resultSet.getString(2);
            if (currentTeam.equals(Team)) {
                credits = resultSet.getString(4);

                cred = Integer.parseInt(credits);
                cred = cred + amount;
                credits = String.valueOf(cred);


                PreparedStatement statement1 = connection.prepareStatement("UPDATE Team SET Credits = ? WHERE TeamName = ? ;");
                statement1.clearParameters();
                statement1.setString(1, credits);
                statement1.setString(2, Team);
                statement1.executeUpdate();

            }
        }
    }
    public static void createTeamsDatabase(Connection connection) throws SQLException {

        int numteams = getNumTeams(connection);

        String teamName;

        for (int i = 1; i <= numteams; i++){

            teamName =  getNthTeam(connection,i);

            PreparedStatement statement1 = connection.prepareStatement("CREATE TABLE IF NOT EXISTS TEAM_" + teamName + "_Assets ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "Asset VARCHAR(15),"
                    + "Amount  VARIANT(15)" + ");");

            statement1.executeUpdate();

        }

    }
    public static void addAssetToTeam(Connection connection,String team, String asset, int amount) throws SQLException {

        PreparedStatement addAsset = null;
        //Adds assets to the teams database
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from TEAM_" + team + "_Assets;");
        int num = getNumTeamAssets(connection, team);
        int match = 0;
        if(resultSet.isClosed()) {

            String INSERT_TEAMASSET = "INSERT INTO TEAM_" + team + "_Assets (Asset, Amount) VALUES (?, ?);";
            addAsset = connection.prepareStatement(INSERT_TEAMASSET);
            addAsset.setString(1, asset);
            addAsset.setString(2, String.valueOf(amount));

        }else{
            for (int i = 0; i < num; i++) {
                resultSet.next();
                if(resultSet.getString(2).equals(asset)){
                    int total = amount + Integer.parseInt( resultSet.getString(3));

                    //System.out.println("total is " + total);

                    addAsset = connection.prepareStatement("UPDATE TEAM_" + team + "_Assets SET Amount = ? WHERE Asset = ? ;");

                    addAsset.setString(1, String.valueOf(total));
                    addAsset.setString(2, asset);
                    match = 1;
                    i = num+1;
                    System.out.println("String is:  " + addAsset);

                }
            }

        }
        if(match == 0){

            String INSERT_TEAMASSET = "INSERT INTO TEAM_" + team + "_Assets (Asset, Amount) VALUES (?, ?);";
            addAsset = connection.prepareStatement(INSERT_TEAMASSET);
            addAsset.setString(1, asset);
            addAsset.setString(2, String.valueOf(amount));

        }
        //System.out.println("String is:  " + addAsset);
        addAsset.execute();



    }

    public static String getShopidx(Connection connection, int Nth ) throws SQLException{
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


        String idx = resultSet.getString(1);
        return idx;
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
    public static String getNthShopTeam(Connection connection, int Nth ) throws SQLException{

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");


        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String Team = resultSet.getString(3);
        return Team;
    }
    public static String getNthTeam(Connection connection, int Nth ) throws SQLException{

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Team;");
        int numTeams = getNumTeams(connection);

        if(Nth > numTeams){
            Nth = numTeams;
        }


        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String Team = resultSet.getString(2);
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

        int numUsers = getNumUsers(connection);

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
        String Asset = null;
        for (int i = 0; i < Nth; i++) {resultSet.next();}
        if(resultSet.isClosed()){
            Asset = null;
        }else{
            Asset = resultSet.getString(2);
        }
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
    public static String getnthAssetfromAssetsTable(Connection connection, int Nth ) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Assets;");

        //resultSet.next();
        if(resultSet.isClosed())
            System.out.println("ThE RESULT WAS CLOSED, might be empty");

        for (int i = 0; i < Nth; i++) {
            resultSet.next();

        }


        String Asset = resultSet.getString(2);
        return Asset;
    }

    public static boolean getNumofATeamAsset(Connection connection, String team, String asset, int howmany) throws SQLException {

        int num = 0;

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from TEAM_" + team + "_Assets;");

        int numAssets = getNumTeamAssets(connection ,team);
        int amount = 0;
        boolean result = false ;

        for(int i = 0; i < numAssets; i++){
            resultSet.next();
            String entry = resultSet.getString(2);
            if(asset.equals(entry)){
                amount = Integer.parseInt(resultSet.getString(3));
                i = numAssets + 1;
            }
        }
        if(amount > howmany){
            result = true;
        }

        return result;

    }
    public static int getNumTeamAssets(Connection connection, String team) throws SQLException {

        int num = 0;

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from TEAM_" + team + "_Assets;");

        resultSet.next();
        int rowNum =  resultSet.getRow();

        if (rowNum == 0){
            System.out.println("The num is  " + rowNum);
        }else{
            int i = 1;
            while(i==1){
                resultSet.next();
                if (resultSet.getRow() > rowNum){
                    rowNum = resultSet.getRow();
                }else{
                    i = 2;
                }
            }
        }

        return rowNum;

    }
    public static int getAmountShop(Connection connection, int index) throws SQLException {

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");
        resultSet.next();

        int numrow = numShopRows(connection);

        for(int i = 1; i < index; i++){
            resultSet.next();
            int idx = Integer.parseInt(resultSet.getString(1));
            if (idx == index){
                i = index;
            }
        }
        //String ass = resultSet.getString(2);
        //String x = resultSet.getString(1);
        int Amount = Integer.parseInt(resultSet.getString(6));
        //System.out.println("the asset is  " + ass + " and its index is " + x);
        return Amount;

    }
    public static int getNumUsers(Connection connection) throws SQLException{

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Users;");

        resultSet.next();
        int rowNum =  resultSet.getRow();

        if (rowNum == 0){
            System.out.println("The num is  " + rowNum);
        }else{
            int i = 1;
            while(i==1){
                resultSet.next();
                if (resultSet.getRow() > rowNum){
                    rowNum = resultSet.getRow();
                }else{
                    i = 2;
                }
            }
        }

        return rowNum;
    }
    public static int getNumTeams(Connection connection) throws SQLException{

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Team;");

        resultSet.next();
        int rowNum =  resultSet.getRow();

        if (rowNum == 0){
            System.out.println("The num is  " + rowNum);
        }else{
            int i = 1;
            while(i==1){
                resultSet.next();
                if (resultSet.getRow() > rowNum){
                    rowNum = resultSet.getRow();
                }else{
                    i = 2;
                }
            }
        }

        return rowNum;
    }
    public static int getNumAssets(Connection connection) throws SQLException{

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Assets;");

        resultSet.next();
        int rowNum =  resultSet.getRow();

        if (rowNum == 0){
            System.out.println("The num is  " + rowNum);
        }else{
            int i = 1;
            while(i==1){
                resultSet.next();
                if (resultSet.getRow() > rowNum){
                    rowNum = resultSet.getRow();
                }else{
                    i = 2;
                }
            }
        }

        return rowNum;
    }
    public static int getHistoryItems(Connection connection) throws SQLException{

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from ShopHistory;");

        resultSet.next();
        int rowNum =  resultSet.getRow();

        if (rowNum == 0){
            System.out.println("The num is  " + rowNum);
        }else{
            int i = 1;
            while(i==1){
                resultSet.next();
                if (resultSet.getRow() > rowNum){
                    rowNum = resultSet.getRow();
                }else{
                    i = 2;
                }
            }
        }

        return rowNum;
    }
    public static int getNumHistoryItems(Connection connection) throws SQLException{

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from ShopHistory;");

        resultSet.next();
        int rowNum =  resultSet.getRow();

        if (rowNum == 0){
            System.out.println("The num is  " + rowNum);
        }else{
            int i = 1;
            while(i==1){
                resultSet.next();
                if (resultSet.getRow() > rowNum){
                    rowNum = resultSet.getRow();
                }else{
                    i = 2;
                }
            }
        }

        return rowNum;
    }


    public static int getCredits(Connection connection, String  Team) throws SQLException {
    //get the amout of credits a team has
        int cred = 0;
        String credits;
        String currentTeam;
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Team;");

        int numTeams = getNumTeams(connection);

        for (int i = 0; i < numTeams; i++) {
            resultSet.next();
            currentTeam = resultSet.getString(2);
            if (currentTeam.equals(Team)) {
                credits = resultSet.getString(4);
                cred = Integer.parseInt(credits);
                return cred;
            }
        }
        return cred;
    }
    public static int numShopRows(Connection connection) throws SQLException{
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
    public static int UserLogin(Connection connection, String UserName, String Password ) throws SQLException {

        int login = 0;
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Users;");

        String Name;
        String pass;
        System.out.println("Check login credentuals");
        int numusers = getNumUsers(connection);
        for (int i = 0; i < numusers; i++) {
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
        int numUsers = getNumUsers(connection);
        boolean isIn = false;
        for (int i = 0; i < numUsers; i++) {
            resultSet.next();
            Name = resultSet.getString(4);
            if (Name.equals(UserName)){
                isIn = true;
                return isIn;
            }
        }

        return isIn;
    }
    public static boolean isTeamLeader(Connection connection, String UserName) throws SQLException {

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Team;");
        String Name;
        boolean isLeader= false;
        int numTeams = getNumTeams(connection);
        for (int i = 0; i < numTeams; i++) {
            resultSet.next();
            Name = resultSet.getString(3);
            if (Name.equals(UserName)){
                isLeader = true;
                return isLeader;
            }
        }
        return isLeader;
    }
    public static boolean TeamHasAsset(Connection connection, String team, String Asset) throws SQLException {

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from TEAM_" + team + "_Assets;");
        String assetsFromTeam;
        boolean TeamHas= false;
        int numAssets = getNumTeamAssets(connection, team);

        for (int i = 0; i < numAssets; i++) {
            resultSet.next();
            assetsFromTeam = resultSet.getString(2);
            if (assetsFromTeam.equals(Asset)){
                TeamHas = true;
                return TeamHas;
            }
        }
        return TeamHas;
    }



}



