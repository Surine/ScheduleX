package cn.surine.schedulex.super_import.viewmodel;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.annotation.Keep;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import cn.surine.schedulex.base.http.BaseHttpSubscriber;
import cn.surine.schedulex.base.utils.SimpleTextWatcher;
import cn.surine.schedulex.super_import.model.SuperBaseModel;
import cn.surine.schedulex.super_import.model.SuperCourseList;
import cn.surine.schedulex.super_import.model.User;

@Keep
public class SuperViewModel extends ViewModel {
    public static final int FETCH_FAIL = 2;
    public static final int TOKEN_FAIL = 3;
    public static final int FETCH_SUCCESS = 1;
    public static final int LOGIN_FAIL = 2;
    public static final int LOGIN_SUCCESS = 1;
    public static final int START_FETCH = 0;
    public static final int START_LOGIN = 0;
    public MutableLiveData<String> account = new MutableLiveData<>();
    public TextWatcher accountWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            account.setValue(editable.toString());
        }
    };
    public MutableLiveData<Integer> getCourseStatus = new MutableLiveData<>();
    public MutableLiveData<Integer> loginStatus = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public TextWatcher passwordWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            password.setValue(editable.toString());
        }
    };
    public MutableLiveData<SuperCourseList> superCourseList;
    private SuperRepository superResository;
    public MutableLiveData<User> userData = new MutableLiveData<>();

    public SuperViewModel(SuperRepository superResository) {
        this.superResository = superResository;
        this.superCourseList = new MutableLiveData<>();
        if (superCourseList.getValue() == null) {
            superCourseList.setValue(new SuperCourseList());
        }
    }

    public SuperCourseList getSuperCourseList() {
        return superCourseList.getValue();
    }

    public void login() {
        if (!TextUtils.isEmpty(account.getValue()) && !TextUtils.isEmpty(password.getValue())) {
            loginStatus.setValue(START_LOGIN);
            superResository.loginSuper(account.getValue(), password.getValue()).subscribe(new BaseHttpSubscriber<SuperBaseModel<User>>() {
                @Override
                public void onSuccess(MutableLiveData<SuperBaseModel<User>> mutableLiveData) {
                    if (mutableLiveData.getValue().status == 1 && mutableLiveData.getValue().data.statusInt == 1) {
                        userData.setValue(mutableLiveData.getValue().data);
                        loginStatus.setValue(LOGIN_SUCCESS);
                    } else {
                        loginStatus.setValue(LOGIN_FAIL);
                    }
                }

                @Override
                public void onFail(Throwable th) {
                    super.onFail(th);
                    loginStatus.setValue(LOGIN_FAIL);
                }
            });
        }
    }

    public void getCourseList(int i, int i2) {
        getCourseStatus.setValue(START_FETCH);
        superResository.getCourseList(i, i2).subscribe(new BaseHttpSubscriber<SuperBaseModel<SuperCourseList>>() {
            @Override
            public void onSuccess(MutableLiveData<SuperBaseModel<SuperCourseList>> mutableLiveData) {
                if (mutableLiveData.getValue().data == null) {
                    SuperViewModel.this.getCourseStatus.setValue(TOKEN_FAIL);
                } else {
                    SuperViewModel.this.superCourseList.setValue((mutableLiveData.getValue()).data);
                    SuperViewModel.this.getCourseStatus.setValue(FETCH_SUCCESS);
                }
            }

            @Override
            public void onFail(Throwable th) {
                super.onFail(th);
                SuperViewModel.this.getCourseStatus.setValue(FETCH_FAIL);
            }
        });
    }

}
