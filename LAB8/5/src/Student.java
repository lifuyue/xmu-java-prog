public class Student {
    private String id;
    private String name;
    private String major;
    private int score;

    public Student(String id, String name, String major, int score) {
        setId(id);
        setName(name);
        setMajor(major);
        setScore(score);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("学号不能为空。");
        }
        this.id = id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空。");
        }
        this.name = name.trim();
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        if (major == null || major.trim().isEmpty()) {
            throw new IllegalArgumentException("专业不能为空。");
        }
        this.major = major.trim();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("成绩必须在 0 到 100 之间。");
        }
        this.score = score;
    }
}
