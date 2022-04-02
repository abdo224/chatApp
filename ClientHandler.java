import java.io.*;
import java.net.Socket;
import java.util.ArrayList;



public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket ;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER "+clientUsername + " has entred the chat");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }
    public void broadcastMessage(String messageTosend){
        for (ClientHandler clientHandler:clientHandlers){
          try {
              if (!clientHandler.clientUsername.equals(clientUsername)) {
                  clientHandler.bufferedWriter.write(messageTosend);
                  clientHandler.bufferedWriter.newLine();
                  clientHandler.bufferedWriter.flush();
              }
          }catch(IOException e){
              e.printStackTrace();
          }
        }
    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER : "+clientUsername+" has left the chat!");
    }
}
