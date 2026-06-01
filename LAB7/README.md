# LAB7 实验七：泛型与集合

本实验包含 7 个控制台程序，主题是泛型、接口、多态、`Set`、`Map` 和质因数分解。每个小题都可以独立编译运行。

## 实验内容

| 题号 | 程序 | 主要知识点 |
| --- | --- | --- |
| 1 | `GenericEqualityDemo` | 泛型方法、`equals` 比较 |
| 2 | `PairDemo` | 泛型类、两个类型参数 |
| 3 | `CarbonFootprintDemo` | 接口、多态、集合遍历 |
| 4 | `UniqueNames` | `LinkedHashSet` 删除重复名字 |
| 5 | `RepeatedWordCounter` | `Map` 统计重复单词 |
| 6 | `LetterCount` | `TreeMap` 统计字母次数 |
| 7 | `PrimeFactorsWithSet` | 质数判断、质因数分解、`Set` 去重 |

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
├── 7/src/PrimeFactorsWithSet.java
├── run.sh
├── run-logs/
└── screenshots/
```

## 快速启动

在仓库根目录执行。脚本参数就是题号：

```bash
bash LAB7/run.sh 1
bash LAB7/run.sh 2
bash LAB7/run.sh 3
```

第 4、5、6、7 题会从控制台读取输入，可以手动输入，也可以直接复制下面的样例命令。

第 4 题：输入名字，空行结束：

```bash
printf "Li Ming\nWang Fang\nLi Ming\nZhang San\n\n" | bash LAB7/run.sh 4
```

第 5 题：输入英文句子，统计重复单词：

```bash
printf "Java is fun, and java is useful. Fun matters!\n" | bash LAB7/run.sh 5
```

第 6 题：输入英文文本，统计字母出现次数：

```bash
printf "Hello Java! Collections and Generics.\n" | bash LAB7/run.sh 6
```

第 7 题：输入整数，判断质数并输出质因子：

```bash
printf "54\n" | bash LAB7/run.sh 7
```

## 手动编译运行

如果不使用脚本，也可以手动编译。以第 2 题为例：

```bash
javac -d LAB7/2/bin LAB7/2/src/*.java
java -cp LAB7/2/bin PairDemo
```

## 设计说明

- 第 1 题用泛型方法 `<T> boolean isEqualTo(T first, T second)` 接收任意引用类型，内部统一调用 `equals`。
- 第 2 题的 `Pair<F, S>` 使用两个类型参数，适合保存“姓名-成绩”“城市-城市”这类不同类型组合。
- 第 3 题把 `Building`、`Car`、`Bicycle` 都放入 `List<CarbonFootprint>`，循环时只依赖接口方法，体现多态。
- 第 4 题使用 `LinkedHashSet`，既能去重，也能保留第一次输入时的顺序。
- 第 5、6 题使用 `Map` 保存“元素 -> 次数”，每读到一次就把次数加 1。
- 第 7 题先判断质数；如果不是质数，再反复除以当前因子，直到不能整除，得到完整分解式。

## 注意事项

- `bin/` 是编译输出目录，可以删除后重新运行脚本生成。
- 第 4 到第 7 题如果直接运行后停住，说明程序正在等待你输入。
- `run-logs/` 和 `screenshots/` 是实验报告材料，运行脚本不会覆盖它们。
