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
public interface ClientParticipant extends Remote {
    public void pushMessage(String message) throws RemoteException;
    public void registerID(int id)throws RemoteException;
    public void obtainNickname(String newNickname)throws RemoteException;
    public void isClientAlive() throws RemoteException;
  
}
