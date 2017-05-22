/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.abhirockzz.kafEEne.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.websocket.Session;

/**
 *
 * @author agupgupt
 */
@Singleton
public class Peers {
    private List<Session> peers;
    
    @PostConstruct
    public void init(){
        peers = new ArrayList<>();
    }
    
    public void add(Session peer){
        peers.add(peer);
        System.out.println("Added peer "+ peer.getId() + " to list of peers");
    }
    
    public void remove(Session peer){
        peers.remove(peer);
        System.out.println("Removed peer "+ peer.getId() + " from list of peers");
    }
    
    @Lock(LockType.READ)
    public List<Session> peers(){
        System.out.println("Getting connected peers..");
        return Collections.unmodifiableList(peers);
    }
    
    @PreDestroy
    public void closeAll(){
        
        peers.stream()
        .filter(s -> s.isOpen())
        .forEach(s -> {
            try {
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(Peers.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        System.out.println("Closed all peer connections");
    }
}
