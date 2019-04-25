package me.sewer.guilds.l18n;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class MessageManager {

    private final Map<Locale, MessageMap> localeMap = new HashMap<>();
    private final Locale fallback;

    public MessageManager(Locale fallback) {
        this.fallback = fallback;
    }

    public void registerLocale(MessageMap messageMap) {
        this.localeMap.put(messageMap.getLocale(), messageMap);
    }

    public void unregisterLocale(MessageMap messageMap) {
        this.localeMap.remove(messageMap.getLocale());
    }

    public Optional<MessageMap> getMessageMap(Locale locale) {
        return Optional.ofNullable(this.localeMap.get(locale));
    }

    public Map<Locale, MessageMap> getLocaleMap() {
        return this.localeMap;
    }

    public Locale getFallback() {
        return this.fallback;
    }

    public String getMessage(Locale locale, String message, Object... params) {
        String text = this.localeMap.containsKey(locale)
                ? this.localeMap.get(locale).get(message)
                : this.localeMap.get(this.fallback).get(message);
        return MessageFormat.format(text, params);
    }
}
