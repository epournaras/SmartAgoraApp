package smartagora.ethz.ch.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import java.util.HashMap;
import java.util.List;

import smartagora.ethz.ch.R;


class CheckBoxViewModel {


    private final View checkBoxView;
    private final CheckBox checkbox;

    @SuppressLint("InflateParams")
    CheckBoxViewModel(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkBoxView = layoutInflater != null ? layoutInflater.inflate(R.layout.check_box_view_model, null) : null;
        checkbox = checkBoxView != null ? (CheckBox) checkBoxView.findViewById(R.id.checkbox) : null;
    }

    View getCheckBoxView(){
        return checkBoxView;
    }

    void updateView(String name, final HashMap<String, String> result, final List<Boolean> selection, int id) {
        try{
            checkbox.setText(name);
            checkbox.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            checkbox.setId(id);
            selection.add(false);
            checkbox.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            CheckBox cb = (CheckBox) v;
                            int id = cb.getId();
                            if (selection.get(id-1)) {
                                cb.setChecked(false);
                                selection.set(id-1, false);
                                result.remove(String.valueOf(id-1));
                            } else {
                                cb.setChecked(true);
                                selection.set(id-1, true);
                                result.put(String.valueOf(id-1), cb.getText().toString());
                            }
                        }
                    }
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
