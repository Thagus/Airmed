package controller;

import javafx.fxml.FXML;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.agenda.Agenda;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.Locale;

public class CalendarController {


    @FXML private Agenda agendaView;

    @FXML private CustomTextField searchField;

    @FXML private CalendarPicker calendarPicker;

    public void init(){

    }
}
