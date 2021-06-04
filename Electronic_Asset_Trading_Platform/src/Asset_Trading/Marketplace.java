package Asset_Trading;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * The market place class that can will facilitate the buying and selling of assets
 */

public class Marketplace {

    public static void main(String[] args) throws SQLException {
        Connection connection = DBConnect.getInstance();

        //DBConnect.deductItemsFromShop(connection, 1, 2);

        marketplace(connection);
    }


    public static void marketplace(Connection connection) throws SQLException {

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

        for (int i = 1; i < num; i++) {
            num = DBConnect.numShopRows(connection);
            if((i+1) <= num){
                Asset1 = DBConnect.getNthAsset(connection,i );
                Asset2 = DBConnect.getNthAsset(connection,i+1 );
                BuyOrSell1 = DBConnect.getNthBuyOrSell(connection,i);
                BuyOrSell2 = DBConnect.getNthBuyOrSell(connection,i+1);
                Team1 = DBConnect.getNthShopTeam(connection,i);
                Team2 = DBConnect.getNthShopTeam(connection,i+1);
                BuyTeam = Team1;
                SellTeam = Team2;
                idx = Integer.parseInt(DBConnect.getShopidx(connection,i));
                idx2 = Integer.parseInt(DBConnect.getShopidx(connection,i+1));

            }else{
                break;
            }

            //if the assets are the same
            if((Asset1.equals(Asset2))&&(!(BuyTeam.equals(SellTeam)))){
                //and one is buying one is selling
                if((BuyOrSell1.equals(BuyOrSell2))){
                    String a = ("");
                }else{
                    // one is buy on is sell compare the price
                    String alltogether = ("they dont match they are buy and sell");
                    String Price1 = DBConnect.getNthPrice(connection,i );
                    String Price2 = DBConnect.getNthPrice(connection,i+1 );
                    int p1 = Integer.parseInt(Price1);
                    int p2 = Integer.parseInt(Price2);

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


                    if(p1 < p2){
                        if((p2-p2*0.2) <= p1){

                            int amountToBeSold = saleCheck(connection, BuyTeam, p1, buyamount, sellamount,sellidx);
                            if( amountToBeSold > 0){
                                if(subCredits(connection, BuyTeam,SellTeam, p1,amountToBeSold)>0){
                                    System.out.println(Asset1 + " is being sold for p1 price of " + p1 );

                                    DBConnect.deductItemsFromShop(connection, buyidx, sellidx, amountToBeSold);
                                    //needs to moved to sales history
                                    DBConnect.addAssetToTeam(connection,BuyTeam, Asset1, amountToBeSold);
                                    DBConnect.addAssetToTeam(connection,SellTeam, Asset1, -amountToBeSold);
                                    DBConnect.addShopHistoryItem(connection,Asset1, BuyTeam, SellTeam, Price1);
                                    num = DBConnect.numShopRows(connection);
                                    i = 0;
                                }
                            }else{
                                System.out.println("sale not procesed insuficiant funds");
                            }
                        }
                    }else if(p2 < p1){
                        if((p1-p1*0.2) <= p2){

                            int amountToBeSold = saleCheck(connection, BuyTeam, p1, buyamount, sellamount,sellidx);
                            if( amountToBeSold > 0){
                                if(subCredits(connection, BuyTeam,SellTeam, p1,amountToBeSold)>0){;
                                    System.out.println(Asset1 + " is being sold for p2 price of " + p2 );
                                    DBConnect.deductItemsFromShop(connection, buyidx, sellidx, amountToBeSold);
                                    DBConnect.addAssetToTeam(connection,BuyTeam, Asset1, amountToBeSold);
                                    DBConnect.addAssetToTeam(connection,SellTeam, Asset1, -amountToBeSold);

                                    //needs to moved to sales history
                                    DBConnect.addShopHistoryItem(connection,Asset1, BuyTeam, SellTeam, Price2);
                                    num = DBConnect.numShopRows(connection);
                                    i = 0;
                                }
                            }else{
                                System.out.println("sale not procesed insuficiant funds");
                            }
                        }

                    }else if(p2 == p1){


                        //check if the buyer has enough credits and then deduct them and add them to seller
                        int amountToBeSold = saleCheck(connection, BuyTeam, p1, buyamount, sellamount,sellidx);
                        if( amountToBeSold > 0){
                            if(subCredits(connection, BuyTeam,SellTeam, p1,amountToBeSold)>0) {
                                System.out.println(Asset1 + " is being sold, p1 and p2 are the same  price of " + p1);
                                DBConnect.deductItemsFromShop(connection, buyidx, sellidx, amountToBeSold);
                                DBConnect.addAssetToTeam(connection,BuyTeam, Asset1, amountToBeSold);
                                DBConnect.addAssetToTeam(connection,SellTeam, Asset1, -amountToBeSold);


                                //needs to moved to sales history
                                DBConnect.addShopHistoryItem(connection, Asset1, BuyTeam, SellTeam, Price1);
                                num = DBConnect.numShopRows(connection);
                                i = 0;
                            }
                        }else{
                            System.out.println("sale not procesed insuficiant funds");
                        }

                    }else{
                        System.out.println(Asset1 + "Not being sold Price is not a match " + p1 + " and " + p2 + " to far apart");
                    }
                }
            }
        }
    }


    public static int saleCheck(Connection connection, String BuyTeam,int cost, int buyamount, int sellamount, int sellidx) throws SQLException {

        int amoutSold = 0;
        int TeamCreds = DBConnect.getCredits(connection, BuyTeam);

        if (buyamount > sellamount){
            amoutSold = sellamount;
        }else if(buyamount < sellamount){
            amoutSold = buyamount;
        }else if(buyamount == sellamount){
            amoutSold = buyamount;
        }

        int inShop = DBConnect.getAmountShop(connection, sellidx);

        int i = 0;
        while(i < 1 ){
            if((amoutSold > inShop)&&(inShop > 0)){
                amoutSold = amoutSold - 1;
            }else{
                i = 2;
            }
        }

        int totalCost = cost * amoutSold;

        i = 0;
        while(i < 1 ){
            totalCost = cost * amoutSold;
            if((TeamCreds >= totalCost)&&(totalCost > 0)){

                return amoutSold;

            }else if ((totalCost <= 0)) {
                return 0;
            }else{

                amoutSold = amoutSold - 1;

            }
        }

        return 0;

    }


    public static int subCredits(Connection connection, String BuyTeam,String SellTeam,int cost, int amount) throws SQLException {

        int TeamCreds = DBConnect.getCredits(connection, BuyTeam);

        int totalCost = amount * cost;

        if((TeamCreds >= totalCost)&&(!(BuyTeam.equals(SellTeam)))){

            DBConnect.addCredits(connection, BuyTeam, -totalCost);
            DBConnect.addCredits(connection, SellTeam, totalCost);
            return 1;
        }
        return 0;
    }

}
