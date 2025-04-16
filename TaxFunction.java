// Kode setelah refactoring
package lib;

import java.util.logging.Logger;
import java.util.logging.Level;

public class TaxFunction {
    
    private static final Logger logger = Logger.getLogger(TaxFunction.class.getName());
    
    // Konstanta untuk pajak dan penghasilan tidak kena pajak
    private static final double TAX_RATE = 0.05;
    private static final int BASE_TAX_FREE_INCOME = 54000000;
    private static final int SPOUSE_TAX_FREE_INCOME = 4500000;
    private static final int CHILD_TAX_FREE_INCOME = 1500000;
    private static final int MAX_CHILDREN_FOR_TAX_DEDUCTION = 3;

    /**
     * Fungsi untuk menghitung jumlah pajak penghasilan pegawai yang harus dibayarkan setahun.
     * 
     * Pajak dihitung sebagai 5% dari penghasilan bersih tahunan (gaji dan pemasukan bulanan lainnya 
     * dikalikan jumlah bulan bekerja dikurangi pemotongan) dikurangi penghasilan tidak kena pajak.
     * 
     * Penghasilan tidak kena pajak:
     * - Dasar: Rp 54.000.000
     * - Jika sudah menikah: +Rp 4.500.000
     * - Per anak (maksimal 3): +Rp 1.500.000 per anak
     * 
     * @param monthlySalary Gaji bulanan
     * @param otherMonthlyIncome Pendapatan bulanan lainnya
     * @param numberOfMonthWorking Jumlah bulan bekerja dalam setahun
     * @param deductible Potongan tahunan
     * @param isMarried Status pernikahan
     * @param numberOfChildren Jumlah anak
     * @return Jumlah pajak tahunan
     */
    public static int calculateTax(int monthlySalary, int otherMonthlyIncome, int numberOfMonthWorking, 
                                  int deductible, boolean isMarried, int numberOfChildren) {
        
        validateInputs(numberOfMonthWorking, numberOfChildren);
        
        // Menghitung penghasilan tahunan
        int annualIncome = (monthlySalary + otherMonthlyIncome) * numberOfMonthWorking;
        
        // Menghitung penghasilan tidak kena pajak
        int taxFreeIncome = calculateTaxFreeIncome(isMarried, numberOfChildren);
        
        // Menghitung penghasilan kena pajak
        int taxableIncome = annualIncome - deductible - taxFreeIncome;
        
        // Menghitung pajak
        int tax = (int) Math.round(TAX_RATE * taxableIncome);
        
        // Pastikan pajak tidak negatif
        return Math.max(0, tax);
    }
    
    /**
     * Memvalidasi input parameter
     */
    private static void validateInputs(int numberOfMonthWorking, int numberOfChildren) {
        if (numberOfMonthWorking > 12) {
            logger.log(Level.WARNING, "Jumlah bulan bekerja melebihi 12 bulan dalam setahun");
        }
        
        if (numberOfMonthWorking < 0) {
            throw new IllegalArgumentException("Jumlah bulan bekerja tidak boleh negatif");
        }
    }
    
    /**
     * Menghitung penghasilan tidak kena pajak berdasarkan status pernikahan dan jumlah anak
     */
    private static int calculateTaxFreeIncome(boolean isMarried, int numberOfChildren) {
        int taxFreeIncome = BASE_TAX_FREE_INCOME;
        
        // Tambahan untuk status pernikahan
        if (isMarried) {
            taxFreeIncome += SPOUSE_TAX_FREE_INCOME;
        }
        
        // Tambahan untuk anak (maksimal 3 anak)
        int childrenForDeduction = Math.min(numberOfChildren, MAX_CHILDREN_FOR_TAX_DEDUCTION);
        taxFreeIncome += (childrenForDeduction * CHILD_TAX_FREE_INCOME);
        
        return taxFreeIncome;
    }
}