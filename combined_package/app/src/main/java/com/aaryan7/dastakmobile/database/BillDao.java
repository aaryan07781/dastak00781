package com.aaryan7.dastakmobile.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface BillDao {
    @Insert
    long insert(Bill bill);

    @Update
    void update(Bill bill);

    @Delete
    void delete(Bill bill);

    @Query("SELECT * FROM bills ORDER BY date DESC")
    List<Bill> getAllBills();

    @Query("SELECT * FROM bills WHERE id = :id")
    Bill getBillById(int id);
    
    @Query("SELECT * FROM bills WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    List<Bill> getBillsByDateRange(Date startDate, Date endDate);
    
    @Query("SELECT SUM(totalAmount) FROM bills WHERE date BETWEEN :startDate AND :endDate")
    double getTotalSalesByDateRange(Date startDate, Date endDate);
    
    @Query("SELECT SUM(totalProfit) FROM bills WHERE date BETWEEN :startDate AND :endDate")
    double getTotalProfitByDateRange(Date startDate, Date endDate);
}
