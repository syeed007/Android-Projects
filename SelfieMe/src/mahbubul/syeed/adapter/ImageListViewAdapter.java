package mahbubul.syeed.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mahbubul.syeed.selfieme.R;
import mahbubul.syeed.selfieme.R.drawable;
import mahbubul.syeed.selfieme.R.id;
import mahbubul.syeed.selfieme.R.layout;
import mahbubul.syeed.support.ConstantVariables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ImageListViewAdapter extends BaseAdapter {

	private final List<ImageDescription> mImages = new ArrayList<ImageDescription>();
	private final Context mContext;

	private static File imgFile;
	private static Bitmap mBitmap;
	private static ImageViewHolder mImageHolder;
	
	/**
	 * constructor
	 * 
	 * @param context
	 */
	public ImageListViewAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	/**
	 * add the newly taken image notify the changes to update the list.
	 * 
	 * @param item
	 */
	public void add(ImageDescription image) {

		//Toast.makeText(mContext, "!! In Add method adpater class",
		//		Toast.LENGTH_SHORT).show();
		mImages.add(image);
		notifyDataSetChanged();

	}

	/**
	 * 
	 */
	public void clear() {

		mImages.clear();
		notifyDataSetChanged();

	}

	/**
	 * ----------------------------------- overwridden methods starts here !
	 * -----------------------------------
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImages.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mImages.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Get the current image description
		final ImageDescription mImageDesc = (ImageDescription) getItem(position);

		// Inflate the View for this image description
		// from list_item_layout.xml
		RelativeLayout itemLayout = null;

		if (null == convertView) {
			itemLayout = (RelativeLayout) LayoutInflater.from(mContext)
					.inflate(R.layout.list_item_layout, parent, false);
		} else {
			itemLayout = (RelativeLayout) convertView;
		}

		// Fill in specific ImageDescription data
		// Remember that the data that goes in this View
		// corresponds to the user interface elements defined
		// in the layout file

		mImageHolder = new ImageViewHolder();

		// Display Image title
		mImageHolder.holderImageTitle = (TextView) itemLayout
				.findViewById(R.id.title);
		mImageHolder.holderImageTitle.setText(mImageDesc.getDescription());

		// Display image date and time
		mImageHolder.holderDateView = (TextView) itemLayout
				.findViewById(R.id.date_time);
		mImageHolder.holderDateView.setText(mImageDesc.getDateTime());

		// display the image as a thumbnil.
		mImageHolder.holderImageView = (ImageView) itemLayout
				.findViewById(R.id.image);

		/**
		 * TODO!! display the original image thumbnil here ......
		 */

		imgFile = new File(mImageDesc.getImagePath()); // get image absolute
														// path

		if (imgFile.exists()) { // if the file exists

			scaleImageAndDisplay();
			
			// get the absolute path of the image
			//mBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

			// set the bitmap to the view
			//mImageHolder.holderImageView.setImageBitmap(mBitmap);

		} else { //if the image is not found
			
			//show the application logo
			mImageHolder.holderImageView.setImageResource(R.drawable.app_icon);
		}

		//Toast.makeText(mContext, "!! returning from view controller",
		//		Toast.LENGTH_SHORT).show();
		
		// Return the View you just created
		return itemLayout;
	}

	
	private void scaleImageAndDisplay() {
		
		// Get the dimensions of the View
	    //int targetW = mImageHolder.holderImageView.getWidth();
	    //int targetH = mImageHolder.holderImageView.getHeight();
		int targetW = ConstantVariables.thumbmilWidth;
	    int targetH = ConstantVariables.thumbmilHeight;
	    
	    
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    System.out.println("View: " + targetH + "-" + targetW + " Photo: " + photoW + "-" + photoH);
	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
	    mImageHolder.holderImageView.setImageBitmap(bitmap);
	}
	
}
