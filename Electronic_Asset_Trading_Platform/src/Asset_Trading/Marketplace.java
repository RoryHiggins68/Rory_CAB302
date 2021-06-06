package Asset_Trading;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;


/**
 * The market place class that can will facilitate the buying and selling of assets, it will proses sales
 * and make checks on teams credits ect
 */
public class Marketplace {

    // How frequently marketplace will check the sore to process sales, 1 is ever 1 minute
    private static final double MINUTES = 0.25;
    // the excepted differance in price, changing this value will raises or lowers the differance between a buy and
    // sell price that will allow a sale to go through. this is a percentage default is 20%.
    private static final double EXECUTABLE_DIFFERANCE = 0.2;

    /**
     * Creates a connection to the database then will run marketplace every minute to check for matching sales
     * This can be changed be changing MINUTES to make the check more or less frequent, can be 0.5 for 30 seconds
     * or 10 for ten minutes. This will continue to run until stoped
     */
    public static void main(String[] args) throws SQLException, InterruptedException {

        int i = 1;
        while(i == 1){

            System.out.println(new Date());
            Thread.sleep((long) ((60 * MINUTES) * 1000));
            Connection connectionmarket = DBConnect.getInstance();
            marketplace(connectionmarket);
            connectionmarket.close();

        }
    }


    /**
     * Creates a connection to the database then will run marketplace every minute to check for matching sales
     * This can be changed be changing MINUTES to make the check more or less frequent, can be 0.5 for 30 seconds
     * or 10 for ten minutes. This will continue to run until stoped
     */
    public static void Start() throws SQLException, InterruptedException {

        int i = 1;
        while(i == 1){

            System.out.println(new Date());
            Thread.sleep((long) ((60 * MINUTES) * 1000));
            Connection connectionmarket = DBConnect.getInstance();
            marketplace(connectionmarket);
            connectionmarket.close();

        }
    }


    /**
     * The market place will query the dataase for an item from the store and then it will query for a second item
     * it will then compare these two item and see if they are are the same asset, one is buying one is selling that
     * they are posted by different teams and that the prices are the same or within 20% percent of each other,
     * if these all pass a credit check will be done and a check to see if the sale will be completely or partially
     * completed. After these checks are all passed the sall is processed and items and its price is moved to the
     * store history, the teams will have their assets added/deducted from their databases and credits added/deducted
     * as well.
     *
     * this will done for ever entry in the shop
     */
    public static void marketplace(Connection connection) throws SQLException {

        //Initialise values
        String Asset1 = DBConnect.getNthAsset(connection,1 );
        String Asset2 = DBConnect.getNthAsset(connection,1 );
        String BuyOrSell1 = DBConnect.getNthBuyOrSell(connection,1);
        String BuyOrSell2 = DBConnect.getNthBuyOrSell(connection,1);
        String Team1 = DBConnect.getNthTeam(connection,1);
        String Team2 = DBConnect.getNthTeam(connection,1);
        String BuyTeam;
        String SellTeam;
        int idx = 0;
        int idx2 = 0;
        int buyidx = idx;
        int sellidx = idx2;
        int num = DBConnect.numShopRows(connection);
        int buyamount = 0;
        int sellamount = 0;

        // begin the main shop loop
        for (int i = 1; i < num; i++) {
            for(int i2 = 1; i2 <= num; i2++) {
                num = DBConnect.numShopRows(connection);

                    //get 2 assets from the store and their related values
                    Asset1 = DBConnect.getNthAsset(connection,i );
                    Asset2 = DBConnect.getNthAsset(connection,i2 );
                    BuyOrSell1 = DBConnect.getNthBuyOrSell(connection,i);
                    BuyOrSell2 = DBConnect.getNthBuyOrSell(connection,i2);
                    Team1 = DBConnect.getNthShopTeam(connection,i);
                    Team2 = DBConnect.getNthShopTeam(connection,i2);
                    BuyTeam = Team1;
                    SellTeam = Team2;
                    idx = Integer.parseInt(DBConnect.getShopidx(connection,i));
                    idx2 = Integer.parseInt(DBConnect.getShopidx(connection,i2));
                    if(BuyOrSell1.equals("BUY")||BuyOrSell1.equals("Buy")||BuyOrSell1.equals("buy")){
                        BuyOrSell1 = "Buy";
                    }else if(BuyOrSell1.equals("SELL")||BuyOrSell1.equals("Sell")||BuyOrSell1.equals("sell")){
                        BuyOrSell1 = "Sell";
                    }

                    if(BuyOrSell2.equals("BUY")||BuyOrSell2.equals("Buy")||BuyOrSell2.equals("buy")){
                        BuyOrSell2 = "Buy";
                    }else if(BuyOrSell2.equals("SELL")||BuyOrSell2.equals("Sell")||BuyOrSell2.equals("sell")){
                        BuyOrSell2 = "Sell";
                    }


                //if the assets are the same
                if((Asset1.equals(Asset2))&&(!(BuyTeam.equals(SellTeam)))){
                    //and one is buying one is selling
                    if(!(BuyOrSell1.equals(BuyOrSell2))){

                        // one is buy on is sell compare the price
                        String Price1 = DBConnect.getNthPrice(connection,i );
                        String Price2 = DBConnect.getNthPrice(connection,i2 );
                        int p1 = Integer.parseInt(Price1);
                        int p2 = Integer.parseInt(Price2);

                        //which one is buy which is selling
                        if(BuyOrSell1.equals("Buy")){
                            BuyTeam = Team1;
                            SellTeam = Team2;
                            buyidx = idx;
                            sellidx = idx2;

                            buyamount = DBConnect.getAmountShop(connection,buyidx);
                            sellamount = DBConnect.getAmountShop(connection,sellidx);

                        }else if(BuyOrSell2.equals("Buy")){
                            BuyTeam = Team2;
                            SellTeam = Team1;
                            buyidx = idx2;
                            sellidx = idx;

                            buyamount = DBConnect.getAmountShop(connection,buyidx);
                            sellamount = DBConnect.getAmountShop(connection,sellidx);

                        }

                        // begin price checking, check that if one price is smaller than the other that they are
                        // with the EXECUTABLE_DIFFERANCE, if they are proses the sale.
                        if(p1 < p2){
                            if((p2-p2*EXECUTABLE_DIFFERANCE) <= p1){

                                // do the pre sale checks
                                int amountToBeSold = saleCheck(connection, BuyTeam, p1, buyamount, sellamount,sellidx);
                                if( amountToBeSold > 0){
                                    if(subCredits(connection, BuyTeam,SellTeam, p1,amountToBeSold)>0){
                                        System.out.println(Asset1 + " is being sold for p1 price of " + p1 );

                                        // remove/change the shop request
                                        DBConnect.deductItemsFromShop(connection, buyidx, sellidx, amountToBeSold);
                                        //move to sales history and add/deduct from assets from the teams
                                        // databases
                                        DBConnect.addAssetToTeam(connection,BuyTeam, Asset1, amountToBeSold);
                                        DBConnect.addAssetToTeam(connection,SellTeam, Asset1, -amountToBeSold);
                                        DBConnect.addShopHistoryItem(connection,Asset1, BuyTeam, SellTeam, Price1);
                                        //recheck the number of posts in the store and stars checking from the top
                                        // as previously checked items may have changed amounts and teams credit balanes
                                        //may have changed
                                        num = DBConnect.numShopRows(connection);
                                        i = 0;
                                        i2 = num;
                                    }
                                }else{
                                    System.out.println("sale not procesed insuficiant funds");
                                }
                            }

                        // begin price checking, check that if one price is smaller than the other that they are
                        // with the EXECUTABLE_DIFFERANCE, if they are proses the sale.
                        }else if(p2 < p1){
                            if((p1-p1*EXECUTABLE_DIFFERANCE) <= p2){

                                // do the pre sale checks
                                int amountToBeSold = saleCheck(connection, BuyTeam, p1, buyamount, sellamount,sellidx);
                                if( amountToBeSold > 0){
                                    if(subCredits(connection, BuyTeam,SellTeam, p1,amountToBeSold)>0){;
                                        System.out.println(Asset1 + " is being sold for p2 price of " + p2 );
                                        // remove/change the shop request
                                        DBConnect.deductItemsFromShop(connection, buyidx, sellidx, amountToBeSold);
                                        //move to sales history and add/deduct from assets from the teams
                                        // databases
                                        DBConnect.addAssetToTeam(connection,BuyTeam, Asset1, amountToBeSold);
                                        DBConnect.addAssetToTeam(connection,SellTeam, Asset1, -amountToBeSold);
                                        DBConnect.addShopHistoryItem(connection,Asset1, BuyTeam, SellTeam, Price2);

                                        //recheck the number of posts in the store and stars checking from the top
                                        // as previously checked items may have changed amounts and teams credit balanes
                                        //may have changed
                                        num = DBConnect.numShopRows(connection);
                                        i = 0;
                                        i2 = num;
                                    }
                                }else{
                                    System.out.println("sale not procesed insuficiant funds");
                                }
                            }
                        // begin price checking for = prices
                        }else if(p2 == p1){


                            //check if the buyer has enough credits and then deduct them and add them to seller
                            int amountToBeSold = saleCheck(connection, BuyTeam, p1, buyamount, sellamount,sellidx);
                            if( amountToBeSold > 0){
                                if(subCredits(connection, BuyTeam,SellTeam, p1,amountToBeSold)>0) {
                                    System.out.println(Asset1 + " is being sold, p1 and p2 are the same  price of " + p1);
                                    // remove/change the shop request
                                    DBConnect.deductItemsFromShop(connection, buyidx, sellidx, amountToBeSold);
                                    //move to sales history and add/deduct from assets from the teams
                                    // databases
                                    DBConnect.addAssetToTeam(connection,BuyTeam, Asset1, amountToBeSold);
                                    DBConnect.addAssetToTeam(connection,SellTeam, Asset1, -amountToBeSold);
                                    DBConnect.addShopHistoryItem(connection, Asset1, BuyTeam, SellTeam, Price1);

                                    //recheck the number of posts in the store and stars checking from the top
                                    // as previously checked items may have changed amounts and teams credit balanes
                                    //may have changed
                                    num = DBConnect.numShopRows(connection);
                                    i = 0;
                                    i2 = num;
                                }
                            }else{
                                //System.out.println("sale not procesed insuficiant funds");
                            }

                        }else{
                            //System.out.println(Asset1 + "Not being sold Price is not a match " + p1 + " and " + p2 + " to far apart");
                        }
                    }
                }
        }
        }
    }




    /**
     *  will check if a complete sale can be made or if the seller dose not have enough items or
     *  the buy dose not have enough credits, this function returns an int representing the number
     *  of items that can be sold.
     *
     * @param connection the connection to the database
     * @param BuyTeam the team doing the buying
     * @param cost the cost of the item
     * @param buyamount how many the buyer wants to buy
     * @param sellamount the amount the seller wants to sell
     * @param sellidx
     *
     * @return amoutSold the amount that can be sold
     *
     */
    public static int saleCheck(Connection connection, String BuyTeam,int cost, int buyamount, int sellamount, int sellidx) throws SQLException {


        // how many will sell
        int amoutSold = 0;
        // get buyers credits
        int TeamCreds = DBConnect.getCredits(connection, BuyTeam);
        // if buyer wants or sell wants more of the item ajust the amount to be sold for the smaller value
        if (buyamount > sellamount){
            amoutSold = sellamount;
        }else if(buyamount < sellamount){
            amoutSold = buyamount;
        }else if(buyamount == sellamount){
            amoutSold = buyamount;
        }
        //get the ammount in the shop
        int inShop = DBConnect.getAmountShop(connection, sellidx);

        int i = 0;
        while(i < 1 ){
            //reduce the ammount sold until the ammount be sold is less than or equile to the amount in shop
            if((amoutSold > inShop)&&(inShop > 0)){
                amoutSold = amoutSold - 1;
            }else{
                i = 2;
            }
        }
        //calculate the total cost
        int totalCost = cost * amoutSold;

        i = 0;
        while(i < 1 ){
            totalCost = cost * amoutSold;
            //if the buyer has enough credits and the amount to buy is not zero return amount to be sold
            if((TeamCreds >= totalCost)&&(totalCost > 0)){

                return amoutSold;
            // if amount is zero cancel perchus and return zero
            }else if ((totalCost <= 0)) {
                amoutSold = 0;
                return amoutSold;

            // if buyer dosent have enough credits reduce the buy amount until they do
            }else{

                amoutSold = amoutSold - 1;

            }
        }
        amoutSold = 0;
        return amoutSold;

    }

    /**
     *  subCredits deducts credits from one team and adds them to another, first checks that the buying team
     *  has enough credits then proses the sale
     *
     * @param connection connection to database
     * @param BuyTeam the team buying the items
     * @param SellTeam the team selling
     * @param cost the per unit cost of the asset
     * @param amount the ammount of the asset being sold
     *
     * @return 1 if sale prosesed 0 if insufficient funds
     */
    public static int subCredits(Connection connection, String BuyTeam,String SellTeam,int cost, int amount) throws SQLException {

        //gets the buy teams credits
        int TeamCreds = DBConnect.getCredits(connection, BuyTeam);

        //gets the total cost of the transaction
        int totalCost = amount * cost;

        //checks if they have enough credits
        if((TeamCreds >= totalCost)&&(!(BuyTeam.equals(SellTeam)))){

            DBConnect.addCredits(connection, BuyTeam, -totalCost);
            DBConnect.addCredits(connection, SellTeam, totalCost);
            //sale processed
            return 1;
        }
        //not enough credits
        return 0;
    }

}
