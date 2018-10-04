package tw.com.justdrink;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import tw.com.justdrink.dinrkreport.DrinkReport;
import tw.com.justdrink.dinrkreport.Weightreport;
import tw.com.justdrink.drinklog.DrinkLog;
import tw.com.justdrink.drinkwater.DrinkWater;
import tw.com.justdrink.reminder.Reminders;


public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private TextView toolbar_text;

    Fragment fragment = null;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_text = (TextView)findViewById(R.id.tbtext);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //實現TOOLBAR上層
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener); //設置監聽器

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
            displayView(R.id.drinkwater);
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Inflate the menu; this adds items to the action bar if it is present.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main,menu);

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displayView(item.getItemId());
        return true;
    }

    public Toolbar.OnMenuItemClickListener onMenuItemClickListener=new Toolbar.OnMenuItemClickListener() {

        Fragment fragment  = null;
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId())
            {
                case R.id.reminder_setting:
                    fragment = new Reminders();
                    toolbar_text.setText(R.string.reminders);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame , fragment ,"My FragmentReminder");
                    transaction.commit();
                    break;
                case R.id.calendar:
//                  Intent intent = new Intent(MainActivity.this,CalendarDialog.class);
//                  startActivity(intent);//Intent 方式不知道該怎麼接上<calendar-CalendarDialog->
                    final int mYear, mMonth, mDay;//設置時間變數
                    final Calendar c = Calendar.getInstance();  //final 是無法被修改的 類別 函數 或變數

                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {//OnDateSetListener 為內建方法 將使用者輸入完成後的日期傳回
                        @Override
                        public void onDateSet(DatePicker view, int mYear, int mMonth, int mDay) {
                             toolbar_text.setText(mYear+"-"+String.valueOf(mMonth+1)+"-"+mDay);//顯示在toolbar上的日期 //轉用到ToolBar上的setTitle
                                  }
                    }, mYear,mMonth, mDay).show();
                    break;
                case R.id.weight_setting:
                    Weight weight = new Weight();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Key01", 0);
                    weight.setArguments(bundle);
                    weight.show(fragmentManager, "Dialog");
                    break;
            }
            return true;

        }

    };

    private void displayView(int viewId) {

        Fragment fragment = null;
        switch (viewId) {
            case R.id.drinkwater:
                fragment = new DrinkWater();
                toolbar_text.setText(R.string.drinkwater);
                break;
            case R.id.drinklog:
                fragment = new DrinkLog();
                toolbar_text.setText(R.string.drinklog);
                break;
            case R.id.drinkreport:
                fragment = new DrinkReport();
                toolbar_text.setText(R.string.drinkreport);
                break;
            case R.id.weightreport:
                fragment = new Weightreport();
                toolbar_text.setText(R.string.weightreport);
                break;
            case R.id.reminders:
                fragment = new Reminders();
                toolbar_text.setText(R.string.reminders);
                break;
            case R.id.nav_settings:
                fragment = new Setting();
                toolbar_text.setText(R.string.nav_settings);
                break;
        }

        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
