package cn.surine.schedulex.base.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.InvocationTargetException;

/**
 * Intro：
 * 想法是设计一个通用的工厂，用来兼容实现ViewModel的各种构造参数。
 * NewInstanceFactory 是一种简单工厂反射实现方式，这里采用同种方式
 * 由调用者决定调用目标的哪个构造方式
 *
 * 奈何只能使用只写一个方法只能传递Object参数，目前还没想到好的解决方案。
 *
 * @author sunliwei
 * @date 2020-01-19 14:43
 */

public class InstanceFactory extends ViewModelProvider.NewInstanceFactory {

    private Object[] mArgs;
    private Class[] mClasses;


    private InstanceFactory(Class[] classes, Object[] args) {
        this.mArgs = args;
        this.mClasses = classes;
    }


    @NonNull
    public static ViewModelProvider.NewInstanceFactory getInstance(Class[] classes,Object[] args) {
        return new InstanceFactory(classes,args);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return modelClass.getConstructor(mClasses).newInstance(mArgs);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Cannot create an instance of " + modelClass, e);
        }
    }

}
