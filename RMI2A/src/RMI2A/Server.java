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
    private Map<User, ClientParticipant> participantList;
    // private ArrayList<User> participants;

    private int idProvider;

    public Server(String[] args) throws RemoteException {
        super();
        //  participants = new ArrayList();
        idProvider = 1;
        //   participantList = new HashMap<Integer,ClientParticipant>();
        participantList = new HashMap<User, ClientParticipant>();
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(args);
            Naming.rebind("chat", server);
            System.out.println("Server running...");
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
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
    }

    @Override
    public void doBroadcast(int thisClientID, String message) throws RemoteException {
        if (checkForExistingUser(thisClientID)) {
            User u = getUser(thisClientID);
            for (Map.Entry<User, ClientParticipant> entry : participantList.entrySet()) {
                //System.out.println("entry " +entry.getKey() );
                if (entry.getKey().getId() != thisClientID) {
                    entry.getValue().pushMessage("From " + u.IdOrNickName() + " > " + message);
                }
            }
        }
    }

    @Override
    public void commandHelp(int thisClientID) throws RemoteException {
        String help = "COMMANDS\n[1]'/who': display list of connected users\n"
                + "[2]'/nick': give yourself a nickname or change the existing one\n"
                + "[3]'quit': quit chat program\n"
                + "[4]'help: provides list of commands";//To change body of generated methods, choose Tools | Templates.
        ClientParticipant c;
        if ((c = selectUser(thisClientID)) != null) {
            c.pushMessage(help);
        }
    }

    @Override
    public void commandChangeName(int clientId, String name) throws RemoteException {
        User u;
        if ((u = getUser(clientId)) != null) {
            ClientParticipant cp = selectUser(clientId);
            if (isNameAvailable(name)) {
                u.setNickname(name);
                cp.obtainNickname(name);
            }
            else {
                cp.pushMessage("Name is occupied");
            }
        } 
    }
    
    @Override
    public void commandWho(int thisClientID) throws RemoteException {
        if (checkForExistingUser(thisClientID)) {
            StringBuilder builder = new StringBuilder();
            builder.append("Users: \n");
            ClientParticipant cp = null;
            for (Map.Entry<User, ClientParticipant> entry : participantList.entrySet()) {
                if (entry.getKey().getId() != thisClientID) {
                    builder.append("[User " + entry.getKey().getId() + "]");
                    builder.append(": " + entry.getKey().getNickname() + "\n");
                }
                else {
                    cp = entry.getValue();
                }
            }
            if (cp != null)
                cp.pushMessage(builder.toString());
        }
    }

    private boolean checkForExistingUser(int id) {
        for (Map.Entry<User, ClientParticipant> entry : participantList.entrySet()) {
            if (entry.getKey().getId() == id) {
                return true;
            }
        }
        return false;
    }

    public ClientParticipant selectUser(int id) throws RemoteException {
        for (Map.Entry<User, ClientParticipant> entry : participantList.entrySet()) {
            if (entry.getKey().getId() == id) {
                return entry.getValue();
            }
        }
        return null;
    }

    private User getUser(int id) {
        for (Map.Entry<User, ClientParticipant> entry : participantList.entrySet()) {
            if (entry.getKey().getId() == id) {
                return entry.getKey();
            }
        }
        return null;
    }

    private boolean isNameAvailable(String name) {
        for (Map.Entry<User, ClientParticipant> entry : participantList.entrySet()) {
            String tmp = entry.getKey().getNickname();
            if (tmp != null) {
                if (tmp.equals(name)) {
                    return false;
                }
            }

        }
        return true;
    }



}
