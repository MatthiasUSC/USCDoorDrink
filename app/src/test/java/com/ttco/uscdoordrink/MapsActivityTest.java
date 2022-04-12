package com.ttco.uscdoordrink;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.widget.Button;

import androidx.core.content.ContextCompat;

import org.junit.Test;

import java.util.List;

public class MapsActivityTest {

    @Test
    public void clickTravelBtnWithNullMarkerWalking(){

        MapsActivity myMapsActivity = new MapsActivity();

        Button itemButton = new Button(null);
        itemButton.setText("Walking");

        myMapsActivity.setLastKnownLocation(null);
        MapsActivity.lastClickedMarker = null;

        myMapsActivity.onClickTravelModeBtn(itemButton);

        assertEquals("Walking", myMapsActivity.getCurrentTravelMode());
    }

    /*
    public void linguinicaradewini(){

        MapsActivity myMapsActivity = new MapsActivity();

        ContextCompat moquito = mock(ContextCompat.class);

        when(moquito.checkSelfPermission(any(),any())).thenReturn(0);

        myMapsActivity.g

        assertEquals(true, listmock.size());
    }
     */

}