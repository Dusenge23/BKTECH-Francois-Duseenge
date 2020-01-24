/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.generic;

import domain.Rooms;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Edmond
 */
public class RoomDao {
    public List<Rooms> getAvailableRoom(){
    Session s=SessionManager.getSession();
    Query qry=s.createQuery("select a from Rooms a where a.status='available'"); 
    List<Rooms> list=qry.list();
    s.close();
    return list;
    }
}
