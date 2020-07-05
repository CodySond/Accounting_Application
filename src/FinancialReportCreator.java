import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.*;
import java.sql.*;

public class FinancialReportCreator {
    public static void createTrialBalance() {
        SQLManager dataManager = null;
        ResultSet rsAssetAccounts = null;
        ResultSet rsRevenueAccounts = null;
        ResultSet rsExpenseAccounts = null;
        ResultSet rsLiabilitiesAccounts = null;
        ResultSet rsOwnerEquityAccounts = null;
        ResultSet rsOwnerWithdrawalsAccounts = null;

        try {
            String filename = "C:/Accounting/TrialBalance.xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Trial Balance");

            HSSFRow rowhead = sheet.createRow(0);
            rowhead.createCell(0).setCellValue("Trial Balance");

            rowhead = sheet.createRow(1);
            rowhead.createCell(0).setCellValue("Placeholder company name");

            rowhead = sheet.createRow(2);
            rowhead.createCell(0).setCellValue("Date goes here");

            rowhead = sheet.createRow(3);
            rowhead.createCell(0).setCellValue("Account name");
            rowhead.createCell(1).setCellValue("Debit value");
            rowhead.createCell(2).setCellValue("Credit value");

            /* AUTOMATED WRITING OF ACCOUNTS & THEIR VALUES */
            dataManager = new SQLManager();
            rsAssetAccounts = dataManager.displayLimitedAccounts("ATYPE='asset'");
            rsExpenseAccounts = dataManager.displayLimitedAccounts("ATYPE='expense'");
            rsRevenueAccounts = dataManager.displayLimitedAccounts("ATYPE='revenue'");
            rsLiabilitiesAccounts = dataManager.displayLimitedAccounts("ATYPE='liability'");
            rsOwnerEquityAccounts = dataManager.displayLimitedAccounts("ATYPE='ownerequity'");
            rsOwnerWithdrawalsAccounts = dataManager.displayLimitedAccounts("ATYPE='ownerwithdrawal'");

            int rowNumber = 4; // Tracks what row we are currently on

            // Debit side accounts
            while(rsAssetAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsAssetAccounts.getString("ANAME"));
                rowhead.createCell(1).setCellValue(rsAssetAccounts.getDouble("AVALUE"));

                rowNumber++;
            }

            while(rsExpenseAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsExpenseAccounts.getString("ANAME"));
                rowhead.createCell(1).setCellValue(rsExpenseAccounts.getDouble("AVALUE"));

                rowNumber++;
            }

            while(rsOwnerWithdrawalsAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsOwnerWithdrawalsAccounts.getString("ANAME"));
                rowhead.createCell(1).setCellValue(rsOwnerWithdrawalsAccounts.getDouble("AVALUE"));

                rowNumber++;
            }

            // Credit side accounts
            while(rsLiabilitiesAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsLiabilitiesAccounts.getString("ANAME"));
                rowhead.createCell(2).setCellValue(rsLiabilitiesAccounts.getDouble("AVALUE"));

                rowNumber++;
            }

            while(rsRevenueAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsRevenueAccounts.getString("ANAME"));
                rowhead.createCell(2).setCellValue(rsRevenueAccounts.getDouble("AVALUE"));

                rowNumber++;
            }

            while(rsOwnerEquityAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsOwnerEquityAccounts.getString("ANAME"));
                rowhead.createCell(2).setCellValue(rsOwnerEquityAccounts.getDouble("AVALUE"));

                rowNumber++;
            }

            rowhead = sheet.createRow(rowNumber);
            rowhead.createCell(0).setCellValue("Totals:");
            rowhead.createCell(1).setCellValue("=SUM(B3:B" + (rowNumber-1) + ")");
            rowhead.createCell(2).setCellValue("=SUM(C3:B" + (rowNumber-1) + ")");

            File file = new File(filename);
            File folder = new File("C:/Accounting/");

            folder.mkdirs();
            if(!file.canWrite()){
                file.setWritable(true);
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
