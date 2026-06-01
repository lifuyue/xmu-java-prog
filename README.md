# xmu-java-prog

这个仓库保存 Java 程序设计课程实验和大作业代码。当前已整理 `LAB1`、`LAB2`、`LAB3`、`LAB5` 到 `LAB10`，以及 Java 大作业目录。每个实验目录都有自己的 README，建议先从下表进入对应实验，再复制其中的命令运行。

## 环境准备

- 建议安装 JDK 21，并确认 `java`、`javac` 可以在终端中使用。
- `LAB3`、`LAB9`、`LAB10` 使用 Maven 运行 JavaFX，第一次执行时 Maven 会自动下载依赖。
- `LAB8`、`LAB9`、`LAB10` 是 GUI 程序，需要在有桌面窗口的环境中运行。

## 实验总览

| 实验 | 主题 | 目录 | 快速入口 |
| --- | --- | --- | --- |
| LAB1 | Java 基础输入输出 | `LAB1/` | 见 `LAB1/README.md` |
| LAB2 | 博饼程序与类拆分 | `LAB2/` | 见 `LAB2/README.md` |
| LAB3 | 素数、选课系统、CAI | `LAB3/` | 见 `LAB3/README.md` |
| LAB4 | 当前仓库未提供目录 | - | - |
| LAB5 | 继承、多态与图形类 | `LAB5/` | 按小题目录编译运行 |
| LAB6 | 异常、泛型方法、薪酬多态 | `LAB6/` | `bash LAB6/run.sh 1` |
| LAB7 | 泛型与集合 | `LAB7/` | `bash LAB7/run.sh 1` |
| LAB8 | Swing GUI 编程 | `LAB8/` | `bash LAB8/run.sh 1` |
| LAB9 | JavaFX 学生选课与 Painter | `LAB9/` | `mvn -f LAB9/pom.xml javafx:run` |
| LAB10 | JavaFX 文件处理与顺序文件 | `LAB10/` | `mvn -f LAB10/pom.xml javafx:run` |
| 大作业 1 | Swing AI 记事本 | `JAVA_BIG_HOMEWORK_1/` | 见 `JAVA_BIG_HOMEWORK_1/README.md` |

## 常用运行方式

控制台实验一般使用 `javac` 编译，再用 `java` 执行。例如运行 `LAB6` 第 5 题：

```bash
bash LAB6/run.sh 5
```

需要输入的控制台程序可以用 `printf` 提供样例输入。例如运行 `LAB7` 第 7 题并输入 `54`：

```bash
printf "54\n" | bash LAB7/run.sh 7
```

JavaFX 实验使用 Maven。例如运行 `LAB10`：

```bash
mvn -f LAB10/pom.xml javafx:run
```

## 推荐阅读顺序

1. 先读对应实验目录的 `README.md`，了解实验内容和运行命令。
2. 再看入口类，例如 `Main.java`、`HelloApplication.java` 或 `MainApp.java`。
3. 最后看核心类和注释，理解数据结构、事件处理或文件读写流程。
