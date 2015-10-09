package com.fourpool.helloworld;

import android.os.Parcel;
import android.os.Parcelable;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import java.util.concurrent.TimeUnit;

public class MainViewModel implements Parcelable {
    private final Observable<Long> interval = Observable.interval(1, TimeUnit.SECONDS);

    private MainView mainView;
    private Subscription subscription;

    // State
    private int currentNumber = 0;
    private boolean timerRunning = false;

    public MainViewModel() {
    }

    protected MainViewModel(Parcel in) {
        in.writeInt(currentNumber);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(currentNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MainViewModel> CREATOR = new Creator<MainViewModel>() {
        @Override
        public MainViewModel createFromParcel(Parcel in) {
            return new MainViewModel(in);
        }

        @Override
        public MainViewModel[] newArray(int size) {
            return new MainViewModel[size];
        }
    };

    public void attach(MainView mainView) {
        this.mainView = mainView;

        if (timerRunning) {
            subscription = interval.observeOn(AndroidSchedulers.mainThread()).subscribe(timerAction);
            mainView.showButtonText("Stop");
        } else {
            mainView.showButtonText("Start");
        }

        mainView.showText(String.valueOf(currentNumber));
    }

    public void detach() {
        this.mainView = null;

        if (timerRunning) {
            this.subscription.unsubscribe();
        }
    }

    public void timerButtonClick() {
        if (timerRunning) {
            subscription.unsubscribe();
            timerRunning = false;
            mainView.showButtonText("Start");
        } else {
            subscription = interval.observeOn(AndroidSchedulers.mainThread()).subscribe(timerAction);
            timerRunning = true;
            mainView.showButtonText("Stop");
        }
    }

    private final Action1<Long> timerAction = new Action1<Long>() {
        @Override
        public void call(Long l) {
            currentNumber++;
            mainView.showText(String.valueOf(currentNumber));
        }
    };
}
