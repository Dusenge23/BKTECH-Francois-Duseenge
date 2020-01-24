/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.intfc;

import java.util.List;

/**
 *
 * @author Godwin
 */
public interface Allrelated<T> {

    public T create(T t);
    public List<T> getAll();
    public T deleteInfo(T t);
    public T updateInfo(T t);
}
