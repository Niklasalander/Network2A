/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package RMI2A;

/**
 *
 * @author fno
 */
public class User {
     private int id;
     private String nickname = "";
     private ClientParticipant cp;
     
     
	public User(int id) {
		this.id = id;
	}
        public User(int id,  ClientParticipant cp) {
                this.cp = cp;
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
        public String IdOrNickName(){
        if(this.getNickname()== ""){
            return Integer.toString(this.getId());
        }
        return this.getNickname();
    }
        
}
