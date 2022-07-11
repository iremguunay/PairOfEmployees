package com.iremgunay;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

public class PairOfEmployees {

    //First, we need to read the CVS file and store the data in a list.
    public static List<String> readCVSFile(String filePath) {
        List<String> employees = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(filePath);
            Scanner scanner = new Scanner(fileReader);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                employees.add(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return employees;
    }

    //Second, we need to convert NULL values into "today date" as string.
    public static String[][] nullToStringDate(List<String> employees) {

        String[][] employeesArray = new String[employees.size()][];

        for (int i = 0; i < employees.size(); i++) {
            String[] employee = employees.get(i).split(";");
            if (employee[3].equals("NULL")) {
                employee[3] = java.time.LocalDate.now().toString();
            }
            employeesArray[i] = employee;
        }

        return employeesArray;
    }

    //Third, we need to compare worked durations employees who work together on same project
    public static void findEmployeesWorkedTogether(String[][] employees) {

        long longestCommonWorkedDays = Long.MIN_VALUE;
        List<String> pairEmployees = new ArrayList<>();
        pairEmployees.add("0");
        pairEmployees.add("0");

        for (int i = 0; i < employees.length - 1; i++) {
            for (int j = i + 1; j < employees.length; j++) {
                if (employees[i][1].equals(employees[j][1]) && !(employees[i][0].equals(employees[j][0]))) {
                    //We need to convert the string date to Date format for comparison.
                    try {
                        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date firstEmpStartDate = sdFormat.parse(employees[i][2]);
                        Date firstEmpEndDate = sdFormat.parse(employees[i][3]);
                        Date secondEmpStartDate = sdFormat.parse(employees[j][2]);
                        Date secondEmpEndDate = sdFormat.parse(employees[j][3]);

                        if (firstEmpStartDate.compareTo(secondEmpStartDate) >= 0 && secondEmpEndDate.compareTo(firstEmpStartDate) > 0) {
                            if (firstEmpEndDate.compareTo(secondEmpEndDate) < 0) {
                                long commonWorkedTime = firstEmpEndDate.getTime() - firstEmpStartDate.getTime();
                                long commonWorkedDays = commonWorkedTime / (1000 * 60 * 60 * 24);
                                if (commonWorkedDays > longestCommonWorkedDays) {
                                    longestCommonWorkedDays = commonWorkedDays;
                                    pairEmployees.set(0, employees[i][0]);
                                    pairEmployees.set(1, employees[j][0]);
                                } else if (commonWorkedDays == longestCommonWorkedDays) {
                                    pairEmployees.add(employees[i][0]);
                                    pairEmployees.add(employees[j][0]);
                                }
                            } else {
                                long commonWorkedTime = secondEmpEndDate.getTime() - firstEmpStartDate.getTime();
                                long commonWorkedDays = commonWorkedTime / (1000 * 60 * 60 * 24);
                                if (commonWorkedDays > longestCommonWorkedDays) {
                                    longestCommonWorkedDays = commonWorkedDays;
                                    pairEmployees.set(0, employees[i][0]);
                                    pairEmployees.set(1, employees[j][0]);
                                } else if (commonWorkedDays == longestCommonWorkedDays) {
                                    pairEmployees.add(employees[i][0]);
                                    pairEmployees.add(employees[j][0]);
                                }
                            }
                        } else if (secondEmpStartDate.compareTo(firstEmpStartDate) >= 0 && firstEmpEndDate.compareTo(secondEmpStartDate) > 0) {
                            if (firstEmpEndDate.compareTo(secondEmpEndDate) < 0) {
                                long commonWorkedTime = firstEmpEndDate.getTime() - secondEmpStartDate.getTime();
                                long commonWorkedDays = commonWorkedTime / (1000 * 60 * 60 * 24);
                                if (commonWorkedDays > longestCommonWorkedDays) {
                                    longestCommonWorkedDays = commonWorkedDays;
                                    pairEmployees.set(0, employees[i][0]);
                                    pairEmployees.set(1, employees[j][0]);
                                } else if (commonWorkedDays == longestCommonWorkedDays) {
                                    pairEmployees.add(employees[i][0]);
                                    pairEmployees.add(employees[j][0]);
                                }
                            } else {
                                long commonWorkedTime = secondEmpEndDate.getTime() - secondEmpStartDate.getTime();
                                long commonWorkedDays = commonWorkedTime / (1000 * 60 * 60 * 24);
                                if (commonWorkedDays > longestCommonWorkedDays) {
                                    longestCommonWorkedDays = commonWorkedDays;
                                    pairEmployees.set(0, employees[i][0]);
                                    pairEmployees.set(1, employees[j][0]);
                                } else if (commonWorkedDays == longestCommonWorkedDays) {
                                    pairEmployees.add(employees[i][0]);
                                    pairEmployees.add(employees[j][0]);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (!pairEmployees.get(0).equals("0")) {
            String delimiter = ", ";
            StringJoiner joiner = new StringJoiner(delimiter);
            pairEmployees.forEach(item -> joiner.add(String.valueOf(item)));
            System.out.println(joiner.toString() + ", " + longestCommonWorkedDays);
        } else {
            System.out.println("No pair employees found");
        }

    }

    public static void main(String[] args) throws FileNotFoundException {

        List<String> employees = readCVSFile("/Users/iremgunay/Desktop/employees.csv");
        String[][] employeesArray = nullToStringDate(employees);
        findEmployeesWorkedTogether(employeesArray);

    }
}