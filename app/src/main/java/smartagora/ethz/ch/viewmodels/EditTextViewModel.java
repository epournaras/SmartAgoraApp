package smartagora.ethz.ch.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import smartagora.ethz.ch.R;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.utils.ExceptionalHandling;


public class EditTextViewModel {

    private final View editTextView;
    private final TextView textViewTV;
    private final EditText editText;


    @SuppressLint("InflateParams")
    public EditTextViewModel(final Context context1) {
        LayoutInflater layoutInflater = (LayoutInflater) context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        editTextView = layoutInflater != null ? layoutInflater.inflate(R.layout.edit_text_view_model, null) :  new View(context1);
        textViewTV = editTextView.findViewById(R.id.textViewTV);
        editText = editTextView.findViewById(R.id.edittext);
    }



    public void updateViewNew(QuestionEntity sampleDataModel) {
        try {
            textViewTV.setText(sampleDataModel.getQuestion());
            textViewTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    public String getValue() {
        return editText == null ? null : editText.getText().toString();
    }

    public View getEditTextView(){
        return  editTextView;
    }

}
