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
import java.util.Scanner;

/**
 *
 * @author Niklas
 */
public class Client extends UnicastRemoteObject implements ClientParticipant {
    private boolean connected;
    private int thisClientID;
    public Client(String[] args) throws RemoteException {
        super();
        Chat message = null;
        try {
            System.out.println("hello");
            message = (Chat) Naming.lookup("rmi://" + args[0] + "/chat");
            message.register(this); //add to list generate id
            
            connected = true;
            Scanner in = new Scanner(System.in);
            String inputText;
            
            while(connected) {
                inputText = in.nextLine();
                if (inputText.charAt(0) == '/') {
                    if (inputText.equals("/QUIT")) {
                        message.deRegister(this);
                        connected = false;
                        break;
                    }
                    // send command
                }
                else {
                    if(inputText==null){
                        System.out.println("is null");
                    }
                    message.doBroadcast(this.thisClientID,inputText);
                }
            }
            
            
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (message != null)
                message.deRegister(this);
            System.exit(0);
        }
    }
    
    
    @Override
    public void pushMessage(String message) throws RemoteException{
      //  System.out.println("FROM " + this.thisClientID + " > " + message);
        System.out.println(message);
    }
    
    public static void main(String[] args) {
        try {
            new Client(args);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    private int getIdFromServer(Scanner in) {
       String tmp = in.nextLine();
       System.out.println("tmp " +tmp);
       tmp.replaceAll("\\D+","");
       return Integer.parseInt(tmp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int registerID(int id) throws RemoteException {
        System.out.println("ID : " + id + " registered");
        this.thisClientID = id;
        return id;
        
        //To change body of generated methods, choose Tools | Templates.
    }
    
}
