# LAB2 博饼程序说明文档

这份文档按“第一次接触这个工程也能看懂”的思路来写，重点回答 4 个问题：

1. 这个实验做了什么。
2. 工程是怎么拆分的。
3. `LAB2/src` 里每个实现分别负责什么。
4. 这个工程到底怎么运行、怎么验证。

## 1. 实验目标

本实验实现了一个 Java 控制台版博饼程序，包含两部分：

- 基本功能：随机掷 6 个骰子，并根据博饼规则判断奖项。
- 扩展功能：支持 6 到 10 名玩家轮流掷骰，结合奖池库存实际发奖，并在结束后输出统计结果。

程序覆盖了以下奖项：

- 一秀
- 二举
- 三红
- 四进
- 对堂
- 状元档

其中“状元档”又细分为：

- 四红
- 五子
- 五王
- 状元插金花
- 六勃红
- 六勃黑
- 遍地锦

## 2. 工程结构

`LAB2` 的目录结构如下：

```text
LAB2/
├── README.md
└── src/
    ├── Main.java
    ├── DiceRoller.java
    ├── BoBingEvaluator.java
    ├── RollResult.java
    ├── PrizePool.java
    ├── AwardOutcome.java
    ├── PlayerRecord.java
    ├── PrizeType.java
    ├── ChampionType.java
    └── TestCases.java
```

可以把它理解成 4 层：

### 第 1 层：程序入口

- `Main.java`

负责菜单、输入输出、单次模式和多人模式的总流程控制。

### 第 2 层：核心业务逻辑

- `DiceRoller.java`
- `BoBingEvaluator.java`
- `PrizePool.java`

分别负责随机掷骰、规则判奖、奖池库存管理。

### 第 3 层：数据对象

- `RollResult.java`
- `AwardOutcome.java`
- `PlayerRecord.java`

这些类负责保存中间结果和最终结果，让各个模块之间传递信息更清楚。

### 第 4 层：枚举和测试

- `PrizeType.java`
- `ChampionType.java`
- `TestCases.java`

前两个是常量定义，最后一个是固定样例测试入口。

## 3. 程序整体运行流程

如果从用户打开程序开始看，流程如下：

1. 进入 `Main.main()`。
2. 打印菜单：单次判奖 / 多人模式 / 退出。
3. 用户输入菜单编号。
4. 如果选择单次判奖：
   - `DiceRoller` 生成 6 个随机骰子点数。
   - `BoBingEvaluator` 按规则判奖。
   - 控制台输出点数和判奖结果。
5. 如果选择多人模式：
   - 先输入玩家人数（限制在 6 到 10）。
   - 创建每个玩家的记录对象。
   - 创建奖池对象并初始化库存。
   - 所有玩家轮流掷骰。
   - 每次先判奖，再尝试从奖池领取实际奖项。
   - 一旦奖池全部发完，游戏立即结束。
   - 最后输出每位玩家获奖明细，以及各类奖项的发放统计。

## 4. 判奖逻辑为什么这样设计

博饼题最容易出错的地方不是“不会写 if”，而是“判奖优先级写错”。

本程序采用的判定顺序是：

1. 先判断所有状元档
2. 再判断对堂
3. 再判断三红
4. 再判断四进及其组合奖
5. 再判断二举
6. 再判断一秀
7. 否则无奖

这个顺序不能随便改，原因如下：

- 如果先判断四进，再判断五子，那么五子可能被错误识别成四进。
- 如果先判断普通四红，再判断状元插金花，那么插金花会被提前截走。
- 如果先判断一秀、二举，再判断四进组合奖，那么组合奖会被拆散。

所以 `BoBingEvaluator` 里面的判断顺序，本质上是在处理“规则冲突”。

## 5. 每个实现的保姆级说明

下面按文件逐个说明。

### 5.1 `Main.java`

文件位置：[Main.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/Main.java)

这是主程序入口，负责把所有模块串起来。

它做的事情有：

- 显示菜单
- 读取菜单编号
- 进入单次判奖模式
- 进入多人模式
- 读取玩家人数
- 打印每一轮结果
- 打印奖池库存
- 打印最终总结

你可以把 `Main` 理解成“导演”，它自己不负责判奖细节，但它负责安排整个程序按顺序执行。

关键方法说明：

- `main()`：程序入口，负责反复显示菜单，直到用户选择退出。
- `runSingleMode()`：执行一次随机掷骰并判奖，适合演示基本题。
- `runMultiPlayerMode()`：执行扩展题，多位玩家轮流掷骰并按库存发奖。
- `readPlayerCount()`：专门处理玩家数量输入，并限制在 6 到 10。
- `readInt()`：安全读取整数，避免输入字母导致程序崩溃。
- `printOutcome()`：打印“规则命中结果”和“库存处理结果”。
- `printPrizePool()`：打印当前库存。
- `printFinalSummary()`：打印最终玩家获奖情况和总统计。

### 5.2 `DiceRoller.java`

文件位置：[DiceRoller.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/DiceRoller.java)

这个类非常单纯，只负责一件事：随机生成 6 个骰子点数。

核心思路：

- 使用 `Random` 生成随机数。
- 因为 `nextInt(6)` 生成的是 `0~5`，所以要加 `1`，变成骰子的合法点数 `1~6`。
- 返回一个长度为 6 的数组。

为什么要单独写一个类，而不是直接在 `Main` 里写随机代码？

- 职责更清晰。
- 如果以后要改成“固定骰子”“可控随机种子”“图形界面掷骰动画”，只需要改这个类附近的逻辑。

### 5.3 `BoBingEvaluator.java`

文件位置：[BoBingEvaluator.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/BoBingEvaluator.java)

这是整个实验最核心的类，负责“按照博饼规则判断奖项”。

它的实现可以拆成 3 步理解：

#### 第一步：校验输入

方法：`validateDice()`

作用：

- 保证传入数组不是 `null`
- 保证数组长度等于 6
- 保证每个点数都在 `1~6`

如果输入非法，就直接抛异常，不让错误数据继续流入判奖逻辑。

#### 第二步：统计每个点数出现次数

方法：`buildCounts()`

这个方法会生成一个长度为 7 的数组 `counts`：

- `counts[1]` 表示 1 出现了几次
- `counts[2]` 表示 2 出现了几次
- ...
- `counts[6]` 表示 6 出现了几次

例如骰子是 `[4, 4, 4, 2, 2, 6]`，那么：

```text
counts[1] = 0
counts[2] = 2
counts[3] = 0
counts[4] = 3
counts[5] = 0
counts[6] = 1
```

有了这个统计数组后，判断奖项就会非常直接。

#### 第三步：按优先级判奖

主方法：`evaluate()`

判定顺序如下：

1. `evaluateChampion()`：先判断状元档
2. `isDuiTang()`：判断对堂
3. `counts[4] == 3`：判断三红
4. `findSiJinNumber()`：判断四进
5. `counts[4] == 2`：判断二举
6. `counts[4] == 1`：判断一秀
7. 否则无奖

其中最值得注意的是“四进组合奖”：

- 如果某个非 4 点数出现 4 次，就是四进。
- 如果同时还有 1 个 4，就是“四进 + 一秀”。
- 如果同时还有 2 个 4，就是“四进 + 二举”。

这也是为什么 `RollResult` 里面要保存一个“命中的奖项列表”，而不是只保存一个奖项。

### 5.4 `RollResult.java`

文件位置：[RollResult.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/RollResult.java)

这个类表示“一次掷骰在规则层面的判奖结果”。

请注意，这里说的是“规则层面”，不是“实际发奖层面”。

例如：

- 你掷出来的是“四进 + 二举”
- 这在规则上就是命中了两个奖
- 但如果奖池里二举已经发完，最终你可能只领到四进

所以这个类只负责回答：

- 本次骰子是什么
- 主奖项是什么
- 是否属于状元档
- 命中了哪些奖项
- 应该怎么展示结果文字

核心字段说明：

- `dice`：本次骰子点数
- `primaryPrize`：主奖项
- `championType`：状元细分类别
- `hitPrizes`：本次命中的所有奖项，支持组合奖

### 5.5 `PrizePool.java`

文件位置：[PrizePool.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/PrizePool.java)

这个类负责“奖池库存管理”，是多人模式的重要组成部分。

它内部维护两张表：

- `remaining`：每种奖还剩多少
- `awarded`：每种奖已经发了多少

初始化库存是：

- 状元 1
- 对堂 2
- 三红 4
- 四进 8
- 二举 16
- 一秀 32

核心方法：

- `claimAwards(RollResult result)`：根据命中结果尝试领取奖项
- `isEmpty()`：检查奖池是否全部发完
- `getRemainingCount()`：查询剩余数量
- `getAwardedCount()`：查询已发数量

这个类存在的意义是把“判奖”和“发奖”分开：

- 判奖：只管规则对不对
- 发奖：只管库存够不够

这样做以后，代码会更清楚，也更容易改。

### 5.6 `AwardOutcome.java`

文件位置：[AwardOutcome.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/AwardOutcome.java)

这个类表示“奖池处理后的结果”。

它和 `RollResult` 很容易混淆，但两者职责不同：

- `RollResult`：规则上命中了什么
- `AwardOutcome`：库存处理后实际发出了什么

例如某轮命中了“四进 + 二举”，但是二举库存不足，那么：

- `RollResult` 仍然会记录命中“四进 + 二举”
- `AwardOutcome` 只会记录实际领到的“四进”，并把“二举”记到库存不足列表里

所以这个类专门为多人模式服务。

### 5.7 `PlayerRecord.java`

文件位置：[PlayerRecord.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/PlayerRecord.java)

这个类负责记录某一位玩家的获奖历史。

它保存两类信息：

- `awardDetails`：按顺序记录玩家真正拿到的奖项名称
- `counts`：记录每类奖项拿到几次

为什么两个都要保留？

- 只看 `awardDetails`，方便最终直接展示给用户。
- 只看 `counts`，方便后续做统计功能。

目前主程序最终主要用了 `awardDetails`，但 `counts` 的设计让类更完整，也便于扩展。

### 5.8 `PrizeType.java`

文件位置：[PrizeType.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/PrizeType.java)

这个枚举定义的是“奖项大类”：

- 状元
- 对堂
- 三红
- 四进
- 二举
- 一秀
- 无奖

把它写成枚举有两个好处：

- 避免到处写字符串，减少拼写错误
- 做 `switch`、统计、映射时更稳定

### 5.9 `ChampionType.java`

文件位置：[ChampionType.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/ChampionType.java)

这个枚举定义的是“状元档的细分类别”。

因为状元不是一种固定情况，而是多种高等级情况的总称，所以必须单独拆出来。

它包含：

- 四红
- 五子
- 五王
- 状元插金花
- 六勃红
- 六勃黑
- 遍地锦
- NONE

其中 `NONE` 表示“这次结果不是状元档”。

### 5.10 `TestCases.java`

文件位置：[TestCases.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/TestCases.java)

这是测试入口，不是游戏主入口。

它包含两部分内容：

- `runRuleTests()`：用固定骰子验证判奖规则
- `runPrizePoolDemo()`：用固定流程演示奖池库存变化

这个类的价值很大，因为随机程序如果没有固定测试数据，很多逻辑错误很难稳定复现。

运行这个类，可以快速验证：

- 判奖顺序有没有写错
- 组合奖有没有写对
- 库存耗尽提示是否正常

## 6. 类之间的关系

如果把类之间的调用关系画成简单链路，大致是这样：

```text
Main
 ├── 调用 DiceRoller 生成骰子
 ├── 调用 BoBingEvaluator 判奖
 │    └── 返回 RollResult
 ├── 调用 PrizePool 尝试发奖
 │    └── 返回 AwardOutcome
 └── 把 AwardOutcome 累加到 PlayerRecord
```

也就是说：

- `Main` 负责调度
- `DiceRoller` 负责随机
- `BoBingEvaluator` 负责规则
- `RollResult` 负责保存规则判定
- `PrizePool` 负责库存发奖
- `AwardOutcome` 负责保存实际发奖
- `PlayerRecord` 负责保存玩家历史

## 7. 怎么运行这个工程

### 方法一：命令行运行

先进入 `LAB2` 目录：

```bash
cd /Users/lifuyue/Projects/xmu-java-prog/LAB2
```

再编译所有源码：

```bash
javac -d bin src/*.java
```

#### 运行主程序

```bash
java -cp bin Main
```

运行后会看到菜单：

```text
========== 博饼程序 ==========
1. 单次判奖
2. 多人模式
3. 退出
```

你可以输入：

- `1`：测试单次判奖
- `2`：进入多人模式
- `3`：退出程序

#### 运行固定测试

```bash
java -cp bin TestCases
```

这个入口不会要求你输入任何内容，它会直接打印固定测试结果。

### 方法二：在 IDE 中运行

如果你使用 Eclipse、IDEA 或 VS Code，本质流程都一样：

1. 导入 `LAB2` 为一个普通 Java 工程
2. 确认 `src` 被识别为源码目录
3. 直接运行 `Main.java`
4. 如果想看固定样例测试，就运行 `TestCases.java`

## 8. 建议的阅读顺序

如果你要给老师讲解，或者自己准备答辩，建议按这个顺序看：

1. 先看 [Main.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/Main.java)，理解程序从哪里开始。
2. 再看 [BoBingEvaluator.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/BoBingEvaluator.java)，理解判奖规则。
3. 再看 [RollResult.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/RollResult.java)，理解结果对象怎么封装。
4. 然后看 [PrizePool.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/PrizePool.java) 和 [AwardOutcome.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/AwardOutcome.java)，理解库存机制。
5. 最后看 [TestCases.java](/Users/lifuyue/Projects/xmu-java-prog/LAB2/src/TestCases.java)，理解程序如何验证。

## 9. 已补充的内容

这次补充的文档和注释重点包括：

- 为 `LAB2/src` 每个类补充类注释
- 为关键方法补充用途、参数和实现思路说明
- 为核心判断分支补充“为什么这样判断”的解释
- 在 README 中补充工程结构、模块职责、运行步骤、阅读顺序

这样做以后，这个工程更适合：

- 提交实验报告
- 给老师演示讲解
- 作为以后复习 Java 面向对象拆分的例子
