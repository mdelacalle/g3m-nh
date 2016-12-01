package glob3mobile.com.g3mnh;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        ExpandableListAdapter menuAdapter;
        ExpandableListView menuExpandable = (ExpandableListView) findViewById(R.id.mainMenu);
        menuAdapter = new MenuAdapter(MenuActivity.this);
        menuExpandable.setAdapter(menuAdapter);

        menuExpandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent,
                                        View v, int groupPosition, int childPosition,
                                        long id) {


                final Intent scenarioIntent = new Intent(MenuActivity.this, ScenarioActivity.class);

                String scenario = ((TextView) v).getText().toString();

                if (scenario.equals("Quiero ir a correr")) {


                    final Dialog dialog = new Dialog(MenuActivity.this);
                    dialog.setContentView(R.layout.layout_dialog_run);
                    dialog.setTitle("Quiero...");

                    ImageView run1 = (ImageView) dialog.findViewById(R.id.run1);
                    run1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            scenarioIntent.putExtra("SCENARIO", "RUN_SCENARIO_EASY");
                            startActivity(scenarioIntent);
                        }
                    });

                    ImageView run2 = (ImageView) dialog.findViewById(R.id.run2);

                    run2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            scenarioIntent.putExtra("SCENARIO", "RUN_SCENARIO_HARD");
                            startActivity(scenarioIntent);
                        }
                    });

                    dialog.show();

                }
                if (scenario.equals("Queremos dar un paseo")) {

                    final Dialog dialog = new Dialog(MenuActivity.this);
                    dialog.setContentView(R.layout.layout_dialog_children);
                    dialog.setTitle("Queremos...");

                    ImageView children1 = (ImageView) dialog.findViewById(R.id.children1);
                    children1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            scenarioIntent.putExtra("SCENARIO", "WALKING_CITY_CULTURE");
                            startActivity(scenarioIntent);
                        }
                    });

                    ImageView children2 = (ImageView) dialog.findViewById(R.id.children2);

                    children2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            scenarioIntent.putExtra("SCENARIO", "WALKING_CITY_ADVENTURE");
                            startActivity(scenarioIntent);
                        }
                    });

                    dialog.show();
                }

                return false;
            }
        });


    }
}
