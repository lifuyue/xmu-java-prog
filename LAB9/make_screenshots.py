from pathlib import Path

from PIL import Image, ImageDraw, ImageFont


ROOT = Path(__file__).resolve().parent
OUT = ROOT / "screenshots"
OUT.mkdir(exist_ok=True)


def font(size, bold=False):
    candidates = [
        "/System/Library/Fonts/PingFang.ttc",
        "/System/Library/Fonts/STHeiti Light.ttc",
        "/Library/Fonts/Arial Unicode.ttf",
    ]
    for candidate in candidates:
        try:
            return ImageFont.truetype(candidate, size=size, index=1 if bold else 0)
        except Exception:
            pass
    return ImageFont.load_default()


FONT_TITLE = font(30, True)
FONT_H2 = font(22, True)
FONT_BODY = font(18)
FONT_SMALL = font(15)
FONT_TABLE = font(16)


def new_canvas(title):
    image = Image.new("RGB", (1200, 760), "#eef3f8")
    draw = ImageDraw.Draw(image)
    draw.rounded_rectangle([24, 24, 1176, 736], radius=18, fill="#ffffff", outline="#cbd5e1", width=2)
    draw.text((54, 48), title, fill="#0f172a", font=FONT_TITLE)
    return image, draw


def button(draw, box, text, fill="#2563eb", color="#ffffff"):
    draw.rounded_rectangle(box, radius=8, fill=fill, outline="#1d4ed8" if fill == "#2563eb" else "#cbd5e1")
    text_box = draw.textbbox((0, 0), text, font=FONT_BODY)
    x = box[0] + (box[2] - box[0] - (text_box[2] - text_box[0])) / 2
    y = box[1] + (box[3] - box[1] - (text_box[3] - text_box[1])) / 2 - 2
    draw.text((x, y), text, fill=color, font=FONT_BODY)


def input_box(draw, box, text):
    draw.rounded_rectangle(box, radius=6, fill="#ffffff", outline="#cbd5e1", width=2)
    draw.text((box[0] + 12, box[1] + 9), text, fill="#64748b", font=FONT_SMALL)


def sidebar(draw, selected):
    draw.rounded_rectangle([54, 100, 194, 688], radius=12, fill="#f4f7fb", outline="#d7e0ea")
    draw.text((84, 128), "系统菜单", fill="#0f172a", font=FONT_H2)
    items = ["学生管理", "课程管理", "选课管理"]
    for index, item in enumerate(items):
        top = 188 + index * 70
        fill = "#2563eb" if item == selected else "#ffffff"
        color = "#ffffff" if item == selected else "#334155"
        outline = "#1d4ed8" if item == selected else "#cbd5e1"
        draw.rounded_rectangle([74, top, 174, top + 42], radius=8, fill=fill, outline=outline)
        draw.text((92, top + 9), item, fill=color, font=FONT_SMALL)


def table(draw, x, y, widths, headers, rows, row_h=42):
    height = row_h * (len(rows) + 1)
    draw.rounded_rectangle([x, y, x + sum(widths), y + height], radius=8, fill="#ffffff", outline="#cbd5e1", width=2)
    draw.rectangle([x, y, x + sum(widths), y + row_h], fill="#eaf2ff")
    cx = x
    for idx, header in enumerate(headers):
        draw.text((cx + 10, y + 11), header, fill="#0f172a", font=FONT_TABLE)
        if idx:
            draw.line([cx, y, cx, y + height], fill="#cbd5e1", width=1)
        cx += widths[idx]
    for ridx, row in enumerate(rows):
        top = y + row_h * (ridx + 1)
        draw.line([x, top, x + sum(widths), top], fill="#e2e8f0", width=1)
        cx = x
        for idx, cell in enumerate(row):
            draw.text((cx + 10, top + 11), str(cell), fill="#334155", font=FONT_TABLE)
            cx += widths[idx]


def student_screen():
    image, draw = new_canvas("LAB9 学生管理查询界面")
    sidebar(draw, "学生管理")
    draw.text((232, 114), "学生信息查询", fill="#0f172a", font=FONT_H2)
    draw.rounded_rectangle([232, 154, 1138, 268], radius=10, fill="#ffffff", outline="#d9e2ec", width=2)
    labels = [("学号:", 252, 180), ("电话:", 516, 180), ("班级:", 780, 180), ("政治面貌:", 252, 228)]
    for text, x, y in labels:
        draw.text((x, y), text, fill="#334155", font=FONT_SMALL)
    input_box(draw, [302, 170, 474, 210], "202400")
    input_box(draw, [566, 170, 738, 210], "138")
    input_box(draw, [830, 170, 1002, 210], "计算机1班")
    input_box(draw, [340, 218, 510, 258], "全部")
    button(draw, [650, 218, 734, 258], "查询")
    button(draw, [754, 218, 838, 258], "重置", fill="#ffffff", color="#334155")
    table(
        draw,
        232,
        292,
        [116, 92, 142, 132, 132, 190],
        ["学号", "姓名", "电话", "班级", "政治面貌", "邮箱"],
        [
            ["2024001", "张三", "13800000001", "计算机1班", "中共党员", "zhangsan@example.com"],
            ["2024002", "李四", "13800000002", "计算机1班", "共青团员", "lisi@example.com"],
            ["2024007", "吴迪", "13788889999", "计算机1班", "群众", "wudi@example.com"],
        ],
    )
    image.save(OUT / "lab9-student-management.png")


def course_screen():
    image, draw = new_canvas("LAB9 课程管理界面")
    sidebar(draw, "课程管理")
    draw.text((232, 114), "课程管理", fill="#0f172a", font=FONT_H2)
    input_box(draw, [232, 154, 650, 198], "输入课程号、课程名或教师查询")
    table(
        draw,
        232,
        224,
        [112, 210, 82, 112, 120, 170],
        ["课程号", "课程名称", "学分", "任课教师", "课程类型", "上课时间"],
        [
            ["JAVA101", "Java程序设计", "3", "陈老师", "专业必修", "周一 1-2 节"],
            ["DB202", "数据库系统", "3", "林老师", "专业必修", "周三 3-4 节"],
            ["UI305", "人机交互设计", "2", "黄老师", "专业选修", "周五 5-6 节"],
        ],
    )
    draw.rounded_rectangle([232, 432, 1138, 590], radius=10, fill="#ffffff", outline="#d9e2ec", width=2)
    fields = [
        ("课程号:", "AI401", 252, 462),
        ("课程名称:", "人工智能基础", 518, 462),
        ("学分:", "2", 820, 462),
        ("任课教师:", "王老师", 252, 520),
        ("课程类型:", "专业选修", 518, 520),
        ("上课时间:", "周四 7-8 节", 820, 520),
    ]
    for label, value, x, y in fields:
        draw.text((x, y + 10), label, fill="#334155", font=FONT_SMALL)
        input_box(draw, [x + 86, y, x + 240, y + 40], value)
    button(draw, [232, 620, 348, 664], "新增课程")
    button(draw, [368, 620, 484, 664], "修改课程")
    button(draw, [504, 620, 620, 664], "删除课程", fill="#ffffff", color="#334155")
    image.save(OUT / "lab9-course-management.png")


def enrollment_screen():
    image, draw = new_canvas("LAB9 选课管理界面")
    sidebar(draw, "选课管理")
    draw.text((232, 114), "选课管理", fill="#0f172a", font=FONT_H2)
    input_box(draw, [232, 154, 650, 198], "输入学号、姓名或课程名查询")
    table(
        draw,
        232,
        224,
        [108, 94, 108, 204, 76, 94],
        ["学号", "姓名", "课程号", "课程名称", "成绩", "状态"],
        [
            ["2024001", "张三", "JAVA101", "Java程序设计", "92", "已结课"],
            ["2024002", "李四", "JAVA101", "Java程序设计", "88", "已结课"],
            ["2024003", "王芳", "DB202", "数据库系统", "未录入", "已选"],
            ["2024005", "孙丽", "UI305", "人机交互设计", "95", "已选"],
        ],
    )
    draw.rounded_rectangle([232, 474, 1138, 592], radius=10, fill="#ffffff", outline="#d9e2ec", width=2)
    draw.text((252, 506), "学生:", fill="#334155", font=FONT_SMALL)
    input_box(draw, [314, 494, 536, 534], "2024004 - 赵强")
    draw.text((574, 506), "课程:", fill="#334155", font=FONT_SMALL)
    input_box(draw, [636, 494, 872, 534], "DB202 - 数据库系统")
    draw.text((252, 552), "成绩:", fill="#334155", font=FONT_SMALL)
    input_box(draw, [314, 540, 448, 580], "未录入")
    draw.text((574, 552), "状态:", fill="#334155", font=FONT_SMALL)
    input_box(draw, [636, 540, 772, 580], "已选")
    button(draw, [232, 620, 348, 664], "新增选课")
    button(draw, [368, 620, 484, 664], "修改记录")
    button(draw, [504, 620, 620, 664], "删除记录", fill="#ffffff", color="#334155")
    draw.text((658, 632), "当前选课记录：4 条", fill="#475569", font=FONT_BODY)
    image.save(OUT / "lab9-enrollment-management.png")


def painter_screen():
    image, draw = new_canvas("LAB9 Painter 绘图界面")
    draw.text((64, 118), "图形:", fill="#334155", font=FONT_BODY)
    button(draw, [126, 106, 204, 150], "矩形")
    button(draw, [218, 106, 296, 150], "圆形", fill="#ffffff", color="#334155")
    button(draw, [310, 106, 388, 150], "直线", fill="#ffffff", color="#334155")
    button(draw, [414, 106, 532, 150], "修改颜色")
    button(draw, [546, 106, 624, 150], "撤销", fill="#ffffff", color="#334155")
    button(draw, [638, 106, 744, 150], "清空画布", fill="#ffffff", color="#334155")
    draw.text((772, 118), "在画布拖拽鼠标即可绘制当前图形", fill="#475569", font=FONT_BODY)
    draw.rounded_rectangle([64, 174, 1136, 688], radius=12, fill="#f8fafc", outline="#cbd5e1", width=2)
    draw.rectangle([96, 206, 1104, 656], fill="#ffffff", outline="#e2e8f0", width=2)
    draw.rectangle([190, 288, 420, 438], outline="#2563eb", width=5)
    draw.ellipse([520, 260, 700, 440], outline="#16a34a", width=5)
    draw.line([780, 482, 1010, 300], fill="#dc2626", width=6)
    draw.rectangle([246, 498, 436, 586], outline="#7c3aed", width=5)
    draw.text((96, 666), "当前画布已绘制：矩形、圆形、直线", fill="#64748b", font=FONT_SMALL)
    image.save(OUT / "lab9-painter.png")


def main():
    student_screen()
    course_screen()
    enrollment_screen()
    painter_screen()


if __name__ == "__main__":
    main()
