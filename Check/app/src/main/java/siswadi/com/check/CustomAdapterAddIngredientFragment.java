package siswadi.com.check;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CustomAdapterAddIngredientFragment extends ArrayAdapter<IngredientV2> {

    DatabaseHelper helper;
    Context context;

    CustomAdapterAddIngredientFragment(Context context, IngredientV2[] i)
    {
        super(context, R.layout.custom_row_add_ingredient_fragment, i);
        helper = new DatabaseHelper(context);
        this.context = context;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row_add_ingredient_fragment, parent, false);
        TextView ingredientNameAddIngredientFragmentTextView = (TextView) customView.findViewById(R.id.ingredientNameAddIngredientFragmentTextView);
        ImageView checkImageView = (ImageView) customView.findViewById(R.id.checkImageView);

        IngredientV2 i = getItem(position);

        ingredientNameAddIngredientFragmentTextView.setText(i.getName());
        Drawable d;

        if(i.getFlag()) d = context.getResources().getDrawable(R.drawable.ic_check_black_24dp);
        else d = context.getResources().getDrawable(R.drawable.ic_clear_black_24dp);

        checkImageView.setImageDrawable(d);

        return customView;
    }
}
