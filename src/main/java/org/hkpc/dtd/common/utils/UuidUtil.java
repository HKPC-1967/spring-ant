package org.hkpc.dtd.common.utils;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;

import java.util.UUID;

/**
 * Use sortable UUID v7 as a PostgreSQL primary key if the ID will be shown in the front-end and you don't want snoopers to know the sequence(auto-increment  key) of your data.
 * Using sortable primary key(including auto-increment  key) is for the benefit of Database btree index
 */
public class UuidUtil {

    /**
     * UUID v1, not sortable, example:
     * fe08c363-b856-11ef-908a-6bc06e8a0485
     * 050dca87-b857-11ef-a255-a93ce4f98b96
     */
//    private static final TimeBasedGenerator uuidV1Generator = Generators.timeBasedGenerator();

    /**
     * UUID v7, Lexicographically Sortable, example:
     * 0193b9b7-8839-7e65-b200-4fb6d9d74d24
     * 0193b9b8-1015-7695-817c-9a1fad3f33bc
     * <p>
     * NOTE: calls within same millisecond produce very similar values; this may be
     * unsafe in some environments.
     * <p>
     */
    private static final TimeBasedEpochGenerator uuidV7Generator = Generators.timeBasedEpochGenerator();

    /**
     * UUID v7
     * <p>
     * Calls within same millisecond use additional per-call randomness to try to create
     * more distinct values, compared to {@link Generators#timeBasedEpochGenerator(java.util.Random)}
     * <p>
     */
    private static final TimeBasedEpochRandomGenerator uuidV7RandomGenerator = Generators.timeBasedEpochRandomGenerator();

    /**
     * UUID v7, Lexicographically Sortable
     * <p>
     * NOTE: calls within same millisecond produce very similar values; this may be
     * unsafe in some environments.
     * <p>
     */
    public static UUID generateUUIDv7() {
        return uuidV7Generator.generate();
    }

    /**
     * UUID v7, use this one if you will generate many UUIDs within the same millisecond
     * <p>
     * Calls within same millisecond use additional per-call randomness to try to create
     * more distinct values
     * <p>
     */
    public static UUID generateUUIDv7Random() {
        return uuidV7RandomGenerator.generate();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            UUID uuidV7 = generateUUIDv7();
            System.out.println("Generated UUID v7 random: " + uuidV7);
        }
    }
}
