package com.ait.android.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.ait.android.minesweeper.data.MinesweeperModel;
import com.ait.android.minesweeper.view.MinesweeperView;

public class MainActivity extends AppCompatActivity {

    MinesweeperView minesweeperView;
    private LinearLayout layoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContent = findViewById(R.id.layoutContent);

        MinesweeperModel.getInstance().placeMines();

        initNewGameBtn();
        initSwitchFlag();
    }

    private void initNewGameBtn() {

        minesweeperView = (MinesweeperView) findViewById(R.id.minesweeperView);
        final Switch switchFlag = findViewById(R.id.switchFlag);

        Button btnClear = findViewById(R.id.btnNewGame);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minesweeperView.newGame();
                switchFlag.setChecked(false);
            }
        });
    }

    private void initSwitchFlag() {
        minesweeperView = findViewById(R.id.minesweeperView);

        Switch switchFlag = findViewById(R.id.switchFlag);
        switchFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MinesweeperModel.getInstance().setFlagMode(!MinesweeperModel.getInstance().getFlagMode());
            }
        });

    }

}
