package com.fourpool.helloworld;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String EXTRA_VIEW_MODEL = "vm";
    private MainViewModel viewModel;

    @Bind(R.id.text) TextView textView;
    @Bind(R.id.button) Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            viewModel = savedInstanceState.getParcelable(EXTRA_VIEW_MODEL);
        }

        if (viewModel == null) {
            viewModel = new MainViewModel();
        }

        viewModel.attach(this);
    }

    @Override
    protected void onDestroy() {
        viewModel.detach();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_VIEW_MODEL, viewModel);
    }

    @Override
    public void showText(String text) {
        textView.setText(text);
    }

    @Override
    public void showButtonText(String text) {
        button.setText(text);
    }

    @OnClick(R.id.button)
    public void onButtonClick() {
        viewModel.timerButtonClick();
    }
}
