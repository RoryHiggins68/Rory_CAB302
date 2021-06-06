package Asset_Trading;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.SQLException;

import static Asset_Trading.DBConnect.getCredits;

//import static jdk.internal.net.http.HttpResponseImpl.RawChannelProvider.connection;


/**
 * The client GUI contains the verius GUIs that make up the the program, it will be launched after
 * a successful login and from here the user will be able to do all main operations of the program
 *
 * The client GUI will be the main area that the user starts from and were the shop and controls
 * cne be seen.
 */
public class ClientGUI {
    private static JFrame Mainframe;
    private static Object ButtonListener;
    private static Connection connection;
    private static JTextField firstname;
    private static JTextField lastname;
    private static JTextField username;
    private static JTextField team;
    private static JTextField assetName;
    private static JTextField quantity;
    private static JTextField BuySell;
    private static JTextField Price;
    private static JComboBox selectAsset;
    private static JComboBox BuyorSell;
    private static JComboBox TeamOption;
    private static JButton newUserbutton;
    private static JButton submit;
    private static JButton submitNewSale;
    private static JButton newSale;
    private static JButton ViewTeamsItems;
    private static JButton PurchaseHistory;
    private static JButton RefreshButton;
    private static String buyorsell;
    private static String teamop;
    private static String assetop;
    private static String userName;
    private static String Team;
    private static JPanel intePanel;
    private static JPanel shopPanel;
    private static int credits;


    /**
     * Sets up the main user interface, from here the user can see the main Shop
     * area with all post buy sell requests, as well as lunch other GUIs. The teams credits will be
     * desplayed as well as there acount and privlage level ie (team lead, admin, user)
     *
     * @param UName the name of the user that has logged on, this will be used to get their team ect.
     */
    public static void showClientGUI(String UName) throws SQLException {

        //set up parameters need for the display
        connection = DBConnect.getInstance();
        userName = UName;
        String Te = DBConnect.getTeam(connection, UName);
        Team = Te;
        credits = getCredits(connection, Team);
        //connection.close();


        //set up window
        Mainframe = new JFrame("Electronic Asset Trading Platform");
        Mainframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Mainframe.setPreferredSize(new Dimension(750, 750));

        //JMenuBar menBar = menuBar();
        //Mainframe.setJMenuBar(menBar);

        //set up main panel side bar and
        intePanel = new JPanel();
        Mainframe.getContentPane().add(intePanel);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        BorderLayout layoutMan = new BorderLayout();
        intePanel.setLayout(layoutMan);
        JPanel sideBar = new JPanel();
        intePanel.add(sideBar, BorderLayout.EAST);
        sideBar.setBorder(blackline);
        JPanel buttonPanel = new JPanel(new GridLayout(10, 1));
        intePanel.add(buttonPanel, BorderLayout.EAST);

        //add buttons and set dimention
        RefreshButton = new JButton("Refresh the store");
        newSale = new JButton("Post new sale");
        ViewTeamsItems = new JButton("View teams items");
        PurchaseHistory = new JButton("Purchase History");
        JButton filterItems = new JButton("Filter Items");
        JButton resetItems = new JButton("Reset Items");
        JButton Details = new JButton("Details");
        newUserbutton = new JButton("Add a new user");
        Dimension dimension = new Dimension(150, 20);
        RefreshButton.setPreferredSize(dimension);
        newSale.setPreferredSize(dimension);
        ViewTeamsItems.setPreferredSize(dimension);
        PurchaseHistory.setPreferredSize(dimension);
        filterItems.setPreferredSize(dimension);
        resetItems.setPreferredSize(dimension);
        Details.setPreferredSize(dimension);
        newUserbutton.setPreferredSize(dimension);

        //add buttons to buttonPanel
        buttonPanel.add(RefreshButton);
        buttonPanel.add(newSale);
        buttonPanel.add(ViewTeamsItems);
        buttonPanel.add(PurchaseHistory);
        buttonPanel.add(newUserbutton);

        // add the Action Listeners for the buttons
        RefreshButton.addActionListener(new ButtonListener());
        newUserbutton.addActionListener(new ButtonListener());
        newSale.addActionListener(new ButtonListener());
        ViewTeamsItems.addActionListener(new ButtonListener());
        PurchaseHistory.addActionListener(new ButtonListener());

        newSale.setPreferredSize(dimension);
        topPanel(intePanel, userName);

        //deplay all posted shop requests
        //connection = DBConnect.getInstance();
        populateshShop( connection, intePanel);
        connection.close();


        //pack fram and display
        Mainframe.pack();
        Mainframe.setLocationRelativeTo(null);
        Mainframe.setVisible(true);


    }

    /**
     *Refreshes the content of the shop
     *
     */
    public static void refreshShop() throws SQLException {

        Mainframe.dispose();
        ClientGUI ClientShop = new ClientGUI();
        ClientShop.showClientGUI(userName);


    }


    /**
     * Quires the database for the content of the shop and then adds all items in to the and displays the
     * on screen with their prices quantile posting team and whether they are buying or selling the item.
     *
     * @param connection is the connection to the database
     * @param intePanel the pannel were the shop posts will be added
     *
     */
    public static void populateshShop(Connection connection, JPanel intePanel) throws SQLException {

        //get the number of rows in database of shop items and create a grid lay out with that many rows

        int num = DBConnect.numShopRows(connection);
        shopPanel = new JPanel(new GridLayout(num, 1));
        JScrollPane shopPanel2 = new JScrollPane(shopPanel);
        intePanel.add((shopPanel2), BorderLayout.CENTER);
        //create labels and set the dimentions
        JPanel[] itempanels = new JPanel[num];
        JLabel[] assetname = new JLabel[num];
        Dimension saleitem = new Dimension(100, 50);


        //for each item in the shop database create a shop entry in the GUI
        for (int i = 0; i < num; i++) {

            itempanels[i] = new JPanel();
            String Asset = DBConnect.getNthAsset(connection,num-i);
            String Team = DBConnect.getNthShopTeam(connection,num-i);
            String BuyOrSell = DBConnect.getNthBuyOrSell(connection,num-i);
            String Price = DBConnect.getNthPrice(connection,num-i);

            String alltogether = ("The "+Team+" team is looking to "+BuyOrSell+" "+Asset+" for " + Price +" Credits");
            assetname[i] = new JLabel(alltogether);
            Border blackline = BorderFactory.createLineBorder(Color.black);
            itempanels[i].setBorder(blackline);
            itempanels[i].add(assetname[i]);
            itempanels[i].setPreferredSize(saleitem);
            shopPanel.add(itempanels[i]);

        }

    }

    /**
     * The GUI that is used to add new users to the user database
     */
    public static void addUserGUI( ){

        //create new frame
        JFrame frame = new JFrame("New User");
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Create panel set dimentions and add it to the frame
        JPanel addUserPanel = new JPanel(new GridLayout(6, 1));
        Dimension addUserDim = new Dimension(500, 300);
        addUserPanel.setPreferredSize(addUserDim);
        frame.add(addUserPanel);

        // write instructions, format the text
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


        //Add field labels
        JLabel enterFirstName = new JLabel("Enter User's First Name Here: ");
        JLabel enterLastName = new JLabel("Enter User's Last Name Here: ");
        JLabel enterUserName = new JLabel("Enter User Name Here: ");
        JLabel enterTeamName = new JLabel("Enter User's  Team Here: ");

        // ceate text fields
        firstname = new JTextField("",20);
        lastname = new JTextField("",20);
        username = new JTextField("",20);
        team = new JTextField("",20);


        //create panels organise fields and labels
        BorderLayout inputlayout = new BorderLayout();
        JPanel DetalsPanel = new JPanel();
        JPanel firstnamePanel = new JPanel();
        JPanel lastnamePanel = new JPanel();
        JPanel usernamePanel = new JPanel();
        JPanel teamnamePanel = new JPanel();
        JPanel Submitbutton = new JPanel();

        // add them to frame
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

        // create submit button add a listener to it and add it to frame
        submit = new JButton("Submit");
        submit.addActionListener(new ButtonListener());
        Submitbutton.add(submit,BorderLayout.CENTER);

        // pack frame and display
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    /**
     * The GUI that is used to add new users to the user database
     */
    public static void addNewSaleGUI( ) throws SQLException {

        // create new frame
        JFrame frame = new JFrame("Post new sale");

        //add the panels to organise the GUI
        JPanel addAssetPanel = new JPanel(new GridLayout(7, 1));
        Dimension addassetDim = new Dimension(400, 350);
        addAssetPanel.setPreferredSize(addassetDim);
        frame.add(addAssetPanel);

        //add instructions
        String text = ("Please select a Asset and enter the price and " +
                "whether you are buying or selling.");

        // formatting of text
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

        //Add text field labels
        JLabel EnterAssetLabel = new JLabel("Enter the name of the Asset:  ");
        JLabel BuySellLabel = new JLabel("Select buying or selling:   ");
        JLabel theTeamLabel = new JLabel("The team making the offer:   ");
        JLabel priceLabel = new JLabel("Enter the price:   ");
        JLabel quantityLabel = new JLabel("Enter the quantity:   ");

        //Add j panels to line the buttons and text labels up
        JPanel assetDetailsPanel = new JPanel();
        JPanel quantityPanel = new JPanel();
        JPanel assetNamePanel = new JPanel();
        JPanel BuySellPanel = new JPanel();
        JPanel TeamPanel = new JPanel();
        JPanel pricePanel = new JPanel();
        JPanel buttonPanel = new JPanel();


        //get all the teams from the team database
        Connection connection3 = DBConnect.getInstance();
        int numTeam = DBConnect.getNumTeams(connection3);
        String teams[] = new String[numTeam];

        for(int i = 1; i <= numTeam; i++){
            teams[i-1] = DBConnect.getNthTeam(connection3, i);
        }

        //get the assets this team has
        int numassets = DBConnect.getNumAssets(connection3);
        String teamsAss[] = new String[numassets + 1];
        teamsAss[0] = " ";
        for(int i = 1; i <= numassets; i++){
            teamsAss[i] = DBConnect.getnthAssetfromAssetsTable(connection3, i);
        }

        connection3.close();

        String team[] = new String[2];
        team[0] = " ";
        team[1] = Team;


        //create the buttons and combo boxes
        quantity = new JTextField("1",20);
        selectAsset = new JComboBox(teamsAss);
        String option[]= {" ", "BUY","SELL"};
        BuyorSell = new JComboBox(option);
        TeamOption = new JComboBox(team);
        Price = new JTextField("1",20);
        submitNewSale = new JButton("Submit");



        //add the panels, buttons, labels and feilds and set the position
        addAssetPanel.add(quantityPanel);
        addAssetPanel.add(assetDetailsPanel);
        addAssetPanel.add(assetNamePanel);
        addAssetPanel.add(BuySellPanel);
        addAssetPanel.add(TeamPanel);
        addAssetPanel.add(pricePanel);
        addAssetPanel.add(buttonPanel);


        assetDetailsPanel.add(textArea,BorderLayout.CENTER);
        assetNamePanel.add(EnterAssetLabel,BorderLayout.EAST);
        assetNamePanel.add(selectAsset,BorderLayout.WEST);
        BuySellPanel.add(BuySellLabel,BorderLayout.EAST);
        BuySellPanel.add(BuyorSell,BorderLayout.WEST);
        TeamPanel.add(theTeamLabel,BorderLayout.EAST);
        TeamPanel.add(TeamOption,BorderLayout.WEST);
        pricePanel.add(quantityLabel,BorderLayout.EAST);
        pricePanel.add(quantity,BorderLayout.WEST);
        pricePanel.add(priceLabel,BorderLayout.EAST);
        pricePanel.add(Price,BorderLayout.WEST);
        buttonPanel.add(submitNewSale,BorderLayout.CENTER);

        // add the appropriate listeners to the button an combo box
        submitNewSale.addActionListener(new ButtonListener());
        selectAsset.addActionListener(new ComboListener());
        BuyorSell.addActionListener(new ComboListener());
        TeamOption.addActionListener(new ComboListener());

        //pack and display GUI
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    /**
     * The GUI that is used to display all items the users team have posted in the store
     */
    public static void teamitems() throws SQLException {


        // create a new frame
        String team = Team;
        JFrame frame = new JFrame("Teams Items");

        //get the number of items in shop

        Connection connection2 = DBConnect.getInstance();
        int num = DBConnect.numShopRows(connection2);
        //connection.close();

        //crate a scroll panel and add it to the frame
        JPanel teamsitems = new JPanel(new GridLayout(num, 1));
        JScrollPane teamsitemsscroll = new JScrollPane(teamsitems);
        frame.add((teamsitemsscroll));
        teamsitemsscroll.setVisible(true);
        teamsitemsscroll.createVerticalScrollBar();

        // Create an array of labels and then set dimentions for them
        JPanel[] itempanels = new JPanel[num];
        JLabel[] assetname = new JLabel[num];
        Dimension saleitem = new Dimension(350, 45);

      // for every item in the shop that has been posted by this team display itin the GUI
        connection = DBConnect.getInstance();
        for (int i = 0; i < num; i++) {

            String itemTeam = DBConnect.getNthTeam(connection,i+1 );
            //connection.close();

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
        //connection.close();

        //pack frame and desplay

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    };

    /**
     * The GUI that is used to display the items have been bought and sold
     */
    public static void SaleHistory() throws SQLException {

        //Create a new frame
        String team = Team;
        JFrame frame = new JFrame("Sale History");

        //get the number of items in shop history database
        //Connection connection = DBConnect.getInstance();
        int num = DBConnect.getNumHistoryItems(connection);
        //connection.close();

        //add a new scroll panel to the scene and create a list of labels and pannels
        JPanel teamsitems = new JPanel(new GridLayout(num, 1));
        JScrollPane teamsitemsscroll = new JScrollPane(teamsitems);
        frame.add((teamsitemsscroll));
        teamsitemsscroll.setVisible(true);
        teamsitemsscroll.createVerticalScrollBar();
        JPanel[] itempanels = new JPanel[num];
        JLabel[] assetname = new JLabel[num];
        Dimension saleitem = new Dimension(350, 45);

        // If there are no items in shop history display a message as such
        if (num == 0){
            String alltogether = ("There has not been any purchase yet.");
            JLabel assetnam = new JLabel(alltogether);
            Border bline = BorderFactory.createLineBorder(Color.black);
            JPanel itempan = new JPanel();
            itempan.setBorder(bline);
            itempan.add(assetnam);
            itempan.setPreferredSize(saleitem);
            teamsitems.add(itempan);
        }

        // for every shop history item add its information to a label and add it to the screen
        for (int i = 0; i < num; i++) {
            connection = DBConnect.getInstance();
            String itemTeam = DBConnect.getNthTeam(connection,i+1 );

                itempanels[i] = new JPanel();
                String Asset = DBConnect.getShopHisAsset(connection,i+1 );
                String BuyTeam = DBConnect.getShopHisBuyTeam(connection,i+1 );
                String SellTeam = DBConnect.getShopHisSellTeam(connection,i+1);
                String Price = DBConnect.getShopHisPrice(connection,i+1);
                //connection.close();
                String alltogether = ("The "+BuyTeam+" team bought "+Asset+" from "+SellTeam+" for " + Price +" Credits");
                assetname[i] = new JLabel(alltogether);
                Border blackline = BorderFactory.createLineBorder(Color.black);
                itempanels[i].setBorder(blackline);
                itempanels[i].add(assetname[i]);
                itempanels[i].setPreferredSize(saleitem);
                teamsitems.add(itempanels[i]);


        }

        // Pack frame and display
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }


    /**
     * Main is used to launch showClientGUI for testing, normally it will launch on user login
     */
    public static void main(String[] args) throws SQLException {

        // creats a new instance of the client GUI
        String userName = "bob_smith";

        ClientGUI ClientShop = new ClientGUI();
        ClientShop.showClientGUI(userName);


    }

    /**
     * ComboListener implements ActionListener and is used to get the values of the combo boxes used in
     * the various GUIs
     */
    private static class ComboListener implements ActionListener{


        // the active lisner for the combo boxes used in menus
        public void actionPerformed(ActionEvent e) {

            System.out.println("COMBO EVENTS");

            JComboBox cb = (JComboBox)e.getSource();



            if(cb == BuyorSell){

                buyorsell = (String)cb.getSelectedItem();
                System.out.println("its 1 " + buyorsell);

            }else if(cb == TeamOption){

                teamop = (String)cb.getSelectedItem();
                System.out.println("its 2 " + teamop);

            }else if(cb == selectAsset){

                assetop = (String)cb.getSelectedItem();
                System.out.println("its 2 " + teamop);

            }

        }
    }

    /**
     * ButtonListener implements ActionListener and is used to get the Button presses from Button
     * used in the various GUIs
     */
    private static class ButtonListener implements ActionListener {



/*        public void addUSer(ActionEvent e) throws SQLException {
            JButton source = (JButton) e.getSource();
            if (source == submit) {
                newUser();
            }
        }*/

        /**
         * This event is triggered by the active listener for the buttons and will trigger the appropriate even
         * based on the button pressed.
         */
        public void actionPerformed(ActionEvent e){

            //see what the source button was
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
                try {
                    launchnewSaleGUI();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
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
            }else if(source == RefreshButton ){
                System.out.println("refreshed");
                try {
                    refreshShop();

                } catch (SQLException throwables) {

                }
            }

        }

        /**
         * Display the GUI for the PurchaseHistory so that users can see historic prices
         */
        private void showteamPurchaseHistory() throws SQLException {

            //launch new PurchaseHistory
            ClientGUI PurchaseHistory = new ClientGUI();
            PurchaseHistory.SaleHistory();
        }

        /**
         * Show the items that this team has posted in the store
         */
        private void showteamitems() throws SQLException {

            //launch new team items GUI
            ClientGUI test = new ClientGUI();
            test.teamitems();

        }

        /**
         * Lauches a new instance of the client gui
         */
        private void launchnewUserGUI() {

            //launch new add user GUI
            ClientGUI test = new ClientGUI();
            test.addUserGUI();

        }

        /**
         * Creats a instance of the new sale window were users can post a new sale
         */
        private void launchnewSaleGUI() throws SQLException {

            // add new sale GUI
            ClientGUI test = new ClientGUI();
            test.addNewSaleGUI();

        }


        /**
         * Add a new user to the user database
         */
        private void newUser() throws SQLException {
            //Check the entered information
            if (firstname.getText() != null ){
                User p = new User(firstname.getText(), lastname.getText(), username
                        .getText(), team.getText());
                System.out.println("new user created " + p.firstName);
                firstname.setText("");
                lastname.setText("");
                username.setText("");
                team.setText("");
                //Connection connection = DBConnect.getInstance();

                // only team leaders can add new users
                if (DBConnect.isTeamLeader(connection,userName)){

                    DBConnect.addUser(connection,p);
                    //connection.close();

                }
                //connection.close();
            }





        }

        /**
         * post a new sale and add it to the shop database
         */
        private void newSale() throws SQLException {
            if (assetop != null ) {
                // get all the info that has been added in to the new user gui
                String bors = buyorsell;
                String tea = teamop;
                String assname = assetop;
                String assetprice = Price.getText();
                String ammount = quantity.getText();

                //get the amount that will and the price and make them int so the reques can have a total price
                int howmuch = Integer.parseInt(assetprice);
                int howmany = Integer.parseInt(ammount);


                //check that all info has been entered if not prompt the user
                if(assname == null){
                    JOptionPane.showMessageDialog(null, "Please check the values entered something has been left blank");
                }else if(assname.length() < 3 || assetprice.length() < 1 ) {
                    JOptionPane.showMessageDialog(null, "Please check the values entered something has been left blank");
                }else if(assname == null || bors == null || tea == null || assetprice == null){
                    JOptionPane.showMessageDialog(null, "Please check the values entered something has been left null");

                //
                }else if(( buyorsell.equals("SELL"))&&!(DBConnect.TeamHasAsset(connection,teamop,assetop))){
                    JOptionPane.showMessageDialog(null, "You do not have " + assname +" to sell");
                }else if(( buyorsell.equals("SELL"))&&!(DBConnect.getNumofATeamAsset(connection, teamop, assname, howmany))){
                    JOptionPane.showMessageDialog(null, "You do not have enough " + assname +" to sell, try again");
                }else if(( buyorsell.equals("BUY"))&&(getCredits(connection,teamop)<howmuch*howmany)){
                    JOptionPane.showMessageDialog(null, "You do not have enough credits to buy " + assname + " x " + howmany + " for " + howmuch + " per unit, your team has " + getCredits(connection,teamop) + " credit.");
                }else{
                    System.out.println("new sale created " + assname + " " + bors + " " + tea + " " + assetprice);
                    JOptionPane.showMessageDialog(null, "The "+tea+" team is looking to "+bors+" "+assname+" for C" + assetprice);
                    connection = DBConnect.getInstance();
                    DBConnect.addItemToShop(connection,assname,tea,bors,assetprice,ammount);
                    //connection.close();
                    //assetName.setText(" ");
                    Price.setText(" ");
                    BuyorSell.setSelectedIndex(1);
                    TeamOption.setSelectedIndex(1);

                }
                //connection.close();
            }
        }
        
    }

    /**
     * Top pannel displays users name, team, credits and permistion level at the top of the page
     *
     * @param intePanel the internal Jpanel that the info will be added to
     * @param UserName the user name is required to quire the data base for info
     */
    public static void topPanel(JPanel intePanel, String UserName) throws SQLException {


        JPanel topPanel = new JPanel(new GridLayout(1, 7));
        JLabel teamName = new JLabel("Team name: " + Team);
        JLabel userName = new JLabel("User name: " + UserName);
        JLabel search_label = new JLabel("         ");
        JLabel credit = new JLabel("Team credits: C " + credits);
        JTextField search = new JTextField( );
        JButton searchButton = new JButton("Search");
        String accType = "User";

       // Connection connection = DBConnect.getInstance();
        boolean isLead = true; // DBConnect.isTeamLeader(connection ,UserName);
        //connection.close();
        if (isLead) {
            accType = "Team leader ";
        }



        JLabel UserType = new JLabel("User account: " + accType);

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
        topPanel.add(UserType);

        //topPanel.add(searchButton);
        //topPanel.add(search);
        //topPanel.add(search_label);
        topPanel.add(credit);

    }

}

