package hk.edu.polyu.P2pMobileApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryListAdapter extends BaseAdapter{
	String [] result;
    Context context;
    int position;
    
    private static LayoutInflater inflater=null;
    
    public HistoryListAdapter(MainActivity mainActivity) {
    	this(mainActivity, null);
	}

    public HistoryListAdapter(MainActivity mainActivity, String[] prgmNameList) {
        // TODO Auto-generated constructor stub
        
        context=mainActivity;
        setList(prgmNameList);
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
	public void setList(String[] prgmNameList){
		result=prgmNameList;
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
        TextView tv1, tv2, tv3, tv4;
    }
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder=new Holder();
        View rowView;       
             rowView = inflater.inflate(R.layout.history_list_item, null);
             
             holder.tv1=(TextView) rowView.findViewById(R.id.textViewHistoryField1);
             holder.tv2=(TextView) rowView.findViewById(R.id.textViewHistoryField2);
             holder.tv3=(TextView) rowView.findViewById(R.id.textViewHistoryField3);
             holder.tv4=(TextView) rowView.findViewById(R.id.textViewHistoryField4);
             
             
         holder.tv1.setText(result[position]);
         holder.tv2.setText(result[position]);
         holder.tv3.setText(result[position]);
         holder.tv4.setText(result[position]);

         rowView.setOnClickListener(new OnClickListener() {            
             @Override
             public void onClick(View v) {
                 // TODO Auto-generated method stub
                 Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
             }
         });   
        return rowView;
	}

}
