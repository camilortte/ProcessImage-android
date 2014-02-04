package com.camilortte.convolve2d;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
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
	private static final int GET_IMAGE_SD_REQUEST=1;
	private static final int NUMBERS_OF_FILTERS=18;
	private static final int WIDTH_OF_IMAGES_FILTERS=250;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.imgView = (ImageView) findViewById(R.id.imageView1);
		containerLayoutFrame = (LinearLayout) findViewById(R.id.layout);		
		filtersInt = new int[NUMBERS_OF_FILTERS];
		imgFilters = new ImageView[filtersInt.length];
		lyparams.width = WIDTH_OF_IMAGES_FILTERS;
		lyparams.height = LayoutParams.MATCH_PARENT;
		onClickListenerImagesFilter=new OnClicListenerImagesFilter();
		

		// we convert the image on imgView to a bitmap
 		abmp = (BitmapDrawable) imgView.getDrawable();
 		bmp = abmp.getBitmap();
 		originalImageSacled=bmp;
 		
 		sacleMainImage();
 		loadFilters();
	}
	
	private class OnClicListenerImagesFilter implements OnClickListener{

		@Override
		public void onClick(View v) {
			Log.i("COSA","CLICK");
			final int id=v.getId();		
			
        	for(int i=0;i<filtersInt.length;i++){
				 Log.i("COSA","ID del evento="+id+" ID del objeto"+imgFilters[i].getId());
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
		imgView.setImageBitmap(bitmap);	
	}

	public void sacleMainImage() {
		int newWidth = 240;
		int newHeight = 180;
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

		// Creación de todos los filtros	
		for (int i = 0; i < imgFilters.length; i++) {
			Log.i("COSA", "EN LA ITERANCON" + i);
			imgFilters[i] = new ImageView(this);
			imgFilters[i].setImageBitmap(BitmapFilter.changeStyle(bmp, filtersInt[i]));
			imgFilters[i].setScaleType(ScaleType.FIT_XY);
			imgFilters[i].setLayoutParams(lyparams);
			imgFilters[i].setOnClickListener(this.onClickListenerImagesFilter);
			imgFilters[i].setId(i);
			addImagenToFrame(imgFilters[i]);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main2, menu);
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
		case R.id.action_settings:
			Intent i2 = new Intent(this, MainActivity22.class);
			startActivity(i2);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void loadImageFormSD(){		
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage,
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
			loadImageFormSD();
			 /*my=new MyTask(MainActivity.this);
			 my.setOperation(MyTask.LOAD_IMAGES_FROM_SD);
			 my.execute();*/
		}
	}

}
