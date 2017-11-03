package com.iteso.ruben.proyectoversion1;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class ActivityAchievement extends AppCompatActivity {

    private ListView listView;
    protected ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        listView = (ListView) findViewById(R.id.list);
        back_button = (ImageButton) findViewById(R.id.activity_a_back_button);

        Resources res = getResources();
        String[] goal = res.getStringArray(R.array.myGoals);

        MyAchieveAdapter adapter = new MyAchieveAdapter(this, goal);
        listView.setAdapter(adapter);


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent select_intent = new Intent(ActivitySleep.this, ActivityHome.class);
                //startActivity(select_intent);
                finish();
            }
        });
       // ArrayAdapter adapter = new ArrayAdapter<String>(this,
       // R.layout.achieves_list_item, R.id.textView1,goal);

      //  ListView listView = (ListView) findViewById(R.id.achieve_cards);
       // listView.setAdapter(adapter);
    }
/* @Override
    protected void onListItemClick(ListView l, View v,int pos, long id){
        String item = (String) getListAdapter().getItem(pos);
        Toast.makeText(this,item+"selected",Toast.LENGTH_LONG).show();
    }*/

}




