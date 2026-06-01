/**
 * LAB9 JavaFX 模块声明：只依赖 javafx.controls，并向 JavaFX 启动器导出应用包。
 */
module com.example.demo {
    requires javafx.controls;

    exports com.example.demo;
}
