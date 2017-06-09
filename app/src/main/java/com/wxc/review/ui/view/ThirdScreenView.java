package com.wxc.review.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

import com.wxc.review.R;
import com.wxc.review.entity.FloatWrapper;
import com.wxc.review.ui.activites.LoginActivity;
import com.wxc.review.utils.Utils;

/**
 * Created by wxc on 2017/4/22.
 */

public class ThirdScreenView extends View {
    private Paint mPaint;

    private float mRadius;
    private float mCircleX;
    private float mCircleY;

    private Bitmap mBitmap1, mBitmap2, mBitmap3, mBitmap4, mBitmap5, mBitmap6;
    private int bm_offsetX, bm_offsetY;

    private Path mAnimPath;
    private PathMeasure mPathMeasure;
    private float mPathLength;

    private float mStep;
    private float mDistance;

    float[] mPos;
    float[] mTan;

    Matrix matrix;

    private boolean mShouldSpheresRotate = true;
    private float mPosition;
    private boolean mAllCirclesDrawn = false;
    private boolean mStartNextScreen = false;


    private float[] pos1 = new float[2];
    private float[] pos2 = new float[2];
    private float[] pos3 = new float[2];
    private float[] pos4 = new float[2];
    private float[] pos5 = new float[2];
    private float[] pos6 = new float[2];


    private int UP_COUNTER = 0;
    private Path mPathLinesArr[] = new Path[6];
    private PathMeasure mLinePmArr[] = new PathMeasure[6];
    private Matrix mLineMxArr[] = new Matrix[6];
    private FloatWrapper mLinesPlArr[] = new FloatWrapper[6];
    private FloatWrapper mLinesDistArr[] = new FloatWrapper[6];
    private FloatWrapper mLinesScArr[] = new FloatWrapper[6];
    private FloatWrapper mSphereOriginalPosArr[] = new FloatWrapper[6];
    private FloatWrapper mSphereStepCountArr[] = new FloatWrapper[6];


    private float mTempDistanceX;
    private float mTempDistanceY;
    private boolean mUpperBoundHit;
    private int mBitmapScaleCounter = 0;

    private Context mContext;


    public ThirdScreenView(Context context) {
        super(context);
        initMyView(context);
    }

    public ThirdScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyView(context);
    }

    public ThirdScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMyView(context);
    }

    public void initMyView(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        //六个图标
        mBitmap1 = Utils.getCircularBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.wangyi));
        mBitmap2 = Utils.getCircularBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.jingdong));
        mBitmap3 = Utils.getCircularBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.iqiyi));
        mBitmap4 = Utils.getCircularBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.taobao));
        mBitmap5 = Utils.getCircularBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.youku));
        mBitmap6 = Utils.getCircularBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.sina));


        bm_offsetX = mBitmap4.getWidth() / 2;
        bm_offsetY = mBitmap4.getHeight() / 2;
        //路径
        mAnimPath = new Path();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);


        mCircleX = size.x / 2;

        mCircleY = (float) (size.y / 3.3);

        mRadius = (float) (2 * 1.0 / 9) * size.x;

        mTempDistanceX = (float) (1.0 / 14) * size.x;
        mTempDistanceY = (float) (1.0 / 14) * size.x;

        RectF rectF = new RectF(mCircleX - mRadius, mCircleY - mRadius, mCircleX + mRadius, mCircleY + mRadius);
        mAnimPath.addArc(rectF, -90, 359);
        mAnimPath.close();

        mPathMeasure = new PathMeasure(mAnimPath, false);
        mPathLength = mPathMeasure.getLength();


        mStep = 1;
        mDistance = 0;
        mPos = new float[2];
        mTan = new float[2];
        matrix = new Matrix();

        initOriginalPos();
        initLinePaths();

    }

    /**
     * 旋转路径
     */
    private void initLinePaths() {

        for (int i = 0; i < 6; i++) {

            mPathLinesArr[i] = new Path();
            mPathLinesArr[i].moveTo(mCircleX, mCircleY);

            mLinePmArr[i] = new PathMeasure();
            mLineMxArr[i] = new Matrix();
            mLinesPlArr[i] = new FloatWrapper(0f);
            mLinesDistArr[i] = new FloatWrapper(mRadius);
            mLinesScArr[i] = new FloatWrapper(0f);
        }

    }

    private void initOriginalPos() {

        for (int i = 0; i < 6; i++) {

            mSphereOriginalPosArr[i] = new FloatWrapper((i * mPathLength) / 6);
            mSphereStepCountArr[i] = new FloatWrapper(0f);
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawPath(mAnimPath, mPaint);

        if (mShouldSpheresRotate || !mAllCirclesDrawn) {

            pos1 = drawCircle(canvas, mSphereOriginalPosArr[0], mSphereStepCountArr[0], mBitmap1);
            pos2 = drawCircle(canvas, mSphereOriginalPosArr[1], mSphereStepCountArr[1], mBitmap2);
            pos3 = drawCircle(canvas, mSphereOriginalPosArr[2], mSphereStepCountArr[2], mBitmap3);
            pos4 = drawCircle(canvas, mSphereOriginalPosArr[3], mSphereStepCountArr[3], mBitmap4);
            pos5 = drawCircle(canvas, mSphereOriginalPosArr[4], mSphereStepCountArr[4], mBitmap5);
            pos6 = drawCircle(canvas, mSphereOriginalPosArr[5], mSphereStepCountArr[5], mBitmap6);
        }

        if (mStartNextScreen) {

            if (UP_COUNTER == 0) {

                initLinePathVars(mPathLinesArr[0], pos1, mLinePmArr[0], mLineMxArr[0], mLinesPlArr[0]);
                initLinePathVars(mPathLinesArr[1], pos2, mLinePmArr[1], mLineMxArr[1], mLinesPlArr[1]);
                initLinePathVars(mPathLinesArr[2], pos3, mLinePmArr[2], mLineMxArr[2], mLinesPlArr[2]);
                initLinePathVars(mPathLinesArr[3], pos4, mLinePmArr[3], mLineMxArr[3], mLinesPlArr[3]);
                initLinePathVars(mPathLinesArr[4], pos5, mLinePmArr[4], mLineMxArr[4], mLinesPlArr[4]);
                initLinePathVars(mPathLinesArr[5], pos6, mLinePmArr[5], mLineMxArr[5], mLinesPlArr[5]);

                UP_COUNTER += 1;
                mStartNextScreen = true;

            }

            boolean isAnim1Completed = moveCircleInOut(mLinesDistArr[0], mLinesPlArr[0], mLineMxArr[0], mLinePmArr[0], canvas, mBitmap1, mLinesScArr[0]);
            boolean isAnim2Completed = moveCircleInOut(mLinesDistArr[1], mLinesPlArr[1], mLineMxArr[1], mLinePmArr[1], canvas, mBitmap2, mLinesScArr[1]);
            boolean isAnim3Completed = moveCircleInOut(mLinesDistArr[2], mLinesPlArr[2], mLineMxArr[2], mLinePmArr[2], canvas, mBitmap3, mLinesScArr[2]);
            boolean isAnim4Completed = moveCircleInOut(mLinesDistArr[3], mLinesPlArr[3], mLineMxArr[3], mLinePmArr[3], canvas, mBitmap4, mLinesScArr[3]);
            boolean isAnim5Completed = moveCircleInOut(mLinesDistArr[4], mLinesPlArr[4], mLineMxArr[4], mLinePmArr[4], canvas, mBitmap5, mLinesScArr[4]);
            boolean isAnim6Completed = moveCircleInOut(mLinesDistArr[5], mLinesPlArr[5], mLineMxArr[5], mLinePmArr[5], canvas, mBitmap6, mLinesScArr[5]);


            if (isAnim1Completed && isAnim2Completed && isAnim3Completed && isAnim4Completed && isAnim5Completed && isAnim6Completed) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
                return;
            }


            invalidate();

        }

        if (!mShouldSpheresRotate && !mStartNextScreen) {

            float sphere1 = (float) (mPosition * 100);
            canvas.drawBitmap(mBitmap1, pos1[0] + sphere1 - bm_offsetX, pos1[1] - bm_offsetY, null);


            float sphere2 = (float) (mPosition * 400);
            canvas.drawBitmap(mBitmap2, pos2[0] + sphere2 - bm_offsetX, pos2[1] - bm_offsetY, null);

            float sphere3 = (float) (mPosition * 100);
            canvas.drawBitmap(mBitmap3, pos3[0] + sphere3 - bm_offsetX, pos3[1] - bm_offsetY, null);

            float sphere4 = (float) (mPosition * 800);
            canvas.drawBitmap(mBitmap4, pos4[0] + sphere4 - bm_offsetX, pos4[1] - bm_offsetY, null);

            float sphere5 = (float) (mPosition * 200);
            canvas.drawBitmap(mBitmap5, pos5[0] + sphere5 - bm_offsetX, pos5[1] - bm_offsetY, null);

            float sphere6 = (float) (mPosition * 1100);
            canvas.drawBitmap(mBitmap6, pos6[0] + sphere6 - bm_offsetX, pos6[1] - bm_offsetY, null);
        }

        if (mShouldSpheresRotate) {
            invalidate();
        }

        mAllCirclesDrawn = true;

    }

    private void initLinePathVars(Path path, float[] pos, PathMeasure pm, Matrix matrix, FloatWrapper pathLength) {

        float x = 0f, y = 0f;

        double len = Math.hypot((pos[0] - mCircleX), (pos[1] - mCircleY));

        double dx = (pos[0] - mCircleX) / len;
        double dy = (pos[1] - mCircleY) / len;

        x = (float) (pos[0] + mTempDistanceX * dx);
        y = (float) (pos[1] + mTempDistanceY * dy);

        path.lineTo(pos[0], pos[1]);
        path.lineTo(x, y);
        path.close();
        pm.setPath(path, false);
        pathLength.floatValue = pm.getLength();


    }

    /**
     * 退出时的圆形图标
     *
     * @param distance
     * @param pathLength
     * @param matrix
     * @param pathMeasure
     * @param canvas
     * @param bm
     * @param scaleCounter
     * @return
     */
    private boolean moveCircleInOut(FloatWrapper distance, FloatWrapper pathLength, Matrix matrix, PathMeasure pathMeasure, Canvas canvas, Bitmap bm, FloatWrapper scaleCounter) {


        float position[] = new float[2];
        float tangent[] = new float[2];


        if (distance.floatValue >= (pathLength.floatValue / 2)) {
            mUpperBoundHit = true;

        }

        if (mUpperBoundHit) {

            int newWidth = (int) (bm.getWidth() - 8 * (scaleCounter.floatValue / 1));
            int newHeight = (int) (bm.getHeight() - 8 * (scaleCounter.floatValue / 1));
            if (newWidth >= 5 && newHeight >= 5) {
                bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
                scaleCounter.floatValue += 1;

            } else {

                bm = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            }


        }


        if (distance.floatValue < pathLength.floatValue) {
            pathMeasure.getPosTan(distance.floatValue, position, tangent);
            matrix.reset();
            matrix.postTranslate(position[0] - bm.getWidth() / 2, position[1] - bm.getHeight() / 2);
            canvas.drawBitmap(bm, matrix, null);

            if (mUpperBoundHit) {

                distance.floatValue += 20;

            } else {
                distance.floatValue += 2;
            }
        } else {

            return true;

        }

        return false;
    }

    /**
     * 绘画六个图标
     *
     * @param canvas
     * @param originalPos
     * @param step
     * @param bm
     * @return
     */
    private float[] drawCircle(Canvas canvas, FloatWrapper originalPos, FloatWrapper step, Bitmap bm) {
        //间距
        float distance = originalPos.floatValue + step.floatValue;
        float positionArray[] = new float[2];
        if (distance < mPathLength) {
            mPathMeasure.getPosTan(distance, mPos, mTan);

            matrix.reset();
            matrix.postTranslate(mPos[0] - bm_offsetX, mPos[1] - bm_offsetY);
            canvas.drawBitmap(bm, matrix, null);

            if (mShouldSpheresRotate) {
                step.floatValue += 1.2f;
            }
        } else {
            originalPos.floatValue = 0f;
            step.floatValue = 0f;

            mPathMeasure.getPosTan(0, mPos, mTan);
            matrix.reset();
            matrix.postTranslate(mPos[0] - bm_offsetX, mPos[1] - bm_offsetY);
            canvas.drawBitmap(bm, matrix, null);


            //invalidate();
        }

        positionArray[0] = mPos[0];
        positionArray[1] = mPos[1];

        return positionArray;

    }

    /**
     * 设置正在旋转
     *
     * @param permission
     */
    public void setRotatingPermission(boolean permission) {

        mShouldSpheresRotate = permission;
        if (mShouldSpheresRotate) {
            invalidate();
        }
    }

    /**
     * 移动位置
     *
     * @param position
     * @param pageWidth
     */
    public void translateTheSpheres(float position, int pageWidth) {

        mPosition = position;
        invalidate();


    }

    /**
     * 记载下一个Activity,及退出引导页
     */
    public void startNextScreen() {

        mStartNextScreen = true;
        mShouldSpheresRotate = false;
        invalidate();

    }

    public void setContext(Context context) {

        mContext = context;
    }
}
