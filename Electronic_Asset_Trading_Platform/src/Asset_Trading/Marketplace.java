package Asset_Trading;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * The market place class that can will facilitate the buying and selling of assets
 */

public class Marketplace {

    public static void main(String[] args) throws SQLException {
        Connection connection = DBConnect.getInstance();
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

        int num = DBConnect.numRows(connection);

        for (int i = 1; i < num; i++) {
            num = DBConnect.numRows(connection);
            if((i+1) <= num){
                Asset1 = DBConnect.getNthAsset(connection,i );
                Asset2 = DBConnect.getNthAsset(connection,i+1 );
                BuyOrSell1 = DBConnect.getNthBuyOrSell(connection,i);
                BuyOrSell2 = DBConnect.getNthBuyOrSell(connection,i+1);
                Team1 = DBConnect.getNthTeam(connection,i);
                Team2 = DBConnect.getNthTeam(connection,i+1);
                BuyTeam = Team1;
                SellTeam = Team2;
            }else{
                break;
            }

            if(Asset1.equals(Asset2)){
                if((BuyOrSell1.equals(BuyOrSell2))){
                    String a = ("");
                }else{
                    String alltogether = ("they dont match they are buy and sell");
                    String Price1 = DBConnect.getNthPrice(connection,i );
                    String Price2 = DBConnect.getNthPrice(connection,i+1 );
                    int p1 = Integer.parseInt(Price1);
                    int p2 = Integer.parseInt(Price2);

                    if(BuyOrSell1 == "Buy"){
                        BuyTeam = Team1;
                        SellTeam = Team2;
                    }else if(BuyOrSell2 == "Buy"){
                        BuyTeam = Team2;
                        SellTeam = Team1;
                    }

                    if(p1 < p2){
                        if((p2-p2*0.2) <= p1){
                            System.out.println(Asset1 + " is being sold for p2 price of " + p2 );
                            DBConnect.removeItemFromShop(connection,i);
                            DBConnect.removeItemFromShop(connection,i+1);
                            //needs to moved to sales history
                            DBConnect.addShopHistoryItem(connection,Asset1, BuyTeam, SellTeam, Price2);
                            i = 1;
                        }
                    }else if(p2 < p1){
                        if((p1-p1*0.2) <= p2){
                            System.out.println(Asset1 + " is being sold for p1 price of " + p1 );
                            DBConnect.removeItemFromShop(connection,i);
                            DBConnect.removeItemFromShop(connection,i+1);
                            //needs to moved to sales history
                            DBConnect.addShopHistoryItem(connection,Asset1, BuyTeam, SellTeam, Price1);
                            num = DBConnect.numRows(connection);
                        }
                    }else if(p2 == p1){
                        System.out.println(Asset1 + " is being sold, p1 and p2 are the same  price of " + p1 );
                        DBConnect.removeItemFromShop(connection,i);
                        DBConnect.removeItemFromShop(connection,i+1);
                        DBConnect.addShopHistoryItem(connection,Asset1, BuyTeam, SellTeam, Price1);
                        num = DBConnect.numRows(connection);
                        //needs to moved to sales history
                    }else{
                        System.out.println(Asset1 + "Not being sold Price is not a match " + p1 + " and " + p2 + " to far apart");
                    }
                }
            }
        }
    }
}
