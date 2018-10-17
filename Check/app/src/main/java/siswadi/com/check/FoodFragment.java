package siswadi.com.check;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private DatabaseHelper helper;
    private Food f;

    public FoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodFragment newInstance(String param1, String param2) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        helper = new DatabaseHelper(this.getActivity());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        toRefresh();
    }

    public void toRefresh()
    {
        //delete
        //helper.toDelete();
        //delete
        int percentage = getArguments().getInt("percentage");
        int foodId = getArguments().getInt("foodId");
        f = helper.getFoodFromId(foodId);
        System.out.println("in FoodFragment-> " + foodId + " " + f.getName());
        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        TextView foodNameFoodFragmentTextView = (TextView) getView().findViewById(R.id.foodNameFoodFragmentTextView);
        TextView percentageTextView_foodFragment = (TextView) getView().findViewById(R.id.percentageTextView_foodFragment);

        progressBar.setProgress(percentage);
        foodNameFoodFragmentTextView.setText(f.getName());
        percentageTextView_foodFragment.setText(percentage+"%");

        final LinkedList<Ingredient> iLinkedList = helper.getIngredientsOfFood(f);
        Ingredient[] iArray = new Ingredient[iLinkedList.size()];
        iArray = iLinkedList.toArray(iArray);
        ListAdapter ingredientListAdapter = new CustomAdapterFoodFragment(getActivity(), iArray);
        ListView fragmentListView = null;

        for(Ingredient i : iLinkedList)
        {
            System.out.println(i.getId() + i.getName());
        }

        if(getView() == null)
        {
            System.out.println("null");
        }
        else
        {
            fragmentListView = (ListView) getView().findViewById(R.id.foodFragmentListView);
            fragmentListView.setAdapter(ingredientListAdapter);
        }
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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

    public Food getFood()
    {
        return this.f;
    }
}
