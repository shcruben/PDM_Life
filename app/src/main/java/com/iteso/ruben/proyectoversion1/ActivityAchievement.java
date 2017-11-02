package com.iteso.ruben.proyectoversion1;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class ActivityAchievement extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        listView = (ListView) findViewById(R.id.list);

        Resources res = getResources();
        String[] goal = res.getStringArray(R.array.myGoals);

        MyAchieveAdapter adapter = new MyAchieveAdapter(this, goal);
        listView.setAdapter(adapter);


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




