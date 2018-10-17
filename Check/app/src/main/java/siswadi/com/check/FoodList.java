package siswadi.com.check;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodList extends Fragment {

    DatabaseHelper helper;
    Account a;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int accountId;

    private OnFragmentInteractionListener mListener;

    public FoodList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodList.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodList newInstance(String param1, String param2) {
        FoodList fragment = new FoodList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        helper = new DatabaseHelper(this.getActivity());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);





        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("I'm here");
        helper = new DatabaseHelper(this.getActivity());
        return inflater.inflate(R.layout.fragment_food_list, container, false);
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        toRefresh();
    }

    public void toRefresh()
    {
        accountId = getArguments().getInt("accountId");
        a = helper.getAccountFromId(accountId);

        /******* Start code ************/
            /*array of food*/
        final LinkedList<Food> menuLL = helper.getMenu(a);
        Food[] menu = new Food[menuLL.size()];
        menu = menuLL.toArray(menu);
        ListAdapter foodListAdapter = new CustomAdapter(getActivity(), menu, a);
        ListView fragmentListView = null;
        if(getView() == null)
        {
            System.out.println("null");
        }
        else
        {
            fragmentListView = (ListView) getView().findViewById(R.id.fragment_list_view);
            fragmentListView.setAdapter(foodListAdapter);
        }

        fragmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //invoke next FoodFragment
                //sendbundle
                System.out.println("positionclicked-> " + position);
                FoodFragment foodFragment = new FoodFragment();
                Bundle bundle = new Bundle();
                Food f = menuLL.get(position);
                for(Food i : menuLL)
                {
                    System.out.println(i.getId() + i.getName());
                }

                int percentage = helper.getPercentage(a, f);

                bundle.putInt("percentage", percentage);
                bundle.putInt("foodId", f.getId());
                foodFragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_fragment, foodFragment, "FoodFragment").commit();
            }
        });

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

    /*https://stackoverflow.com/questions/24811536/android-listview-get-item-view-by-position*/
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
