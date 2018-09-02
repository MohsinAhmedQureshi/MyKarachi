package com.fyp.mykarachi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFragment extends RoundedBottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.custom_bottom_sheet, container, false);

        ((TextView) view.findViewById(R.id.hello)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Text Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
