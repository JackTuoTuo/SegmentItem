package com.jack.segment_item;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 自定义分割栏布局
 */
public class SegmentItem extends RelativeLayout {


    private ImageView leftImage;

    private TextView leftText;

    private TextView rightText;

    private EditText rightEditText;

    private ImageView rightImage;

    private ImageView arrow;

    private LinearLayout layoutSegmentItem;

    private String mLeftText, mRightText;
    private String mLeftTextHint, mRightTextHint;
    private float mLeftTextSize, mRightTextSize;
    private int mLeftImageWidth, mLeftTextWidth, mRightImageWidth;
    private Drawable mLeftDrawable, mRightDrawable;
    private int mLeftTextColor, mLeftTextHintColor, mRightTextColor, mRightTextHintColor;
    private int mRightTextGravity;

    // 右边箭头
    private Drawable mArrowDrawable;
    // 右文字框是否可输入
    private boolean mEditable;

    /**
     * 设置布局边距
     * <p>
     * 如果需要设置，请使用自定义属性segmentPadding
     */
    private int mPaddingLeft = 0;
    private int mPaddingTop = 0;
    private int mPaddingRight = 0;
    private int mPaddingBottom = 0;

    private boolean mHasTopLine = false;
    private boolean mHasBottomLine = false;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public SegmentItem(Context context) {
        this(context, null);
    }

    public SegmentItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(getContext()).inflate(R.layout.layout_segment_item, this);
        leftImage = this.findViewById(R.id.left_image);
        leftText = this.findViewById(R.id.left_text);
        rightText = this.findViewById(R.id.right_text);
        rightEditText = this.findViewById(R.id.right_edit_text);
        rightImage = this.findViewById(R.id.right_image);
        arrow = this.findViewById(R.id.arrow);
        layoutSegmentItem = this.findViewById(R.id.layout_segment_item);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SegmentItem, defStyle, R.style.SegmentItem);

        mLeftText = a.getString(R.styleable.SegmentItem_segmentLeftText);
        mRightText = a.getString(R.styleable.SegmentItem_segmentRightText);

        mLeftTextHint = a.getString(R.styleable.SegmentItem_segmentLeftTextHint);
        mRightTextHint = a.getString(R.styleable.SegmentItem_segmentRightTextHint);

        mLeftTextColor = a.getColor(R.styleable.SegmentItem_segmentLeftTextColor, Color.BLACK);
        mLeftTextHintColor = a.getColor(R.styleable.SegmentItem_segmentLeftTextHintColor, Color.BLACK);
        mRightTextColor = a.getColor(R.styleable.SegmentItem_segmentRightTextColor, Color.BLACK);
        mRightTextHintColor = a.getColor(R.styleable.SegmentItem_segmentRightTextHintColor, Color.BLACK);

        mLeftTextSize = a.getDimension(R.styleable.SegmentItem_segmentLeftTextSize, sp2px(context, 18));
        mRightTextSize = a.getDimension(R.styleable.SegmentItem_segmentRightTextSize, sp2px(context, 16));

        mLeftTextWidth = a.getDimensionPixelSize(R.styleable.SegmentItem_segmentLeftTextWidth, 0);
        mLeftImageWidth = a.getDimensionPixelSize(R.styleable.SegmentItem_segmentLeftImageWidth, 0);
        mRightImageWidth = a.getDimensionPixelSize(R.styleable.SegmentItem_segmentRightImageWidth, 0);

        mLeftDrawable = a.getDrawable(R.styleable.SegmentItem_segmentLeftImage);
        mRightDrawable = a.getDrawable(R.styleable.SegmentItem_segmentRightImage);

        mRightTextGravity = a.getInt(R.styleable.SegmentItem_segmentRightTextGravity, Gravity.RIGHT);

        mPaddingLeft = a.getDimensionPixelOffset(R.styleable.SegmentItem_segmentPaddingLeft, 0);
        mPaddingTop = a.getDimensionPixelOffset(R.styleable.SegmentItem_segmentPaddingTop, 0);
        mPaddingRight = a.getDimensionPixelOffset(R.styleable.SegmentItem_segmentPaddingRight, 0);
        mPaddingBottom = a.getDimensionPixelOffset(R.styleable.SegmentItem_segmentPaddingBottom, 0);

        mArrowDrawable = a.getDrawable(R.styleable.SegmentItem_segmentArrow);
        mEditable = a.getBoolean(R.styleable.SegmentItem_segmentEditable, false);

        mHasTopLine = a.getBoolean(R.styleable.SegmentItem_segmentHasTopLine, false);
        mHasBottomLine = a.getBoolean(R.styleable.SegmentItem_segmentHasBottomLine, false);

        a.recycle();

        initSegmentItem();
        setLeftImage(mLeftDrawable, mLeftImageWidth);
        setRightImage(mRightDrawable, mRightImageWidth);
        setLeftText(mLeftText, mLeftTextWidth, mLeftTextHint, mLeftTextSize, mLeftTextColor, mLeftTextHintColor);
        setRightText(mRightText, mRightTextHint, mRightTextSize, mRightTextColor, mRightTextHintColor, mRightTextGravity);
        setArrow(mArrowDrawable);

    }

    /**
     * 初始化布局
     */
    private void initSegmentItem() {
        layoutSegmentItem.setClickable(this.isClickable());
        setLayoutPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);

        if (mEditable) {
            rightText.setVisibility(View.GONE);
            rightEditText.setVisibility(View.VISIBLE);
        } else {
            rightText.setVisibility(View.VISIBLE);
            rightEditText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int dividerHeight = dp2px(getContext(), 0.6f);
        mPaint.reset();
        mPaint.setColor(Color.parseColor("#E4E4E4"));
        mPaint.setStrokeWidth(dividerHeight);

        if (mHasTopLine) {
            canvas.drawLine(0, 0, getWidth(), 0, mPaint);
        }
        if (mHasBottomLine) {
            canvas.drawLine(0, getHeight() - dividerHeight, getWidth(), getHeight() - dividerHeight, mPaint);
        }
    }


    /**
     * 设置布局边距
     */
    private void setLayoutPadding(int left, int top, int right, int bottom) {
        layoutSegmentItem.setPadding(left, top, right, bottom);
    }

    //------------------------------ 提供外部调用方法 ------------------------------

    /**
     * 设置左边icon
     *
     * @param width 图片宽度，高度按比例缩放
     */
    public void setLeftImage(int drawableResId, int width) {
        setLeftImage(getContext().getResources().getDrawable(drawableResId), width);
    }

    /**
     * 设置左边icon
     *
     * @param width 图片宽度，高度按比例缩放
     */
    public void setLeftImage(Drawable drawable, int width) {
        if (width == 0) {
            leftImage.setVisibility(View.GONE);
        } else {
            leftImage.setVisibility(View.VISIBLE);
        }
        leftImage.setImageDrawable(drawable);
        LinearLayout.LayoutParams layoutParam = (LinearLayout.LayoutParams) leftImage.getLayoutParams();
        layoutParam.width = width;
        leftImage.setLayoutParams(layoutParam);
    }

    /**
     * 设置右边icon
     *
     * @param width 图片宽度，高度按比例缩放
     */
    public void setRightImage(int drawableResId, int width) {
        setRightImage(getContext().getResources().getDrawable(drawableResId), width);
    }

    /**
     * 设置右边icon
     *
     * @param width 图片宽度，高度按比例缩放
     */
    public void setRightImage(Drawable drawable, int width) {
        if (width == 0) {
            rightImage.setVisibility(View.GONE);
        } else {
            rightImage.setVisibility(View.VISIBLE);
        }
        rightImage.setImageDrawable(drawable);
        LinearLayout.LayoutParams layoutParam = (LinearLayout.LayoutParams) rightImage.getLayoutParams();
        layoutParam.width = width;
        rightImage.setLayoutParams(layoutParam);
    }

    /**
     * 设置左边text
     * <p>
     * 使用默认字体大小和颜色
     */
    public void setLeftText(CharSequence text) {
        leftText.setText(text);
    }


    /**
     * 设置左边text
     */
    public void setLeftText(CharSequence text, int leftTextWidth, CharSequence textHint, float textSize, int textColor, int textHintColor) {
        leftText.setText(text);
        if (leftTextWidth > 0) {
            leftText.setWidth(leftTextWidth);
        }
        leftText.setHint(textHint);
        leftText.setHintTextColor(textHintColor);
        leftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        leftText.setTextColor(textColor);
    }

    /**
     * 设置右边text
     * <p>
     * 使用默认字体大小和颜色
     */
    public void setRightText(CharSequence text) {
        if (mEditable) {
            rightEditText.setText(text);
        } else {
            rightText.setText(text);
        }
    }

    /**
     * 设置右边text
     */
    public void setRightText(CharSequence text, CharSequence textHint, float textSize, int textColor, int textHintColor, int gravity) {
        if (mEditable) {
            rightEditText.setText(text);
            rightEditText.setHint(textHint);
            rightEditText.setHintTextColor(textHintColor);
            rightEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            rightEditText.setTextColor(textColor);
            rightEditText.setGravity(Gravity.CENTER_VERTICAL | gravity);
        } else {
            rightText.setText(text);
            rightText.setHint(textHint);
            rightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            rightText.setTextColor(textColor);
            rightText.setGravity(Gravity.CENTER_VERTICAL | gravity);
        }
    }

    /**
     * 设置箭头图标
     */
    public void setArrow(Drawable drawable) {
        arrow.setVisibility(drawable == null ? View.GONE : View.VISIBLE);
        arrow.setImageDrawable(drawable);
    }

    //-------------------------- 需要的话自己获取View设置属性 --------------------------------
    public ImageView getLeftImage() {
        return leftImage;
    }

    public TextView getLeftText() {
        return leftText;
    }

    public TextView getRightText() {
        return rightText;
    }

    public EditText getRightEditText() {
        return rightEditText;
    }

    public ImageView getRightImage() {
        return rightImage;
    }

    public ImageView getArrow() {
        return arrow;
    }

    public void setEditable(boolean editable) {
        this.mEditable = editable;
        initSegmentItem();
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     */
    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}