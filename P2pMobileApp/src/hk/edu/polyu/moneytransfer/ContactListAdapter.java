package hk.edu.polyu.moneytransfer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class ContactListAdapter extends BaseAdapter {
	
	String [] result;
    Context context;
    int [] imageId;
    int position;
    
    private static LayoutInflater inflater=null;
    
    public ContactListAdapter(MainActivity mainActivity) {
    	this(mainActivity, null, null);
	}

    public ContactListAdapter(MainActivity mainActivity, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        
        context=mainActivity;
        setList(prgmNameList, prgmImages);
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
	public void setList(String[] prgmNameList, int[] prgmImages){
		result=prgmNameList;
		imageId=prgmImages;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return result.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		this.position = position;
		return this.position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		this.position = position;
		return this.position;
	}
	
	public class Holder
    {
        TextView tv;
        ImageView img;
    }
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder=new Holder();
        View rowView;       
             rowView = inflater.inflate(R.layout.contact_list_item, null);
             
             holder.tv=(TextView) rowView.findViewById(R.id.textView1);
             holder.img=(ImageView) rowView.findViewById(R.id.imageView1);   
             
         holder.tv.setText(result[position]);
         holder.img.setImageResource(imageId[position]);
         holder.img.setOnClickListener(new OnClickListener() {            
             @Override
             public void onClick(View v) {
                 // TODO Auto-generated method stub
                 Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
             }
         });   
        return rowView;
	}


}
