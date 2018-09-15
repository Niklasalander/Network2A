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

/**
 *
 * @author Niklas
 */
public class Server extends UnicastRemoteObject implements Chat {
    private ArrayList<ClientParticipant> participants;
    private HashMap<User,ClientParticipant> participantList;
    public Server(String[] args) throws RemoteException {
        super();
        participants = new ArrayList();
        
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
        for (ClientParticipant cp : participants) {
            cp.pushMessage(message);
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
    public synchronized void regiseter(ClientParticipant cp) throws RemoteException {
        participants.add(cp);
    }

    @Override
    public synchronized void deRegister(ClientParticipant cp) throws RemoteException {
        participants.remove(cp);
    }

    @Override
    public void generateID() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
