package siswadi.com.check;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddIngredientFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddIngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddIngredientFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private DatabaseHelper helper;
    private Food food;


    public AddIngredientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddIngredientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddIngredientFragment newInstance(String param1, String param2) {
        AddIngredientFragment fragment = new AddIngredientFragment();
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
        return inflater.inflate(R.layout.fragment_add_ingredient, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        toRefresh();
    }

    public void toRefresh() {
        int foodId = getArguments().getInt("foodId");
        food = helper.getFoodFromId(foodId);
        Account a = helper.getAccountFromId(food.getAccountId());
        LinkedList<Ingredient> inventory = helper.getInventory(a);
        LinkedList<IngredientV2> inventoryV2 = castToIngredientV2(inventory);
        inventoryV2 = setFlagsIfIngredientOfFood(inventoryV2);
        IngredientV2[] inventoryArray = new IngredientV2[inventoryV2.size()];
        inventoryArray = inventoryV2.toArray(inventoryArray);

        /*get custom adapter and pass the array*/
        ListAdapter addIngredientListAdapter = new CustomAdapterAddIngredientFragment(getActivity(), inventoryArray);

        /*set adapter to the target listView*/
        ListView fragmentListView = (ListView) getView().findViewById(R.id.inventoryAddIngredientFragmentListView);
        fragmentListView.setAdapter(addIngredientListAdapter);

        TextView nameOfFoodTextView_AddIngredientFragment = (TextView) getView().findViewById(R.id.ingredientNameaddIngredientFragmentTextView);
        nameOfFoodTextView_AddIngredientFragment.setText(food.getName());
    }

    private LinkedList<IngredientV2> setFlagsIfIngredientOfFood(LinkedList<IngredientV2> inventoryV2) {
        for(IngredientV2 i : inventoryV2)
        {
            if(helper.isIngredientOfFood((Ingredient)i, food))
            {
                i.setFlag(true);
            }
            else i.setFlag(false);
            System.out.println("Xfood-> " + food.getName() + " " + i.getName() + "-" + i.getFlag() + "-" + i.getId());
        }
        return inventoryV2;
    }

    private LinkedList<IngredientV2> castToIngredientV2(LinkedList<Ingredient> inventory) {
        LinkedList<IngredientV2> toReturn = new LinkedList<>();
        for(Ingredient i : inventory)
        {
            IngredientV2 iV2 = new IngredientV2();
            iV2.setAccountId(i.getAccountId());
            iV2.setAmount(i.getAmount());
            iV2.setExpiryDate(i.getExpiryDate());
            iV2.setId(i.getId());
            iV2.setInStock(i.getInStock());
            iV2.setLocation(i.getLocation());
            iV2.setName(i.getName());
            iV2.setFlag(false);
            toReturn.add(iV2);
        }
        return toReturn;
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
        System.out.println("The food ing is added to is -> " + food.getName());
        return this.food;
    }
}
