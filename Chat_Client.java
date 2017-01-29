// ChatClient class'ı.
 
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
 
public class ChatClient implements Runnable {
 
    private static Socket clientSocket = null; /// bağlantı noktası
    private static PrintStream socketWriter = null; // soket yazıcı
    private static DataInputStream socketReader = null; /// soket okuyucu
    private static Scanner input = new Scanner(System.in); /// konsol okuyucu
    private static boolean isClosed = false;
 
    public static void main(String[] args) {
         
        String host = "localhost"; // adress
        int portNo = 3333;  /// bağlantı portu
         
        /*
         * Girilen host ve port numaraları ile Client açılması
         */
        try {
            clientSocket = new Socket(host, portNo);
            socketWriter = new PrintStream(clientSocket.getOutputStream());
            socketReader = new DataInputStream(clientSocket.getInputStream());
         
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Bağlantı sağlanamadı");
        }
 
        /*
         * Bağlantının başarılı şekilde gerçekleşmesi halinde mesaj yazma
         */
        if (clientSocket != null && socketWriter != null && socketReader != null) {
            try {
                /* Serverdan okuma için Thread */
                new Thread(new ChatClient()).start();
                while (!isClosed) {
                    socketWriter.println(input.nextLine().trim());
                }
 
                socketWriter.close();
                socketReader.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException:  " + e);
            }
        }
    }
 
    @Override
    public void run() {
        String cevap;
        try {
            while ((cevap = socketReader.readLine()) != null) {
                System.out.println(cevap);
                if (cevap.indexOf(" Gule Gule!") != -1) {
                    break;
                }
            }
            isClosed = true;
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
