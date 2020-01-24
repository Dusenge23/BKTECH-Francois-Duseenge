/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import dao.generic.AbstractDao;
import dao.intfc.IloginUsers;


import domain.User;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author RTAP4
 */
public class LoginImpl extends AbstractDao<Long, User> implements IloginUsers {

//    private static final transient Log LOGGER = LogFactory.getLog(LoginImpl.class);
    @Override
    public boolean checkUserNameAndPasswod(String userName, String Password) {
        Object log = null;
        log = getLongIn(userName, Password);
        if (log == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public User userDetail(String userName) {
        try {
            return (User) super.getLongInUserDeails(userName);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String criptPassword(String password) throws NoSuchAlgorithmException {

        String pas = password;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(pas.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return (sb.toString());
    }

    @Override
    public String getIpAddress() throws Exception {

        String ip = null;
        try {
            InetAddress Ip = InetAddress.getLocalHost();
            ip = Ip.toString();
        } catch (Exception e) {
//            LOGGER.info(e.getMessage());
        }
        return ip;
    } //To change body of generated methods, choose Tools | Templates.
}
