package org.elise.test.tracer;

import java.util.HashMap;



/**
 * Created by Glenn on 2016/10/25.
 */
public enum TracerLevelEnum {
    INFO("INFO", 1), WARN("WARN", 2), ERROR("ERROR", 3), SPECIAL("SPECIAL", 4), CLOSE("CLOSE", 5);

    private static final HashMap<Integer, TracerLevelEnum> MAP = new HashMap<Integer, TracerLevelEnum>();

    static {
        for (TracerLevelEnum emun : TracerLevelEnum.values()) {
            MAP.put(emun.getLevelValue(), emun);
        }
    }

    private String levelName;
    private Integer levelValue;

    private TracerLevelEnum(String levelName, Integer levelValue) {
        this.levelName = levelName;
        this.levelValue = levelValue;
    }

    public static TracerLevelEnum parseFromInt(Integer level) {
        return MAP.get(level);
    }

    /**
     * If tracer level is available
     * return true,else return false;
     *
     * @return Boolean
     */
    public boolean compare(TracerLevelEnum level) {
        return this.getLevelValue() > level.getLevelValue() ? false : true;
    }

    public String getLevelName() {
        return levelName;
    }

    public Integer getLevelValue() {
        return levelValue;
    }
}
