package me.sewer.guilds.module.impl.render;

import java.util.List;

public interface WordBlackList {

    default boolean constains(String word, List<String> blackList) {
        String wordLowerCase = word.toLowerCase();
        for (String string : blackList) {
            if (wordLowerCase.contains(string.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
