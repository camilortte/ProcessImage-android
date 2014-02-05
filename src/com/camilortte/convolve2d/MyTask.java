package com.camilortte.convolve2d;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import cn.Ragnarok.BitmapFilter;

public class MyTask extends AsyncTask<Void, Boolean, Bitmap> {

	private ProgressDialog dialog;
	private MainActivity ma;
	private int style;
	private  Bitmap originalImageScaled;
	public static final int LOAD_FILTERS=0;
	public static final int LOAD_IMAGES_FROM_SD=1;	
	private int currentOpcion;
	
	public MyTask(MainActivity ma) {
		this.ma = ma;
		dialog = new ProgressDialog(ma);		
	}	
	
	public int getStyle() {
		return style;
	}
	
	public void setStyle(int style) {
		this.style = style;
	}
	
	public void setOperation(int operation){
		this.currentOpcion=operation;
	}

	public void onPreExecute() {
		super.onPreExecute();	
	    dialog = new ProgressDialog(ma);	    
	    dialog.setMessage(ma.getString(R.string.wait)+"...");
	    dialog.setCancelable(false);
	    dialog.show();  
	}

	@Override
	protected Bitmap doInBackground(Void... arg0) {		
		switch (currentOpcion) {
		case LOAD_FILTERS:
			return loadFilters();			
			
		case LOAD_IMAGES_FROM_SD:
			loadFromSD();
			return null;
			
		default:
			break;
		}
		return originalImageScaled;
		
	}

	public void onPostExecute(Bitmap unused) {		
		
		switch (currentOpcion) {
		case LOAD_FILTERS:
			ma.setMainImage(unused);			
			break;
		case LOAD_IMAGES_FROM_SD:
			ma.updateGUI();
			break;
		default:
			break;
		}
		
		dialog.dismiss();
	}
	
	private Bitmap loadFilters(){
		this.originalImageScaled=ma.getOriginalImagenScaled();
		Bitmap myBitmap=BitmapFilter.changeStyle(originalImageScaled,style);		
		return myBitmap;
	}
	
	private void loadFromSD(){
		ma.loadImageFormSD();
	}

}