import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 第 1 题主程序。
 */
public class Main {
    public static void main(String[] args) {
        List<Student> students = createStudents();
        List<RequiredCourse> requiredCourses = createRequiredCourses();
        List<ElectiveCourse> electiveCourses = createElectiveCourses();

        autoSelectRequiredCourses(students, requiredCourses);

        System.out.println("=== 学生选课系统 ===");
        printStudentSummary(students);
        printElectiveCatalog(electiveCourses);

        try (Scanner scanner = new Scanner(System.in)) {
            manualSelectElectiveCourses(scanner, students, electiveCourses);
        }

        printSelectionResults(students);
    }

    private static List<Student> createStudents() {
        return Arrays.asList(
                new UndergraduateStudent("20241001", "张三", "软件工程1班"),
                new UndergraduateStudent("20241002", "李四", "计算机科学2班"),
                new GraduateStudent("20249001", "王五", "计算机技术研一班", "陈教授"),
                new GraduateStudent("20249002", "赵六", "人工智能研一班", "林教授")
        );
    }

    private static List<RequiredCourse> createRequiredCourses() {
        return Arrays.asList(
                new RequiredCourse("R101", "Java程序设计", 4),
                new RequiredCourse("R102", "数据结构", 3)
        );
    }

    private static List<ElectiveCourse> createElectiveCourses() {
        return Arrays.asList(
                new ElectiveCourse("E201", "Web开发基础", 2),
                new ElectiveCourse("E202", "机器学习导论", 2)
        );
    }

    private static void autoSelectRequiredCourses(List<Student> students, List<RequiredCourse> requiredCourses) {
        for (Student student : students) {
            for (RequiredCourse course : requiredCourses) {
                student.addCourse(course);
            }
        }
    }

    private static void printStudentSummary(List<Student> students) {
        System.out.println("已创建 4 个学生：");
        for (Student student : students) {
            System.out.printf(
                    "%s %s（学号：%s，班级：%s）%n",
                    student.getStudentType(),
                    student.getName(),
                    student.getStudentId(),
                    student.getClassName()
            );
        }
        System.out.println();
    }

    private static void printElectiveCatalog(List<ElectiveCourse> electiveCourses) {
        System.out.println("可选选修课如下：");
        for (ElectiveCourse course : electiveCourses) {
            System.out.printf(
                    "%s - %s（%d 学分）%n",
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCredits()
            );
        }
        System.out.println();
    }

    private static void manualSelectElectiveCourses(
            Scanner scanner,
            List<Student> students,
            List<ElectiveCourse> electiveCourses
    ) {
        Map<String, ElectiveCourse> electiveCourseMap = buildElectiveCourseMap(electiveCourses);

        for (Student student : students) {
            while (true) {
                System.out.printf(
                        "请为%s %s 输入 1-2 门选修课编号，多个编号用空格分隔：%n",
                        student.getStudentType(),
                        student.getName()
                );
                String input = scanner.nextLine().trim();
                List<ElectiveCourse> chosenCourses = parseElectiveSelection(input, electiveCourseMap);
                if (chosenCourses == null) {
                    System.out.println("输入无效：只能输入 1-2 门存在的选修课编号，且不能重复。请重新输入。\n");
                    continue;
                }

                for (ElectiveCourse course : chosenCourses) {
                    student.addCourse(course);
                }
                System.out.println("选课成功。\n");
                break;
            }
        }
    }

    private static Map<String, ElectiveCourse> buildElectiveCourseMap(List<ElectiveCourse> electiveCourses) {
        Map<String, ElectiveCourse> electiveCourseMap = new LinkedHashMap<>();
        for (ElectiveCourse course : electiveCourses) {
            electiveCourseMap.put(course.getCourseId(), course);
        }
        return electiveCourseMap;
    }

    private static List<ElectiveCourse> parseElectiveSelection(
            String input,
            Map<String, ElectiveCourse> electiveCourseMap
    ) {
        if (input.isEmpty()) {
            return null;
        }

        String[] parts = input.split("\\s+");
        if (parts.length < 1 || parts.length > 2) {
            return null;
        }

        List<ElectiveCourse> chosenCourses = new ArrayList<>();
        for (String part : parts) {
            ElectiveCourse course = electiveCourseMap.get(part);
            if (course == null || containsCourse(chosenCourses, part)) {
                return null;
            }
            chosenCourses.add(course);
        }
        return chosenCourses;
    }

    private static boolean containsCourse(List<ElectiveCourse> courses, String courseId) {
        for (ElectiveCourse course : courses) {
            if (course.getCourseId().equals(courseId)) {
                return true;
            }
        }
        return false;
    }

    private static void printSelectionResults(List<Student> students) {
        System.out.println("=== 每个学生的选课信息 ===");
        for (Student student : students) {
            System.out.printf("学生类型：%s%n", student.getStudentType());
            System.out.printf("学号：%s%n", student.getStudentId());
            System.out.printf("姓名：%s%n", student.getName());
            System.out.printf("班级：%s%n", student.getClassName());
            if (student instanceof GraduateStudent graduateStudent) {
                System.out.printf("导师：%s%n", graduateStudent.getAdvisor());
            }
            System.out.println("已选课程：");
            for (Course course : student.getSelectedCourses()) {
                System.out.printf(
                        "  - [%s] %s（%s，%d 学分）%n",
                        course.getCourseType(),
                        course.getCourseName(),
                        course.getCourseId(),
                        course.getCredits()
                );
            }
            System.out.println();
        }
    }
}
