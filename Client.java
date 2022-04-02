import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket ;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username ;
    public Client(Socket socket,String username){
       try {
           this.socket = socket;
           this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           this.username = username;

       }catch(IOException e){
           e.printStackTrace();
       }
    }
    public void sendMessage(){
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();;
            Scanner sc = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = sc.nextLine();
                bufferedWriter.write(username + " : "+messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat ;
                while(socket.isConnected()){
                    try{
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Scanner sc  = new Scanner(System.in);
        System.out.println("Enter your username name to connect to the chat ! : ");
        String username = sc.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket,username);
        client.listenForMessage();
        client.sendMessage();
    }
}