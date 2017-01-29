// ClientThread Class'ı. 
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
 
/*
 * Client soketler için oluşturulmuş Thread sınıfı
 * Her bir Client ile Thread eşleştirilmesi ile Multithread uygulama
 */
public class ClientThread extends Thread {
 
    private DataInputStream dis = null; // Okuyucu
    private PrintStream ps = null; // Yazıcı
    private Socket clientSocket = null; // bağlantı noktası
    private final ClientThread[] threads;  // diğer kullanıcıların havuzu
    private final int maxClientSayisi;
 
    public ClientThread(Socket clientSocket, ClientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxClientSayisi = threads.length;
    }
 
    @Override
    public void run() {
        int maxClientSayisi = this.maxClientSayisi;
        ClientThread[] threads = this.threads;
 
        try {
            dis = new DataInputStream(clientSocket.getInputStream()); /// okuyucu
            ps = new PrintStream(clientSocket.getOutputStream()); // yazıcı
            ps.println("Nickname: ");
            String name;
            name = dis.readLine().trim();
            ps.println("Merhaba " + name + "! Mesajlasma uygulamasina hosgeldiniz. Uygulamadan cikmak icin -quit- yazip enterlayin.");
             
            for (int i = 0; i < maxClientSayisi; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].ps.println(name + " adli kisi odaya baglandi.");
                }
            }
            while (true) {
                String satir = dis.readLine();
                if (satir.startsWith("/quit")) {
                    break;
                }
                for (int i = 0; i < maxClientSayisi; i++) {
                    if (threads[i] != null) {
                        threads[i].ps.println("<" + name + ">: " + satir);
                    }
                }
            }
            for (int i = 0; i < maxClientSayisi; i++) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].ps.println(name + " adlı kisi odadan ayrildi.");
                }
            }
            ps.println(name + " Gule Gule!");
 
            /*
             * Yeni bir Clientın bağlanabilmesi için aktif olan Client null yapılır
             */
            for (int i = 0; i < maxClientSayisi; i++) {
                if (threads[i] == this) {
                    threads[i] = null;
                }
            }
            dis.close();
            ps.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}
