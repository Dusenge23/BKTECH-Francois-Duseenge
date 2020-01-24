/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import dao.generic.AbstractDao;
import dao.intfc.IClient;
import domain.Client;
import domain.Client;
import java.util.List;

/**
 *
 * @author 2eM yadah
 */
public class ClientImpl extends AbstractDao<Long, Client> implements IClient{
     @Override
    public Client create(Client t) {
        try {
            return saveIntable(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Client> getAll() {
        try {
            return (List<Client>) (Object) getModelList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Client deleteInfo(Client t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Client updateInfo(Client t) {
        try {
            return updateIntable(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
