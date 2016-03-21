package hk.edu.polyu.moneytransfer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileFragment extends Fragment {

	MainActivity mainActivity;
	public ProfileFragment(MainActivity mainActivity) {
		// TODO Auto-generated constructor stub
		this.mainActivity = mainActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		return rootView;
	}

}
