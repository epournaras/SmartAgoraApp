package smartagora.ethz.ch.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import smartagora.ethz.ch.R;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.RadioButtonThread;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.RadioButtonEntity;
import smartagora.ethz.ch.models.QuestionDataModel;
import smartagora.ethz.ch.utils.ExceptionalHandling;


public class RadioButtonViewModel {


    private final Context context;
    private final View radioView;
    private final TextView textViewTV;
    private final RadioGroup radioGroup;


    @SuppressLint("InflateParams")
    public RadioButtonViewModel(Context context1) {
        context = context1;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (layoutInflater != null){
            radioView = layoutInflater.inflate(R.layout.radio_button_view_model, null);
            textViewTV = radioView.findViewById(R.id.textViewTV);
            radioGroup = radioView.findViewById(R.id.radioGroup);
        }else{
            radioView = null;
            textViewTV = null;
            radioGroup = null;
        }
    }
    public void updateViewNew(QuestionEntity sampleDataModel) {
        try {
            textViewTV.setText(sampleDataModel.getQuestion());
            textViewTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            int qid = sampleDataModel.getId();
            String aId = sampleDataModel.getAssignmentId();

            RadioButtonThread radioButtonThread = new RadioButtonThread(context);
            List<RadioButtonEntity> radioButtonEntityList = radioButtonThread.getAllRadioButtonsFromQuestionInAssignment(aId, qid);
            RadioButton[] radioButtonsArray = new RadioButton[radioButtonEntityList.size()];
            for (int i = 0; i < radioButtonEntityList.size(); i++) {
                radioButtonsArray[i] = new RadioButton(context);
                radioButtonsArray[i].setText(radioButtonEntityList.get(i).getName());
                radioButtonsArray[i].setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Medium);
                radioButtonsArray[i].setId(i + 100);
                radioGroup.addView(radioButtonsArray[i]);
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }




    public void updateView(QuestionDataModel sampleDataModel) {
        try {
            textViewTV.setText(sampleDataModel.Question);
            textViewTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            RadioButton[] radioButtonsArray = new RadioButton[sampleDataModel.Option.size()];
            for (int i = 0; i < sampleDataModel.Option.size(); i++) {
                radioButtonsArray[i] = new RadioButton(context);
                radioButtonsArray[i].setText(sampleDataModel.Option.get(i).getName());
                radioButtonsArray[i].setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Medium);
                radioButtonsArray[i].setId(i + 100);
                radioGroup.addView(radioButtonsArray[i]);
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    public String getValue() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            Button optionButton = (RadioButton) radioView.findViewById(selectedId);
            return optionButton.getText().toString();
        }
        return null;
    }

    public int getSelectedId() {
        return radioGroup.getCheckedRadioButtonId() % (100);
    }

    public View getRadioView(){
        return radioView;
    }


}
