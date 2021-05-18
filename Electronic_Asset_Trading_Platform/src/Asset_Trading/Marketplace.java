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


        int num = DBConnect.numRows(connection);

        for (int i = 1; i < num; i++) {
            if((i+1) <= num){
                Asset1 = DBConnect.getNthAsset(connection,i );
                Asset2 = DBConnect.getNthAsset(connection,i+1 );
                BuyOrSell1 = DBConnect.getNthBuyOrSell(connection,i);
                BuyOrSell2 = DBConnect.getNthBuyOrSell(connection,i+1);
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

                    if(p1 < p2){
                        if((p2-p2*0.2) <= p1){
                            System.out.println(Asset1 + " is being sold for p1 price of " + p1 );
                            DBConnect.removeItemFromShop(connection,i);
                            DBConnect.removeItemFromShop(connection,i+1);
                            //needs to moved to sales history

                        }
                    }else if(p2 < p1){
                        if((p1-p1*0.2) <= p2){
                            System.out.println(Asset1 + " is being sold for p1 price of " + p1 );
                            DBConnect.removeItemFromShop(connection,i);
                            DBConnect.removeItemFromShop(connection,i+1);
                            //needs to moved to sales history
                        }
                    }else if(p2 == p1){
                        System.out.println(Asset1 + " is being sold, p1 and p2 are the same  price of " + p1 );
                        DBConnect.removeItemFromShop(connection,i);
                        DBConnect.removeItemFromShop(connection,i+1);
                        //needs to moved to sales history
                    }else{
                        System.out.println(Asset1 + "Not being sold Price is not a match " + p1 + " and " + p2 + " to far apart");

                    }
                }
            }

        }



    }


}
