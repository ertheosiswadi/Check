package siswadi.com.check;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class CustomAdapterInventoryFragment extends ArrayAdapter<Ingredient> {
    DatabaseHelper helper;

    CustomAdapterInventoryFragment(Context context, Ingredient[] f)
    {
        super(context, R.layout.custom_row_inventory_fragment, f);
        helper = new DatabaseHelper(context);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row_inventory_fragment, parent, false);
        TextView ingredientNameTextView_inventoryFragment = (TextView) customView.findViewById(R.id.ingredientNameTextView_inventoryFragment);
        TextView numberTextView_inventoryFragment = (TextView) customView.findViewById(R.id.numberTextView_inventoryFragment);

        Ingredient i = getItem(position);

        ingredientNameTextView_inventoryFragment.setText(i.getName());
        numberTextView_inventoryFragment.setText(position+1 + ".");

        return customView;
    }
}
