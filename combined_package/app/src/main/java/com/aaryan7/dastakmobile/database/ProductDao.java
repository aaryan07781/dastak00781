package com.aaryan7.dastakmobile.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    long insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE id = :id")
    Product getProductById(int id);

    @Query("SELECT * FROM products WHERE name LIKE '%' || :searchQuery || '%'")
    List<Product> searchProducts(String searchQuery);
}
