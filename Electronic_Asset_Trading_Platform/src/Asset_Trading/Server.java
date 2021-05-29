package Asset_Trading;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

public class Server {

    private static Connection connection;

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        ServerSocket serverSocket = new ServerSocket(12345,4);
        for(;;){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            String name = (String) objectInputStream.readObject();
            String pass = (String) objectInputStream.readObject();

            System.out.println("got this name from the client: " + name);
            System.out.println("got this password from the client: " + pass);

            connection = DBConnect.getInstance();

            int grantLogin = DBConnect.UserLogin(connection, name, pass);

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(grantLogin);

            objectInputStream.close();
            socket.close();
        }
    }
}
