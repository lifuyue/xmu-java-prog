# LAB10 文件处理

## 运行方式

```bash
mvn javafx:run
```

## 功能说明

- 路径解析：输入文件或文件夹路径。若为文件夹，统计其直接包含的文件数和文件夹数；若为文件，显示大小和最后修改日期。
- 学生顺序文件管理：使用 `data/students.txt` 保存学生信息，每行一条记录，字段用制表符分隔。
- 照片管理：新增或修改学生时可选择 `.JPG/.JPEG` 文件，程序会复制到 `data/photos/` 集中保存。
- 顺序文件操作：新增、删除、修改后重新顺序写入文本文件；查询和显示结果支持“上一条”“下一条”浏览。

## 截图生成

```bash
mvn javafx:run -Djavafx.args="--screenshots"
```

该命令会打开真实 JavaFX 窗口，并调用 macOS `screencapture` 生成 `screenshots/` 下的实验截图。
