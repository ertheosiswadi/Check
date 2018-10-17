package siswadi.com.check;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CustomAdapter extends ArrayAdapter<Food> {

        DatabaseHelper helper;
        Account a;

        CustomAdapter(Context context, Food[] f, Account account)
        {
            super(context, R.layout.custom_row_food_list, f);
            helper = new DatabaseHelper(context);
            this.a = account;
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView = inflater.inflate(R.layout.custom_row_food_list, parent, false);
            TextView foodNameTextView = (TextView) customView.findViewById(R.id.foodNameTextView);
            TextView percentageTextView = (TextView) customView.findViewById(R.id.percentageTextView);

            Food f = getItem(position);

            foodNameTextView.setText(f.getName());
            percentageTextView.setText(helper.getPercentage(a, f) + "%");

            return customView;
        }

}
