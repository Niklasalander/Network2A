/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package RMI2A;

import java.rmi.RemoteException;
import java.util.TimerTask;

/**
 *
 * @author fno
 */
public class ServerResponseTimeout extends TimerTask {
    
    private Chat chat = null;
    private int thisID;
    private Client client;
    private boolean connected;
    public ServerResponseTimeout(Chat c, int clientId, Client thisClient){
        this.thisID = clientId;
        this.chat = c;
        this.client = thisClient;
      
    }
    @Override
    public void run() {
        try {
            chat.doTimeOut(thisID);
        } catch (RemoteException ex) {
            System.out.println("We lost connection with the server, server likely has crashed... ");
            System.out.println("Press Enter to exit");
            this.cancel();
        }
    }

    @Override
    public boolean cancel() {
        this.client.disConnected();
        return super.cancel(); //To change body of generated methods, choose Tools | Templates.
    }
    
}