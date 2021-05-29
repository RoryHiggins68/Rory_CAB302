package Asset_Trading;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.SQLException;

//import static jdk.internal.net.http.HttpResponseImpl.RawChannelProvider.connection;

public class ClientGUI {
    private static Object ButtonListener;
    private static Connection connection;
    private static JTextField firstname;
    private static JTextField lastname;
    private static JTextField username;
    private static JTextField team;
    private static JTextField assetName;
    private static JTextField BuySell;
    private static JTextField Price;
    private static JComboBox BuyorSell;
    private static JComboBox TeamOption;
    private static JButton newUserbutton;
    private static JButton submit;
    private static JButton submitNewSale;
    private static JButton newSale;
    private static JButton ViewTeamsItems;
    private static JButton PurchaseHistory;
    private static String buyorsell;
    private static String teamop;
    private static String userName;
    private static String Team;
    private static int credits;

    public static void showClientGUI(String UName) throws SQLException {

        connection = DBConnect.getInstance();
        userName = UName;
        credits = DBConnect.getCredits(connection, UName);
        String Te = DBConnect.getTeam(connection, UName);
        Team = Te;


        //set up window
        JFrame frame = new JFrame("my GUI");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel label = new JLabel("hello world");
        frame.setPreferredSize(new Dimension(750, 750));

        JMenuBar menBar = menuBar();
        frame.setJMenuBar(menBar);

        //set up panel
        JPanel intePanel = new JPanel();
        frame.getContentPane().add(intePanel);
        Border blackline = BorderFactory.createLineBorder(Color.black);

        BorderLayout layoutMan = new BorderLayout();
        intePanel.setLayout(layoutMan);
        JPanel sideBar = new JPanel();
        intePanel.add(sideBar, BorderLayout.EAST);
        sideBar.setBorder(blackline);
        JPanel buttonPanel = new JPanel(new GridLayout(10, 1));
        intePanel.add(buttonPanel, BorderLayout.EAST);

        newSale = new JButton("Post new sale");
        ViewTeamsItems = new JButton("View teams items");
        PurchaseHistory = new JButton("Purchase History");
        JButton filterItems = new JButton("Filter Items");
        JButton resetItems = new JButton("Reset Items");
        JButton Details = new JButton("Details");
        newUserbutton = new JButton("Add a new user");

        //newUserbutton.addActionListener(new ButtonListener());

        Dimension dimension = new Dimension(150, 20);
        newSale.setPreferredSize(dimension);
        ViewTeamsItems.setPreferredSize(dimension);
        PurchaseHistory.setPreferredSize(dimension);
        filterItems.setPreferredSize(dimension);
        resetItems.setPreferredSize(dimension);
        Details.setPreferredSize(dimension);
        newUserbutton.setPreferredSize(dimension);


        buttonPanel.add(newSale);
        buttonPanel.add(ViewTeamsItems);
        buttonPanel.add(PurchaseHistory);
        buttonPanel.add(newUserbutton);

        newUserbutton.addActionListener(new ButtonListener());
        newSale.addActionListener(new ButtonListener());
        ViewTeamsItems.addActionListener(new ButtonListener());
        PurchaseHistory.addActionListener(new ButtonListener());

        newSale.setPreferredSize(dimension);

        topPanel(intePanel, userName);

//        connection = DBConnect.getInstance();

        //int i = 1;
        //while (i == 1){
        refreshShop( connection, intePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
       // }

    }

    public static void refreshShop(Connection connection,JPanel intePanel) throws SQLException {

        int num = DBConnect.numRows(connection);
        JPanel shopPanel = new JPanel(new GridLayout(num, 1));
        JScrollPane shopPanel2 = new JScrollPane(shopPanel);
        intePanel.add((shopPanel2), BorderLayout.CENTER);

        JPanel[] itempanels = new JPanel[num];
        JLabel[] assetname = new JLabel[num];

        Dimension saleitem = new Dimension(100, 50);

        for (int i = 0; i < num; i++) {

            itempanels[i] = new JPanel();
            String Asset = DBConnect.getNthAsset(connection,i+1 );
            String Team = DBConnect.getNthTeam(connection,i+1 );
            String BuyOrSell = DBConnect.getNthBuyOrSell(connection,i+1);
            String Price = DBConnect.getNthPrice(connection,i+1);

            String alltogether = ("The "+Team+" team is looking to "+BuyOrSell+" "+Asset+" for " + Price +" Credits");
            assetname[i] = new JLabel(alltogether);
            Border blackline = BorderFactory.createLineBorder(Color.black);
            itempanels[i].setBorder(blackline);
            itempanels[i].add(assetname[i]);
            itempanels[i].setPreferredSize(saleitem);
            shopPanel.add(itempanels[i]);

        }

    }

    public static void addUserGUI( ){
        JFrame frame = new JFrame("my GUI");
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        JPanel addUserPanel = new JPanel(new GridLayout(6, 1));
        Dimension addUserDim = new Dimension(500, 300);
        addUserPanel.setPreferredSize(addUserDim);
        frame.add(addUserPanel);

        String text = ("Please enter the users first and last name, then a user name for logging onto the " +
                "system and finaly enter the team " +"the users is a member of.");


        JTextArea textArea = new JTextArea(2, 35);
        textArea.setText(text);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBackground(UIManager.getColor("Label.background"));
        textArea.setFont(UIManager.getFont("Label.font"));
        textArea.setBorder(UIManager.getBorder("Label.border"));



        JLabel enterFirstName = new JLabel("Enter User's First Name Here: ");
        JLabel enterLastName = new JLabel("Enter User's Last Name Here: ");
        JLabel enterUserName = new JLabel("Enter User Name Here: ");
        JLabel enterTeamName = new JLabel("Enter User's  Team Here: ");

        firstname = new JTextField("",20);
        lastname = new JTextField("",20);
        username = new JTextField("",20);
        team = new JTextField("",20);


        BorderLayout inputlayout = new BorderLayout();

        JPanel DetalsPanel = new JPanel();
        JPanel firstnamePanel = new JPanel();
        JPanel lastnamePanel = new JPanel();
        JPanel usernamePanel = new JPanel();
        JPanel teamnamePanel = new JPanel();
        JPanel Submitbutton = new JPanel();

        addUserPanel.add(DetalsPanel);
        addUserPanel.add(firstnamePanel);
        addUserPanel.add(lastnamePanel);
        addUserPanel.add(usernamePanel);
        addUserPanel.add(teamnamePanel);
        addUserPanel.add(Submitbutton);

        DetalsPanel.add(textArea,BorderLayout.CENTER);
        firstnamePanel.add(enterFirstName,BorderLayout.EAST);
        lastnamePanel.add(enterLastName,BorderLayout.EAST);
        usernamePanel.add(enterUserName,BorderLayout.EAST);
        teamnamePanel.add(enterTeamName,BorderLayout.EAST);
        firstnamePanel.add(firstname,BorderLayout.WEST);
        lastnamePanel.add(lastname,BorderLayout.WEST);
        usernamePanel.add(username,BorderLayout.WEST);
        teamnamePanel.add(team,BorderLayout.WEST);

        submit = new JButton("Submit");

        submit.addActionListener(new ButtonListener());

        Submitbutton.add(submit,BorderLayout.CENTER);


        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void addNewSaleGUI( ) {
        JFrame frame = new JFrame("my GUI");
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        JPanel addAssetPanel = new JPanel(new GridLayout(6, 1));
        Dimension addassetDim = new Dimension(400, 250);
        addAssetPanel.setPreferredSize(addassetDim);
        frame.add(addAssetPanel);

        String text = ("Please enter the name Name the price what team is offering and " +
                "whether you are buying or selling.");


        JTextArea textArea = new JTextArea(2, 35);
        textArea.setText(text);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBackground(UIManager.getColor("Label.background"));
        textArea.setFont(UIManager.getFont("Label.font"));
        textArea.setBorder(UIManager.getBorder("Label.border"));


        JLabel EnterAssetLabel = new JLabel("Enter the name of the Asset:  ");
        JLabel BuySellLabel = new JLabel("Select buying or selling:   ");
        JLabel theTeamLabel = new JLabel("The team making the offer:   ");
        JLabel priceLabel = new JLabel("Enter the price:   ");

        JPanel assetDetailsPanel = new JPanel();
        JPanel assetNamePanel = new JPanel();
        JPanel BuySellPanel = new JPanel();
        JPanel TeamPanel = new JPanel();
        JPanel pricePanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        assetName = new JTextField(" ",20);
        String option[]= {" ", "BUY","SELL"};
        BuyorSell = new JComboBox(option);
        String teamoption[]= {" ", "A","B","C","D","E"};
        TeamOption = new JComboBox(teamoption);
        Price = new JTextField(" ",20);
        submitNewSale = new JButton("Submit");

        addAssetPanel.add(assetDetailsPanel);
        addAssetPanel.add(assetNamePanel);
        addAssetPanel.add(BuySellPanel);
        addAssetPanel.add(TeamPanel);
        addAssetPanel.add(pricePanel);
        addAssetPanel.add(buttonPanel);


        assetDetailsPanel.add(textArea,BorderLayout.CENTER);
        assetNamePanel.add(EnterAssetLabel,BorderLayout.EAST);
        assetNamePanel.add(assetName,BorderLayout.WEST);
        BuySellPanel.add(BuySellLabel,BorderLayout.EAST);
        BuySellPanel.add(BuyorSell,BorderLayout.WEST);
        TeamPanel.add(theTeamLabel,BorderLayout.EAST);
        TeamPanel.add(TeamOption,BorderLayout.WEST);
        pricePanel.add(priceLabel,BorderLayout.EAST);
        pricePanel.add(Price,BorderLayout.WEST);
        buttonPanel.add(submitNewSale,BorderLayout.CENTER);

        submitNewSale.addActionListener(new ButtonListener());
        BuyorSell.addActionListener(new ComboListener());
        TeamOption.addActionListener(new ComboListener());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void teamitems() throws SQLException {

        String team = Team;

        JFrame frame = new JFrame("Teams Items");
        //JPanel teamitempanel = new JPanel();
        //frame.add(teamitempanel);


        int num = DBConnect.numRows(connection);
        JPanel teamsitems = new JPanel(new GridLayout(num, 1));
        JScrollPane teamsitemsscroll = new JScrollPane(teamsitems);

        frame.add((teamsitemsscroll));
        teamsitemsscroll.setVisible(true);
        teamsitemsscroll.createVerticalScrollBar();


        JPanel[] itempanels = new JPanel[num];
        JLabel[] assetname = new JLabel[num];
        Dimension saleitem = new Dimension(350, 45);


        for (int i = 0; i < num; i++) {

            String itemTeam = DBConnect.getNthTeam(connection,i+1 );

            //if (itemTeam == team){
            if (itemTeam.equals(team)){

                itempanels[i] = new JPanel();
                String Asset = DBConnect.getNthAsset(connection,i+1 );
                String Team = DBConnect.getNthTeam(connection,i+1 );
                String BuyOrSell = DBConnect.getNthBuyOrSell(connection,i+1);
                String Price = DBConnect.getNthPrice(connection,i+1);

                String alltogether = ("The "+Team+" team is looking to "+BuyOrSell+" "+Asset+" for " + Price +" Credits");
                assetname[i] = new JLabel(alltogether);
                Border blackline = BorderFactory.createLineBorder(Color.black);
                itempanels[i].setBorder(blackline);
                itempanels[i].add(assetname[i]);
                itempanels[i].setPreferredSize(saleitem);
                teamsitems.add(itempanels[i]);

            }
        }


        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    };

    public static void SaleHistory() throws SQLException {

        String team = Team;

        JFrame frame = new JFrame("Sale History");
        //JPanel teamitempanel = new JPanel();
        //frame.add(teamitempanel);


        int num = DBConnect.numRows(connection);
        num = 4;
        JPanel teamsitems = new JPanel(new GridLayout(num, 1));
        JScrollPane teamsitemsscroll = new JScrollPane(teamsitems);

        frame.add((teamsitemsscroll));
        teamsitemsscroll.setVisible(true);
        teamsitemsscroll.createVerticalScrollBar();


        JPanel[] itempanels = new JPanel[num];
        JLabel[] assetname = new JLabel[num];
        Dimension saleitem = new Dimension(350, 45);


        for (int i = 0; i < 4; i++) {

            String itemTeam = DBConnect.getNthTeam(connection,i+1 );

            //if (itemTeam == team){
            //if (itemTeam.equals(team)){

                itempanels[i] = new JPanel();
                String Asset = DBConnect.getShopHisAsset(connection,i+1 );
                String BuyTeam = DBConnect.getShopHisBuyTeam(connection,i+1 );
                String SellTeam = DBConnect.getShopHisSellTeam(connection,i+1);
                String Price = DBConnect.getShopHisPrice(connection,i+1);

                String alltogether = ("The "+BuyTeam+" team bought "+Asset+" from "+SellTeam+" for " + Price +" Credits");
                assetname[i] = new JLabel(alltogether);
                Border blackline = BorderFactory.createLineBorder(Color.black);
                itempanels[i].setBorder(blackline);
                itempanels[i].add(assetname[i]);
                itempanels[i].setPreferredSize(saleitem);
                teamsitems.add(itempanels[i]);


        }


        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }

    public static void main(String[] args) throws SQLException {
        String userName = "bob_smith";

        ClientGUI ClientShop = new ClientGUI();
        ClientShop.showClientGUI(userName);

        //ClientGUI test = new ClientGUI();
        //test.addUserGUI();

        //ClientGUI test = new ClientGUI();
        //test.addNewSaleGUI();

    }

    private static class ComboListener implements ActionListener{



        public void actionPerformed(ActionEvent e) {

            System.out.println("COMBO EVENTS");

            JComboBox cb = (JComboBox)e.getSource();

            if(cb == BuyorSell){

                buyorsell = (String)cb.getSelectedItem();
                System.out.println("its 1 " + buyorsell);

            }else if(cb == TeamOption){

                teamop = (String)cb.getSelectedItem();
                System.out.println("its 2 " + teamop);

            }

        }
    }
    
    private static class ButtonListener implements ActionListener {

        public void addUSer(ActionEvent e) throws SQLException {
            JButton source = (JButton) e.getSource();
            if (source == submit) {
                newUser();
            }
        }

        public void actionPerformed(ActionEvent e){


            JButton source = (JButton) e.getSource();
            if (source == submit) {
                try {
                    newUser();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else if (source == newUserbutton) {
                launchnewUserGUI();
            }else if (source == newSale){
                launchnewSaleGUI();
            }else if (source == submitNewSale){
                try {
                    newSale();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else if(source == ViewTeamsItems){
                try {
                    showteamitems();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }else if(source == PurchaseHistory ){
                try {
                    showteamPurchaseHistory();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        }

        private void showteamPurchaseHistory() throws SQLException {
            ClientGUI PurchaseHistory = new ClientGUI();
            PurchaseHistory.SaleHistory();


        }

        private void showteamitems() throws SQLException {
            ClientGUI test = new ClientGUI();
            test.teamitems();

        }

        private void launchnewUserGUI() {
            ClientGUI test = new ClientGUI();
            test.addUserGUI();

        }

        private void launchnewSaleGUI() {
            ClientGUI test = new ClientGUI();
            test.addNewSaleGUI();

        }

        private void newUser() throws SQLException {
            if (firstname.getText() != null ){
                User p = new User(firstname.getText(), lastname.getText(), username
                        .getText(), team.getText());
                System.out.println("new user created " + p.firstName);
                firstname.setText("");
                lastname.setText("");
                username.setText("");
                team.setText("");

                DBConnect.addUser(connection,p);

            }





        }

        private void newSale() throws SQLException {
            if (assetName.getText() != null ){

                String bors = buyorsell;
                String tea = teamop;
                String assname = assetName.getText();
                String assetprice = Price.getText();

                if(assname.length() < 3 || assetprice.length() < 2 ) {
                    JOptionPane.showMessageDialog(null, "Please check the values entered something has been left blank");
                }else if(assname == null || bors == null || tea == null || assetprice == null){
                    JOptionPane.showMessageDialog(null, "Please check the values entered something has been left null");
                }else{
                    System.out.println("new sale created " + assname + " " + bors + " " + tea + " " + assetprice);
                    JOptionPane.showMessageDialog(null, "The "+tea+" team is looking to "+bors+" "+assname+" for C" + assetprice);
                    connection = DBConnect.getInstance();
                    DBConnect.addItemToShop(connection,assname,tea,bors,assetprice);
                    assetName.setText(" ");
                    Price.setText(" ");
                    BuyorSell.setSelectedIndex(1);
                    TeamOption.setSelectedIndex(1);

                }



            }
        }
        
    }

    public static JMenuBar menuBar(){

        //add basic widow stuff
        JMenuBar menBar = new JMenuBar();
        JMenu menu = new JMenu("file");
        menu.add("open");
        menu.add("save");
        menBar.add(menu);

        //JMenuBar men2 = new JMenuBar();
        JMenu menu2 = new JMenu("Account");
        menu2.add("open");
        menu2.add("save");
        menBar.add(menu2);

        //JMenuBar men3 = new JMenuBar();
        JMenu menu3 = new JMenu("Team");
        menu3.add("open");
        menu3.add("save");
        menBar.add(menu3);

        //JMenuBar menbar4 = new JMenuBar();
        JMenu menu4 = new JMenu("Help");
        menu4.add("open");
        menu4.add("save");
        menBar.add(menu4);

        return menBar;
    }

    public static void topPanel(JPanel intePanel, String UserName){


        JPanel topPanel = new JPanel(new GridLayout(1, 6));
        JLabel teamName = new JLabel("Team name: " + Team);
        JLabel userName = new JLabel("User name: " + UserName);
        JLabel search_label = new JLabel("         ");
        JLabel credit = new JLabel("Team credits: C " + credits);
        JTextField search = new JTextField( );
        JButton searchButton = new JButton("Search");

        Dimension labelDimension = new Dimension(750, 35);
        topPanel.setPreferredSize(labelDimension);

        Dimension searchDim = new Dimension(10, 5);
        search_label.setPreferredSize(searchDim);
        searchButton.setPreferredSize(searchDim);
        search.setPreferredSize(searchDim);

        intePanel.add((topPanel), BorderLayout.NORTH);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        topPanel.setBorder(blackline);

        topPanel.add(teamName);
        topPanel.add(userName);

        //topPanel.add(searchButton);
        //topPanel.add(search);
        //topPanel.add(search_label);
        topPanel.add(credit);

    }

}

