package com.example.ae_id_sorter;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

public final class IdSorter {
    public static final Map<Class<? extends AEKey>, ToIntFunction<AEKey>> comparatorMap = new HashMap<>();

    static {
        registry(AEItemKey.class, (item) -> BuiltInRegistries.ITEM.getId(item.getItem()));
        registry(AEFluidKey.class, (fluid) -> BuiltInRegistries.FLUID.getId(fluid.getId()));
    }

    private IdSorter() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends AEKey> void registry(Class<T> clazz, ToIntFunction<T> comparator) {
        assert clazz != null;
        assert comparator != null;
        comparatorMap.put(clazz, (ToIntFunction<AEKey>) comparator);
    }

    private static byte getTypePriority(AEKey key) {
        return switch (key) {
            case AEItemKey ignored -> Byte.MIN_VALUE;
            case AEFluidKey ignored -> Byte.MIN_VALUE + 1;
            default -> key.getType().getRawId();
        };
    }

    private static int compare(AEKey o1, AEKey o2) {
        Class<? extends AEKey> o1Class = o1.getClass();
        if (o1Class == o2.getClass()) {
            ToIntFunction<AEKey> keyComparator = comparatorMap.get(o1Class);
            if (keyComparator != null) {
                return Integer.compare(keyComparator.applyAsInt(o1), keyComparator.applyAsInt(o2));
            }
            return 0;
        } else {
            return Byte.compare(getTypePriority(o1), getTypePriority(o2));
        }
    }

    public static Comparator<AEKey> comparator() {
        return IdSorter::compare;
    }
}
