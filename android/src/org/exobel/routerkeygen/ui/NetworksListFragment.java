package org.exobel.routerkeygen.ui;

import java.util.List;

import org.exobel.routerkeygen.WiFiScanReceiver.OnScanListener;
import org.exobel.routerkeygen.algorithms.Keygen;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class NetworksListFragment extends ListFragment implements OnScanListener {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private OnItemSelectionListener mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    
	private List<Keygen> networksFound;

    public interface OnItemSelectionListener {

        public void onItemSelected(Keygen id);
    }

    private static OnItemSelectionListener sDummyCallbacks = new OnItemSelectionListener() {
        public void onItemSelected(Keygen id) {
        }
    };

    
    public NetworksListFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && savedInstanceState
                .containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof OnItemSelectionListener)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        
        mCallbacks = (OnItemSelectionListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onItemSelected(networksFound.get(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
    
    

	public void onScanFinished(List<Keygen> networks) {
        this.networksFound = networks;
        if ( getActivity() != null )
        	setListAdapter(new WifiListAdapter(this.networksFound, getActivity()));
	}

}