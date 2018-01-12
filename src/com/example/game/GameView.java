package com.example.game;

import java.util.ArrayList;
import java.util.Random;

import android.R.color;
import android.R.drawable;
import android.R.raw;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
	enum GameState {
		Menu, playing, Over
	}

	GameState gameState = GameState.Menu;
	public static final int MAX_X = 4;
	public static final int MAX_Y = 4;
	private int twidth;
	private int srcW;
	private int srcH;
	private int offsetX;
	private int offsetY;
	private Paint paint;
	private Tile[][] tiles;
	private Random random;
	private float downX, downY, upX, upY;
	private ArrayList<Tile> zeroList;
	private MainActivity mainActivity;

	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mainActivity = (MainActivity) context;
		srcW = ((MainActivity) context).getWindowManager().getDefaultDisplay()
				.getWidth();
		srcH = ((MainActivity) context).getWindowManager().getDefaultDisplay()
				.getHeight();
		offsetX = 30;
		offsetY = (srcH - srcW) / 2;// 使棋盘位于屏幕中央
		twidth = (srcW - offsetX * 2) / MAX_X;
		paint = new Paint();
		tiles = new Tile[MAX_X][MAX_Y];
		setKeepScreenOn(true);// 保持屏幕常亮
		random = new Random();
		zeroList = new ArrayList<Tile>();

	}

	private void newGame() {
		for (int x = 0; x < MAX_X; x++) {
			for (int y = 0; y < MAX_Y; y++) {
				tiles[x][y] = new Tile(x, y, 0);
			}
		}
		newTile();
		newTile();
	}

	private void newTile() {
		// TODO Auto-generated method stub
		zeroList.removeAll(zeroList);
		for (int x = 0; x < MAX_X; x++) {
			for (int y = 0; y < MAX_Y; y++) {
				if (tiles[x][y].getNumber() == 0) {
					zeroList.add(tiles[x][y]);
				}
			}
		}
		int size = zeroList.size();
		if (size > 0) {
			// 出现4概率是20%，出现2的概率是80%
			if (random.nextInt(5) == 0) {
				zeroList.get(random.nextInt(size)).setNumber(4);
			} else {
				zeroList.get(random.nextInt(size)).setNumber(2);
			}
		} else {
			return;
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		switch (gameState) {
		case Menu:
			menuDraw(canvas);
			break;
		case playing:
			pLayingDraw(canvas);
			break;
		case Over:
			overDraw(canvas);
			break;
		default:
			break;
		}

	}

	private void overDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		int max = 0;
		for (int x = 0; x < MAX_X; x++) {
			for (int y = 0; y < MAX_Y; y++) {
				if (tiles[x][y].getNumber() > max) {
					max = tiles[x][y].getNumber();
				}
			}
		}
		canvas.drawColor(Color.WHITE);
		paint.setColor(Color.BLACK);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(twidth / 2);
		canvas.drawText("Top:" + max, srcW / 2, srcH / 4, paint);
		canvas.drawText("Try again", srcW / 2, srcH / 2, paint);
	}

	private void pLayingDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawColor(Color.LTGRAY);
		// 画主游戏区域
	   
		paint.setColor(0xFF999999);
		paint.setStrokeWidth(10);
		for (int x = 0; x < MAX_X + 1; x++) {
			canvas.drawLine(offsetX + x * twidth, offsetY,
					offsetX + x * twidth, offsetY + twidth * MAX_Y, paint);
		}
		for (int y = 0; y < MAX_Y + 1; y++) {
			canvas.drawLine(offsetX, offsetY + y * twidth, offsetX + MAX_X
					* twidth, offsetY + y * twidth, paint);
		}
		for (int x = 0; x < MAX_X; x++) {
			for (int y = 0; y < MAX_Y; y++) {
				if (tiles[x][y].getNumber() != 0) {
					switch (tiles[x][y].getNumber()) {
					case 2:
						paint.setColor(0xffff6666);
						 
						break;
					case 4:
						paint.setColor(0xffff9900);
						break;
					case 8:
						paint.setColor(0xff99CC33);
						break;
					case 16:
						paint.setColor(0xff33CC99);
						break;
					case 32:
						paint.setColor(0xff666699);
						break;
					case 64:
						paint.setColor(0xff339933);
						break;
					case 128:
						paint.setColor(0xff0099CC);
						break;
					case 256:
						paint.setColor(0xff660033);
						break;
					case 512:
						paint.setColor(0xff006633);
						break;
					case 1024:
						paint.setColor(0xff660099);
						break;
					case 2048:
						paint.setColor(0xffff0033);
						break;
					case 4096:
						paint.setColor(0xff333333);
						break;
					case 8192:
						paint.setColor(0xff000000);
						break;
					default:
						paint.setColor(0xff000000);
						break;
					}
					canvas.drawText(tiles[x][y].getNumber() + "", offsetX + x
							* twidth + twidth / 2, offsetY + y * twidth
							+ twidth * 3 / 4, paint);
				}

			}
		}
	}

	private void menuDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawColor(Color.WHITE);
		paint.setColor(Color.BLACK);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTextSize(twidth / 2);
		canvas.drawText("2048", srcW / 2, srcH / 4, paint);
		canvas.drawText("Touch", srcW / 2, srcH / 2, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (gameState) {
		case Menu:
			menuLogic(event);
			break;
		case playing:
			playingLoic(event);
			break;
		case Over:
			overLogic(event);
			break;
		}
		return true;
	}

	private void menuLogic(MotionEvent event) {
		if (event.getX() > 0) {
			gameState = GameState.playing;
			newGame();
			invalidate();
		}
	}

	private void playingLoic(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downX = event.getX();
			downY = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			upX = event.getX();
			upY = event.getY();
			float moveX = upX - downX;
			float moveY = upY - downY;
			if (Math.abs(moveX) > Math.abs(moveY)) {
				if (moveX > 50) {
					// 右滑
					// 合并
					boolean isMove = false;// 判断是否有tile移动
					for (int y = 0; y < MAX_Y; y++) {
						for (int x = MAX_X - 1; x >= 0; x--) {
							if (tiles[x][y].getNumber() == 0) {
								continue;
							}
							for (int x0 = x - 1; x0 >= 0; x0--) {
								if (tiles[x0][y].getNumber() == 0) {
									continue;
								} else if (tiles[x][y].getNumber() == tiles[x0][y]
										.getNumber()) {
									tiles[x][y].setNumber(tiles[x][y]
											.getNumber() * 2);
									tiles[x0][y].setNumber(0);
									isMove = true;
									break;
								} else {
									break;
								}
							}
						}
					}
					// 移动
					for (int y = 0; y < MAX_Y; y++) {
						for (int x = MAX_X - 1; x >= 0; x--) {
							if (tiles[x][y].getNumber() != 0) {
								continue;
							}
							for (int x0 = x - 1; x0 >= 0; x0--) {
								if (tiles[x0][y].getNumber() == 0) {
									continue;
								} else {
									tiles[x][y].setNumber(tiles[x0][y]
											.getNumber());
									tiles[x0][y].setNumber(0);
									isMove = true;
									break;
								}
							}
						}
					}
					// 有tile移动才添加新的tile
					if (isMove) {
						newTile();
						invalidate();
						judgeOver();
					}
				} else if (moveX < -50) {
					// 左滑
					// 合并
					boolean isMove = false;
					for (int y = 0; y < MAX_Y; y++) {
						for (int x = 0; x < MAX_X; x++) {
							if (tiles[x][y].getNumber() == 0) {
								continue;
							}
							for (int x0 = x + 1; x0 < MAX_X; x0++) {
								if (tiles[x0][y].getNumber() == 0) {
									continue;
								} else if (tiles[x][y].getNumber() == tiles[x0][y]
										.getNumber()) {
									tiles[x][y].setNumber(tiles[x][y]
											.getNumber() * 2);
									tiles[x0][y].setNumber(0);
									isMove = true;
									break;
								} else {
									break;
								}
							}
						}
					}
					// 移动
					for (int y = 0; y < MAX_Y; y++) {
						for (int x = 0; x < MAX_X; x++) {
							if (tiles[x][y].getNumber() != 0) {
								continue;
							}
							for (int x0 = x + 1; x0 < MAX_X; x0++) {
								if (tiles[x0][y].getNumber() == 0) {
									continue;
								} else {
									tiles[x][y].setNumber(tiles[x0][y]
											.getNumber());
									tiles[x0][y].setNumber(0);
									isMove = true;
									break;
								}
							}
						}
					}
					if (isMove) {
						newTile();
						invalidate();
						judgeOver();
					}
				}
			} else {
				if (moveY > 50) {
					// 下滑
					// 合并
					boolean isMove = false;
					for (int x = 0; x < MAX_X; x++) {
						for (int y = MAX_Y - 1; y >= 0; y--) {
							if (tiles[x][y].getNumber() == 0) {
								continue;
							}
							for (int y0 = y - 1; y0 >= 0; y0--) {
								if (tiles[x][y0].getNumber() == 0) {
									continue;
								} else if (tiles[x][y].getNumber() == tiles[x][y0]
										.getNumber()) {
									tiles[x][y].setNumber(tiles[x][y]
											.getNumber() * 2);
									tiles[x][y0].setNumber(0);
									isMove = true;
									break;
								} else {
									break;
								}
							}
						}
					}
					// 移动
					for (int x = 0; x < MAX_X; x++) {
						for (int y = MAX_Y - 1; y >= 0; y--) {
							if (tiles[x][y].getNumber() != 0) {
								continue;
							}
							for (int y0 = y - 1; y0 >= 0; y0--) {
								if (tiles[x][y0].getNumber() == 0) {
									continue;
								} else {
									tiles[x][y].setNumber(tiles[x][y0]
											.getNumber());
									tiles[x][y0].setNumber(0);
									isMove = true;
									break;
								}
							}
						}
					}
					if (isMove) {
						newTile();
						invalidate();
						judgeOver();
					}
				} else if (moveY < -50) {
					// 上滑
					// 合并
					boolean isMove = false;
					for (int x = 0; x < MAX_X; x++) {
						for (int y = 0; y < MAX_Y; y++) {
							if (tiles[x][y].getNumber() == 0) {
								continue;
							}
							for (int y0 = y + 1; y0 < MAX_Y; y0++) {
								if (tiles[x][y0].getNumber() == 0) {
									continue;
								} else if (tiles[x][y].getNumber() == tiles[x][y0]
										.getNumber()) {
									tiles[x][y].setNumber(tiles[x][y]
											.getNumber() * 2);
									tiles[x][y0].setNumber(0);
									isMove = true;
									break;
								} else {
									break;
								}
							}
						}
					}
					// 移动
					for (int x = 0; x < MAX_X; x++) {
						for (int y = 0; y < MAX_Y; y++) {
							if (tiles[x][y].getNumber() != 0) {
								continue;
							}
							for (int y0 = y + 1; y0 < MAX_Y; y0++) {
								if (tiles[x][y0].getNumber() == 0) {
									continue;
								} else {
									tiles[x][y].setNumber(tiles[x][y0]
											.getNumber());
									tiles[x][y0].setNumber(0);
									isMove = true;
									break;
								}
							}
						}
					}
					if (isMove) {
						newTile();
						invalidate();
						judgeOver();
					}
				}
			}
		}
	}

	private void overLogic(MotionEvent event) {
		if (event.getX() > 0) {
			newGame();
			gameState = GameState.playing;
		}
	}

	private void judgeOver() {
		// 右滑
		// 合并
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = MAX_X - 1; x >= 0; x--) {
				if (tiles[x][y].getNumber() == 0) {
					continue;
				}
				for (int x0 = x - 1; x0 >= 0; x0--) {
					if (tiles[x0][y].getNumber() == 0) {
						continue;
					} else if (tiles[x][y].getNumber() == tiles[x0][y]
							.getNumber()) {
						return;
					} else {
						break;
					}
				}
			}
		}
		// 移动
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = MAX_X - 1; x >= 0; x--) {
				if (tiles[x][y].getNumber() != 0) {
					continue;
				}
				for (int x0 = x - 1; x0 >= 0; x0--) {
					if (tiles[x0][y].getNumber() == 0) {
						continue;
					} else {
						return;
					}
				}
			}
		}
		// 左滑
		// 合并
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				if (tiles[x][y].getNumber() == 0) {
					continue;
				}
				for (int x0 = x + 1; x0 < MAX_X; x0++) {
					if (tiles[x0][y].getNumber() == 0) {
						continue;
					} else if (tiles[x][y].getNumber() == tiles[x0][y]
							.getNumber()) {
						return;
					} else {
						break;
					}
				}
			}
		}
		// 移动
		for (int y = 0; y < MAX_Y; y++) {
			for (int x = 0; x < MAX_X; x++) {
				if (tiles[x][y].getNumber() != 0) {
					continue;
				}
				for (int x0 = x + 1; x0 < MAX_X; x0++) {
					if (tiles[x0][y].getNumber() == 0) {
						continue;
					} else {
						return;
					}
				}
			}
		}
		// 下滑
		// 合并
		for (int x = 0; x < MAX_X; x++) {
			for (int y = MAX_Y - 1; y >= 0; y--) {
				if (tiles[x][y].getNumber() == 0) {
					continue;
				}
				for (int y0 = y - 1; y0 >= 0; y0--) {
					if (tiles[x][y0].getNumber() == 0) {
						continue;
					} else if (tiles[x][y].getNumber() == tiles[x][y0]
							.getNumber()) {
						return;
					} else {
						break;
					}
				}
			}
		}
		// 移动
		for (int x = 0; x < MAX_X; x++) {
			for (int y = MAX_Y - 1; y >= 0; y--) {
				if (tiles[x][y].getNumber() != 0) {
					continue;
				}
				for (int y0 = y - 1; y0 >= 0; y0--) {
					if (tiles[x][y0].getNumber() == 0) {
						continue;
					} else {
						return;
					}
				}
			}
		}
		// 上滑
		// 合并
		for (int x = 0; x < MAX_X; x++) {
			for (int y = 0; y < MAX_Y; y++) {
				if (tiles[x][y].getNumber() == 0) {
					continue;
				}
				for (int y0 = y + 1; y0 < MAX_Y; y0++) {
					if (tiles[x][y0].getNumber() == 0) {
						continue;
					} else if (tiles[x][y].getNumber() == tiles[x][y0]
							.getNumber()) {
						return;
					} else {
						break;
					}
				}
			}
		}
		// 移动
		for (int x = 0; x < MAX_X; x++) {
			for (int y = 0; y < MAX_Y; y++) {
				if (tiles[x][y].getNumber() != 0) {
					continue;
				}
				for (int y0 = y + 1; y0 < MAX_Y; y0++) {
					if (tiles[x][y0].getNumber() == 0) {
						continue;
					} else {
						return;
					}
				}
			}
		}
		gameState = GameState.Over;
		invalidate();
	}
}
