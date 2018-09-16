//package RMI2A;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Niklas
 */
public class Server extends UnicastRemoteObject implements Chat {
    //private ArrayList<ClientParticipant> participants;
    private Map<Integer,ClientParticipant> participantList;
   // private ArrayList<User> participants;
    
    private int idProvider;
    public Server(String[] args) throws RemoteException {
        super();
      //  participants = new ArrayList();
        idProvider=1;
       participantList = new HashMap<Integer,ClientParticipant>();
    }
    
    public static void main(String[] args) {
        try {
            Server server = new Server(args);
            Naming.rebind("chat", server);
            System.out.println("Server running...");
        }  catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
    
    
    @Override
    public synchronized void doBroadcast(String message) throws RemoteException {
       /* for (ClientParticipant cp : participants) {
           // System.out.println("Here " + cp.)
            cp.pushMessage(message);
        }*/
        
        for (Map.Entry<Integer,ClientParticipant> entry : participantList.entrySet()) {
		    System.out.println("entry " +entry.getKey() );
                    entry.getValue().pushMessage(message);
         }
    }

    @Override
    public void commandWho() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void commandChangeName(String name) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void commandHelp() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void register(ClientParticipant cp) throws RemoteException {
      
        participantList.put(this.idProvider, cp);
        cp.registerID(idProvider);
        idProvider++;
       /* cp.registerID(idProvider);
        participants.add(cp);
        this.idProvider++;*/
    }

    @Override
    public synchronized void deRegister(ClientParticipant cp) throws RemoteException {
        participantList.remove(cp);
       // participants.remove(cp);
    }

    @Override
    public void generateID() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doBroadcast(int thisClientID, String message) throws RemoteException {
        
        if(checkForExistingUser(thisClientID)){
             for (Map.Entry<Integer,ClientParticipant> entry : participantList.entrySet()) {
		    //System.out.println("entry " +entry.getKey() );
                    entry.getValue().pushMessage("From " +thisClientID +" > "+message);
         }
        }
        //To change body of generated methods, choose Tools | Templates.
       
    }

    @Override
    public boolean checkForExistingUser(int id) {
         //To change body of generated methods, choose Tools | Templates.
          for (Map.Entry<Integer,ClientParticipant> entry : participantList.entrySet()) {
		if(entry.getKey()==id){
                    return true;
                }
                    
         }
         return false;
    }
    
}
