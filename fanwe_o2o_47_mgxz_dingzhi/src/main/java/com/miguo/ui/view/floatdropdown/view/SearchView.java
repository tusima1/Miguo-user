package com.miguo.ui.view.floatdropdown.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.didikee.uilibs.utils.DisplayUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.dropdown.interf.ExpandReverse;
import com.miguo.ui.view.floatdropdown.interf.OnSearchActionListener;
import com.miguo.ui.view.floatdropdown.interf.OnTextChangedListener;

/**
 * Created by didik 
 * Created time 2017/1/6
 * Description: 
 */

public class SearchView extends FrameLayout implements ExpandReverse {
    private ImageView closeImg;
    private EditText editText;
    private AnimationSet hideSet;
    private AnimationSet showSet;

    private final int offset;
    private boolean isEmpty=true;//输入栏是否是空
    private boolean anim=false;//有没有执行动画
    private OnTextChangedListener textChangedListener;
    private OnSearchActionListener searchActionListener;

    public SearchView(Context context) {
        this(context,null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_searchview,this);

        offset = DisplayUtil.dp2px(context,22);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.edit);
        closeImg = (ImageView) findViewById(R.id.iv_delete);

        editText.addTextChangedListener(innerTextWatcher);

        initAnimation();

        closeImg.setVisibility(GONE);

        closeImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    String editText = getEditText();
                    if (TextUtils.isEmpty(editText)){
                        return false;
                    }
                    if (searchActionListener!=null){
                        searchActionListener.doSearch(editText);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void initAnimation() {
        hideSet =new AnimationSet(true);
        showSet =new AnimationSet(true);

        Animation hideAnimation = new TranslateAnimation(0, offset, 0, 0);
        Animation showAnimation = new TranslateAnimation(offset, 0, 0, 0);

        Animation hideRotateAnimation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        Animation showRotateAnimation = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        Animation hideAlpha=new AlphaAnimation(1.0f,0f);
        Animation showAlpha=new AlphaAnimation(0f,1.0f);

        hideSet.addAnimation(hideAnimation);
        hideSet.addAnimation(hideAlpha);
//        hideSet.addAnimation(hideRotateAnimation);
        hideSet.setDuration(300);
        hideSet.setFillAfter(false);
        hideSet.setInterpolator(new DecelerateInterpolator());

        hideSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                closeImg.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        showSet.addAnimation(showAnimation);
        showSet.addAnimation(showAlpha);
//        showSet.addAnimation(showRotateAnimation);
        showSet.setDuration(300);
        showSet.setFillAfter(false);
        showSet.setInterpolator(new AccelerateInterpolator());
        showSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                closeImg.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private TextWatcher innerTextWatcher=new TextWatcher() {
        private int total;
        private CharSequence str;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            total = s.length();
            str= s;
        }

        @Override
        public void afterTextChanged(Editable s) {
            isEmpty = total==0;
            if (textChangedListener!=null){
                textChangedListener.onTextChanged(isEmpty,str);
            }
            start(isEmpty);
        }
    };

    public void setTextChangedListener(OnTextChangedListener textChangedListener) {
        this.textChangedListener = textChangedListener;
    }

    public void setSearchActionListener(OnSearchActionListener searchActionListener) {
        this.searchActionListener = searchActionListener;
    }

    private void start(boolean isEmpty){
        if (isEmpty ){
            if (anim){
                reverse();
            }
        }else if (!anim){
            expand();
        }


    }

    @Override
    public void expand() {
        anim = true;
        closeImg.clearAnimation();
        closeImg.setAnimation(showSet);
        showSet.start();
    }

    @Override
    public void reverse() {
        anim =false;
        closeImg.clearAnimation();
        closeImg.setAnimation(hideSet);
        hideSet.start();
    }

    public String getEditText(){
        return editText.getText().toString().trim();
    }

    public void setText(String text){
        if (!TextUtils.isEmpty(text)){
            editText.setText(text);
            editText.setSelection(text.length());
        }
    }
}
