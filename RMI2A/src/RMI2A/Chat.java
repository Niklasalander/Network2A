//package RMI2A;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Niklas
 */
public interface Chat extends Remote {
    public void doBroadcast(String message) throws RemoteException;
    public void commandWho() throws RemoteException;
    public void commandChangeName(String name) throws RemoteException;
    public void commandHelp() throws RemoteException;
    public void regiseter(ClientParticipant cp) throws RemoteException;
    public void deRegister(ClientParticipant cp) throws RemoteException;
    public void generateID()throws RemoteException;
}
