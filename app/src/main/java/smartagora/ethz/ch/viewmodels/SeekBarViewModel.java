package smartagora.ethz.ch.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import smartagora.ethz.ch.R;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.LikertScaleThread;
import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.models.QuestionDataModel;
import smartagora.ethz.ch.utils.ExceptionalHandling;


public class SeekBarViewModel {


    private final Context context;

    private final View seekBarView;
    private final TextView textViewTV;
    private final TextView seekBarName;
    private final SeekBar seekBar;
    private final TextView seekBarValue;


    @SuppressLint("InflateParams")
    public SeekBarViewModel(final Context context1) {
        context = context1;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        seekBarView = layoutInflater != null ? layoutInflater.inflate(R.layout.seek_bar_view_model, null) : new View(context1);

        textViewTV = seekBarView.findViewById(R.id.textViewTV); //can be null
        seekBarName = seekBarView.findViewById(R.id.seekBarName);//can be null
        seekBarValue = seekBarView.findViewById(R.id.seekBarValue);//can be null
        seekBar = seekBarView.findViewById(R.id.seekBar);//can be null
        if (seekBar != null)
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBarValue.setText(String.valueOf(progress));

                }
            });
    }


    public void updateViewNew(QuestionEntity sampleDataModel) {
        try {
            textViewTV.setText(sampleDataModel.getQuestion());
            int qid = sampleDataModel.getId();
            String aId = sampleDataModel.getAssignmentId();

            LikertScaleThread likertScaleThread = new LikertScaleThread(context);
            LikertScaleEntity likertScaleEntity = likertScaleThread.getAllLikertScalesFromQuestionInAssignment(aId, qid + "");

            seekBarName.setText(likertScaleEntity.getName());
            textViewTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            seekBarName.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Small);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    public void updateView(QuestionDataModel sampleDataModel) {
        try {
            textViewTV.setText(sampleDataModel.Question);
            seekBarName.setText(sampleDataModel.Option.get(0).getName());
            textViewTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            seekBarName.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_Small);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    public View getSeekBarView(){
        return seekBarView;
    }


    public String getValue() {
        if(seekBar != null)
            return Integer.toString(seekBar.getProgress());
        else
            return null;
    }

}
