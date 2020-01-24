/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import dao.generic.AbstractDao;
import dao.intfc.IUser;
import domain.User;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserImpl extends AbstractDao<Long, User> implements IUser {

    @Override
    public User create(User t) {
        return saveIntable(t);
    }

    @Override
    public List<User> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User deleteInfo(User t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User updateInfo(User t) {
        try {
            return updateIntable(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isExist(User user){
        try {
            
            return (this.getModelWithMyHQL(new String[]{"username"},new Object[]{user.getUsername()}," from User"))==null;
        } catch (Exception ex) {
            Logger.getLogger(UserImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
