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

            File file = new File(filename);
            File folder = new File("C:/Accounting/");

            folder.mkdirs();
            if(!file.canWrite()){
                file.setWritable(true);
            }
            FileOutputStream fileOut = new FileOutputStream(file);

            HSSFRow rowhead = sheet.createRow(0);
            rowhead.createCell(0).setCellValue("Trial Balance");
            //workbook.write(fileOut);

            rowhead = sheet.createRow(1);
            rowhead.createCell(0).setCellValue("Placeholder company name");
            //workbook.write(fileOut);
            System.out.println("Row 2 complete");

            rowhead = sheet.createRow(2);
            rowhead.createCell(0).setCellValue("Date goes here");
            //workbook.write(fileOut);
            System.out.println("Row 3 complete");

            rowhead = sheet.createRow(3);
            rowhead.createCell(0).setCellValue("Account name");
            rowhead.createCell(1).setCellValue("Debit value");
            rowhead.createCell(2).setCellValue("Credit value");
            //workbook.write(fileOut);
            System.out.println("Row 4 complete");



            /* AUTOMATED WRITING OF ACCOUNTS & THEIR VALUES */
            dataManager = new SQLManager();
            rsAssetAccounts = dataManager.displayLimitedAccounts("ATYPE='asset'");
            rsExpenseAccounts = dataManager.displayLimitedAccounts("ATYPE='expense'");
            rsRevenueAccounts = dataManager.displayLimitedAccounts("ATYPE='revenue'");
            rsLiabilitiesAccounts = dataManager.displayLimitedAccounts("ATYPE='liability'");
            rsOwnerEquityAccounts = dataManager.displayLimitedAccounts("ATYPE='ownerequity'");
            rsOwnerWithdrawalsAccounts = dataManager.displayLimitedAccounts("ATYPE='ownerwithdrawal'");

            int rowNumber = 4; // Tracks what row we are currently on

            System.out.println("Reached loops");
            // Debit side accounts
            while(rsAssetAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsAssetAccounts.getString("ANAME"));
                rowhead.createCell(1).setCellValue(rsAssetAccounts.getDouble("AVALUE"));

                System.out.println("Asset account written");

                rowNumber++;
                //workbook.write(fileOut);
            }

            while(rsExpenseAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsExpenseAccounts.getString("ANAME"));
                rowhead.createCell(1).setCellValue(rsExpenseAccounts.getDouble("AVALUE"));

                rowNumber++;
                //workbook.write(fileOut);
            }

            while(rsOwnerWithdrawalsAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsOwnerWithdrawalsAccounts.getString("ANAME"));
                rowhead.createCell(1).setCellValue(rsOwnerWithdrawalsAccounts.getDouble("AVALUE"));

                rowNumber++;
                //workbook.write(fileOut);
            }

            /* Credit side accounts */
            while(rsLiabilitiesAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsLiabilitiesAccounts.getString("ANAME"));
                rowhead.createCell(2).setCellValue(rsLiabilitiesAccounts.getDouble("AVALUE"));

                rowNumber++;
                //workbook.write(fileOut);
            }

            while(rsRevenueAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsRevenueAccounts.getString("ANAME"));
                rowhead.createCell(2).setCellValue(rsRevenueAccounts.getDouble("AVALUE"));

                System.out.println("Revenue account written");

                rowNumber++;
                //workbook.write(fileOut);
            }

            while(rsOwnerEquityAccounts.next()){
                rowhead = sheet.createRow(rowNumber);
                rowhead.createCell(0).setCellValue(rsOwnerEquityAccounts.getString("ANAME"));
                rowhead.createCell(2).setCellValue(rsOwnerEquityAccounts.getDouble("AVALUE"));

                rowNumber++;
                //workbook.write(fileOut);
            }

            rowhead = sheet.createRow(rowNumber);
            rowhead.createCell(0).setCellValue("Totals:");
            rowhead.createCell(1).setCellFormula("SUM(B3:B" + (rowNumber-1) + ")");
            rowhead.createCell(2).setCellFormula("SUM(C3:B" + (rowNumber-1) + ")");
            workbook.write(fileOut);

            workbook.close();
            fileOut.close();

            // Reset ResultSets for use in the text file version
            rsAssetAccounts = dataManager.displayLimitedAccounts("ATYPE='asset'");
            rsExpenseAccounts = dataManager.displayLimitedAccounts("ATYPE='expense'");
            rsRevenueAccounts = dataManager.displayLimitedAccounts("ATYPE='revenue'");
            rsLiabilitiesAccounts = dataManager.displayLimitedAccounts("ATYPE='liability'");
            rsOwnerEquityAccounts = dataManager.displayLimitedAccounts("ATYPE='ownerequity'");
            rsOwnerWithdrawalsAccounts = dataManager.displayLimitedAccounts("ATYPE='ownerwithdrawal'");

            FileWriter writer = new FileWriter("TrialBalance.txt");

            double debitSideTotal = 0.0;
            double creditSideTotal = 0.0;

            writer.write("Trial Balance\n\nDEBIT SIDE\n");
            while(rsAssetAccounts.next()){
                writer.write(rsAssetAccounts.getString("ANAME") + ": " + rsAssetAccounts.getDouble("AVALUE") + "\n");
                debitSideTotal += rsAssetAccounts.getDouble("AVALUE");
            }
            while(rsExpenseAccounts.next()){
                writer.write(rsExpenseAccounts.getString("ANAME") + ": " + rsExpenseAccounts.getDouble("AVALUE") + "\n");
                debitSideTotal += rsExpenseAccounts.getDouble("AVALUE");
            }
            while(rsOwnerWithdrawalsAccounts.next()){
                writer.write(rsOwnerWithdrawalsAccounts.getString("ANAME") + ": " + rsOwnerWithdrawalsAccounts.getDouble("AVALUE") + "\n");
                debitSideTotal += rsOwnerWithdrawalsAccounts.getDouble("AVALUE");
            }
            writer.write("Debit side total: " + debitSideTotal + "\n\nCREDIT SIDE\n");

            while(rsLiabilitiesAccounts.next()){
                writer.write(rsLiabilitiesAccounts.getString("ANAME") + ": " + rsLiabilitiesAccounts.getDouble("AVALUE") + "\n");
                creditSideTotal += rsLiabilitiesAccounts.getDouble("AVALUE");
            }
            while(rsRevenueAccounts.next()){
                writer.write(rsRevenueAccounts.getString("ANAME") + ": " + rsRevenueAccounts.getDouble("AVALUE") + "\n");
                creditSideTotal += rsRevenueAccounts.getDouble("AVALUE");
            }
            while(rsOwnerEquityAccounts.next()){
                writer.write(rsOwnerEquityAccounts.getString("ANAME") + ": " + rsOwnerEquityAccounts.getDouble("AVALUE") + "\n");
                creditSideTotal += rsOwnerEquityAccounts.getDouble("AVALUE");
            }
            writer.write("Credit side total: " + creditSideTotal + "\n");
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rsAssetAccounts.close();
                rsExpenseAccounts.close();
                rsLiabilitiesAccounts.close();
                rsOwnerEquityAccounts.close();
                rsOwnerWithdrawalsAccounts.close();
                rsRevenueAccounts.close();
                dataManager = null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
