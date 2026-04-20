public class Main {
    public static void main(String[] args) {
        Student student = new Student();

        try {
            student.setName("");
        } catch (IllegaNameException e) {
            System.out.println("姓名设置失败：" + e.getMessage());
        }

        try {
            student.setAddress("福建厦门");
        } catch (IllegalAddressException e) {
            System.out.println("地址设置失败：" + e.getMessage());
        }

        try {
            student.setName("张三");
            student.setAddress("福建省厦门市");
            System.out.println("学生信息设置成功：");
            System.out.println(student);
        } catch (IllegaNameException | IllegalAddressException e) {
            System.out.println("设置学生信息时出现异常：" + e.getMessage());
        }
    }
}

class Student {
    private String name;
    private String address;

    public void setName(String name) throws IllegaNameException {
        if (name == null || name.length() < 1 || name.length() > 5) {
            throw new IllegaNameException("姓名长度必须在 1 到 5 个字符之间。");
        }
        this.name = name;
    }

    public void setAddress(String address) throws IllegalAddressException {
        if (address == null || (!address.contains("省") && !address.contains("市"))) {
            throw new IllegalAddressException("地址中必须包含“省”或“市”关键字。");
        }
        this.address = address;
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', address='" + address + "'}";
    }
}

class IllegaNameException extends Exception {
    public IllegaNameException(String message) {
        super(message);
    }
}

class IllegalAddressException extends Exception {
    public IllegalAddressException(String message) {
        super(message);
    }
}
