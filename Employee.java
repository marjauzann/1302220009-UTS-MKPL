package lib;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Employee {

	private String employeeId;
	private String firstName;
	private String lastName;
	private String idNumber;
	private String address;
	
	// Kode refactoring - Deklarasi variabel
	private LocalDate joinDate;
	private int monthWorkingInYear;
	
	private boolean isForeigner;
	private boolean gender; //true = Laki-laki, false = Perempuan
	
	private int monthlySalary;
	private int otherMonthlyIncome;
	private int annualDeductible;
	
	private String spouseName;
	private String spouseIdNumber;

	private List<String> childNames;
	private List<String> childIdNumbers;
	
	// Kode refactoring - Constructor
	public Employee(String employeeId, String firstName, String lastName, String idNumber, String address, 
	               int yearJoined, int monthJoined, int dayJoined, boolean isForeigner, boolean gender) {
	    this.employeeId = employeeId;
	    this.firstName = firstName;
	    this.lastName = lastName;
	    this.idNumber = idNumber;
	    this.address = address;
	    this.joinDate = LocalDate.of(yearJoined, monthJoined, dayJoined);
	    this.isForeigner = isForeigner;
	    this.gender = gender;
	    
	    childNames = new ArrayList<>();
	    childIdNumbers = new ArrayList<>();
	}
	
	/**
	 * Fungsi untuk menentukan gaji bulanan pegawai berdasarkan grade kepegawaiannya (grade 1: 3.000.000 per bulan, grade 2: 5.000.000 per bulan, grade 3: 7.000.000 per bulan)
	 * Jika pegawai adalah warga negara asing gaji bulanan diperbesar sebanyak 50%
	 */
	
	// Kode setelah refactoring
	public void setMonthlySalary(int grade) {	
	    switch(grade) {
	        case 1:
	            monthlySalary = 3000000;
	            break;
	        case 2:
	            monthlySalary = 5000000;
	            break;
	        case 3:
	            monthlySalary = 7000000;
	            break;
	        default:
	            throw new IllegalArgumentException("Grade tidak valid: " + grade);
	    }
	    
	    if (isForeigner) {
	        monthlySalary = (int) (monthlySalary * 1.5);
	    }
	}

	
	public void setAnnualDeductible(int deductible) {	
		this.annualDeductible = deductible;
	}
	
	public void setAdditionalIncome(int income) {	
		this.otherMonthlyIncome = income;
	}
	
	public void setSpouse(String spouseName, String spouseIdNumber) {
	    this.spouseName = spouseName;
	    this.spouseIdNumber = spouseIdNumber; // Bug fixed: was using idNumber instead of spouseIdNumber
	}
	
	public void addChild(String childName, String childIdNumber) {
		childNames.add(childName);
		childIdNumbers.add(childIdNumber);
	}
	
	// Kode refactoring - getAnnualIncomeTax dan calculateMonthsWorkedInYear
	public int getAnnualIncomeTax() {
	    calculateMonthsWorkedInYear();
	    
	    boolean hasSpouse = spouseIdNumber != null && !spouseIdNumber.isEmpty();
	    int numberOfChildren = childIdNumbers.size();
	    
	    return TaxFunction.calculateTax(monthlySalary, otherMonthlyIncome, monthWorkingInYear, 
	                                    annualDeductible, hasSpouse, numberOfChildren);
	}

	/**
	 * Menghitung berapa lama pegawai bekerja dalam setahun ini.
	 * Jika pegawai sudah bekerja dari tahun sebelumnya maka otomatis dianggap 12 bulan.
	 */
	private void calculateMonthsWorkedInYear() {
	    LocalDate currentDate = LocalDate.now();
	    
	    if (currentDate.getYear() == joinDate.getYear()) {
	        monthWorkingInYear = currentDate.getMonthValue() - joinDate.getMonthValue();
	        // Handle edge case where employee joined this month
	        if (monthWorkingInYear < 0) {
	            monthWorkingInYear = 0;
	        }
	    } else {
	        monthWorkingInYear = 12;
	    }
	}
	
	// Getters tambahan
	public String getEmployeeId() {
	    return employeeId;
	}

	public String getFullName() {
	    return firstName + " " + lastName;
	}

	public boolean isForeigner() {
	    return isForeigner;
	}

	public int getMonthlySalary() {
	    return monthlySalary;
	}

	public int getOtherMonthlyIncome() {
	    return otherMonthlyIncome;
	}

	public int getMonthWorkingInYear() {
	    calculateMonthsWorkedInYear();
	    return monthWorkingInYear;
	}
}
