package com.example.slickkwear

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.pinkpanterwear.R
import com.example.slickkwear.Prevalent.Prevalent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
    private var bottomNavigationView: BottomNavigationView? = null

    private val productRef: FirebaseFirestore? = null
    private val query: Query? = null

    private val search_back_icon: TextView? = null
    private val user_search_input: EditText? = null
    private val search_interface: LinearLayout? = null
    private val search_container_layout: LinearLayout? = null

    private val notificationBadge: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //        userSearch();

//        SETTING UP BOTTOM NAVIGATION BAR
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, UserHomeFragment())
            .commit()

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.getOrCreateBadge(R.id.bottom_nav_cart).isVisible = true
        bottomNavigationView.getOrCreateBadge(R.id.bottom_nav_cart).number = Prevalent.CartItems

        //        bottomNavigationView.getOrCreateBadge(R.id.bottom_nav_help).setVisible(true);
//        bottomNavigationView.getOrCreateBadge(R.id.bottom_nav_help).setNumber(0);

//
//
//        user_search_input = (EditText) findViewById(R.id.user_search_input);
        /**/        search_edit_text = (EditText) findViewById (R.id.search_edit_text); */
//
//        search_back_icon = (TextView) findViewById(R.id.search_back_icon);
//        search_interface = (LinearLayout) findViewById(R.id.search_interface);
//        search_container_layout = (LinearLayout) findViewById(R.id.search_container_layout);

//        TextView test12 = (TextView) findViewById(R.id.search_btn);
//        search_container_layout.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                        search_interface.setVisibility(View.VISIBLE);
//                        search_interface.startAnimation(inFromRightAnimation());
        /**/                        search_interface!!.visibility = View.VISIBLE
        * /                        Animation leftSwipe = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_left_search_interface)
        * /                        search_interface.startAnimation(leftSwipe); */
//                    }
//                }
//        );
//
//        search_back_icon.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        user_search_input.setText("");
//                        user_search_input.clearFocus();
//                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//
//                        search_interface.startAnimation(outToRightAnimation());
//                        search_interface.setVisibility(View.GONE);
        /**/                        Animation rightSwipe = AnimationUtils . loadAnimation (applicationContext, R.anim.anim_right_search_interface)
        * /                        search_interface.startAnimation(rightSwipe); */
//                    }
//                }
//        );

//        search_edit_text.setOnFocusChangeListener(
//                new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View view, boolean b) {
//
        /**/                        s * /
//                        Toast.makeText(MainActivity.this, "Result0" + width1, Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//        );

//        homeSliderBanner();
    }

    //    private Animation inFromRightAnimation() {
    //        Animation inFromRight = new TranslateAnimation( Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
    //        inFromRight.setDuration(500);
    //        inFromRight.setInterpolator(new AccelerateInterpolator());
    //        return inFromRight;
    //    }
    //
    //    private Animation outToRightAnimation() {
    //        Animation outToRight = new TranslateAnimation( Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
    //        outToRight.setDuration(500);
    //        outToRight.setInterpolator(new AccelerateInterpolator());
    //        return outToRight;
    //    }
    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        getMenuInflater().inflate(R.menu.search_menu, menu);
    //
    //        MenuItem item = menu.findItem(R.id.action_search);
    //        searchView.setMenuItem(item);
    //
    //        return true;
    //    }
    //    private void userSearch() {
    //
    //        String[] list = new String[] {"one", "two", "three"};
    //
    /**/        searchView = (MaterialSearchView) findViewById(R.id.search_view)
    * /        searchView.setSuggestions(list); */
    //        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
    //            @Override
    //            public boolean onQueryTextSubmit(String query) {
    //                searchView.setSuggestions(list);
    //                return false;
    //            }
    //
    //            @Override
    //            public boolean onQueryTextChange(String newText) {
    //                //Do some magic
    //                searchView.setSuggestions(list);
    //                return false;
    //            }
    //        });
    //
    //        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
    //            @Override
    //            public void onSearchViewShown() {
    /**/                searchView.showSuggestions(); */ //                //Do some magic
    //            }
    //
    //            @Override
    //            public void onSearchViewClosed() {
    //                //Do some magic
    //            }
    //        });
    //    }
    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            if (item.itemId == R.id.bottom_nav_home) {
                selectedFragment = UserHomeFragment()
            } else if (item.itemId == R.id.bottom_nav_category) {
                selectedFragment = UserCategoryFragment()
            } else if (item.itemId == R.id.bottom_nav_cart) {
                selectedFragment = UserCartFragment()
            } else if (item.itemId == R.id.bottom_nav_help) {
                selectedFragment = UserHelpFragment()
            } else if (item.itemId == R.id.bottom_nav_account) {
                selectedFragment = UserAccountFragment()
            }
            //            getSupportFragmentManager()
            //                    .set
            //
            //                    .beginTransaction().replace(R.id.fragment_container, selectedFragment)
            //                    .commit();
            val transaction = supportFragmentManager.beginTransaction()
            //            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
            transaction.replace(R.id.fragment_container, selectedFragment!!)
            transaction.commit()
            true
        }

    override fun onBackPressed() {
        if (supportFragmentManager
                .backStackEntryCount > 0
        ) {
            super.onBackPressed()
        } else {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton(
                    "Yes"
                ) { dialog, which ->
                    finish()
                    System.exit(0)
                }.setNegativeButton("No", null).show()
        }
    }
}