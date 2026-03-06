# LAB1

这是一个最基础的 Java 控制台输入输出实验。

## 程序说明
- 输入方式 1：`BufferedReader` 读取整行文本
- 输入方式 2：`Scanner` 解析年龄数字
- 输出方式 1：`System.out.println`
- 输出方式 2：`System.out.printf`

## Eclipse 操作
1. 打开 Eclipse。
2. 选择 `File -> Open Projects from File System...`，导入 `LAB1`。
3. 展开 `src`，打开 `Main.java`。
4. 右键 `Main.java`，选择 `Run As -> Java Application`。
5. 在控制台按提示输入姓名、年龄和一句话，查看输出。

如果老师要求从零新建，也可以自己操作：
1. `File -> New -> Java Project`，项目名填写 `LAB1`。
2. 在 `src` 上右键，选择 `New -> Class`。
3. 类名填写 `Main`，勾选 `public static void main(String[] args)`。
4. 将 `Main.java` 中的代码替换为本目录示例代码。
5. 运行程序并查看输出。

## 命令行运行
在 `LAB1` 目录下执行：

```bash
javac -d bin src/Main.java
java -cp bin Main
```
