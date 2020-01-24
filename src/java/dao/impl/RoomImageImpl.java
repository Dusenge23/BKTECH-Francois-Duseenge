/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.impl;

import dao.generic.AbstractDao;
import dao.intfc.IRoomImages;
import domain.RoomImage;
import java.util.List;

/**
 *
 * @author 2eM yadah
 */
public class RoomImageImpl extends AbstractDao<Long, RoomImage> implements IRoomImages {
     @Override
    public RoomImage create(RoomImage t) {
        try {
            return saveIntable(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RoomImage> getAll() {
        try {
            return (List<RoomImage>) (Object) getModelList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RoomImage deleteInfo(RoomImage t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RoomImage updateInfo(RoomImage t) {
        try {
            return updateIntable(t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
