package Asset_Trading;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;



public class Server {

    private static Connection connection;

    /**
     * The server will listen for a connection on port 12345 and once it receves one
     * it will wait for an objectInputStream once it gets it one it will check the
     * recived username against the database and if there is a match it will check
     * that the password matches the one in the database. It will then use a
     * outputStream to send an int back to the client indicating whether or not the
     * login was successfully.
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        ServerSocket serverSocket = new ServerSocket(12345,4);
        for(;;){

            //accept the connection
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            //recevie the username and password from client
            String name = (String) objectInputStream.readObject();
            String pass = (String) objectInputStream.readObject();


            //get connection to database
            connection = DBConnect.getInstance();

            //check for a match of the user credentials
            int grantLogin = DBConnect.UserLogin(connection, name, pass);

            //send back loggin ether aproved or failed
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(grantLogin);

            objectInputStream.close();
            socket.close();
        }
    }
}
