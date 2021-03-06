import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
 
public class ChatServer {
 
    // Server soket
    private static ServerSocket serverSocket = null;
    // Client soket
    private static Socket clientSocket = null;
    // Maximum private static final int maxClientSayisi = 10;bağlantı sayısı
    private static final int maxClientSayisi = 10;
    // Her bir client için oluşturlacak bağlantı demeti
    private static final ClientThread[] threads = new ClientThread[maxClientSayisi];
 
    public static void main(String args[]) {
 
        int portNo = 3333;
        try {
            serverSocket = new ServerSocket(portNo);
        } catch (IOException e) {
            System.out.println(e);
        }
        /*
         * Her bir client için ayrı soketler ve threadlerin oluşturulması
         */
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int i = 0;
                for (i = 0; i < maxClientSayisi; i++) {
                    if (threads[i] == null) {
                        (threads[i] = new ClientThread(clientSocket, threads)).start();
                        break;
                    }
                }
                if (i == maxClientSayisi) {
                    PrintStream ps = new PrintStream(clientSocket.getOutputStream());
                    ps.println("Server maksimum kapasiteye ulasti.");
                    ps.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
