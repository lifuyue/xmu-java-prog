# Java 图书管理系统大作业设计规格

日期：2026-06-01

## 1. 背景与目标

本规格用于 `JAVA_BIG_HOMEWORK_2/`。目标是按课程大作业要求完成一个 JavaFX 图书管理系统，覆盖图书录入、删除、修改、查询、借阅、归还、借阅记录查看、角色权限、文件持久化、运行截图、简单文档和提交压缩包。

作业要求来自 `/Users/lifuyue/Downloads/Java 大作业.docx`，主题为“图书管理系统”。系统需要区分普通读者和管理员，数据必须通过 IO 流保存到文本文件，图形界面必须使用 JavaFX。

完整交付范围：

- Maven JavaFX 工程源码。
- 包名：`com.library.lifuyue`。
- 数据文件：`books.txt`、`records.txt`、`users.txt`。
- 4 张真实运行截图。
- Word 简单文档，包含类图、核心功能说明、遇到的问题及解决方法。
- `submission/` 目录和可提交 zip。

Word 文档阶段必须使用 `documents:documents` 技能，并按其渲染 DOCX 到 PNG 的流程做视觉检查。运行截图必须来自真实运行的 JavaFX 程序，用 Computer Use 操作本机窗口截图，不使用伪造或合成运行图。

## 2. 已确认决策

- 作业目录：`JAVA_BIG_HOMEWORK_2/`。
- 技术栈：Maven + JavaFX。
- JavaFX 工程结构参考仓库已有 `LAB9`、`LAB10`。
- 包名：`com.library.lifuyue`。
- 角色方案：登录界面；管理员账号密码登录，普通读者输入姓名进入。
- 管理员账号来源：`users.txt`。
- 交付档位：完整交付，包含工程、数据、截图、Word 文档和提交 zip。
- 截图方式：真实运行程序后用 Computer Use 截图。
- Word 文档方式：使用 `documents:documents` 并完成渲染检查。

## 3. 备选方案与选择理由

### 3.1 方案 A：Maven + JavaFX + 分层结构 + TXT 文件持久化

使用 `com.library.lifuyue` 包，拆分 `model`、`repository`、`service`、`ui`。`books.txt`、`records.txt`、`users.txt` 使用 `BufferedReader`、`BufferedWriter` 读写。

优点：

- 最贴合评分点：面向对象、集合框架、异常处理、IO、代码规范、JavaFX 界面都能清楚体现。
- 分层清晰，报告和当面答辩容易解释。
- 便于准备真实截图和测试数据。

缺点：

- 文件数量多于单文件方案，需要控制范围，避免过度设计。

结论：采用此方案。

### 3.2 方案 B：少量类 + 单窗口 JavaFX

只保留实体类、一个系统类和一个主界面类。

优点：

- 开发速度快。

缺点：

- 类职责不清晰，继承、多态和代码规范分数不容易体现。
- 文件读写、业务规则和界面事件容易混在一起。

结论：不采用。

### 3.3 方案 C：完整业务系统式多页面权限管理

增加更完整的用户管理、历史记录筛选、多页面导航和更复杂权限。

优点：

- 更接近正式业务系统。

缺点：

- 对课程作业偏重，调试和截图成本高。
- 容易超出要求，影响稳定交付。

结论：不采用。

## 4. 项目目录设计

```text
JAVA_BIG_HOMEWORK_2/
├── pom.xml
├── README.md
├── data/
│   ├── books.txt
│   ├── records.txt
│   └── users.txt
├── src/main/java/
│   └── com/library/lifuyue/
│       ├── MainApp.java
│       ├── model/
│       │   ├── Book.java
│       │   ├── BorrowRecord.java
│       │   ├── User.java
│       │   ├── AdminUser.java
│       │   └── ReaderUser.java
│       ├── repository/
│       │   ├── BookRepository.java
│       │   ├── BorrowRecordRepository.java
│       │   └── UserRepository.java
│       ├── service/
│       │   ├── LibrarySystem.java
│       │   └── AuthService.java
│       └── ui/
│           ├── LoginPane.java
│           ├── AdminDashboardPane.java
│           ├── ReaderDashboardPane.java
│           ├── BookTablePane.java
│           └── BorrowPane.java
├── src/main/resources/
│   └── style.css
├── screenshots/
├── report/
├── submission/
└── build_report.py
```

`README.md` 说明编译运行、管理员账号、数据文件格式、截图和提交结构。`screenshots/` 保存真实运行截图。`report/` 保存 Word 文档。`submission/` 保存最终提交包内容。

## 5. 架构设计

系统分为模型层、仓储层、服务层和界面层。

### 5.1 模型层

`Book` 表示图书，包含 ISBN、书名、作者、出版社、库存数量。

`BorrowRecord` 表示借阅记录，包含记录编号、读者姓名、ISBN、书名、借阅日期、归还日期、是否归还。

`User` 是用户基类，包含账号、姓名和角色。`AdminUser`、`ReaderUser` 继承 `User`，用于体现管理员和普通读者的角色差异，满足面向对象设计中继承和多态的要求。

### 5.2 仓储层

仓储类专门负责文件读写：

- `BookRepository` 读写 `data/books.txt`。
- `BorrowRecordRepository` 读写 `data/records.txt`。
- `UserRepository` 读写 `data/users.txt`。

实现必须使用 `FileReader`、`FileWriter`、`BufferedReader`、`BufferedWriter` 或其明确组合。仓储层捕获或抛出 IO 异常，由服务层和界面层转化为中文提示。

### 5.3 服务层

`AuthService` 负责登录和用户创建：

- 管理员必须使用 `users.txt` 中的账号密码登录。
- 普通读者输入姓名进入；如果姓名不存在，系统自动创建读者用户并保存。

`LibrarySystem` 负责核心业务，并作为课程要求中的系统类：

- 添加、删除、修改、查看图书。
- 按书名、作者、ISBN 查询图书。
- 借书、还书、查看未归还记录。
- 启动加载数据，退出自动保存数据。

### 5.4 界面层

界面层使用 JavaFX 控件，不直接承担文件读写细节。界面调用服务层方法，服务层返回操作结果或抛出业务异常，界面负责弹窗展示结果。

## 6. 功能设计

### 6.1 登录与角色权限

启动后显示登录窗口，分为两个区域：

- 管理员登录：输入账号和密码。账号密码来自 `users.txt`，初始数据包含 `admin,admin123,ADMIN,管理员`。
- 普通读者进入：输入读者姓名。系统根据姓名查找读者，不存在则自动创建。

管理员进入管理员主界面，可以管理图书和查看所有未归还记录。普通读者进入读者主界面，只能查询图书、借书、还书、查看自己的未归还记录。

### 6.2 图书管理

管理员可以：

- 添加图书：录入书名、作者、ISBN、出版社、库存数量。
- 删除图书：根据 ISBN 删除，删除前弹窗确认。
- 修改图书：修改书名、作者、出版社、库存数量。
- 查看全部图书：以 `TableView<Book>` 列表展示。

删除规则：如果该书存在未归还记录，禁止删除，避免借阅记录指向不存在的图书。

### 6.3 图书查询

管理员和普通读者都可以查询图书：

- 按书名模糊查询。
- 按作者查询。
- 按 ISBN 精确查询。
- 重置为查看全部图书。

查询结果显示在图书表格中。ISBN 查询如果没有结果，界面显示空表格并弹窗提示。

### 6.4 借阅管理

普通读者可以输入或选择 ISBN 执行借书和还书。

管理员可以查看当前所有未归还的借阅记录。

借阅规则：

- 库存为 0 时不可借阅。
- 同一本书同一读者已有未归还记录时不可重复借阅。
- 借书成功后库存减 1，并生成未归还记录。
- 还书成功后库存加 1，记录归还日期并标记为已归还。

`records.txt` 保存所有借阅记录，包括已归还记录。界面默认展示未归还记录，报告中说明保留已归还记录是为了追踪历史。

### 6.5 数据持久化

程序启动时从 `data/` 读取图书、用户和借阅记录。程序退出时自动保存当前数据到对应文件。

文件格式使用简单 CSV 风格文本，每行一条记录。字段中避免使用英文逗号。读到格式错误的行时跳过该行并记录提示，不影响其他数据加载。

## 7. 界面设计

### 7.1 管理员主界面

管理员主界面使用 `TabPane`：

- `图书管理`：图书表格 + 添加、修改、删除表单。
- `图书查询`：关键词查询、作者查询、ISBN 查询、查看全部。
- `借阅记录`：显示所有未归还记录，可按读者姓名或 ISBN 筛选。
- `系统信息`：显示数据文件路径、图书数量、未还记录数量、当前登录用户。

### 7.2 普通读者主界面

普通读者主界面使用 `TabPane`：

- `图书查询`：只读表格和查询条件。
- `借阅/归还`：输入或选择 ISBN 后借书、还书。
- `我的借阅`：查看当前读者未归还记录。

### 7.3 交互反馈

所有关键操作都给出明确反馈：

- 成功：信息弹窗或状态栏提示。
- 输入错误：警告弹窗。
- 文件读写失败：错误弹窗，保留内存中的当前数据。
- 删除确认：确认弹窗，取消时不做修改。

## 8. 错误处理与输入校验

必须处理以下情况：

- 管理员账号或密码错误。
- ISBN、书名、作者、出版社为空。
- 库存不是整数或库存小于 0。
- 添加图书时 ISBN 已存在。
- 修改、删除、借阅、归还时 ISBN 不存在。
- 库存为 0 时借阅。
- 同一读者重复借阅同一本未归还图书。
- 读者归还未借阅的图书。
- 文件不存在时创建默认数据文件。
- 文件读写异常时弹窗提示。

异常处理使用 `try-catch` 覆盖文件读写、数字转换和业务失败场景。

## 9. 截图与文档设计

### 9.1 真实运行截图

实现完成后真实运行 JavaFX 程序，并用 Computer Use 操作窗口截图。截图保存到 `JAVA_BIG_HOMEWORK_2/screenshots/`：

- `login.png`：登录与角色入口。
- `book-management.png`：管理员图书管理。
- `book-query.png`：图书查询。
- `borrow-records.png`：借阅、归还或未还记录。

截图必须来自运行中的程序窗口，不使用 `ScreenshotExporter` 合成截图。

### 9.2 Word 简单文档

Word 文档保存到 `JAVA_BIG_HOMEWORK_2/report/Java图书管理系统实验文档.docx`。

文档内容：

- 标题和学生信息。
- 类图。
- 核心功能说明。
- 数据持久化说明。
- 运行截图。
- 遇到的问题及解决方法。

生成或编辑 Word 文档时必须使用 `documents:documents` 技能。完成后必须执行 DOCX 渲染到 PNG 的视觉检查流程，确认页面、表格、图片和文字没有重叠、截断或错位。如果本机缺少 LibreOffice 导致无法渲染，需要在最终说明中明确说明。

## 10. 测试与验收计划

### 10.1 编译运行

在 `JAVA_BIG_HOMEWORK_2/` 下执行：

```bash
mvn clean javafx:run
```

确认 JavaFX 程序能启动并进入登录窗口。

### 10.2 功能测试

覆盖以下场景：

- 管理员登录成功和失败。
- 普通读者输入姓名进入。
- 添加图书。
- 修改图书库存和出版社。
- 删除图书前确认。
- 有未归还记录的图书禁止删除。
- 按书名、作者、ISBN 查询。
- 库存充足时借书成功。
- 库存为 0 时借书失败。
- 同一读者重复借阅同一本书失败。
- 还书成功。
- 归还未借阅图书失败。
- 管理员查看所有未还记录。
- 读者查看自己的未还记录。

### 10.3 持久化测试

操作后退出程序，再重新启动，确认：

- 新增和修改的图书仍存在。
- 库存数量保持正确。
- 借阅记录仍存在。
- 已归还记录仍保留，未归还筛选正确。
- 新读者用户保存到 `users.txt`。

### 10.4 交付验收

最终交付前确认：

- 源码包名为 `com.library.lifuyue`。
- 至少包含 `Book`、`User`、`BorrowRecord`、`LibrarySystem`，体现封装、继承、多态。
- 使用 `ArrayList`、`HashMap` 或 JavaFX `ObservableList` 等集合管理数据。
- 使用 IO 流读写 `books.txt`、`records.txt`、`users.txt`。
- 有清晰异常处理和中文提示。
- 4 张截图来自真实程序窗口。
- Word 文档包含类图、核心功能说明、遇到的问题及解决方法。
- `submission/` 目录和 zip 可直接提交。
