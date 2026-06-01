/**
 * 碳足迹接口：不同对象都用同一个方法返回年度碳排放估算值。
 */
public interface CarbonFootprint {
    double getCarbonFootprint();
}
