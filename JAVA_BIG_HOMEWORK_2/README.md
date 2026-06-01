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

## 课堂检查演示顺序

建议按下面 4 个功能模块演示，每个模块都操作一遍，覆盖老师要求的“4 个功能截图/演示”。

### 1. 图书增删查改

1. 管理员用 `admin / admin123` 登录。
2. 进入 `图书管理` 页签。
3. 添加一本测试书，例如：
   - ISBN：`9780000000001`
   - 书名：`测试图书`
   - 作者：`测试作者`
   - 出版社：`测试出版社`
   - 库存：`2`
4. 在左侧表格中单击某一本书，右侧 `图书信息` 表单会自动显示该书的 ISBN、书名、作者、出版社和库存。
5. 修改右侧表单中的库存或出版社，点击 `修改图书`。
6. 删除时也先在左侧表格选中图书，让右侧表单自动填入对应信息，再点击 `删除图书`。系统会弹出确认框；如果这本书还有未归还记录，会拒绝删除。

这个交互的代码位置：

- 表格选中后自动填充表单：`AdminDashboardPane.bookManagementTab()` 中的 `selectedItemProperty().addListener(...)`。
- 填充右侧文本框：`AdminDashboardPane.fillBookForm(Book book)`。
- 删除按钮处理：`AdminDashboardPane.deleteBook()`。
- 真正删除规则：`LibrarySystem.deleteBook(String isbn)`。

### 2. 图书查询

1. 切换到 `图书查询` 页签。
2. 分别演示三种查询：
   - 书名关键词：输入 `Java`。
   - 作者：输入 `李华`。
   - ISBN：输入完整 ISBN，例如 `9787302561234`。
3. 点击 `重置` 展示全部图书。

代码位置：

- 查询按钮：`AdminDashboardPane.applyBookQuery()` 或 `ReaderDashboardPane.applyBookQuery()`。
- 查询规则：`LibrarySystem.searchBooks(String titleKeyword, String author, String isbn)`。

### 3. 借书和还书

1. 普通读者输入姓名进入，例如 `李富悦`。
2. 在 `图书查询` 中选一本库存大于 0 的书，切换到 `借阅归还`。
3. 点击 `借书`，库存会减 1，并新增未归还记录。
4. 再次借同一本书会被拒绝，因为同一读者不能重复借阅同一本未归还图书。
5. 点击 `还书`，库存会加 1，记录会标记为已归还。

代码位置：

- 借还按钮：`BorrowPane.borrowBook()`、`BorrowPane.returnBook()`。
- 借阅规则：`LibrarySystem.borrowBook(String isbn, String readerName)`。
- 归还规则：`LibrarySystem.returnBook(String isbn, String readerName)`。

### 4. 借阅记录和数据持久化

1. 管理员进入 `借阅记录` 页签，查看所有未归还记录。
2. 普通读者进入 `我的借阅` 页签，只能看到自己的未归还记录。
3. 退出程序后重新运行，确认图书库存、用户和借阅记录仍然存在。
4. 打开 `data/books.txt`、`data/records.txt`、`data/users.txt`，说明程序用文本文件保存数据。

代码位置：

- 管理员未还记录：`AdminDashboardPane.recordsTab()`。
- 读者本人记录：`ReaderDashboardPane.myRecordsTab()`。
- 启动读取：`LibrarySystem.load()`。
- 保存数据：`LibrarySystem.saveAll()`。

## 答辩说明：LibrarySystem 怎么工作，为什么这样设计

`LibrarySystem` 是本项目的业务核心类，位于：

```text
src/main/java/com/library/lifuyue/service/LibrarySystem.java
```

它的作用是把界面和文件读写隔开：

- `ui` 层只负责 JavaFX 界面和按钮事件。
- `LibrarySystem` 负责业务规则，例如库存不足不能借、重复借阅要拦截、有未归还记录不能删除图书。
- `repository` 层只负责 `books.txt`、`records.txt`、`users.txt` 的读写。
- `model` 层保存数据对象，例如 `Book`、`User`、`BorrowRecord`。

这样设计的原因：

- 职责清晰：界面不直接操作文件，文件读写也不混入按钮事件。
- 方便回答问题：老师问借书逻辑，只看 `LibrarySystem.borrowBook`；问文件读写，只看 `repository` 包。
- 方便测试：不用启动 JavaFX，也能直接调用 `LibrarySystem` 验证业务规则。
- 体现评分点：封装、继承、多态、集合框架、异常处理、IO 流都能明确对应到类和方法。

`LibrarySystem` 内部使用的数据结构：

- `Map<String, Book> books`：用 ISBN 快速找到某一本书。
- `List<BorrowRecord> records`：按顺序保存借阅记录，便于展示和保存历史。
- `Map<String, User> users`：用账号快速查找管理员或读者。

## 常见答辩问题速查

| 老师可能问 | 回答要点 | 代码位置 |
|---|---|---|
| 管理员登录在哪里做？ | `AuthService` 根据 `users.txt` 中的账号密码校验管理员。 | `service/AuthService.java` |
| 为什么要有 `AdminUser` 和 `ReaderUser`？ | 它们继承 `User`，体现角色差异和多态。 | `model/User.java`、`model/AdminUser.java`、`model/ReaderUser.java` |
| 添加图书怎么防止重复？ | 添加前用 ISBN 查 `books`，已存在就抛出异常。 | `LibrarySystem.addBook` |
| 删除图书为什么可能失败？ | 如果这本书存在未归还记录，删除会造成记录指向不存在的图书，所以禁止删除。 | `LibrarySystem.deleteBook` |
| 查询怎么实现？ | 对所有图书 stream 过滤；书名和作者是包含查询，ISBN 是精确匹配。 | `LibrarySystem.searchBooks` |
| 借书怎么实现？ | 先校验图书存在、库存大于 0、没有重复未还记录；成功后库存减 1 并新增记录。 | `LibrarySystem.borrowBook` |
| 还书怎么实现？ | 找到当前读者这本书的未归还记录，标记归还日期，库存加 1。 | `LibrarySystem.returnBook` |
| 数据怎么保存？ | Repository 使用 `FileReader/FileWriter` 和 `BufferedReader/BufferedWriter` 读写文本文件。 | `repository` 包 |
| 为什么选中表格就能删除？ | 表格选中监听会把选中图书填入右侧表单，删除按钮读取右侧 ISBN 后调用服务层删除。 | `AdminDashboardPane.bookManagementTab`、`fillBookForm`、`deleteBook` |

## 主要文件位置

```text
src/main/java/com/library/lifuyue/MainApp.java                         # 程序入口
src/main/java/com/library/lifuyue/ui/LoginPane.java                    # 登录界面
src/main/java/com/library/lifuyue/ui/AdminDashboardPane.java           # 管理员界面
src/main/java/com/library/lifuyue/ui/ReaderDashboardPane.java          # 读者界面
src/main/java/com/library/lifuyue/ui/BorrowPane.java                   # 借书还书界面
src/main/java/com/library/lifuyue/service/AuthService.java             # 登录校验
src/main/java/com/library/lifuyue/service/LibrarySystem.java           # 核心业务规则
src/main/java/com/library/lifuyue/repository/BookRepository.java       # 图书文件读写
src/main/java/com/library/lifuyue/repository/BorrowRecordRepository.java # 借阅记录文件读写
src/main/java/com/library/lifuyue/repository/UserRepository.java       # 用户文件读写
```
