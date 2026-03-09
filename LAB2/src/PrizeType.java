public enum PrizeType {
    ZHUANGYUAN("状元"),
    DUITANG("对堂"),
    SANHONG("三红"),
    SIJIN("四进"),
    ERJU("二举"),
    YIXIU("一秀"),
    NONE("无奖");

    private final String displayName;

    PrizeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
