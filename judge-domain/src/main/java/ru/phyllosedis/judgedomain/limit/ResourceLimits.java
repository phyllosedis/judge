package ru.phyllosedis.judgedomain.limit;

public record ResourceLimits(
        double maxCpu,
        int maxMemoryMb,
        int timeoutMs
) {
    public static ResourceLimits defaults() {
        return new ResourceLimits(0.5, 128, 2000);
    }
}
