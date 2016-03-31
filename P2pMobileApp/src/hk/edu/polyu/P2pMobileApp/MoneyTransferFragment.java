package hk.edu.polyu.P2pMobileApp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MoneyTransferFragment extends Fragment implements OnItemSelectedListener, OnClickListener{

	MainActivity mainActivity;
	EditText editTextMoneyTransferAmount;
	Spinner spinnerFirendList;
	Button buttonMoneyTransfer;
	
	public MoneyTransferFragment(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_money_transfer, container, false);
		spinnerFirendList = (Spinner) rootView.findViewById(R.id.spinnerFirendList);
		editTextMoneyTransferAmount = (EditText) rootView.findViewById(R.id.editTextMoneyTransferAmount);
		buttonMoneyTransfer = (Button) rootView.findViewById(R.id.buttonMoneyTransfer);
		
		buttonMoneyTransfer.setOnClickListener(this);
		spinnerFirendList.setOnItemSelectedListener(this);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter(mainActivity, android.R.layout.simple_spinner_item, new String[]{"friend 1","friend 2","friend 3"});

		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinnerFirendList.setAdapter(adapter);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(mainActivity, "You Clicked "+v.getId(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(mainActivity, "Selected "+parent.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		Toast.makeText(mainActivity, "onNothingSelected", Toast.LENGTH_LONG).show();
	}

}
