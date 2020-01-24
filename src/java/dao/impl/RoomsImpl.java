/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import dao.generic.AbstractDao;
import dao.intfc.IRooms;
import domain.Rooms;
import java.util.List;

/**
 *
 * @author 2eM yadah
 */
public class RoomsImpl extends AbstractDao<Long, Rooms> implements IRooms{
     @Override
    public Rooms create(Rooms t) {
        try {
            return saveIntable(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Rooms> getAll() {
        try {
            return (List<Rooms>) (Object) getModelList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Rooms deleteInfo(Rooms t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Rooms updateInfo(Rooms t) {
        try {
            return updateIntable(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
