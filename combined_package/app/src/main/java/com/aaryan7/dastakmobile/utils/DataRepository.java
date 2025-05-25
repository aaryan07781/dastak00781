package com.aaryan7.dastakmobile.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.aaryan7.dastakmobile.database.AppDatabase;
import com.aaryan7.dastakmobile.database.Bill;
import com.aaryan7.dastakmobile.database.BillDao;
import com.aaryan7.dastakmobile.database.BillItem;
import com.aaryan7.dastakmobile.database.BillItemDao;
import com.aaryan7.dastakmobile.database.Product;
import com.aaryan7.dastakmobile.database.ProductDao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository class to handle all database operations
 * This class ensures all database operations are performed off the main thread
 * and provides a clean API for the rest of the app to interact with the database
 */
public class DataRepository {
    private static DataRepository instance;
    private final AppDatabase database;
    private final ExecutorService executor;
    
    private DataRepository(Context context) {
        database = AppDatabase.getInstance(context);
        executor = Executors.newFixedThreadPool(4);
    }
    
    public static synchronized DataRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DataRepository(context);
        }
        return instance;
    }
    
    // Product operations
    public void insertProduct(Product product, DataOperationCallback<Long> callback) {
        executor.execute(() -> {
            long id = database.productDao().insert(product);
            if (callback != null) {
                callback.onComplete(id);
            }
        });
    }
    
    public void updateProduct(Product product, DataOperationCallback<Void> callback) {
        executor.execute(() -> {
            database.productDao().update(product);
            if (callback != null) {
                callback.onComplete(null);
            }
        });
    }
    
    public void deleteProduct(Product product, DataOperationCallback<Void> callback) {
        executor.execute(() -> {
            database.productDao().delete(product);
            if (callback != null) {
                callback.onComplete(null);
            }
        });
    }
    
    public void getAllProducts(DataOperationCallback<List<Product>> callback) {
        executor.execute(() -> {
            List<Product> products = database.productDao().getAllProducts();
            if (callback != null) {
                callback.onComplete(products);
            }
        });
    }
    
    public void getProductById(int id, DataOperationCallback<Product> callback) {
        executor.execute(() -> {
            Product product = database.productDao().getProductById(id);
            if (callback != null) {
                callback.onComplete(product);
            }
        });
    }
    
    public void searchProducts(String query, DataOperationCallback<List<Product>> callback) {
        executor.execute(() -> {
            List<Product> products = database.productDao().searchProducts(query);
            if (callback != null) {
                callback.onComplete(products);
            }
        });
    }
    
    // Bill operations
    public void insertBill(Bill bill, List<BillItem> items, DataOperationCallback<Long> callback) {
        executor.execute(() -> {
            // Insert bill first to get the bill ID
            long billId = database.billDao().insert(bill);
            
            // Update bill items with the new bill ID
            for (BillItem item : items) {
                item.setBillId((int) billId);
                database.billItemDao().insert(item);
            }
            
            if (callback != null) {
                callback.onComplete(billId);
            }
        });
    }
    
    public void deleteBill(Bill bill, DataOperationCallback<Void> callback) {
        executor.execute(() -> {
            // Delete bill items first (cascade will handle this automatically)
            database.billDao().delete(bill);
            
            if (callback != null) {
                callback.onComplete(null);
            }
        });
    }
    
    public void getAllBills(DataOperationCallback<List<Bill>> callback) {
        executor.execute(() -> {
            List<Bill> bills = database.billDao().getAllBills();
            if (callback != null) {
                callback.onComplete(bills);
            }
        });
    }
    
    public void getBillWithItems(int billId, DataOperationCallback<BillWithItems> callback) {
        executor.execute(() -> {
            Bill bill = database.billDao().getBillById(billId);
            List<BillItem> items = database.billItemDao().getBillItemsByBillId(billId);
            
            BillWithItems billWithItems = new BillWithItems(bill, items);
            
            if (callback != null) {
                callback.onComplete(billWithItems);
            }
        });
    }
    
    // Analysis operations
    public void getDailySales(Date date, DataOperationCallback<Double> callback) {
        executor.execute(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date startDate = calendar.getTime();
            
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDate = calendar.getTime();
            
            double sales = database.billDao().getTotalSalesByDateRange(startDate, endDate);
            
            if (callback != null) {
                callback.onComplete(sales);
            }
        });
    }
    
    public void getWeeklySales(Date date, DataOperationCallback<Double> callback) {
        executor.execute(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date startDate = calendar.getTime();
            
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDate = calendar.getTime();
            
            double sales = database.billDao().getTotalSalesByDateRange(startDate, endDate);
            
            if (callback != null) {
                callback.onComplete(sales);
            }
        });
    }
    
    public void getMonthlySales(Date date, DataOperationCallback<Double> callback) {
        executor.execute(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date startDate = calendar.getTime();
            
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDate = calendar.getTime();
            
            double sales = database.billDao().getTotalSalesByDateRange(startDate, endDate);
            
            if (callback != null) {
                callback.onComplete(sales);
            }
        });
    }
    
    public void getYearlySales(int year, DataOperationCallback<Double> callback) {
        executor.execute(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date startDate = calendar.getTime();
            
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDate = calendar.getTime();
            
            double sales = database.billDao().getTotalSalesByDateRange(startDate, endDate);
            
            if (callback != null) {
                callback.onComplete(sales);
            }
        });
    }
    
    // Profit analysis
    public void getDailyProfit(Date date, DataOperationCallback<Double> callback) {
        executor.execute(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date startDate = calendar.getTime();
            
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDate = calendar.getTime();
            
            double profit = database.billDao().getTotalProfitByDateRange(startDate, endDate);
            
            if (callback != null) {
                callback.onComplete(profit);
            }
        });
    }
    
    public void getWeeklyProfit(Date date, DataOperationCallback<Double> callback) {
        executor.execute(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date startDate = calendar.getTime();
            
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDate = calendar.getTime();
            
            double profit = database.billDao().getTotalProfitByDateRange(startDate, endDate);
            
            if (callback != null) {
                callback.onComplete(profit);
            }
        });
    }
    
    public void getMonthlyProfit(Date date, DataOperationCallback<Double> callback) {
        executor.execute(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date startDate = calendar.getTime();
            
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDate = calendar.getTime();
            
            double profit = database.billDao().getTotalProfitByDateRange(startDate, endDate);
            
            if (callback != null) {
                callback.onComplete(profit);
            }
        });
    }
    
    public void getYearlyProfit(int year, DataOperationCallback<Double> callback) {
        executor.execute(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date startDate = calendar.getTime();
            
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            Date endDate = calendar.getTime();
            
            double profit = database.billDao().getTotalProfitByDateRange(startDate, endDate);
            
            if (callback != null) {
                callback.onComplete(profit);
            }
        });
    }
    
    // Helper class for bill with items
    public static class BillWithItems {
        private Bill bill;
        private List<BillItem> items;
        
        public BillWithItems(Bill bill, List<BillItem> items) {
            this.bill = bill;
            this.items = items;
        }
        
        public Bill getBill() {
            return bill;
        }
        
        public List<BillItem> getItems() {
            return items;
        }
    }
    
    // Callback interface for database operations
    public interface DataOperationCallback<T> {
        void onComplete(T result);
    }
}
