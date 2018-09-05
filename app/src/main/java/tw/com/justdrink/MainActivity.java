package tw.com.justdrink;

import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private MenuItem navItem;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private CharSequence mTitle;
    private TextView toolbar_text;

//    private void disableNavigationViewScrollbars(NavigationView navigationView) {
//        if (navigationView != null) {
//            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
//            if (navigationMenuView != null) {
//                navigationMenuView.setVerticalScrollBarEnabled(false);
//            }
//        }
//    }

 @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_text = (TextView)findViewById(R.id.tbtext);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //實現TOOLBAR上層
        setSupportActionBar(toolbar);

     //fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override//MAIL點擊觸發
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    // disableNavigationViewScrollbars(navigationView);

     toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
         @Override
         public void onDrawerOpened(View drawerView) {
             super.onDrawerOpened(drawerView);
             invalidateOptionsMenu();
         }

         @Override
         public void onDrawerClosed(View drawerView) {
             super.onDrawerClosed(drawerView);
             invalidateOptionsMenu();
         }
     };

     if (savedInstanceState == null) {
         navigationView.getMenu().performIdentifierAction(R.id.drinkwater, 0);
         //toolbar_text.setText(R.string.drinkwater);
     }
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        navItem = item;
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (id == R.id.drinkwater) {
            DrinkWater fragment = new DrinkWater();
            toolbar_text.setText(R.string.drinkwater);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            drawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.drinklog) {
            toolbar_text.setText(R.string.drinklog);
            Drinklog fragment = new Drinklog();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            drawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.drinkreport) {
            Drinkreport fragment = new Drinkreport();
            toolbar_text.setText(R.string.drinkreport);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            drawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.weightreport) {
            Weightreport fragment = new Weightreport();
            toolbar_text.setText(R.string.weightreport);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            drawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.reminders) {
            Reminders fragment = new Reminders();
            toolbar_text.setText(R.string.reminders);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            drawerLayout.closeDrawers();
            return true;
        } else if (id == R.id.nav_settings) {
            Setting fragment = new Setting();
            toolbar_text.setText(R.string.nav_settings);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            drawerLayout.closeDrawers();
            return true;
        //} else if (id == R.id.widgets) {

        //} else if (id == R.id.nav_share) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
//    public void setActionbarTitle(String title){
//        toolbar.setTitle(title);
//        (MainActivity)g
//    }
}
