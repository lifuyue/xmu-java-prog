/**
 * 奖项大类枚举。
 * <p>
 * 这里描述的是“奖项主类别”，例如一秀、二举、四进、状元等。
 * 其中状元的细分类别由 {@link ChampionType} 单独表示。
 */
public enum PrizeType {
    ZHUANGYUAN("状元"),
    DUITANG("对堂"),
    SANHONG("三红"),
    SIJIN("四进"),
    ERJU("二举"),
    YIXIU("一秀"),
    NONE("无奖");

    private final String displayName;

    /**
     * @param displayName 奖项中文显示名称
     */
    PrizeType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 返回奖项中文显示名称。
     */
    public String getDisplayName() {
        return displayName;
    }
}
