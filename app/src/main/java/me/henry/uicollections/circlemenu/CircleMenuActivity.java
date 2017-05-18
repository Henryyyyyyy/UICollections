package me.henry.uicollections.circlemenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import me.henry.uicollections.R;




/**
 * <pre>
 * @author zhy
 * http://blog.csdn.net/lmj623565791/article/details/43131133
 * </pre>
 */
public class CircleMenuActivity extends AppCompatActivity {
    private ArrayList<ItemBean> itemBeans = new ArrayList<ItemBean>();
    private ArrayList<ItemBean> itemnewBeans;
    private MyCircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[] { "模式1 ", "模式2", "模式3", "模式4", "模式5", "模式6", "模式7 ", "模式8", "模式9",
            "模式10" };
    private int[] mItemImgsSelected = new int[] { R.drawable.mode_round_ic1_select, R.drawable.mode_round_ic2_select,
            R.drawable.mode_round_ic3_select, R.drawable.mode_round_ic4_select, R.drawable.mode_round_ic5_select,
            R.drawable.mode_round_ic6_select, R.drawable.mode_round_ic1_select, R.drawable.mode_round_ic2_select,
            R.drawable.mode_round_ic3_select, R.drawable.mode_round_ic4_select };
    private int[] mItemImgsNoSelected = new int[] { R.drawable.mode_round_ic1, R.drawable.mode_round_ic2,
            R.drawable.mode_round_ic3, R.drawable.mode_round_ic4, R.drawable.mode_round_ic5, R.drawable.mode_round_ic6,
            R.drawable.mode_round_ic1, R.drawable.mode_round_ic2, R.drawable.mode_round_ic3, R.drawable.mode_round_ic4 };

    // --------------------------
    private String[] newTexts = new String[] { "模式1 ", "模式2", "模式3", "模式4", "模式4", "模式1 ", "模式2", "模式3", "模式4", "模式5",
            "模式1 ", "模式2" };
    private int[] newSelected = new int[] { R.drawable.mode_round_ic1_select, R.drawable.mode_round_ic2_select,
            R.drawable.mode_round_ic3_select, R.drawable.mode_round_ic4_select, R.drawable.mode_round_ic5_select,
            R.drawable.mode_round_ic6_select, R.drawable.mode_round_ic1_select, R.drawable.mode_round_ic2_select,
            R.drawable.mode_round_ic3_select, R.drawable.mode_round_ic4_select, R.drawable.mode_round_ic5_select,
            R.drawable.mode_round_ic6_select };
    private int[] newNoSelected = new int[] { R.drawable.mode_round_ic1, R.drawable.mode_round_ic2,
            R.drawable.mode_round_ic3, R.drawable.mode_round_ic4, R.drawable.mode_round_ic5, R.drawable.mode_round_ic6,
            R.drawable.mode_round_ic1, R.drawable.mode_round_ic2, R.drawable.mode_round_ic3, R.drawable.mode_round_ic4,
            R.drawable.mode_round_ic5, R.drawable.mode_round_ic6 };

    // -------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 自已切换布局文件看效果
        setContentView(R.layout.hahaha);

        initData();
        mCircleMenuLayout = (MyCircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(itemBeans);
        mCircleMenuLayout.setOnMenuItemClickListener(new MyCircleMenuLayout.OnMenuItemClickListener() {

            @Override
            public void itemClick(View view, int pos) {
                Toast.makeText(CircleMenuActivity.this, mItemTexts[pos], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void itemSelected(ItemBean itemBean, boolean isZero) {


            }

            @Override
            public void itemLongClick(View view, int pos) {

            }

        });

    }

    private void initData() {
        ItemBean itemBean;

        for (int i = 0; i < 6; i++) {
            itemBean = new ItemBean();
            itemBean.setResImgOn(mItemImgsSelected[i]);
            itemBean.setResImgOff(mItemImgsNoSelected[i]);
            itemBean.setTxt(mItemTexts[i]);
            itemBeans.add(itemBean);
        }

    }

    private void initnewData() {
        itemnewBeans = new ArrayList<ItemBean>();
        ItemBean itemBean;
        for (int i = 0; i < 12; i++) {
            itemBean = new ItemBean();
            itemBean.setResImgOn(newSelected[i]);
            itemBean.setResImgOff(newNoSelected[i]);
            itemBean.setTxt(newTexts[i]);
            itemnewBeans.add(itemBean);
        }

    }
}

