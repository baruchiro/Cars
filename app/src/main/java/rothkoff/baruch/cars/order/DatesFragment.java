package rothkoff.baruch.cars.order;


import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import rothkoff.baruch.cars.B;
import rothkoff.baruch.cars.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DatesFragment extends MainOrderFragment implements CompoundButton.OnCheckedChangeListener {

    private EditText etDateStart,etDateEnd;
    private Switch sIsOneDay;

    private Calendar dateStart,dateEnd;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialogStart,datePickerDialogEnd;

    public DatesFragment() {
        // Required empty public constructor
    }

    public static DatesFragment newInstance() {
        DatesFragment fragment = new DatesFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dates, container, false);

        InitMembers(view);
        BehaviorMembers();

        return view;
    }

    private void InitMembers(View view) {
        etDateStart = (EditText) view.findViewById(R.id.frag_order_edit_datestart);
        etDateEnd = (EditText) view.findViewById(R.id.frag_order_edit_dateend);
        sIsOneDay = (Switch)view.findViewById(R.id.frag_order_switch_oneday);

        dateStart = Calendar.getInstance();
        dateEnd = Calendar.getInstance();

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    }

    private void BehaviorMembers() {
        OnDateSetListener dateSetListenerStart = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                DateStartMembers(i, i1, i2);
            }
        };
        datePickerDialogStart = new DatePickerDialog(getContext(), dateSetListenerStart, 0, 0, 0);

        OnDateSetListener dateSetListenerEnd = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                DateEndMembers(i, i1, i2);
            }
        };
        datePickerDialogEnd = new DatePickerDialog(getContext(), dateSetListenerEnd, 0, 0, 0);

        dateStart.add(Calendar.DATE,1);
        datePickerDialogStart.getDatePicker().setMinDate(dateStart.getTimeInMillis());
        DateStartMembers(dateStart.get(Calendar.YEAR),
                dateStart.get(Calendar.MONTH),
                dateStart.get(Calendar.DAY_OF_MONTH));

        dateEnd.add(Calendar.DATE,2);
        DateEndMembers(dateEnd.get(Calendar.YEAR),
                dateEnd.get(Calendar.MONTH),
                dateEnd.get(Calendar.DAY_OF_MONTH));

        etDateStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!datePickerDialogStart.isShowing()) datePickerDialogStart.show();
                return false;
            }
        });

        etDateEnd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!datePickerDialogEnd.isShowing()) datePickerDialogEnd.show();
                return true;
            }
        });

        sIsOneDay.setOnCheckedChangeListener(this);
    }

    private void DateStartMembers(int year,int month,int day){
        datePickerDialogStart.updateDate(year, month, day);
        dateStart.set(year, month, day);
        etDateStart.setText(simpleDateFormat.format(dateStart.getTime()));

        datePickerDialogEnd.getDatePicker().setMinDate(dateStart.getTimeInMillis()+B.Constants.DAY_IN_MILISECONDS);
    }
    private void DateEndMembers(int year,int month,int day){
        datePickerDialogEnd.updateDate(year, month, day);
        dateEnd.set(year, month, day);
        etDateEnd.setText(simpleDateFormat.format(dateEnd.getTime()));
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        getActivity().findViewById(R.id.frag_order_input_dateend).setVisibility(b?View.GONE:View.VISIBLE);
    }


    @Override
    public int getPageTitle() {
        return R.string.order_dates_frag_title;
    }
}
