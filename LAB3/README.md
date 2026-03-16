# LAB3

本实验在 `LAB3` 目录中完成，现已改为 Maven 工程，共包含 3 个独立程序：

- `PrimeExperiment`：控制台素数实验，输出小于 `10000` 的全部素数，并比较 `n/2` 与 `sqrt(n)` 两种判定方法的试除代价。
- `CourseSelectionApp`：JavaFX 版简单选课系统，完成学生信息录入、至少 4 门课程选择、选课结果展示和随机成绩生成。
- `MultiplicationCai`：控制台乘法练习程序，包含基础题和“随机评语”扩展题。

## 目录结构

```text
LAB3/
├── README.md
├── pom.xml
└── src/
    ├── PrimeExperiment.java
    ├── PrimeRunStats.java
    ├── PrimeUtils.java
    ├── Student.java
    ├── Course.java
    ├── Score.java
    ├── CourseSelectionApp.java
    └── MultiplicationCai.java
```

## 1. 素数实验

### 功能说明

- 判断一个数是否为素数。
- 输出所有小于 `10000` 的素数。
- 统计需要测试的整数个数。
- 分别使用“最多测试 `n/2` 次”和“最多测试 `sqrt(n)` 次”两种方案运行并比较总试除次数。

### Maven 运行

在 `LAB3` 目录下执行：

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn -Dexec.mainClass=PrimeExperiment clean compile exec:java
```

## 2. 简单选课系统

### 功能说明

- 学生信息：学号、姓名、班级、电话（电话可为空）。
- 内置 5 门课程：
  - 大学英语
  - Java程序设计
  - 操作系统
  - 数据库
  - C语言
- 至少选择 4 门课程才能提交。
- 生成已选课程的随机成绩，分数范围为 `80~100`。

### Maven 运行 JavaFX

Maven 会自动从 Maven Central 下载 JavaFX 依赖，不需要手动下载 JavaFX SDK。  
运行前建议先让 Maven 使用本机的 Java 21：

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn clean javafx:run
```

如果在 Eclipse 中使用 Maven 工程：

1. 选择 `File -> Import -> Existing Maven Projects`。
2. 选中 `LAB3` 目录并导入。
3. 确认 Eclipse 使用的是 Java 21。
4. 在 Maven 视图中运行 `javafx:run`，或右键项目执行 `Run As -> Maven build...`，输入 `javafx:run`。

## 3. 计算机辅助教学程序

### 功能说明

- 随机生成两个 `1~9` 的整数。
- 显示乘法问题。
- 学生答错时继续回答同一题。
- 学生答对后生成下一题。
- 正确和错误反馈都从 4 种评语中随机选择。

### Maven 运行

在 `LAB3` 目录下执行：

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn -Dexec.mainClass=MultiplicationCai compile exec:java
```

## 4. 建议的课堂演示顺序

1. 先运行 `PrimeExperiment`，展示素数结果和两种方法的试除次数差异。
2. 再运行 `CourseSelectionApp`，录入学生信息、完成选课并生成成绩。
3. 最后运行 `MultiplicationCai`，展示随机出题与随机评语功能。
