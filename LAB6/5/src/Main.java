/**
 * 第 5 题入口：创建多个员工，演示同一个 Employee 可以替换不同薪酬模型。
 */
public class Main {
    public static void main(String[] args) {
        Employee salariedEmployee =
                new Employee("Alice", "Zhang", "1001", new SalariedCompensationModel(2000.0));
        Employee hourlyEmployee =
                new Employee("Bob", "Lin", "1002", new HourlyCompensationModel(80.0, 45.0));
        Employee commissionEmployee =
                new Employee(
                        "Cindy",
                        "Wang",
                        "1003",
                        new CommissionCompensationModel(50000.0, 0.10));
        Employee basePlusEmployee =
                new Employee(
                        "David",
                        "Chen",
                        "1004",
                        new BasePlusCommissionCompensationModel(40000.0, 0.08, 3000.0));

        System.out.println("初始收入情况：");
        printEmployeeEarnings(salariedEmployee);
        printEmployeeEarnings(hourlyEmployee);
        printEmployeeEarnings(commissionEmployee);
        printEmployeeEarnings(basePlusEmployee);

        System.out.println();
        System.out.println("动态修改 Alice 的薪酬模型后：");
        // 替换模型后，不需要修改 Employee 类，earnings 会自动走新的计算规则。
        salariedEmployee.setCompensationModel(
                new BasePlusCommissionCompensationModel(30000.0, 0.12, 2500.0));
        printEmployeeEarnings(salariedEmployee);
    }

    private static void printEmployeeEarnings(Employee employee) {
        System.out.printf("%s, earnings=%.2f%n", employee, employee.earnings());
    }
}
