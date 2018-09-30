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
    public void register(ClientParticipant cp) throws RemoteException;
    public void deRegister(int thisClientID) throws RemoteException;

    public void doBroadcast(int thisClientID, String inputText) throws RemoteException;

    public void commandHelp(int thisClientID) throws RemoteException;
    public void commandChangeName(int thisClientID, String name) throws RemoteException;
    public void commandWho(int thisClientID) throws RemoteException;
}
