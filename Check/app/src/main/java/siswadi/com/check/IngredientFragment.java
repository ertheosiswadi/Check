package siswadi.com.check;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IngredientFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Ingredient i;
    private int ingredientId;
    private DatabaseHelper helper;

    public IngredientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IngredientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IngredientFragment newInstance(String param1, String param2) {
        IngredientFragment fragment = new IngredientFragment();
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
            getActivity().setTitle("Ingredient");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        helper = new DatabaseHelper(getContext());
        return inflater.inflate(R.layout.fragment_ingredient, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        toRefresh();
    }

    public void toRefresh()
    {
        ingredientId = getArguments().getInt("ingredientId");
        i = helper.getIngredientFromId(ingredientId);
        final EditText ingredientNameEditText_ingredientFragment = (EditText) getView().findViewById(R.id.ingredientNameEditText_ingredientFragment);
        final EditText amountEditText_ingredientFragment = (EditText) getView().findViewById(R.id.amountEditText_ingredientFragment);
        final EditText expiryDateEditText_ingredientFragment = (EditText) getView().findViewById(R.id.expiryDateEditText_ingredientFragment);
        final EditText locationEditText_ingredientFragment = (EditText) getView().findViewById(R.id.locationEditText_ingredientFragment);
        ImageView ingredientImageView_ingredientFragment = (ImageView) getView().findViewById(R.id.ingredientImageView_ingredientFragment);
        FloatingActionButton fab_ingredientFragment = (FloatingActionButton) getView().findViewById(R.id.floatingActionButton_ingredientFragment);
        CheckBox inStockCheckBox_ingredientFragment = (CheckBox) getView().findViewById(R.id.checkBox_ingredientFragment);

        ingredientNameEditText_ingredientFragment.setText(i.getName());
        amountEditText_ingredientFragment.setText(i.getAmount());
        expiryDateEditText_ingredientFragment.setText(i.getExpiryDate());
        locationEditText_ingredientFragment.setText(i.getLocation());
        if(i.getInStock() == 1)inStockCheckBox_ingredientFragment.setChecked(true);
        else inStockCheckBox_ingredientFragment.setChecked(false);

        final Ingredient ingredientModified = new Ingredient();

        ingredientModified.setId(i.getId());

        inStockCheckBox_ingredientFragment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    ingredientModified.setInStock(1);
                    amountEditText_ingredientFragment.setVisibility(View.VISIBLE);
                }
                else
                {
                    ingredientModified.setInStock(0);
                    amountEditText_ingredientFragment.setVisibility(View.INVISIBLE);
                }
            }
        });

        fab_ingredientFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientModified.setName(ingredientNameEditText_ingredientFragment.getText().toString());
                ingredientModified.setAmount(amountEditText_ingredientFragment.getText().toString());
                ingredientModified.setExpiryDate(expiryDateEditText_ingredientFragment.getText().toString());
                ingredientModified.setLocation(locationEditText_ingredientFragment.getText().toString());
                int suucessitivity = helper.updateIngredient(ingredientModified);
                String message;
                if(suucessitivity == 1) message = "saved";
                else message = "failed to save";
                Toast t = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                t.show();
                InventoryFragment inventoryFragment = new InventoryFragment();
                Bundle b = new Bundle();
                b.putInt("accountId", ((DrawerActivity)getActivity()).getAccount().getId());
                inventoryFragment.setArguments(b);

                FragmentManager fManager = getActivity().getSupportFragmentManager();
                fManager.beginTransaction().replace(R.id.frame_fragment, inventoryFragment, "InventoryFragment").commit();
                getActivity().setTitle("Inventory");
            }
        });

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
}
