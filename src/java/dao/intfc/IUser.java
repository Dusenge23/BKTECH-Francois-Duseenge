/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.intfc;

import domain.User;

/**
 *
 * @author Godwin
 */
public interface  IUser extends  Allrelated<User> {
    boolean isExist(User user);
}
