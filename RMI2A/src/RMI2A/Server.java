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
   // private Map<Integer,ClientParticipant> participantList;
     private Map<User,ClientParticipant> participantList;
   // private ArrayList<User> participants;
    
    private int idProvider;
    public Server(String[] args) throws RemoteException {
        super();
      //  participants = new ArrayList();
        idProvider=1;
    //   participantList = new HashMap<Integer,ClientParticipant>();
         participantList = new HashMap<User,ClientParticipant>();
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
        
        /*for (Map.Entry<Integer,ClientParticipant> entry : participantList.entrySet()) {
		    System.out.println("entry " +entry.getKey() );
                    entry.getValue().pushMessage(message);
         }*/
        for (Map.Entry<User,ClientParticipant> entry : participantList.entrySet()) {
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
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void commandHelp() throws RemoteException {
        String help ="COMMANDS\n[1]'/who': display list of connected users\n"
                + "[2]'/nick': give yourself a nickname or change the existing one\n"
                + "[3]/'quit': quit chat program\n"
                + "[4]'/help: provides list of commands";
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public synchronized void register(ClientParticipant cp) throws RemoteException {
       
       // participantList.put(this.idProvider, cp);
       participantList.put(new User(this.idProvider), cp);
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
        
        /*if(checkForExistingUser(thisClientID)){
             for (Map.Entry<Integer,ClientParticipant> entry : participantList.entrySet()) {
		    //System.out.println("entry " +entry.getKey() );
                    entry.getValue().pushMessage("From " +thisClientID +" > "+message);
         }
        }*/
        User u = getUser(thisClientID);
        if(checkForExistingUser(thisClientID)){
             for (Map.Entry<User,ClientParticipant> entry : participantList.entrySet()) {
		    //System.out.println("entry " +entry.getKey() );
                    entry.getValue().pushMessage("From " + u.IdOrNickName() +" > "+message);
         }
        }
        //To change body of generated methods, choose Tools | Templates.
       
    }
    
    
    @Override
    public boolean checkForExistingUser(int id) {
         //To change body of generated methods, choose Tools | Templates.
         /* for (Map.Entry<Integer,ClientParticipant> entry : participantList.entrySet()) {
		if(entry.getKey()==id){
                    return true;
                }
                    
         }*/
         for (Map.Entry<User,ClientParticipant> entry : participantList.entrySet()) {
		if(entry.getKey().getId()==id){
                    return true;
                }
                    
         }
         return false;
    }

    @Override
    public void commandHelp(int thisClientID) throws RemoteException {
        String help ="COMMANDS\n[1]'/who': display list of connected users\n"
                + "[2]'/nick': give yourself a nickname or change the existing one\n"
                + "[3]'quit': quit chat program\n"
                + "[4]'help: provides list of commands";//To change body of generated methods, choose Tools | Templates.
        ClientParticipant c;
        if((c = selectUser(thisClientID))!=null){
            c.pushMessage(help);
        }
        
        
    }

    @Override
    public ClientParticipant selectUser(int id) throws RemoteException {
         /*for (Map.Entry<Integer,ClientParticipant> entry : participantList.entrySet()) {
		if(entry.getKey()==id){
                    return entry.getValue();
                }
         }*/
         for (Map.Entry<User,ClientParticipant> entry : participantList.entrySet()) {
		if(entry.getKey().getId()==id){
                    return entry.getValue();
                }
         }
        return null;
        
    }
    private User getUser(int id){
        for (Map.Entry<User,ClientParticipant> entry : participantList.entrySet()) {
		if(entry.getKey().getId()==id){
                    return entry.getKey();
                }
         }
        return null;
    }
    private boolean isNameAvailable(String name){
        for (Map.Entry<User,ClientParticipant> entry : participantList.entrySet()) {
              String tmp = entry.getKey().getNickname();
              if(tmp!=null){
                  if(tmp.equals(name)){
                      return false;
                  }
              }
		
         }
        return true;
    }
    @Override
    public void commandChangeName(int clientId, String name) throws RemoteException {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        User u;
        if((u = getUser(clientId))!=null && isNameAvailable(name)){
            u.setNickname(name);
            ClientParticipant cp = selectUser(clientId);
            cp.obtainNickname(name);
        }
    }
        
    
}
