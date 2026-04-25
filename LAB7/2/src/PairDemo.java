public class PairDemo {
    public static void main(String[] args) {
        Pair<String, Integer> studentScore = new Pair<>("Li Ming", 95);
        Pair<String, String> cityPair = new Pair<>("Xiamen", "Fuzhou");

        System.out.println("学生成绩: " + studentScore);
        System.out.println("第一项: " + studentScore.getFirst());
        System.out.println("第二项: " + studentScore.getSecond());

        studentScore.setSecond(98);
        System.out.println("修改后的学生成绩: " + studentScore);

        System.out.println("城市组合: " + cityPair);
    }
}

