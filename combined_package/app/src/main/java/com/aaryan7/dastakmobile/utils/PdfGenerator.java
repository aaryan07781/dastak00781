package com.aaryan7.dastakmobile.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import com.aaryan7.dastakmobile.R;
import com.aaryan7.dastakmobile.database.Bill;
import com.aaryan7.dastakmobile.database.BillItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PdfGenerator {
    private static final int PAGE_WIDTH = 595; // A4 width in points
    private static final int PAGE_HEIGHT = 842; // A4 height in points
    private static final int MARGIN = 50;
    
    private Context context;
    
    public PdfGenerator(Context context) {
        this.context = context;
    }
    
    public File generateBillPdf(Bill bill, List<BillItem> items) throws IOException {
        PdfDocument document = new PdfDocument();
        
        // Create a page
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        
        // Define paints
        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.rgb(33, 150, 243)); // Primary color
        titlePaint.setTextSize(24);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        
        Paint headerPaint = new Paint();
        headerPaint.setColor(Color.BLACK);
        headerPaint.setTextSize(18);
        
        Paint normalPaint = new Paint();
        normalPaint.setColor(Color.BLACK);
        normalPaint.setTextSize(12);
        
        Paint boldPaint = new Paint();
        boldPaint.setColor(Color.BLACK);
        boldPaint.setTextSize(12);
        boldPaint.setFakeBoldText(true);
        
        Paint linePaint = new Paint();
        linePaint.setColor(Color.LTGRAY);
        linePaint.setStrokeWidth(1);
        
        // Draw shop name
        canvas.drawText(context.getString(R.string.app_name), PAGE_WIDTH / 2, MARGIN, titlePaint);
        
        // Draw bill details
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateStr = dateFormat.format(bill.getDate());
        
        int yPos = MARGIN + 40;
        canvas.drawText("Bill #: " + bill.getId(), MARGIN, yPos, headerPaint);
        yPos += 20;
        canvas.drawText("Date: " + dateStr, MARGIN, yPos, headerPaint);
        yPos += 40;
        
        // Draw table header
        canvas.drawText("Item", MARGIN, yPos, boldPaint);
        canvas.drawText("Price", PAGE_WIDTH - 200, yPos, boldPaint);
        canvas.drawText("Qty", PAGE_WIDTH - 150, yPos, boldPaint);
        canvas.drawText("Total", PAGE_WIDTH - MARGIN, yPos, boldPaint);
        yPos += 10;
        
        // Draw line
        canvas.drawLine(MARGIN, yPos, PAGE_WIDTH - MARGIN, yPos, linePaint);
        yPos += 20;
        
        // Draw items
        for (BillItem item : items) {
            canvas.drawText(item.getProductName(), MARGIN, yPos, normalPaint);
            canvas.drawText(String.format(Locale.getDefault(), "₹%.2f", item.getPrice()), 
                    PAGE_WIDTH - 200, yPos, normalPaint);
            canvas.drawText(String.valueOf(item.getQuantity()), 
                    PAGE_WIDTH - 150, yPos, normalPaint);
            canvas.drawText(String.format(Locale.getDefault(), "₹%.2f", item.getTotalPrice()), 
                    PAGE_WIDTH - MARGIN, yPos, normalPaint);
            yPos += 20;
        }
        
        // Draw line
        yPos += 10;
        canvas.drawLine(MARGIN, yPos, PAGE_WIDTH - MARGIN, yPos, linePaint);
        yPos += 30;
        
        // Draw total
        canvas.drawText("Total Amount:", PAGE_WIDTH - 200, yPos, boldPaint);
        canvas.drawText(String.format(Locale.getDefault(), "₹%.2f", bill.getTotalAmount()), 
                PAGE_WIDTH - MARGIN, yPos, boldPaint);
        
        // Draw footer
        yPos = PAGE_HEIGHT - MARGIN;
        normalPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Thank you for shopping at " + context.getString(R.string.app_name), 
                PAGE_WIDTH / 2, yPos, normalPaint);
        
        // Finish the page
        document.finishPage(page);
        
        // Write the document to file
        File pdfFolder = new File(context.getExternalFilesDir(null), "bills");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
        }
        
        File pdfFile = new File(pdfFolder, "Bill_" + bill.getId() + ".pdf");
        FileOutputStream fos = new FileOutputStream(pdfFile);
        document.writeTo(fos);
        document.close();
        fos.close();
        
        return pdfFile;
    }
}
