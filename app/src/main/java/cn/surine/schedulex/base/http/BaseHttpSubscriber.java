package cn.surine.schedulex.base.http;

import androidx.annotation.CallSuper;
import androidx.lifecycle.MutableLiveData;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import cn.surine.schedulex.base.utils.Exceptions;
import cn.surine.schedulex.data.entity.BaseVm;

/**
 * Intro：
 * 自定义一个订阅者，主要作用是将返回的数据转换为LiveData
 *
 * @author sunliwei
 * @date 2020-01-19 16:52
 */
public abstract class BaseHttpSubscriber<T extends BaseVm> implements Subscriber<T> {

    private MutableLiveData<T> data;

    public BaseHttpSubscriber() {
        this.data = new MutableLiveData<>();
    }

    private void setValue(T t) {
        data.setValue(t);
    }

    @Override
    public void onSubscribe(Subscription s) {
        //下游调用此方法告诉上游需要发送多少数据，这里传1
        s.request(1);
    }


    /**
     * 正确的时候封装正常返回实体给invoker
     */
    @Override
    public void onNext(T t) {
        setValue(t);
        try {
            onSuccess(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 错误的时候封装错误信息返回给invoker
     */
    @Override
    public void onError(Throwable t) {
        onFail(t);
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(MutableLiveData<T> vm) throws Exception;


    @CallSuper
    public void onFail(Throwable t) {
        Exceptions.handle(t);
    }

}
