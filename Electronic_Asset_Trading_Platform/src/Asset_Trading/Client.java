package Asset_Trading;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * This the users client that will allow them to access the system by logging
 * in with their user credentials.
 */

public class Client {

    private static JFrame frame;
    private static JButton Login;
    private static JTextField UserName;
    private static JTextField Password;
    private static Client loginClient;


    public static void main(String[] args) throws IOException, SQLException {
        loginClient = new Client();
        loginClient.showClientGUI();


    }



    /**
     * Displays the user login, this is the first screen the user will from here they can loging to
     * the system
     */
    public static void showClientGUI() throws SQLException {

        //set up window
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //create the panels
        frame.setPreferredSize(new Dimension(400, 300));
        JLabel instructionlabel = new JLabel("Enter username and password.");
        JLabel labelName = new JLabel("User Name");
        JLabel labelpass = new JLabel("Password");

        JPanel LogonPanel = new JPanel(new GridLayout(4, 1));
        JPanel LogonPanel2 = new JPanel(new GridLayout(1, 2));
        JPanel LogonPanel3 = new JPanel(new GridLayout(1, 2));

        //create the text fields
        frame.getContentPane().add(LogonPanel);
        UserName = new JTextField();
        Password = new JTextField();
        Login = new JButton("Login");

        //add them to the frame
        LogonPanel.add(instructionlabel);
        LogonPanel.add(LogonPanel2);
        LogonPanel2.add(labelName);
        LogonPanel2.add(UserName);
        LogonPanel.add(LogonPanel3);
        LogonPanel3.add(labelpass);
        LogonPanel3.add(Password);
        LogonPanel.add(Login);

        // add a listener for the login button
        Login.addActionListener(new Client.BListener());

        //pack the frame
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    /**
     * Displays the user login, this is the first screen the user will from here they can loging to
     * the system
     *
     * @param userName for login will be checked against the users in the database
     * @param password for login this will be checked against the database entry of the user name
     */
    public static void Clientrequest(String userName, String password) throws IOException, ClassNotFoundException, SQLException {

        // attempt to connect to the local server on port 12345
        Socket socket = new Socket("127.0.0.1", 12345);
        ServerSocket serverSocket = new ServerSocket();

        // send the username and password over the connection to the server
        //NOTE PASSWORED TO BE HASHED
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(userName);
        objectOutputStream.writeObject(password);

        // wait for a reply from the server
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        int login = (int) objectInputStream.readObject();
        // if login exepted then launch main GUI if login failed let them try again
        if (login == 1){
            JOptionPane.showMessageDialog(null, "Username or password correct, logging in");
            frame.setVisible(false);
            frame.dispose();

            ClientGUI ClientShop = new ClientGUI();
            ClientShop.showClientGUI(userName);


        }else{
            JOptionPane.showMessageDialog(null, "Username or password incorrect");
            Password.setText("");
        }

        objectOutputStream.close();
        socket.close();

    }

    /**
     * BListener implements ActionListener and will attept to login when user name
     * and password are entered.
     */
    private static class BListener implements ActionListener {


        public void actionPerformed(ActionEvent e) {

            JButton source = (JButton) e.getSource();
                if (source == Login) {
                    try {
                        newUser();
                    } catch (SQLException | IOException | ClassNotFoundException throwables) {
                        throwables.printStackTrace();
                    }
                }

        }

        private void newUser() throws SQLException, IOException, ClassNotFoundException {

            String unam = null;
            unam = UserName.getText();
            String pass = Password.getText();

            if ((unam.length() > 3)&&(pass.length() > 3)){
                System.out.println("User name: " + unam + " and password is " + pass);
                Clientrequest(unam, pass);
            }else{
                System.out.println("somthjing not entered User name: " + unam + " and password is " + pass);
                JOptionPane.showMessageDialog(null, "User name or password not entered");
            }

        }

    }

}