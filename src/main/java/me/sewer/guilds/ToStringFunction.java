package me.sewer.guilds;

@FunctionalInterface
public interface ToStringFunction<T> {

    String applyAsString(T t);
}