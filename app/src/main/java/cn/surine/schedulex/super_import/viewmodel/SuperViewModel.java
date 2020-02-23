package cn.surine.schedulex.super_import.viewmodel;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import cn.surine.schedulex.base.http.BaseHttpSubscriber;
import cn.surine.schedulex.base.utils.SimpleTextWatcher;
import cn.surine.schedulex.super_import.model.SuperBaseModel;
import cn.surine.schedulex.super_import.model.SuperCourseList;
import cn.surine.schedulex.super_import.model.SuperResository;
import cn.surine.schedulex.super_import.model.User;

public class SuperViewModel extends ViewModel {
    public static final int FETCH_FAIL = 2;
    public static final int FETCH_SUCCESS = 1;
    public static final int LOGIN_FAIL = 2;
    public static final int LOGIN_SUCCESS = 1;
    public static final int START_FETCH = 0;
    public static final int START_LOGIN = 0;
    public MutableLiveData<String> account = new MutableLiveData<>();
    public TextWatcher accountWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            SuperViewModel.this.account.setValue(editable.toString());
        }
    };
    public MutableLiveData<Integer> getCourseStatus = new MutableLiveData<>();
    public MutableLiveData<Integer> loginStatus = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public TextWatcher passwordWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            SuperViewModel.this.password.setValue(editable.toString());
        }
    };
    public MutableLiveData<SuperCourseList> superCourseList;
    private SuperResository superResository;

    public SuperViewModel(SuperResository superResository2) {
        this.superResository = superResository2;
        this.superCourseList = new MutableLiveData<>();
        if (this.superCourseList.getValue() == null) {
            this.superCourseList.setValue(new SuperCourseList());
        }
    }

    public SuperCourseList getSuperCourseList() {
        return this.superCourseList.getValue();
    }

    public void login() {
        if (!TextUtils.isEmpty(this.account.getValue()) && !TextUtils.isEmpty((CharSequence) this.password.getValue())) {
            this.loginStatus.setValue(Integer.valueOf(0));
            this.superResository.loginSuper(this.account.getValue(), (String) this.password.getValue()).subscribe(new BaseHttpSubscriber<SuperBaseModel<User>>() {
                @Override
                public void onSuccess(MutableLiveData<SuperBaseModel<User>> mutableLiveData) {
                    if (mutableLiveData.getValue().status == 1) {
                        SuperViewModel.this.loginStatus.setValue(Integer.valueOf(1));
                    } else {
                        SuperViewModel.this.loginStatus.setValue(Integer.valueOf(2));
                    }
                }

                @Override
                public void onFail(Throwable th) {
                    super.onFail(th);
                    SuperViewModel.this.loginStatus.setValue(Integer.valueOf(2));
                }
            });
        }
    }

    public void getCourseList(int i, int i2) {
        this.getCourseStatus.setValue(Integer.valueOf(0));
        this.superResository.getCourseList(i, i2).subscribe(new BaseHttpSubscriber<SuperBaseModel<SuperCourseList>>() {
            @Override
            public void onSuccess(MutableLiveData<SuperBaseModel<SuperCourseList>> mutableLiveData) throws Exception {
                SuperViewModel.this.superCourseList.setValue((mutableLiveData.getValue()).data);
                SuperViewModel.this.getCourseStatus.setValue(Integer.valueOf(1));
            }

            @Override
            public void onFail(Throwable th) {
                super.onFail(th);
                SuperViewModel.this.getCourseStatus.setValue(Integer.valueOf(2));
            }
        });
    }

}
