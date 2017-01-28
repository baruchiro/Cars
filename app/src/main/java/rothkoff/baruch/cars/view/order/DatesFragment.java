package rothkoff.baruch.cars.view.order;


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

    private EditText etDateStart, etDateEnd;
    private Switch sIsOneDay;

    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialogStart, datePickerDialogEnd;
    private OnDateSetListener dateSetListenerEnd;

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
        View view = inflater.inflate(R.layout.fragment_order_dates, container, false);

        InitMembers(view);
        BehaviorMembers();
        Refresh();

        return view;
    }

    private void InitMembers(View view) {
        etDateStart = (EditText) view.findViewById(R.id.frag_order_edit_datestart);
        etDateEnd = (EditText) view.findViewById(R.id.frag_order_edit_dateend);
        sIsOneDay = (Switch) view.findViewById(R.id.frag_order_switch_oneday);

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
        datePickerDialogStart.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() + B.Constants.DAY_IN_MILISECONDS);

        dateSetListenerEnd = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                DateEndMembers(i, i1, i2);
            }
        };
        datePickerDialogEnd = new DatePickerDialog(getContext(), dateSetListenerEnd, 0, 0, 0);
        datePickerDialogEnd.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis() + B.Constants.DAY_IN_MILISECONDS);

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

    private void DateStartMembers(int year, int month, int day) {
        datePickerDialogStart.updateDate(year, month, day);
        boolean b = setDateStart(year, month, day);
        etDateStart.setText(simpleDateFormat.format(getDateStart().getTime()));

        if (b) DateEndMembers(year, month, day);
    }

    private void DateEndMembers(int year, int month, int day) {
        datePickerDialogEnd.updateDate(year, month, day);
        boolean b = setDateEnd(year, month, day);
        etDateEnd.setText(simpleDateFormat.format(getDateEnd().getTimeInMillis()));

        if (b) DateStartMembers(year, month, day);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        getActivity().findViewById(R.id.frag_order_input_dateend).setVisibility(b ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getPageTitle() {
        return R.string.order_dates_frag_title;
    }

    @Override
    public void Refresh() {
        if (getDateStart() != null && getDateEnd() != null) {
            DateStartMembers(getDateStart().get(Calendar.YEAR), getDateStart().get(Calendar.MONTH), getDateStart().get(Calendar.DAY_OF_MONTH));
            DateEndMembers(getDateEnd().get(Calendar.YEAR), getDateEnd().get(Calendar.MONTH), getDateEnd().get(Calendar.DAY_OF_MONTH));
        }
    }

    @Override
    public void setTitle() {

    }
}
