package cn.surine.schedulex.base.utils;

import androidx.annotation.NonNull;

/**
 * Intro：对象管理
 *
 * @author sunliwei
 * @date 2020-01-15 13:22
 */
public class Objs {
    public interface NonCall {
        void notNull();
    }

    /**
     * 对目标对象判空
     *
     * @param target 目标对象
     */
    public static boolean notNull(Object target) {
        return target != null;
    }

    /**
     * 对目标对象判空
     *
     * @param target 目标对象
     */
    public static boolean isNull(Object target) {
        return !notNull(target);
    }


    /**
     * 使用lambda 表达式简化你的判空流程
     *
     * @param target  目标对象
     * @param nonCall 非空回调
     * @apiNote 不能在需要return的地方使用该方法，该方法只适合简单操作，如果需要return请使用 Objs.notNull方法
     */
    public static void notNullCall(Object target, @NonNull NonCall nonCall) {
        if (target != null) {
            nonCall.notNull();
        }
    }


    public static void isNullCall(Object target, @NonNull NonCall nonCall) {
        if (target == null) {
            nonCall.notNull();
        }
    }
}
