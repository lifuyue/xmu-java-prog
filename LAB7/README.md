# LAB7 实验七：泛型与集合

本实验包含 7 个 Java 控制台程序，分别放在 `LAB7/1` 到 `LAB7/7` 中。每个小题都可以独立编译运行。

## 目录结构

```text
LAB7/
├── 1/src/GenericEqualityDemo.java
├── 2/src/Pair.java
├── 2/src/PairDemo.java
├── 3/src/CarbonFootprint.java
├── 3/src/Building.java
├── 3/src/Car.java
├── 3/src/Bicycle.java
├── 3/src/CarbonFootprintDemo.java
├── 4/src/UniqueNames.java
├── 5/src/RepeatedWordCounter.java
├── 6/src/LetterCount.java
└── 7/src/PrimeFactorsWithSet.java
```

## 编译与运行

在仓库根目录下执行以下命令。

### 题目 1：泛型方法 isEqualTo

```bash
javac -d LAB7/1/bin LAB7/1/src/*.java
java -cp LAB7/1/bin GenericEqualityDemo
```

### 题目 2：泛型类 Pair

```bash
javac -d LAB7/2/bin LAB7/2/src/*.java
java -cp LAB7/2/bin PairDemo
```

### 题目 3：CarbonFootprint 接口多态

```bash
javac -d LAB7/3/bin LAB7/3/src/*.java
java -cp LAB7/3/bin CarbonFootprintDemo
```

### 题目 4：Set 删除重复名字

```bash
javac -d LAB7/4/bin LAB7/4/src/*.java
printf "Li Ming\\nWang Fang\\nLi Ming\\nZhang San\\n\\n" | java -cp LAB7/4/bin UniqueNames
```

### 题目 5：统计句子中的重复单词

```bash
javac -d LAB7/5/bin LAB7/5/src/*.java
printf "Java is fun, and java is useful. Fun matters!\\n" | java -cp LAB7/5/bin RepeatedWordCounter
```

### 题目 6：统计每个字母出现次数

```bash
javac -d LAB7/6/bin LAB7/6/src/*.java
printf "Hello Java! Collections and Generics.\\n" | java -cp LAB7/6/bin LetterCount
```

### 题目 7：判断质数并输出质因子

```bash
javac -d LAB7/7/bin LAB7/7/src/*.java
printf "54\\n" | java -cp LAB7/7/bin PrimeFactorsWithSet
```

