/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.intfc;


import domain.User;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author RTAP4
 */
public interface IloginUsers {

    boolean checkUserNameAndPasswod(String userName, String Password);

    User userDetail(String userName);

    String criptPassword(String password) throws NoSuchAlgorithmException;

    String getIpAddress() throws Exception;
}
