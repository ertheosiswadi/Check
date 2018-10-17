package siswadi.com.check;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity {
    ActionBarDrawerToggle mToggle;
    DatabaseHelper helper = new DatabaseHelper(this);
    private DrawerLayout mDrawerlayout;
    Account a;
    private String whichFragment;
    private String unit;
    private boolean isUnitSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav = (NavigationView) this.findViewById(R.id.navigationView);
        setupDrawerContent(nav);

        /* Drawer code stuff ends here */

        /*Initialize Views*/
        /*final LayoutInflater factory = getLayoutInflater();
        final View header = factory.inflate(R.layout.header, null);*/

        View header = nav.getHeaderView(0);

        ImageView drawerProfilePicture = (ImageView) header.findViewById(R.id.drawerProfilePicture);
        TextView drawerUsername = (TextView) header.findViewById(R.id.drawerUsername);
        TextView drawerEmail = (TextView) header.findViewById(R.id.drawerEmail);


        /*get account*/
        int accountId = getIntent().getIntExtra("AccountId",-1);
        a = helper.getAccountFromId(accountId);

        System.out.println(accountId + " " + a.getUsername() + " " + a.getEmail());

        /*Initialize Text in Views*/
        drawerUsername.setText(a.getUsername());
        drawerEmail.setText(a.getEmail());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        /*add ingredient dialog, it is used in 2 fragments(inventoryfrag & addingredientfrag, declare outside*/
        View dialogAddIngredientView = getLayoutInflater().inflate(R.layout.dialog_add_ingredient, null);
        final EditText nameOfIngredientEditText_dialog = (EditText) dialogAddIngredientView.findViewById(R.id.nameOfIngredientEditText_dialog);
        final EditText amountOfIngredientEditText_dialog = (EditText) dialogAddIngredientView.findViewById(R.id.amountOfIngredientEditText_dialog);
        final EditText expiryDateEditText_dialog = (EditText) dialogAddIngredientView.findViewById(R.id.expiryDateEditText_dialog);
        final EditText locationOfIngredientEditText_dialog = (EditText) dialogAddIngredientView.findViewById(R.id.locationOfIngredientEditText_dialog);
        final CheckBox inStockCheckBox = (CheckBox) dialogAddIngredientView.findViewById(R.id.inStockCheckBox);
        final Button addIngredientDialogButton = (Button) dialogAddIngredientView.findViewById(R.id.addIngredientDialogButton);
        final Spinner spinner = (Spinner) dialogAddIngredientView.findViewById(R.id.spinnerUnits_addIngredientDialog);
            /*Implementing the spinner*/
            List<String> listOfUnits = new ArrayList<>();
            listOfUnits.add("g");listOfUnits.add("kg");listOfUnits.add("mL");listOfUnits.add("L");
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOfUnits);
            spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinner.setPrompt("unit");
            isUnitSelected = false;

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    unit = parent.getItemAtPosition(position).toString();
                    isUnitSelected = true;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    isUnitSelected = false;
                }
            });
            final Ingredient i = new Ingredient();
            inStockCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked)
                    {
                        i.setInStock(1);
                        amountOfIngredientEditText_dialog.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        i.setInStock(0);
                        amountOfIngredientEditText_dialog.setVisibility(View.INVISIBLE);
                    }
                }
            });

        /*what to do with the add button for each fragment*/
        if (item.getItemId() == R.id.add_button) {
            final Fragment foodListFrag = (Fragment) getSupportFragmentManager().findFragmentByTag("FoodList");
            final Fragment foodFragmentFrag = (Fragment) getSupportFragmentManager().findFragmentByTag("FoodFragment");
            final Fragment addIngredientFrag = (Fragment) getSupportFragmentManager().findFragmentByTag("AddIngredientFragment");
            final Fragment inventoryFrag = (Fragment) getSupportFragmentManager().findFragmentByTag("InventoryFragment");

            if(foodListFrag != null)//function during foodList fragment
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_food, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                /*Initialize views in dialog layout*/
                final EditText nameOfFoodEditText = (EditText) dialogView.findViewById(R.id.nameOfFoodEditText);
                final EditText linkEditText = (EditText) dialogView.findViewById(R.id.linkEditText);
                Button addFoodDialogButton = (Button) dialogView.findViewById(R.id.addFoodDialogButton);

                /*when the add button in dialog is presssed*/
                addFoodDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Food f = new Food();
                        f.setName(nameOfFoodEditText.getText().toString());
                        f.setRecipeLink(linkEditText.getText().toString());
                        f.setAccountId(a.getId());
                        helper.addFood(f);
                        dialog.dismiss();
                        FoodList fList = (FoodList) foodListFrag;
                        fList.toRefresh();
                    }
                });
            }
            else if (foodFragmentFrag != null)//if pressed go to addIngredientsFragment
            {
                //invoke next AddIngredientFragment
                //sendBundle
                Bundle bundle = new Bundle();
                FoodFragment f = (FoodFragment) getSupportFragmentManager().findFragmentByTag("FoodFragment");
                Food food = f.getFood();
                bundle.putInt("foodId", food.getId());

                AddIngredientFragment addIngredientFragment = new AddIngredientFragment();
                addIngredientFragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_fragment, addIngredientFragment, "AddIngredientFragment").commit();
            }
            else if(addIngredientFrag != null)
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                /*View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_ingredient, null);*/
                dialogBuilder.setView(dialogAddIngredientView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                /*Initialize views in dialog layout*/


                /*final EditText nameOfIngredientEditText_dialog = (EditText) dialogView.findViewById(R.id.nameOfIngredientEditText_dialog);
                final EditText amountOfIngredientEditText_dialog = (EditText) dialogView.findViewById(R.id.amountOfIngredientEditText_dialog);
                final EditText expiryDateEditText_dialog = (EditText) dialogView.findViewById(R.id.expiryDateEditText_dialog);
                final EditText locationOfIngredientEditText_dialog = (EditText) dialogView.findViewById(R.id.locationOfIngredientEditText_dialog);
                final CheckBox inStockCheckBox = (CheckBox) dialogView.findViewById(R.id.inStockCheckBox);
                final Button addIngredientDialogButton = (Button) dialogView.findViewById(R.id.addIngredientDialogButton);*/

                /*final Ingredient i = new Ingredient();
                inStockCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked)
                        {
                            i.setInStock(1);
                            amountOfIngredientEditText_dialog.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            i.setInStock(0);
                            amountOfIngredientEditText_dialog.setVisibility(View.INVISIBLE);
                        }
                    }
                });*/

                /*when the add button in dialog is presssed*/
                /*addIngredientDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        i.setName(nameOfIngredientEditText_dialog.getText().toString());
                        i.setExpiryDate(expiryDateEditText_dialog.getText().toString());
                        i.setLocation(locationOfIngredientEditText_dialog.getText().toString());
                        String amount = amountOfIngredientEditText_dialog.getText().toString();
                        if(amount != null)i.setAmount(amountOfIngredientEditText_dialog.getText().toString());
                        else i.setAmount("zero");
                        i.setAccountId(a.getId());

                        AddIngredientFragment addIngF = (AddIngredientFragment) addIngredientFrag;

                        helper.addIngredientOfFood(i, addIngF.getFood());
                        System.out.println("I received the food" + addIngF.getFood().getId() + " " + addIngF.getFood().getName() + "The ingredient is-> " + i.getName() + " " +i.getId());
                        dialog.dismiss();
                        addIngF.toRefresh();
                    }
                });*/
                addIngredientDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isUnitSelected) {
                            i.setName(nameOfIngredientEditText_dialog.getText().toString());
                            i.setExpiryDate(expiryDateEditText_dialog.getText().toString());
                            i.setLocation(locationOfIngredientEditText_dialog.getText().toString());
                            String amount = amountOfIngredientEditText_dialog.getText().toString();
                            if (!(amount.equals(0) || amount == null))
                                i.setAmount(amountOfIngredientEditText_dialog.getText().toString() + " " + unit);
                            else i.setAmount("0 " + unit);
                            i.setAccountId(a.getId());

                            AddIngredientFragment addIngF = (AddIngredientFragment) addIngredientFrag;

                            helper.addIngredientOfFood(i, addIngF.getFood());
                            //System.out.println("I received the food" + addIngF.getFood().getId() + " " + addIngF.getFood().getName() + "The ingredient is-> " + i.getName() + " " +i.getId());
                            dialog.dismiss();
                            addIngF.toRefresh();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please select appropriate unit", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else if(inventoryFrag != null)
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                /*View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_ingredient, null);*/
                dialogBuilder.setView(dialogAddIngredientView);
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                /*when the add button in dialog is presssed*/
                addIngredientDialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isUnitSelected) {
                            i.setName(nameOfIngredientEditText_dialog.getText().toString());
                            i.setExpiryDate(expiryDateEditText_dialog.getText().toString());
                            i.setLocation(locationOfIngredientEditText_dialog.getText().toString());
                            String amount = amountOfIngredientEditText_dialog.getText().toString();
                            if (!(amount.equals(0) || amount == null))
                                i.setAmount(amountOfIngredientEditText_dialog.getText().toString() + " " + unit);
                            else i.setAmount("0 " + unit);
                            i.setAccountId(a.getId());

                            InventoryFragment inventoryFragment = (InventoryFragment) inventoryFrag;

                            helper.addIngredient(i);
                            //System.out.println("I received the food" + addIngF.getFood().getId() + " " + addIngF.getFood().getName() + "The ingredient is-> " + i.getName() + " " +i.getId());
                            dialog.dismiss();
                            inventoryFragment.toRefresh();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please select appropriate unit", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /*Initialize views in dialog layout*/


                /*final EditText nameOfIngredientEditText_dialog = (EditText) dialogView.findViewById(R.id.nameOfIngredientEditText_dialog);
                final EditText amountOfIngredientEditText_dialog = (EditText) dialogView.findViewById(R.id.amountOfIngredientEditText_dialog);
                final EditText expiryDateEditText_dialog = (EditText) dialogView.findViewById(R.id.expiryDateEditText_dialog);
                final EditText locationOfIngredientEditText_dialog = (EditText) dialogView.findViewById(R.id.locationOfIngredientEditText_dialog);
                final CheckBox inStockCheckBox = (CheckBox) dialogView.findViewById(R.id.inStockCheckBox);
                final Button addIngredientDialogButton = (Button) dialogView.findViewById(R.id.addIngredientDialogButton);*/

               /* final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinnerUnits_addIngredientDialog);


                List<String> listOfUnits = new ArrayList<>();
                listOfUnits.add("g");listOfUnits.add("kg");listOfUnits.add("mL");listOfUnits.add("L");
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOfUnits);
                spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinner.setPrompt("unit");
                isUnitSelected = false;

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        unit = parent.getItemAtPosition(position).toString();
                        isUnitSelected = true;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        isUnitSelected = false;
                    }
                });*/


            }
            /*else{} */ //function during inventory fragment
        }

        return super.onOptionsItemSelected(item);
    }

    /*add button on action bar on the right*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void selectItemDrawer(MenuItem menuItem)
    {
        Fragment myFragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId())
        {
            case R.id.food_list_menu:
                fragmentClass = FoodList.class;
                whichFragment = "FoodList";
                break;
            case R.id.inventory_menu:
                fragmentClass = InventoryFragment.class;
                whichFragment = "InventoryFragment";
                break;
            default:
                fragmentClass = FoodList.class;
        }
        try
        {
            myFragment = (Fragment) fragmentClass.newInstance();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        /*Send Bundle to Fragment*/
        Bundle bundle = new Bundle();
        bundle.putInt("accountId", a.getId());
        myFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_fragment, myFragment, whichFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerlayout.closeDrawers();
    }
    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                selectItemDrawer(item);
                return true;
            }
        });
    }

    public Account getAccount()
    {
        return this.a;
    }

}
