//package RMI2A;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Timer;

/**
 *
 * @author Niklas
 */
public class Client extends UnicastRemoteObject implements ClientParticipant {
    private static final String QUIT = "/QUIT";
    private static final String HELP = "/HELP";
    private static final String NICK = "/NICK";
    private static final String WHO = "/WHO";

    private static boolean connected;
    private int thisClientID;
    private String nickname;
    private Timer responseTimer;
    public Client(String[] args) throws RemoteException {
        super();
        Chat chat = null;
        try {
            System.out.println("Hello");
            chat = (Chat) Naming.lookup("rmi://" + args[0] + "/chat");
            chat.register(this); //add to list generate id
            
            connected = true;
            responseTimer = new Timer();
           
            Scanner in = new Scanner(System.in);
            String inputText;
            responseTimer.scheduleAtFixedRate(new ServerResponseTimeout(chat,this.thisClientID,this), 3500, 8000);
            while (connected) {
                inputText = in.nextLine();
                if (inputText.length() != 0 && connected) {
                    if (inputText.charAt(0) == '/') {
                        String[] received = inputText.split(" ");
                        String command = received[0].trim().toUpperCase();
                        switch(command) {
                            case QUIT :
                                chat.deRegister(this.thisClientID);
                                connected = false;
                                break;
                            case HELP :
                                chat.commandHelp(this.thisClientID);
                                break;
                            case NICK : 
                                if (received.length <= 1) {
                                    System.out.println("error: you have inserted no argument");
                                } else {
                                    String argument = received[1].trim().toUpperCase();
                                    chat.commandChangeName(this.thisClientID, argument);
                                }
                                break;
                            case WHO : 
                                chat.commandWho(thisClientID);
                                break;
                            default :
                                System.out.println("No command recognized");
                        }
                    } else {
                        chat.doBroadcast(this.thisClientID, inputText);
                    }
                } 
            }
        
        } catch (NotBoundException ex) {
            System.out.println("Server is not running or you typed wrong name");
        }
        
        catch (MalformedURLException ex) {
            System.out.println("URL is not correct");
        } catch (RemoteException ex) {
            System.out.println("Server seems unresponsive you should probably /QUIT");
        } catch (NoSuchElementException ex) {
        } catch (Exception ex) {
            System.out.println("Something went wrong");
            ex.printStackTrace();
        } finally {
            if (chat != null) {
                try {
                    chat.deRegister(this.thisClientID);
                } catch (RemoteException ex) {
                }
            }
            if (responseTimer != null)
                responseTimer.cancel();
        }
    }
     
    public void disConnected(){
        this.connected = false;
    }
    @Override
    public void pushMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    public static void main(String[] args) {
        try {
            new Client(args);
            System.exit(0);
        } catch (RemoteException ex) {
            System.out.println("Main method caught Remote Exception");
            System.exit(1);
        }
    }

    @Override
    public void registerID(int id) throws RemoteException {
        System.out.println("ID : " + id + " registered");
        this.thisClientID = id;
    }

    @Override
    public void obtainNickname(String newNickname) throws RemoteException {
        this.nickname = newNickname;
        System.out.println("Obtained new name : " + this.nickname);
    }

    @Override
    public void isClientAlive() throws RemoteException {
        // Do nothing
    }
    
    private String IdorNickName() {
        if (this.nickname == null) {
            return Integer.toString(this.thisClientID);
        }
        return this.nickname;
    }
    
    private int getIdFromServer(Scanner in) {
        String tmp = in.nextLine();
        System.out.println("tmp " + tmp);
        tmp.replaceAll("\\D+", "");
        return Integer.parseInt(tmp);
    }
}
