/**
 * 状元档的细分类型枚举。
 * <p>
 * 在博饼规则中，“状元”不是一个单一结果，而是包含多种更细的情况，
 * 例如四红、五子、五王、状元插金花等。
 * 使用枚举后，代码里不需要到处写字符串，既清晰又不容易拼错。
 */
public enum ChampionType {
    SI_HONG("四红"),
    WU_ZI("五子"),
    WU_WANG("五王"),
    CHA_JIN_HUA("状元插金花"),
    LIU_BO_HONG("六勃红"),
    LIU_BO_HEI("六勃黑"),
    BIAN_DI_JIN("遍地锦"),
    NONE("");

    private final String displayName;

    /**
     * @param displayName 控制台显示用名称
     */
    ChampionType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 返回给用户看的中文名称。
     */
    public String getDisplayName() {
        return displayName;
    }
}
