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


/**
 * The DBConnect class manages all aspects of the databases as well as connections to them
 * if the have not been created then they will be and there is also code to populate the databases
 * test information. DBConnect also contains all the required methods needed to querie the data base
 * for various pieces of information.
 */

public class DBConnect{

    /**
     * If the the following tables dont exist the they be created the firt time DBconnection called.
     */
    // Creates the user table with all required fields.
    public static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS Users ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "FirstName VARCHAR(15),"
                    + "LastName VARCHAR(15),"
                    + "UserName VARCHAR(15),"
                    + "Password VARCHAR(30),"
                    + "Team VARCHAR(30)" + ");";

    // Creates the Asset table with all required fields.
    public static final String CREATE_ASSET_TABLE =
            "CREATE TABLE IF NOT EXISTS Assets ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "AssetName VARCHAR(15),"
                    + "HighPrice VARCHAR(10),"
                    + "LowPrice VARCHAR(10),"
                    + "Description VARCHAR(150)" + ");";

    // Creates the shop table with all required fields.
    public static final String CREATE_SHOP_TABLE =
            "CREATE TABLE IF NOT EXISTS Shop ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "Asset VARCHAR(15),"
                    + "TeamRequest VARCHAR(15),"
                    + "BuyOrSell VARCHAR(15),"
                    + "Price VARIANT(15),"
                    + "Amount VARCHAR(10)" +
                    ");";

    // Creates the team table with all required fields.
    public static final String CREATE_TEAM_TABLE =
            "CREATE TABLE IF NOT EXISTS Team ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "TeamName VARCHAR(15),"
                    + "TeamLeader VARCHAR(15),"
                    + "Credits VARCHAR(15)" + ");";

    // Creates the shop history table with all required fields.
    public static final String CREATE_SHOP_HISTORY_TABLE =
            "CREATE TABLE IF NOT EXISTS ShopHistory ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "Asset VARCHAR(15),"
                    + "BuyTeam VARCHAR(15),"
                    + "SellTeam VARCHAR(15),"
                    + "Price VARIANT(15)" + ");";


    private static Connection connection = null;

    /**
     * Connects to the database through the driver manager, the calls the createDatabase(connection);
     * if the database have not been created this will create them, next it will check if data bases
     * are empty if they are it will populate them with test data.
     *
     * @throws SQLException
     */
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

    /**
     * creates a new database connection and returns it.
     *
     * @return Connection to the database
     * @throws SQLException
     */
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


    /**
     * This will populate the user table and the teams table of the database.
     * @param connection a connection to the database.
     * @throws SQLException
     */
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


    /**
     *      * Adds a team to the team database  and adds a user to be the leader of the team.
     * @param connection a connection to the database.
     * @param TeamName The Name of the new team
     * @param TeamLeader THe Leader of the team
     * @param Credits the amount of credits the team has
     * @throws SQLException
     */
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

    /**
     * Populates some of the databases with test data, the asset, shop and the teams databases will
     * be populated.
     * @param connection a connection to the database.
     * @throws SQLException
     */
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



    }

    /**
     * Creates all the database if they already exsist they will not be recreated, the calls
     * createTeamsDatabase(connection);
     * @param connection a connection to the database.
     * @throws SQLException
     */
    public static void createDatabase(Connection connection) throws SQLException {

        Statement st = connection.createStatement();
        st.execute(CREATE_USER_TABLE);
        st.execute(CREATE_ASSET_TABLE);
        st.execute(CREATE_SHOP_TABLE);
        st.execute(CREATE_TEAM_TABLE);
        st.execute(CREATE_SHOP_HISTORY_TABLE);
        createTeamsDatabase(connection);



    }

    /**
     * Adds a new asset to the asset database
     *
     * @param connection  a connection to the database.
     * @param AssetName The name of the new asset
     * @param HighPrice The high of the asset
     * @param LowPrice The low price of the asset
     * @param Description A description of the asset
     * @throws SQLException
     */
    public static void addAsset(Connection connection,String AssetName, String HighPrice, String LowPrice, String Description) throws SQLException {

        //prepares the sql string
        String INSERT_ASSET = "INSERT INTO Assets (AssetName, HighPrice, LowPrice, Description) VALUES (?, ?, ?, ?);";

        PreparedStatement addAsset = connection.prepareStatement(INSERT_ASSET);

        // adds parameters
        addAsset.setString(1, AssetName);
        addAsset.setString(2, HighPrice);
        addAsset.setString(3, LowPrice);
        addAsset.setString(4, Description);

        //execute
        addAsset.execute();

    }
    /**
     * Adds a new sale to the shop
     * @param connection a connection to the database.
     * @param Asset assets name
     * @param TeamRequest The team making the request
     * @param BuyOrSell is it a buy or sell request
     * @param Price what the price is
     * @param Amount How many
     * @throws SQLException
     */
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
    /**
     *add a new user to the data takes a user calss object as input
     * @param connection a connection to the database.
     * @param U The user object that is being added
     * @throws SQLException
     */
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

    /**
     * Adds a new user to the user data base, is call buy the add user gui, takes user info as input
     * @param connection a connection to the database.
     * @param firstName users firts name
     * @param lastName users last name
     * @param Username the uername they will login with
     * @param Password the users password
     * @param Team the team the user is part of
     * @throws SQLException
     */
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

    /**
     * when a sale is prossesed in the shop the item is moved to the shop history data base so user can see the price
     * history and what items are selling well
     *
     * @param connection a connection to the database.
     * @param Asset  the asset name
     * @param BuyTeam the buyer
     * @param SellTeam the seller
     * @param Price the price
     * @throws SQLException
     */
    public static void addShopHistoryItem(Connection connection, String Asset, String BuyTeam, String SellTeam, String Price) throws SQLException {
        String INSERT_SHOPHISTORY = "INSERT INTO ShopHistory (Asset, BuyTeam, SellTeam, Price) VALUES (?, ?, ?, ?);";

        PreparedStatement addShopHistoryItem = connection.prepareStatement(INSERT_SHOPHISTORY);

        addShopHistoryItem.setString(1, Asset);
        addShopHistoryItem.setString(2, BuyTeam);
        addShopHistoryItem.setString(3, SellTeam);
        addShopHistoryItem.setString(4, Price);
        addShopHistoryItem.execute();
    }

    /**
     * Removes a item from the shop one the sale has been prossesed
     * @param connection a connection to the database.
     * @param index the database index of the item.
     * @throws SQLException
     */
    public static void removeItemFromShop(Connection connection, int index) throws SQLException{

        PreparedStatement statement = connection.prepareStatement("DELETE FROM Shop WHERE idx = ? ;");
        statement.setInt(1,index);
        statement.executeUpdate();
    }

    /**
     * Used when a request is not fully completed in the case were one or the other team dose not have enough credits
     * or enough of an asset to fulfill the request. in the case the amount that can be sold is passed here and then
     * this function will edit the shop requests by reducing the price and the buy and sell amounts by the amount and
     * then the request are put back in the store.
     *
     *
     * @param connection a connection to the database.
     * @param indexbuy index of the buy request in the shop database
     * @param indexsell index of the Sell request in the shop database
     * @param amountToBeSold how man will be sold
     * @throws SQLException
     */
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


    /**
     * Adds or deducts credits from a team, used when sales are processed
     * @param connection a connection to the database.
     * @param Team the whos credits will be edited
     * @param amount the amount of credits
     * @throws SQLException
     */
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

    /**
     * creates team databases that will stor the assets that a team has and how many they have. This function
     * will go through the teams databas and for every team create the a database.
     *
     * @param connection a connection to the database.
     * @throws SQLException
     */
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

    /**
     * adds assets to a teams database use to populate and when a team makes a successful buy request
     * @param connection a connection to the database.
     * @param team the team have assets added
     * @param asset the asset being added
     * @param amount how many of the asset
     * @throws SQLException
     */
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

    /**
     * returns the idx of the nth asset in the shop
     * @param connection a connection to the database.
     * @param Nth number representing the nth asset
     * @return a string representing the inx of that item
     * @throws SQLException
     */
    public static String getShopidx(Connection connection, int Nth ) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        //resultSet.next();
        if(resultSet.isClosed())
            System.out.println("ThE RESULT WAS CLOSED, might be empty");




        for (int i = 0; i < Nth; i++) {
            resultSet.next();
        }


        String idx = resultSet.getString(1);
        return idx;
    }

    /** Gets the nth asset in the shop database
     *
     * @param connection  a connection to the database.
     * @param Nth the shop databas index
     * @return the asset name
     * @throws SQLException
     */
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

    /** Returns the team the is making the nth shop request in the shop database
     *
     * @param connection a connection to the database.
     * @param Nth the request number
     * @return returns the team
     * @throws SQLException
     */
    public static String getNthShopTeam(Connection connection, int Nth ) throws SQLException{

        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");


        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String Team = resultSet.getString(3);
        return Team;
    }

    /**
     * Returns the nth team in the team database
     * @param connection a connection to the database.
     * @param Nth the number of the team
     * @return the team
     * @throws SQLException
     */
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

    /**
     * get request type of the nth request of in the shop database
     * @param connection a connection to the database.
     * @param Nth the number of the request
     * @return buy or sell depending on the request type
     * @throws SQLException
     */
    public static String getNthBuyOrSell(Connection connection, int Nth ) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        //resultSet.next();

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String BuyOrSell = resultSet.getString(4);
        return BuyOrSell;
    }

    /**
     * Get the price of the nth request int the shop database
     * @param connection a connection to the database.
     * @param Nth the number of the item
     * @return the price
     * @throws SQLException
     */
    public static String getNthPrice(Connection connection, int Nth ) throws SQLException{
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from Shop;");

        //resultSet.next();

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String Price = resultSet.getString(5);
        return Price;
    }

    /**
     *
     * @param connection a connection to the database.
     * @param Nth
     * @return
     * @throws SQLException
     */
  /*  public static String getNth(Connection connection, int Nth ) throws SQLException{
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
*/

    /**
     *Gets the team that the user is a part of
     * @param connection a connection to the database.
     * @param UserName the user we want to know the team of
     * @return the team name
     * @throws SQLException
     */
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

    /**
     * get the nth item in the shop history database
     * @param connection a connection to the database.
     * @param Nth the number of the item
     * @return the asset
     * @throws SQLException
     */
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

    /**
     * get the buy team of the nth shop history item
     * @param connection a connection to the database.
     * @param Nth item number
     * @return the buy team name
     * @throws SQLException
     */
    public static String getShopHisBuyTeam(Connection connection, int Nth ) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from ShopHistory;");

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String BuyTeam = resultSet.getString(3);
        return BuyTeam;

    }

    /**
     *
     * @param connection a connection to the database.
     * @param Nth
     * @return
     * @throws SQLException
     */
    public static String getShopHisSellTeam(Connection connection, int Nth ) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from ShopHistory;");

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String SellTeam = resultSet.getString(4);
        return SellTeam;
    }

    /**
     * Get the sell team of the nth shop history item
     * @param connection a connection to the database.
     * @param Nth item number
     * @return the sell team name
     * @throws SQLException
     */
    public static String getShopHisPrice(Connection connection, int Nth )throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from ShopHistory;");

        for (int i = 0; i < Nth; i++) {resultSet.next();}
        String Price = resultSet.getString(5);
        return Price;
    }

    /**
     * get the nth asset from the asset database
     * @param connection a connection to the database.
     * @param Nth asset number
     * @return the asset
     * @throws SQLException
     */
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

    /**
     *  get the number of assets that the team has.
     * @param connection a connection to the database.
     * @param team the team
     * @return number of different assets
     * @throws SQLException
     */
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

    /**
     * Get the amount of an asset being requested in a shop request for the asset at database inx
     * @param connection a connection to the database.
     * @param index the data base index of the asset
     * @return the amount of a asset being requested
     * @throws SQLException
     */
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

    /**
     * Gets the number of users in the users database
     * @param connection a connection to the database.
     * @return the number of users in the users database
     * @throws SQLException
     */
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

    /**
     * Get the number of teams in the Teams database
     * @param connection a connection to the database.
     * @return the number of teams in the Teams database
     * @throws SQLException
     */
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

    /**
     * Gets the number of assets stored in the assets database
     * @param connection a connection to the database.
     * @return the number of assets stored in the assets database
     * @throws SQLException
     */
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

    /**
     * the number of items in the shop history database
     * @param connection a connection to the database.
     * @return number of items in the shop history database
     * @throws SQLException
     */
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

    /**
     * get the number of credits that a team has
     * @param connection a connection to the database.
     * @param Team the team whos credis we are checking
     * @return number of credits
     * @throws SQLException
     */
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

    /**
     *get the number of rows in the shop database and return it
     * @param connection a connection to the database.
     * @return the number of rows in the shop
     * @throws SQLException
     */
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

    /**
     * Checks the user name and password provided with the name and passwords stored in the user database if the
     * username matches check the passwords if ether dont match then login fails
     * @param connection a connection to the database.
     * @param UserName the user name to checked
     * @param Password the password to be checked
     * @return 1 if the credentuals match 0 if not
     * @throws SQLException
     */
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

    /**
     * checks if the team has more than or the same amount of an asset as the number howmany if there are then it will
     * return true
     * @param connection a connection to the database.
     * @param team the whos assets we are checking
     * @param asset the asset that we are checking the amount of
     * @param howmany how they need to have more than
     * @return True if the number of assets the team has is greater than or equal to howmany
     * @throws SQLException
     */
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

    /**
     * check if the user exists in the user database
     * @param connection  a connection to the database.
     * @param UserName the user we are checking for
     * @return returns true if user exists
     * @throws SQLException
     */
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

    /**
     * checks if a user is the team leader, used to check if they are allowed to do sertain things like add new users
     * @param connection  a connection to the database.
     * @param UserName the name of the user being checked
     * @return True if user is a team leader
     * @throws SQLException
     */
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

    /**
     * checks if a team has a particular asset
     * @param connection a connection to the database.
     * @param team the team be checked
     * @param Asset the asset being checked for
     * @return true if the team has the asset and false if the team dose not
     * @throws SQLException
     */
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



