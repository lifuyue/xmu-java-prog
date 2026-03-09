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

    ChampionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
