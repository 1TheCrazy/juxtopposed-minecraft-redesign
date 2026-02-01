package dev.onethecrazy.config.world;

public interface IJuxWorld {
    void join();
    String getName();
    String getDescription();
    boolean sameAs(IJuxWorld other);
}
