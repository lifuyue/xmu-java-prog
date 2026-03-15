# xmu-java-prog

这个仓库用于存放 Java 程序设计实验代码，目前包含 `LAB1` 和 `LAB2` 两个实验目录。  
如果你这次主要要看 `LAB2`，建议直接阅读 [LAB2/README.md](/Users/lifuyue/Projects/xmu-java-prog/LAB2/README.md)，里面已经补充了工程结构、每个类的作用、判奖逻辑和运行步骤。

## 仓库结构

```text
xmu-java-prog/
├── README.md              # 仓库总说明
├── LAB1/                  # 实验一
│   ├── README.md
│   └── src/
│       └── Main.java
└── LAB2/                  # 实验二：博饼程序
    ├── README.md
    └── src/
        ├── Main.java
        ├── BoBingEvaluator.java
        ├── DiceRoller.java
        ├── PrizePool.java
        ├── RollResult.java
        ├── AwardOutcome.java
        ├── PlayerRecord.java
        ├── PrizeType.java
        ├── ChampionType.java
        └── TestCases.java
```

## 每个实验怎么运行

### 运行 LAB1

在目录 [LAB1](/Users/lifuyue/Projects/xmu-java-prog/LAB1) 下执行：

```bash
javac -d bin src/*.java
java -cp bin Main
```

### 运行 LAB2

在目录 [LAB2](/Users/lifuyue/Projects/xmu-java-prog/LAB2) 下执行：

```bash
javac -d bin src/*.java
java -cp bin Main
```

如果要运行固定测试样例：

```bash
java -cp bin TestCases
```

## 推荐阅读顺序

如果你是第一次看这个工程，建议按下面顺序理解：

1. 先看 [LAB2/README.md](/Users/lifuyue/Projects/xmu-java-prog/LAB2/README.md)，建立整体认识。
2. 再看 [Main.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/Main.java)，理解程序入口和运行流程。
3. 再看 [BoBingEvaluator.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/BoBingEvaluator.java)，理解核心判奖逻辑。
4. 最后看奖池、玩家记录、测试类，理解多人模式和工程拆分。
