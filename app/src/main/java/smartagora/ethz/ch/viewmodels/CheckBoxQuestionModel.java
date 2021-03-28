package smartagora.ethz.ch.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smartagora.ethz.ch.R;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.CheckBoxThread;
import smartagora.ethz.ch.roomDatabase.entityModels.CheckBoxEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.models.QuestionDataModel;
import smartagora.ethz.ch.utils.ExceptionalHandling;


public class CheckBoxQuestionModel {

    private final View checkBoxQuestionView;

    private final LinearLayout options;
    private final TextView question;
    private final Context mContext;
    private final HashMap<String, String> resultMap;
    private final List<Boolean> selection;

    @SuppressLint("InflateParams")
    public CheckBoxQuestionModel(Context context) {
        mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        checkBoxQuestionView = layoutInflater != null ? layoutInflater.inflate(R.layout.checkbox_question_view_model, null) : new View(context);

        options = checkBoxQuestionView.findViewById(R.id.options);
        question = checkBoxQuestionView.findViewById(R.id.question);

        resultMap = new HashMap<>();
        selection = new ArrayList<>();
    }


    public void updateViewNew(QuestionEntity sampleDataModel) {
        try {
            question.setText(sampleDataModel.getQuestion());
            question.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            int qid = sampleDataModel.getId();
            String aId = sampleDataModel.getAssignmentId();

            CheckBoxThread checkBoxThread = new CheckBoxThread(mContext);
            List<CheckBoxEntity> checkBoxEntities = checkBoxThread.getAllCheckBoxesFromQuestionInAssignment(aId, qid + "");

            for (int i = 0; i < checkBoxEntities.size(); i++) {
                CheckBoxViewModel checkBoxViewModel = new CheckBoxViewModel(mContext);
                checkBoxViewModel.updateView(checkBoxEntities.get(i).getName(), resultMap, selection, i + 1);
                options.addView(checkBoxViewModel.getCheckBoxView(), i);
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    public void updateView(QuestionDataModel sampleDataModel) {
        try {
            question.setText(sampleDataModel.Question);
            question.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            for (int i = 0; i < sampleDataModel.Option.size(); i++) {
                CheckBoxViewModel checkBoxViewModel = new CheckBoxViewModel(mContext);
                checkBoxViewModel.updateView(sampleDataModel.Option.get(i).getName(), resultMap, selection, i + 1);
                options.addView(checkBoxViewModel.getCheckBoxView(), i);
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    public View getCheckBoxQuestionView(){
        return checkBoxQuestionView;
    }

    public HashMap<String, String> getResult() {
        return resultMap;
    }
}
