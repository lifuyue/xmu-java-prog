/**
 * 薪酬模型接口：不同收入计算方式都通过 earnings 暴露统一入口。
 */
public interface CompensationModel {
    double earnings();
}
