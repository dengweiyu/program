package com.example.game;

public class Tile {
private int number;
private int x,y;
 public Tile(int x,int y,int number){
	 this.setX(x);
	 this.setY(y);
	 this.setNumber(number);
 }
public int getY() {
	return y;
}
public void setY(int y) {
	this.y = y;
}
public int getX() {
	return x;
}
public void setX(int x) {
	this.x = x;
}
public int getNumber() {
	return number;
}
public void setNumber(int number) {
	this.number = number;
}
 
}
