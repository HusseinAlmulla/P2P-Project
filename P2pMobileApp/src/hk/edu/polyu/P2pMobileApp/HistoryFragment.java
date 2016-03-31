package hk.edu.polyu.P2pMobileApp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HistoryFragment extends Fragment {
	ListView historyListView;
	MainActivity mainActivity;
	
	public String [] prgmNameList={
			"Let Us C",
			"c++",
			"JAVA",
			"Jsp",
			"Microsoft .Net",
			"Android",
			"PHP",
			"Jquery",
			"JavaScript"
			};
	
	public HistoryFragment(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_history, container, false);
		final HistoryListAdapter historyListAdapter = new HistoryListAdapter(mainActivity);
		historyListAdapter.setList(prgmNameList);
		
		historyListView = (ListView) rootView.findViewById(R.id.historyListView);
		historyListView.setAdapter(historyListAdapter);
		historyListAdapter.notifyDataSetChanged();
		
		return rootView;
	}

}
