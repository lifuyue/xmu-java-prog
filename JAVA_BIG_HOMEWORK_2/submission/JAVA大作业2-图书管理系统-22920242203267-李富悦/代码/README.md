# Java 大作业 2：图书管理系统

本目录是 Java 大作业 2 的完整工程，使用 JavaFX 实现图书管理系统。

## 编译运行

```bash
mvn clean javafx:run
```

默认管理员账号来自 `data/users.txt`：

- 账号：`admin`
- 密码：`admin123`

普通读者在登录页输入姓名即可进入；如果姓名不存在，系统会自动创建读者并保存到 `users.txt`。

## 目录结构

```text
JAVA_BIG_HOMEWORK_2/
├── data/          # books.txt、records.txt、users.txt
├── src/           # JavaFX 源码，包名 com.library.lifuyue
├── screenshots/   # 真实运行截图
├── report/        # Word 实验文档
└── submission/    # 可提交文件和 zip
```

## 功能清单

- 管理员登录、普通读者进入。
- 添加、删除、修改、查看全部图书。
- 按书名模糊查询、按作者查询、按 ISBN 精确查询。
- 借书、还书、查看未归还借阅记录。
- 库存为 0 时禁止借阅，同一读者不可重复借阅同一本未归还图书。
- 启动读取文本数据，操作后和退出时自动保存文本数据。
- 输入校验、业务异常和文件读写异常均有中文提示。

## 数据文件格式

- `books.txt`：`ISBN,书名,作者,出版社,库存`
- `records.txt`：`记录编号,读者姓名,ISBN,书名,借阅日期,归还日期,是否归还`
- `users.txt`：`账号,密码,角色,显示姓名`

字段中不要包含英文逗号。
