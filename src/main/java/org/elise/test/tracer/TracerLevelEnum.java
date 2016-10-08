package org.elise.test.tracer;

import java.util.HashMap;

public enum TracerLevelEnum {
	INFO("INFO", 1), WARN("WARN", 2), ERROR("ERROR", 3), SPECIAL("SPECIAL", 4);

	private String levelName;
	private Integer levelValue;
	private static final HashMap<Integer, TracerLevelEnum> MAP = new HashMap<Integer, TracerLevelEnum>();

	static {
		for (TracerLevelEnum emun : TracerLevelEnum.values()) {
			MAP.put(emun.getLevelVaule(), emun);
		}
	}

	public static TracerLevelEnum parseFromInt(Integer level) {
		return MAP.get(level);
	}

	private TracerLevelEnum(String levelName, Integer levelValue) {
		this.levelName = levelName;
		this.levelValue = levelValue;
	}

	public String getLevelName() {
		return levelName;
	}

	public Integer getLevelVaule() {
		return levelValue;
	}
}
