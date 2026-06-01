# LAB6 实验六：异常、泛型方法与多态薪酬模型

本实验包含 5 个控制台程序，主题包括异常抛出与捕获、自定义异常、泛型方法和接口多态。每个小题都可以独立编译运行。

## 实验内容

| 题号 | 程序 | 主要知识点 |
| --- | --- | --- |
| 1 | `Main` | 构造方法抛出自定义异常 |
| 2 | `Main` | 捕获异常、重抛异常、打印栈踪迹 |
| 3 | `Main` | 学生属性校验、自定义异常 |
| 4 | `Main` | 泛型方法 `isEqualTo` 与 `equals` |
| 5 | `Main` | 接口、组合、多态薪酬模型 |

## 目录结构

```text
LAB6/
├── 1/src/Main.java
├── 2/src/Main.java
├── 3/src/Main.java
├── 4/src/Main.java
├── 5/src/CompensationModel.java
├── 5/src/Employee.java
├── 5/src/SalariedCompensationModel.java
├── 5/src/HourlyCompensationModel.java
├── 5/src/CommissionCompensationModel.java
├── 5/src/BasePlusCommissionCompensationModel.java
├── 5/src/Main.java
└── run.sh
```

## 快速启动

在仓库根目录执行。脚本参数就是题号：

```bash
bash LAB6/run.sh 1
bash LAB6/run.sh 2
bash LAB6/run.sh 3
bash LAB6/run.sh 4
bash LAB6/run.sh 5
```

脚本会自动编译对应小题的 `src/*.java`，把 `.class` 文件放到该小题的 `bin/` 目录，然后运行入口类 `Main`。

## 手动编译运行

如果不使用脚本，也可以手动运行。以第 5 题为例：

```bash
javac -d LAB6/5/bin LAB6/5/src/*.java
java -cp LAB6/5/bin Main
```

## 设计说明

- 第 1 题让构造方法主动抛出 `ConstructionException`，主方法用 `try-catch` 捕获并打印信息。
- 第 2 题让 `someMethod2` 抛出异常，`someMethod` 捕获后重抛，最后由 `main` 打印完整栈踪迹。
- 第 3 题把学生姓名和地址校验放在 setter 中，非法数据通过自定义异常报告。
- 第 4 题用泛型方法接收不同类型对象，再统一调用对象自己的 `equals` 方法。
- 第 5 题用 `CompensationModel` 表示“收入计算规则”。`Employee` 持有一个薪酬模型对象，因此可以在运行时更换计算方式，体现组合和多态。

## 注意事项

- `bin/` 是编译输出目录，可以删除后重新运行脚本生成。
- 第 1、2、3 题会故意触发异常，这是实验内容，不代表程序运行失败。
- 第 5 题的薪资数值是演示数据，重点是接口和多态结构。
