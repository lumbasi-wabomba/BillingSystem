package com.system.billingsystem.service;

import com.system.billingsystem.dao.SalesItemsDao;
import com.system.billingsystem.models.SalesItems;

import java.sql.SQLException;
import java.util.List;

public class SalesItemsService {
    private  final SalesItemsDao salesItemsDao;

    public SalesItemsService(SalesItemsDao salesItemsDao) {
        this.salesItemsDao = salesItemsDao;
    }

    // Service methods for sales item operations
    public SalesItems getSoldItem(SalesItems soldItem) throws SQLException{
        try{
            return salesItemsDao.get(soldItem);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public List<SalesItems> getAllSoldItems() throws  SQLException{
        try{
            return salesItemsDao.getAll();
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public SalesItems saveSoldItem(SalesItems soldItem) throws  SQLException{
        try{
            return  salesItemsDao.save(soldItem);
        } catch (Exception e) {
            throw new SQLException("Error"+ e.getMessage(),e);
        }
    }
    public SalesItems updateSoldItem(SalesItems soldItem, String[] details) throws  SQLException{
        try{
            return  salesItemsDao.update(soldItem, details);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
    public  SalesItems deleteSoldItem(String id) throws  SQLException{
        try{
            return salesItemsDao.delete(id);
        } catch (Exception e) {
            throw new SQLException("Error: "+ e.getMessage(), e);
        }
    }
}
