package mahbubul.syeed.myToDos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;

import mahbubul.syeed.myToDos.ToDoItem.Priority;
import mahbubul.syeed.myToDos.ToDoItem.Status;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import course.labs.todomanager.R;

public class ToDoManagerActivity extends ListActivity {

	private static final int ADD_TODO_ITEM_REQUEST = 0;
	private static final String FILE_NAME = "myToDos.txt";
	private static final String TAG = "Lab-UserInterface";

	// IDs for menu items
	private static final int MENU_DELETE = Menu.FIRST;
	private static final int MENU_DUMP = Menu.FIRST + 1;

	ToDoListAdapter mAdapter;
	TextView footerView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a new TodoListAdapter for this ListActivity's ListView
		mAdapter = new ToDoListAdapter(getApplicationContext());

		// Put divider between ToDoItems and FooterView
		getListView().setFooterDividersEnabled(true);

		// TODO - Inflate footerView for footer_view.xml file
		footerView = null;
		footerView = (TextView) getLayoutInflater().inflate(
				R.layout.footer_view, null);

		// NOTE: You can remove this block once you've implemented the
		// assignment
		// if (null == footerView) {
		// return;
		// }
		// TODO - Add footerView to ListView
		getListView().addFooterView(footerView);

		this.addListenerToFooterView();

		// TODO - Attach the adapter to this ListActivity's ListView
		getListView().setAdapter(mAdapter);
		
		//register for context menu
				registerForContextMenu(getListView());
	}

	private void addListenerToFooterView() {
		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.i(TAG, "Entered footerView.OnClickListener.onClick()");

				// TODO - Implement OnClick().
				Intent mAddToDo = new Intent(ToDoManagerActivity.this,
						AddToDoActivity.class);
				startActivityForResult(mAddToDo, ADD_TODO_ITEM_REQUEST);

			}
		});
		
		

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "Entered onActivityResult()");

		// TODO - Check result code and request code
		// if user submitted a new ToDoItem
		// Create a new ToDoItem from the data Intent
		// and then add it to the adapter
		switch (requestCode) {
		case ADD_TODO_ITEM_REQUEST:
			if(resultCode == RESULT_OK){
				//ToDoItem mToDo = new ToDoItem(data);
				mAdapter.add(new ToDoItem(data));
			}
		}
	}

	// Do not modify below here

	@Override
	public void onResume() {
		super.onResume();

		// Load saved ToDoItems, if necessary

		if (mAdapter.getCount() == 0)
			loadItems();
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Save ToDoItems

		saveItems();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
		menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DELETE:
			mAdapter.clear();
			return true;
		case MENU_DUMP:
			dump();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	
	 @Override
	    public void onCreateContextMenu(ContextMenu menu, View v,
	    		ContextMenuInfo menuInfo) {
	    	// TODO Auto-generated method stub
	    	super.onCreateContextMenu(menu, v, menuInfo);
	    	
	    	menu.add("Delete all");
	    	
	    	//MenuInflater contextMenu = getMenuInflater();
	    	//contextMenu.inflate(R.menu.menu_context, menu);
	    	
	    }
	    
	 /*
	    @Override
	    public boolean onContextItemSelected(MenuItem item) {
	    	// TODO Auto-generated method stub
	    	int id = item.getItemId();
	    	switch(id){
	    	case R.id.rajit_me:
	    		Toast.makeText(ToDoManagerActivity.this, "context rajit", Toast.LENGTH_SHORT).show();
	    		break;
	    	case R.id.mou_me:
	        	Toast.makeText(ToDoManagerActivity.this, "context mou", Toast.LENGTH_SHORT).show();
	        	break;
	        case R.id.suha_me:
	        	Toast.makeText(ToDoManagerActivity.this, "context suha", Toast.LENGTH_SHORT).show();
	        	break;
	        case R.id.samah_me:
	        	Toast.makeText(ToDoManagerActivity.this, "context samah", Toast.LENGTH_SHORT).show();
	        	break;
	    		
	    	}
	    	return super.onContextItemSelected(item);
	    }
	*/
	
	
	
	private void dump() {

		for (int i = 0; i < mAdapter.getCount(); i++) {
			String data = ((ToDoItem) mAdapter.getItem(i)).toLog();
			Log.i(TAG,
					"Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ","));
		}

	}

	// Load stored ToDoItems
	private void loadItems() {
		BufferedReader reader = null;
		try {
			FileInputStream fis = openFileInput(FILE_NAME);
			reader = new BufferedReader(new InputStreamReader(fis));

			String title = null;
			String priority = null;
			String status = null;
			Date date = null;

			while (null != (title = reader.readLine())) {
				priority = reader.readLine();
				status = reader.readLine();
				date = ToDoItem.FORMAT.parse(reader.readLine());
				mAdapter.add(new ToDoItem(title, Priority.valueOf(priority),
						Status.valueOf(status), date));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Save ToDoItems to file
	private void saveItems() {
		PrintWriter writer = null;
		try {
			FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					fos)));

			for (int idx = 0; idx < mAdapter.getCount(); idx++) {

				writer.println(mAdapter.getItem(idx));

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
	}
}