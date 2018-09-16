package com.fyp.mykarachi;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdatesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdatesFragment extends androidx.fragment.app.Fragment {

    private static final String TAG = UpdatesFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    private RecyclerView pendingUpdates, verifiedUpdates;

    private DatabaseReference myRefPendingUpdates, myRefVerifiedUpdates;
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    private OnFragmentInteractionListener mListener;

    public UpdatesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * <p>
     * //     * @param param1 Parameter 1.
     * ////     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment UpdatesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdatesFragment newInstance() {
        UpdatesFragment fragment = new UpdatesFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myRefPendingUpdates = FirebaseDatabase.getInstance().getReference().child("Pending Updates");
        myRefVerifiedUpdates = FirebaseDatabase.getInstance().getReference().child("Verified Updates");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_updates, container, false);
//        expandableListView = view.findViewById(R.id.expandableListView);
//        expandableListDetail = ExpandableListDataPump.getData();
//        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
//        Toast.makeText(getContext(), TAG + "Expandable List: Title Length: " + expandableListTitle.size(), Toast.LENGTH_SHORT).show();
//        expandableListAdapter = new CustomExpandableListAdapter(getContext(), expandableListTitle, expandableListDetail);
//        expandableListView.setAdapter(expandableListAdapter);

        pendingUpdates = view.findViewById(R.id.pending_news_recycler_view);
        verifiedUpdates = view.findViewById(R.id.verified_news_recycler_view);

        setUpPendingNewsAdapter(pendingUpdates);
        setUpVerifiedNewsAdapter(verifiedUpdates);

//        RecyclerView.LayoutManager firstLayoutManager = new LinearLayoutManager(getActivity());
//        RecyclerView.LayoutManager secondLayoutManager = new LinearLayoutManager(getActivity());
//        pendingUpdates.setLayoutManager(firstLayoutManager);
//        verifiedUpdates.setLayoutManager(secondLayoutManager);
//
//        String[] data = {"element1", "element2", "element3", "element4", "element5", "element6", "element7", "element8",
//                "element1", "element2", "element3", "element4", "element5", "element6", "element7", "element8",
//                "element1", "element2", "element3", "element4", "element5", "element6", "element7", "element8"};
//
//        pendingUpdates.setAdapter(new TestRecyclerViewAdapter(getActivity(), data));
//        verifiedUpdates.setAdapter(new TestRecyclerViewAdapter(getActivity(), data));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
        Toast.makeText(getContext(), TAG + "onButtonPressed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setUpPendingNewsAdapter(RecyclerView mxRecyclerView) {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<News, PendingNewsViewHolder>
                (News.class, R.layout.pending_news_list_item, PendingNewsViewHolder.class, myRefPendingUpdates) {

            @Override
            protected void populateViewHolder(PendingNewsViewHolder viewHolder, News model, int position) {
                Log.d(TAG, "populateViewHolder: ");
                viewHolder.bindNews(model);
            }
        };

        mxRecyclerView.setHasFixedSize(true);
        mxRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mxRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private void setUpVerifiedNewsAdapter(RecyclerView mxRecyclerView) {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<News, VerifiedNewsViewHolder>
                (News.class, R.layout.verified_news_list_item, VerifiedNewsViewHolder.class, myRefVerifiedUpdates) {

            @Override
            protected void populateViewHolder(VerifiedNewsViewHolder viewHolder, News model, int position) {
                Log.d(TAG, "populateViewHolder: ");
                viewHolder.bindNews(model);
            }
        };

        mxRecyclerView.setHasFixedSize(true);
        mxRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mxRecyclerView.setAdapter(mFirebaseAdapter);
    }

//    @Override
//    public void onResume() {
//        Toast.makeText(getContext(), "Updates Fragment: OnResume", Toast.LENGTH_SHORT).show();
//        HomeScreenActivity.fabTransition();
//        super.onResume();
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
