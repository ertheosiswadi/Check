package siswadi.com.check;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CustomAdapterFoodFragment extends ArrayAdapter<Ingredient> {

    DatabaseHelper helper;

    CustomAdapterFoodFragment(Context context, Ingredient[] i)
    {
        super(context, R.layout.custom_row_food_fragment, i);
        helper = new DatabaseHelper(context);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row_food_fragment, parent, false);
        TextView ingredientNameFoodFragmentTextView = (TextView) customView.findViewById(R.id.ingredientNameFoodFragmentTextView);

        Ingredient i = getItem(position);

        ingredientNameFoodFragmentTextView.setText((position+1) + ".      " + i.getName());

        return customView;
    }
}
