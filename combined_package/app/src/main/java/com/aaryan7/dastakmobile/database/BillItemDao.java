package com.aaryan7.dastakmobile.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BillItemDao {
    @Insert
    long insert(BillItem billItem);

    @Update
    void update(BillItem billItem);

    @Delete
    void delete(BillItem billItem);

    @Query("SELECT * FROM bill_items WHERE billId = :billId")
    List<BillItem> getBillItemsByBillId(int billId);
    
    @Query("SELECT * FROM bill_items WHERE id = :id")
    BillItem getBillItemById(int id);
    
    @Query("DELETE FROM bill_items WHERE billId = :billId")
    void deleteAllItemsForBill(int billId);
}
