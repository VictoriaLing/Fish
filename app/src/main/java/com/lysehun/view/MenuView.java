package com.lysehun.view;

import com.lysehun.constant.ConstantUtil;
import com.lysehun.fish.R;
import com.lysehun.sounds.GameSoundPool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
/*游戏开始前的界面类*/
public class MenuView extends BaseView{
	private int currentFrame;
	private float shumu_x;					// 图片的坐标
	private float shumu_y;
	private float shumu_height;
	private float shumu_width;
	private float text_x;
	private float text_y;
	private float button_x;
	private float button_y;
	private float button_y2;
	private float strwid;
	private float strhei;
	private boolean isBtChange;				// 按钮图片改变的标记
	private boolean isBtChange2;
	private String startGame = "开始游戏";	// 按钮的文字
	private String exitGame = "退出游戏";
	private Bitmap text;					// 文字图片
	private Bitmap button;					// 按钮图片
	private Bitmap button2;					// 按钮图片
	private Bitmap smmove;				// 水母运动的图片
	private Bitmap background;				// 背景图片
	private Rect rect;						// 绘制文字的区域
	public MenuView(Context context,GameSoundPool sounds) {
		super(context,sounds);
		paint.setTextSize(40);
		rect = new Rect();
		thread = new Thread(this);
		sounds.playSound(5, -1);
	}
	// 视图改变的方法
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.surfaceChanged(arg0, arg1, arg2, arg3);
	}
	// 视图创建的方法
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceCreated(arg0);
		initBitmap();
		if(thread.isAlive()){
			thread.start();
		}
		else{
			thread = new Thread(this);
			thread.start();
		}
	}
	// 视图销毁的方法
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceDestroyed(arg0);
		release();
	}
	// 响应触屏事件的方法
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& event.getPointerCount() == 1) {
			float x = event.getX();
			float y = event.getY();
			//判断第一个按钮是否被按下
			if (x > button_x && x < button_x + button.getWidth()
					&& y > button_y && y < button_y + button.getHeight()) {
				sounds.playSound(7, 0);
				isBtChange = true;
				drawSelf();
				mainActivity.getHandler().sendEmptyMessage(ConstantUtil.GAME_RUN_VIEW);
			}
			//判断第二个按钮是否被按下
			else if (x > button_x && x < button_x + button.getWidth()
					&& y > button_y2 && y < button_y2 + button.getHeight()) {
				sounds.playSound(7, 0);
				isBtChange2 = true;
				drawSelf();
				mainActivity.getHandler().sendEmptyMessage(ConstantUtil.GAME_END);
			}
			return true;
		}
		//响应屏幕单点移动的消息
		else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			float x = event.getX();
			float y = event.getY();
			if (x > button_x && x < button_x + button.getWidth()
					&& y > button_y && y < button_y + button.getHeight()) {
				isBtChange = true;
			} else {
				isBtChange = false;
			}
			if (x > button_x && x < button_x + button.getWidth()
					&& y > button_y2 && y < button_y2 + button.getHeight()) {
				isBtChange2 = true;
			} else {
				isBtChange2 = false;
			}
			return true;
		}
		//响应手指离开屏幕的消息
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			isBtChange = false;
			isBtChange2 = false;
			return true;
		}
		return false;
	}
	// 初始化图片资源方法
	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub
		background = BitmapFactory.decodeResource(getResources(),R.drawable.back0);
		text = BitmapFactory.decodeResource(getResources(), R.drawable.buble2);
		smmove = BitmapFactory.decodeResource(getResources(), R.drawable.buble1);
		button = BitmapFactory.decodeResource(getResources(), R.drawable.button);
		button2 = BitmapFactory.decodeResource(getResources(),R.drawable.button);
		scalex = screen_width / background.getWidth();
		scaley = screen_height / background.getHeight();
		text_x = screen_width / 2 - text.getWidth() / 2;
		text_y = screen_height / 2 - text.getHeight();
		shumu_x = screen_width / 2 - smmove.getWidth() / 2;
		shumu_height = smmove.getHeight();
		shumu_width = smmove.getWidth()/6;
		shumu_y = text_y - shumu_height - 20;
		button_x = screen_width / 2 - button.getWidth() / 2;
		button_y = screen_height / 2 + button.getHeight()/2;
		button_y2 = button_y + button.getHeight() + 40;
		// 返回包围整个字符串的最小的一个Rect区域
		paint.getTextBounds(startGame, 0, startGame.length(), rect);
		strwid = rect.width();
		strhei = rect.height();
	}
	// 释放图片资源的方法
	@Override
	public void release() {
		// TODO Auto-generated method stub
		if (!text.isRecycled()) {
			text.recycle();
		}
		if (!button.isRecycled()) {
			button.recycle();
		}
		if (!button2.isRecycled()) {
			button2.recycle();
		}
		if (!smmove.isRecycled()) {
			smmove.recycle();
		}
		if (!background.isRecycled()) {
			background.recycle();
		}
	}
	// 绘图方法
	@Override
	public void drawSelf() {
		// TODO Auto-generated method stub
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.BLACK); 						// 绘制背景色
			canvas.save();
			canvas.scale(scalex, scaley, 0, 0);					// 计算背景图片与屏幕的比例
			canvas.drawBitmap(background, 0, 0, paint); 		// 绘制背景图
			canvas.restore();
			canvas.drawBitmap(text, text_x, text_y, paint);		// 绘制文字图片

			//当手指滑过按钮时变换图片
			if (isBtChange) {
				canvas.drawBitmap(button2, button_x, button_y, paint);

			}
			else {
				canvas.drawBitmap(button, button_x, button_y, paint);
			}
			if (isBtChange2) {
				canvas.drawBitmap(button2, button_x, button_y2, paint);
			}
			else {
				canvas.drawBitmap(button, button_x, button_y2, paint);
			}
			
			//开始游戏的按钮												
			canvas.drawText(startGame, screen_width / 2 - strwid / 2, button_y
					+ button.getHeight() / 2 - strhei/2+10 , paint);
			//退出游戏的按钮												
			canvas.drawText(exitGame, screen_width / 2 - strwid / 2, button_y2
					+ button.getHeight() / 2 + strhei / 2, paint);
			//水母运动的动画
			canvas.save();
			canvas.clipRect(shumu_x, shumu_y, shumu_x + shumu_width, shumu_y + shumu_height);
			canvas.drawBitmap(smmove, shumu_x-currentFrame * shumu_width, shumu_y,paint);
		
			currentFrame++;
			if (currentFrame >= 6) {
				currentFrame = 0;
			}

			canvas.restore();
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}
	// 线程运行的方法
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (threadFlag) {
			long startTime = System.currentTimeMillis();
			//sounds.playSound(5, -1);
			drawSelf();
			long endTime = System.currentTimeMillis();
			try {							//400
				if (endTime - startTime < 400)
					//400
					Thread.sleep(400 - (endTime - startTime));
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
	}
}
