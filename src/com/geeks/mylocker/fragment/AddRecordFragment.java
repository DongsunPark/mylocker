package com.geeks.mylocker.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geeks.mylocker.R;
import com.geeks.mylocker.dao.Group;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link BlankFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link AddRecordFragment#newInstance} factory method to create an instance of
 * this fragment.
 *
 */
public class AddRecordFragment extends Fragment {
	
	private static final String ARG_GROUP = "ARG_GROUP"; 

	private Group mGroup;

	private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 */
	public static AddRecordFragment newInstance(Group group) {
		AddRecordFragment fragment = new AddRecordFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARG_GROUP, group);
		fragment.setArguments(args);
		return fragment;
	}

	public AddRecordFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mGroup = (Group) getArguments().getSerializable(ARG_GROUP);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_add_record, container, false);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
}
