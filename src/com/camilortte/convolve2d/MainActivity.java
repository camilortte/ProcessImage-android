package com.camilortte.convolve2d;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.Ragnarok.BitmapFilter;

public class MainActivity extends FragmentActivity {

	private Bitmap bmp;
	private ImageView imgView;
	private LinearLayout containerLayoutFrame;
	private BitmapDrawable abmp;
	private Bitmap myBitmap;
	private OnClicListenerImagesFilter onClickListenerImagesFilter;	
	private int filtersInt[];
	private ImageView imgFilters[];
	private LayoutParams lyparams = new LayoutParams();
	private Bitmap originalImageSacled;
	private MyTask my;
	private Uri selectedImage;
	private Cursor cursor;
	private static final int GET_IMAGE_SD_REQUEST=1;
	private static final int NUMBERS_OF_FILTERS=18;
	private final String path = Environment.getExternalStorageDirectory().toString()+"/"+
							android.os.Environment.DIRECTORY_DCIM+"/Convolve2D";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.imgView = (ImageView) findViewById(R.id.imageView1);
		containerLayoutFrame = (LinearLayout) findViewById(R.id.layout);		
		filtersInt = new int[NUMBERS_OF_FILTERS];
		imgFilters = new ImageView[filtersInt.length];		
		lyparams.height = LayoutParams.MATCH_PARENT;
		lyparams.width = LayoutParams.WRAP_CONTENT;
		
		onClickListenerImagesFilter=new OnClicListenerImagesFilter();
		
		filtersInt[0]=BitmapFilter.GRAY_STYLE;		
		filtersInt[1]=BitmapFilter.BLOCK_STYLE;		
		filtersInt[2]=BitmapFilter.BLUR_STYLE;		
		filtersInt[3]=BitmapFilter.GAUSSIAN_BLUR_STYLE;		
		filtersInt[4]=BitmapFilter.HDR_STYLE;		
		filtersInt[5]=BitmapFilter.INVERT_STYLE;		
		filtersInt[6]=BitmapFilter.LIGHT_STYLE;		
		filtersInt[7]=BitmapFilter.LOMO_STYLE;		
		filtersInt[8]=BitmapFilter.NEON_STYLE;		
		filtersInt[9]=BitmapFilter.OIL_STYLE;		
		filtersInt[10]=BitmapFilter.OLD_STYLE;		
		filtersInt[11]=BitmapFilter.PIXELATE_STYLE;		
		filtersInt[12]=BitmapFilter.RELIEF_STYLE;		
		filtersInt[13]=BitmapFilter.SHARPEN_STYLE;		
		filtersInt[14]=BitmapFilter.SKETCH_STYLE;		
		filtersInt[15]=BitmapFilter.SOFT_GLOW_STYLE;		
		filtersInt[16]=BitmapFilter.TOTAL_FILTER_NUM;		
		filtersInt[17]=BitmapFilter.TV_STYLE;
		
		
		// we convert the image on imgView to a bitmap
 		abmp = (BitmapDrawable) imgView.getDrawable();
 		bmp = abmp.getBitmap();
 		originalImageSacled=bmp;
 		myBitmap=bmp; 		
 		
 		sacleMainImage();
 		loadFilters();
	}
	
	private class OnClicListenerImagesFilter implements OnClickListener{		
		//When click a small filter_image
		@Override
		public void onClick(View v) {			
			final int id=v.getId();				
        	for(int i=0;i<filtersInt.length;i++){
				 if(imgFilters[i].getId()==id){								 
					 //myBitmap=BitmapFilter.changeStyle(originalImageSacled,filtersInt[i]);
					 //imgView.setImageBitmap(myBitmap);
					 my=new MyTask(MainActivity.this);
					 my.setOperation(MyTask.LOAD_FILTERS);
					 my.setStyle(filtersInt[i]);
					 my.execute();					 
					 break;
				 }
			 }	
		}
		
	}
	
	public Bitmap getOriginalImagenScaled(){
		return this.originalImageSacled;
	}
	
	//Scale the original image for minimize resources
	public void setMainImage(Bitmap bitmap){
		myBitmap=bitmap;
		imgView.setImageBitmap(bitmap);	
	}

	public void sacleMainImage() {
		int newWidth = 150;
		int newHeight = 100;
		sacleMainImage(newWidth, newHeight);
	}
	
	public void sacleMainImage(int newWidth,int newHeight ) {
		float scaleWidth = ((float) newWidth) / bmp.getWidth();
		float scaleHeight = ((float) newHeight) / bmp.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		
		bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
				matrix, true);
		containerLayoutFrame.removeAllViews();
	}

	public void addImagenToFrame(ImageView imageView) {		
		containerLayoutFrame.addView(imageView);
	}

	
	//load all Images filters and put in a mainView
	public void loadFilters() {		
		for (int i = 0; i < imgFilters.length; i++) {
			imgFilters[i] = new ImageView(this);
			imgFilters[i].setImageBitmap(BitmapFilter.changeStyle(bmp, filtersInt[i]));
			imgFilters[i].setScaleType(ScaleType.FIT_START);
			imgFilters[i].setLayoutParams(lyparams);
			imgFilters[i].setOnClickListener(this.onClickListenerImagesFilter);
			imgFilters[i].setId(i);
			addImagenToFrame(imgFilters[i]);
		}		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		//action load imagen from sd
		case R.id.action_open_image:			
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, GET_IMAGE_SD_REQUEST);
			return true;
		//action settings
		case R.id.action_about:
			//Intent i2 = new Intent(this, MainActivity22.class);
			//startActivity(i2);
			Builder dialog = new AlertDialog.Builder(MainActivity.this);
			dialog.setTitle(MainActivity.this.getResources().getString(R.string.app_name));  
			dialog.setMessage(getString(R.string.about_this));  
			dialog.setIcon(R.drawable.ic_launcher);  
			dialog.setPositiveButton("OK", null);
			dialog.show();  
			return true;
		case R.id.action_save_image:
			saveImageAs();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	//Load a image select by user from sd
	public void loadImageFormSD(){		
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		cursor = getContentResolver().query(selectedImage,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);			
		try{
			myBitmap = BitmapFactory.decodeFile(picturePath);						
		}catch(OutOfMemoryError e){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			myBitmap = BitmapFactory.decodeFile(picturePath,options);	
		}			
	}
	
	public void updateGUI(){
		imgView.setImageBitmap(myBitmap);
		originalImageSacled=myBitmap;
		if (bmp != null) {
			bmp.recycle();
		}
		abmp = (BitmapDrawable) imgView.getDrawable();
		bmp = abmp.getBitmap();
		cursor.close();			
		//When the load a new images we applicate the filters
		sacleMainImage();
		loadFilters();		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//When the request of get imagen to sd result ok
		if (requestCode == GET_IMAGE_SD_REQUEST && resultCode == RESULT_OK && null != data) {
			selectedImage = data.getData();
			//loadImageFormSD();
			 my=new MyTask(MainActivity.this);
			 my.setOperation(MyTask.LOAD_IMAGES_FROM_SD);
			 my.execute();
		}
		
	}
	
	public void saveImageAs() {
		
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	Calendar cal = Calendar.getInstance();
		     	    File myDir = new File(path);//"/100ANDRO");		     	    
		     	    myDir.mkdirs();
		     	    String fname = "Image-"+ String.valueOf(cal.getTimeInMillis()) +".jpg";
		     	    File file = new File (myDir, fname);
		     	    if (file.exists ()) file.delete (); 
		     	    try {
		     	           FileOutputStream out = new FileOutputStream(file);
		     	           myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
		     	           out.flush();
		     	           out.close();
		     	           Toast.makeText(MainActivity.this, getString(R.string.save_image_success_toast)+path, 5000).show();

		     	    } catch (Exception e) {
		     	           e.printStackTrace();
		     	    }
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		        	Toast.makeText(MainActivity.this, getString(R.string.save_image_cancel_toast), 4000).show();
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.sure_save_image).setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
		
	   
	}

}
